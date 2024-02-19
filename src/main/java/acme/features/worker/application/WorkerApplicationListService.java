/*
 * WorkerApplicationListService.java
 *
 * Copyright (C) 2012-2024 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.worker.application;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.jobs.Application;
import acme.roles.Worker;

@Service
public class WorkerApplicationListService extends AbstractService<Worker, Application> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private WorkerApplicationRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Application> object;
		int workerId;

		workerId = super.getRequest().getPrincipal().getActiveRoleId();
		object = this.repository.findManyApplicationsByWorkerId(workerId);

		super.getBuffer().addData(object);
	}

	@Override
	public void unbind(final Application object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "reference", "status");
		dataset.put("title", object.getJob().getTitle());

		super.getResponse().addData(dataset);
	}

}
