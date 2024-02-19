/*
 * AnyShoutListService.java
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

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Any;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.shouts.Shout;

@Service
public class AnyShoutListService extends AbstractService<Any, Shout> {

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
		Collection<Shout> objects;

		objects = this.repository.findMany();

		super.getBuffer().addData(objects);
	}

	@Override
	public void unbind(final Shout object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "author", "text", "moment", "moreInfo");

		super.getResponse().addData(dataset);
	}

}
