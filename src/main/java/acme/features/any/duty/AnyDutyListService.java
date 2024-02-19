/*
 * AnyDutyListService.java
 *
 * Copyright (C) 2012-2024 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.any.duty;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Any;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.jobs.Duty;
import acme.entities.jobs.Job;

@Service
public class AnyDutyListService extends AbstractService<Any, Duty> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AnyDutyRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Job job;

		masterId = super.getRequest().getData("masterId", int.class);
		job = this.repository.findOneJobById(masterId);
		status = job != null && !job.isDraftMode();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<Duty> objects;
		int masterId;

		masterId = super.getRequest().getData("masterId", int.class);
		objects = this.repository.findManyDutiesByMasterId(masterId);

		super.getBuffer().addData(objects);
	}

	@Override
	public void unbind(final Duty object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "title", "description", "workLoad", "moreInfo");

		super.getResponse().addData(dataset);
	}

}
