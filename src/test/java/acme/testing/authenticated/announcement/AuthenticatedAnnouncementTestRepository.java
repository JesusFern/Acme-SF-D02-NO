/*
 * AuthenticatedAnnouncementTestRepository.java
 *
 * Copyright (C) 2012-2024 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.testing.authenticated.announcement;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;
import acme.entities.announcements.Announcement;

public interface AuthenticatedAnnouncementTestRepository extends AbstractRepository {

	@Query("select a from Announcement a where a.moment < :deadline")
	Collection<Announcement> findManyAnnouncementsBeforeDeadline(final Date deadline);

}
