/*
 * EmployerApplicationShowService.java
 *
 * Copyright (C) 2012-2024 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.employer.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.jobs.Application;
import acme.entities.jobs.ApplicationStatus;
import acme.roles.Employer;

@Service
public class EmployerApplicationShowService extends AbstractService<Employer, Application> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private EmployerApplicationRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int applicationId;
		Application application;

		applicationId = super.getRequest().getData("id", int.class);
		application = this.repository.findOneApplicationById(applicationId);
		status = application != null && super.getRequest().getPrincipal().hasRole(application.getJob().getEmployer());

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

		SelectChoices choices;
		Dataset dataset;

		choices = SelectChoices.from(ApplicationStatus.class, object.getStatus());

		dataset = super.unbind(object, "reference", "moment", "status", "statement", "skills", "qualifications");
		dataset.put("masterId", object.getJob().getId());
		dataset.put("statuses", choices);

		super.getResponse().addData(dataset);
	}

}
