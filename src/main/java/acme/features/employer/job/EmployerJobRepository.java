/*
 * EmployerJobRepository.java
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

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.companies.Company;
import acme.entities.jobs.Duty;
import acme.entities.jobs.Job;
import acme.roles.Employer;

@Repository
public interface EmployerJobRepository extends AbstractRepository {

	@Query("select j from Job j where j.id = :id")
	Job findOneJobById(int id);

	@Query("select j from Job j where j.reference = :reference")
	Job findOneJobByReference(String reference);

	@Query("select e from Employer e where e.id = :id")
	Employer findOneEmployerById(int id);

	@Query("select j from Job j where j.employer.id = :employerId")
	Collection<Job> findManyJobsByEmployerId(int employerId);

	@Query("select j from Job j where j.draftMode = false and j.deadline > :currentMoment")
	Collection<Job> findManyJobsByAvailability(Date currentMoment);

	@Query("select d from Duty d where d.job.id = :jobId")
	Collection<Duty> findManyDutiesByJobId(int jobId);

	@Query("select sum(d.workLoad) from Duty d where d.job.id = :jobId")
	Double computeWorkLoadByJobId(int jobId);

	@Query("select c from Company c")
	Collection<Company> findAllContractors();

	@Query("select c from Company c where c.id = :contractorId")
	Company findOneContractorById(int contractorId);

	@Query("select wf.contractor from WorksFor wf where wf.proxy.id = :employerId")
	Collection<Company> findManyContractorsByEmployerId(int employerId);

}
