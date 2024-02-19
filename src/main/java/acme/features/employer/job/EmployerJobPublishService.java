/*
 * EmployerJobPublishService.java
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

import java.time.temporal.ChronoUnit;
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
import acme.roles.Employer;

@Service
public class EmployerJobPublishService extends AbstractService<Employer, Job> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private EmployerJobRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int jobId;
		Job job;
		Employer employer;

		jobId = super.getRequest().getData("id", int.class);
		job = this.repository.findOneJobById(jobId);
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

		if (!super.getBuffer().getErrors().hasErrors("deadline")) {
			Date minimumDeadline;

			minimumDeadline = MomentHelper.deltaFromCurrentMoment(7, ChronoUnit.DAYS);
			super.state(MomentHelper.isAfter(object.getDeadline(), minimumDeadline), "deadline", "employer.job.form.error.too-close");
		}

		if (!super.getBuffer().getErrors().hasErrors("reference")) {
			Job existing;

			existing = this.repository.findOneJobByReference(object.getReference());
			super.state(existing == null || existing.equals(object), "reference", "employer.job.form.error.duplicated");
		}

		if (!super.getBuffer().getErrors().hasErrors("salary"))
			super.state(object.getSalary().getAmount() > 0, "salary", "employer.job.form.error.negative-salary");

		{
			Double workLoad;

			workLoad = this.repository.computeWorkLoadByJobId(object.getId());
			super.state(workLoad != null && workLoad == 100.0, "*", "employer.job.form.error.bad-work-load");
		}
	}

	@Override
	public void perform(final Job object) {
		assert object != null;

		object.setDraftMode(false);
		this.repository.save(object);
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
