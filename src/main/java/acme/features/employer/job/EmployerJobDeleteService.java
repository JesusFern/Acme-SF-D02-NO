/*
 * EmployerJobDeleteService.java
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.companies.Company;
import acme.entities.jobs.Duty;
import acme.entities.jobs.Job;
import acme.roles.Employer;

@Service
public class EmployerJobDeleteService extends AbstractService<Employer, Job> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private EmployerJobRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Job job;
		Employer employer;

		masterId = super.getRequest().getData("id", int.class);
		job = this.repository.findOneJobById(masterId);
		employer = job == null ? null : job.getEmployer();
		status = job != null && job.isDraftMode() && super.getRequest().getPrincipal().hasRole(employer);

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
	public void bind(final Job object) {
		assert object != null;

		int contractorId;
		Company contractor;

		contractorId = super.getRequest().getData("contractor", int.class);
		contractor = this.repository.findOneContractorById(contractorId);

		super.bind(object, "reference", "title", "deadline", "salary", "score", "moreInfo", "description");
		object.setContractor(contractor);
	}

	@Override
	public void validate(final Job object) {
		assert object != null;
	}

	@Override
	public void perform(final Job object) {
		assert object != null;

		Collection<Duty> duties;

		duties = this.repository.findManyDutiesByJobId(object.getId());
		this.repository.deleteAll(duties);
		this.repository.delete(object);
	}

	@Override
	public void unbind(final Job object) {
		assert object != null;

		int employerId;
		Collection<Company> contractors;
		SelectChoices choices;
		Dataset dataset;

		employerId = super.getRequest().getPrincipal().getActiveRoleId();
		contractors = this.repository.findManyContractorsByEmployerId(employerId);
		choices = SelectChoices.from(contractors, "name", object.getContractor());

		dataset = super.unbind(object, "reference", "title", "deadline", "salary", "score", "moreInfo", "description", "draftMode");
		dataset.put("contractor", choices.getSelected().getKey());
		dataset.put("contractors", choices);

		super.getResponse().addData(dataset);
	}

}
