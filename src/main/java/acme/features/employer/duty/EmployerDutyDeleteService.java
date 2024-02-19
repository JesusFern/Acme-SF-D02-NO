/*
 * EmployerDutyDeleteService.java
 *
 * Copyright (C) 2012-2024 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.employer.duty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.jobs.Duty;
import acme.entities.jobs.Job;
import acme.roles.Employer;

@Service
public class EmployerDutyDeleteService extends AbstractService<Employer, Duty> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private EmployerDutyRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int dutyId;
		Job job;

		dutyId = super.getRequest().getData("id", int.class);
		job = this.repository.findOneJobByDutyId(dutyId);
		status = job != null && job.isDraftMode() && super.getRequest().getPrincipal().hasRole(job.getEmployer());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Duty object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneDutyById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Duty object) {
		assert object != null;

		super.bind(object, "title", "description", "workLoad", "moreInfo");
	}

	@Override
	public void validate(final Duty object) {
		assert object != null;
	}

	@Override
	public void perform(final Duty object) {
		assert object != null;

		this.repository.delete(object);
	}

	@Override
	public void unbind(final Duty object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "title", "description", "workLoad", "moreInfo");
		dataset.put("masterId", object.getJob().getId());
		dataset.put("draftMode", object.getJob().isDraftMode());

		super.getResponse().addData(dataset);
	}

}
