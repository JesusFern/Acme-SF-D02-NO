/*
 * AnyShoutCreateService.java
 *
 * Copyright (C) 2012-2024 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.any.shout;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Any;
import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.shouts.Shout;

@Service
public class AnyShoutCreateService extends AbstractService<Any, Shout> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AnyShoutRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Shout object;

		object = new Shout();

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Shout object) {
		assert object != null;

		Date moment;

		moment = MomentHelper.getCurrentMoment();
		super.bind(object, "author", "text", "moreInfo");
		object.setMoment(moment);
	}

	@Override
	public void validate(final Shout object) {
		assert object != null;
	}

	@Override
	public void perform(final Shout object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Shout object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "author", "text", "moreInfo");

		super.getResponse().addData(dataset);

	}

}
