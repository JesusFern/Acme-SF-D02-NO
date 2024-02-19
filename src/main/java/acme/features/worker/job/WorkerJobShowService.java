/*
 * WorkerJobShowService.java
 *
 * Copyright (C) 2012-2024 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.worker.job;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.companies.Company;
import acme.entities.jobs.Job;
import acme.roles.Worker;

@Service
public class WorkerJobShowService extends AbstractService<Worker, Job> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private WorkerJobRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Job job;
		Date currentMoment;

		masterId = super.getRequest().getData("id", int.class);
		job = this.repository.findOneById(masterId);
		currentMoment = MomentHelper.getCurrentMoment();
		status = !job.isDraftMode() && MomentHelper.isAfter(job.getDeadline(), currentMoment);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Job object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void unbind(final Job object) {
		assert object != null;

		Collection<Company> contractors;
		SelectChoices choices;
		Dataset dataset;

		contractors = this.repository.findAllContractors();
		choices = SelectChoices.from(contractors, "name", object.getContractor());

		dataset = super.unbind(object, "reference", "title", "deadline", "salary", "score", "moreInfo", "description", "draftMode");
		dataset.put("contractor", choices.getSelected().getKey());
		dataset.put("contractors", choices);

		super.getResponse().addData(dataset);
	}

}
