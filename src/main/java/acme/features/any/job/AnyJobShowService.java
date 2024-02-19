/*
 * AnyJobShowService.java
 *
 * Copyright (C) 2012-2024 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.any.job;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Any;
import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.companies.Company;
import acme.entities.jobs.Job;

@Service
public class AnyJobShowService extends AbstractService<Any, Job> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AnyJobRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int id;
		Job job;

		id = super.getRequest().getData("id", int.class);
		job = this.repository.findOneJobById(id);
		status = job != null && !job.isDraftMode() && MomentHelper.isPresentOrFuture(job.getDeadline());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Job object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneJobById(id);

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
