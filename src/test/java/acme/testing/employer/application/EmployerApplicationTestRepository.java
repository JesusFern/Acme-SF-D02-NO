/*
 * EmployerApplicationTestRepository.java
 *
 * Copyright (C) 2012-2024 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.testing.employer.application;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;
import acme.entities.jobs.Application;
import acme.entities.jobs.ApplicationStatus;

public interface EmployerApplicationTestRepository extends AbstractRepository {

	@Query("select a from Application a where a.job.employer.userAccount.username = :username")
	Collection<Application> findManyApplicationsByEmployerUsername(final String username);

	@Query("select a from Application a where a.job.employer.userAccount.username = :username and a.status = :status")
	Collection<Application> findManyApplicationsByEmployerUsernameAndStatus(final String user, final ApplicationStatus status);

}
