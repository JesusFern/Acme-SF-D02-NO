/*
 * AuthenticatedMoneyExchangePerformService.java
 *
 * Copyright (C) 2012-2024 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.authenticated.moneyExchange;

import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import acme.client.data.accounts.Authenticated;
import acme.client.data.datatypes.Money;
import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.helpers.StringHelper;
import acme.client.services.AbstractService;
import acme.components.ExchangeRate;
import acme.forms.MoneyExchange;

@Service
public class AuthenticatedMoneyExchangePerformService extends AbstractService<Authenticated, MoneyExchange> {

	// AbstractService interface ----------------------------------------------

	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		MoneyExchange object;

		object = new MoneyExchange();

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final MoneyExchange object) {
		assert object != null;

		super.bind(object, "source", "targetCurrency", "date", "target");
	}

	@Override
	public void validate(final MoneyExchange object) {
		assert object != null;
	}

	@Override
	public void perform(final MoneyExchange object) {
		assert object != null;

		Money source, target;
		String targetCurrency;
		Date date;
		MoneyExchange exchange;

		source = super.getRequest().getData("source", Money.class);
		targetCurrency = super.getRequest().getData("targetCurrency", String.class);
		exchange = this.computeMoneyExchange(source, targetCurrency);
		super.state(exchange != null, "*", "authenticated.money-exchange.form.label.api-error");
		if (exchange == null) {
			object.setTarget(null);
			object.setDate(null);
		} else {
			target = exchange.getTarget();
			object.setTarget(target);
			date = exchange.getDate();
			object.setDate(date);
		}
	}

	@Override
	public void unbind(final MoneyExchange object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "source", "targetCurrency", "date", "target");

		super.getResponse().addData(dataset);
	}

	// Ancillary methods ------------------------------------------------------

	public MoneyExchange computeMoneyExchange(final Money source, final String targetCurrency) {
		assert source != null;
		assert !StringHelper.isBlank(targetCurrency);

		MoneyExchange result;
		RestTemplate api;
		ExchangeRate record;
		String sourceCurrency, key;
		Double sourceAmount, targetAmount, rate;
		Money target;
		Date moment;

		try {
			api = new RestTemplate();

			sourceCurrency = source.getCurrency();
			sourceAmount = source.getAmount();

			record = api.getForObject( //				
				"http://apilayer.net/api/live?source={0}&currencies={1}&access_key={2}&format=1", //
				ExchangeRate.class, //
				sourceCurrency, //
				targetCurrency, //
				"63259d4389193b9554785f5cea807177");

			assert record != null;
			key = String.format("%s%s", sourceCurrency, targetCurrency);
			rate = record.getQuotes().get(key);
			assert rate != null;
			targetAmount = rate * sourceAmount;

			target = new Money();
			target.setAmount(targetAmount);
			target.setCurrency(targetCurrency);

			moment = new Date(record.getTimestamp() * 1000L);

			result = new MoneyExchange();
			result.setSource(source);
			result.setTargetCurrency(targetCurrency);
			result.setDate(moment);
			result.setTarget(target);

			MomentHelper.sleep(1000); // HINT: need to pause the requests to the API a bit down to prevent DOS attacks
		} catch (final Throwable oops) {
			result = null;
		}

		return result;
	}

}
