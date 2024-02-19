/*
 * WorkerApplicationShowService.java
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.jobs.Application;
import acme.roles.Worker;

@Service
public class WorkerApplicationShowService extends AbstractService<Worker, Application> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private WorkerApplicationRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int applicationId;
		Application application;

		applicationId = super.getRequest().getData("id", int.class);
		application = this.repository.findOneApplicationById(applicationId);
		status = application != null && super.getRequest().getPrincipal().hasRole(application.getWorker());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Application object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneApplicationById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void unbind(final Application object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "reference", "moment", "status", "statement", "skills", "qualifications");
		dataset.put("jobId", object.getId());

		super.getResponse().addData(dataset);
	}

}
