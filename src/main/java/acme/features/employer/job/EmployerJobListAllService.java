/*
 * EmployerJobListAllService.java
 *
 * Copyright (C) 2012-2024 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.employer.job;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.jobs.Job;
import acme.roles.Employer;

@Service
public class EmployerJobListAllService extends AbstractService<Employer, Job> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private EmployerJobRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Job> objects;
		Date currentMoment;

		currentMoment = MomentHelper.getCurrentMoment();
		objects = this.repository.findManyJobsByAvailability(currentMoment);

		super.getBuffer().addData(objects);
	}

	@Override
	public void unbind(final Job object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "reference", "title", "deadline");

		super.getResponse().addData(dataset);
	}

}
