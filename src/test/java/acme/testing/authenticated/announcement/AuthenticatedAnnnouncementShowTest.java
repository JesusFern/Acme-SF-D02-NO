/*
 * AuthenticatedAnnnouncementShowTest.java
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

import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.client.helpers.MomentHelper;
import acme.entities.announcements.Announcement;
import acme.testing.TestHarness;

public class AuthenticatedAnnnouncementShowTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuthenticatedAnnouncementTestRepository repository;

	// Test methods -----------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/authenticated/announcement/show-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int recordIndex, final String title, final String status, final String text, final String moreInfo) {
		// HINT: this test signs in as an employer, then lists the announcements,
		// HINT+ and checks that the listing shows the expected data.

		super.signIn("employer1", "employer1");

		super.clickOnMenu("Authenticated", "List announcements");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.clickOnListingRecord(recordIndex);

		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("status", status);
		super.checkInputBoxHasValue("text", text);
		super.checkInputBoxHasValue("moreInfo", moreInfo);

		super.signOut();
	}

	@Test
	public void test200Negative() {
		// HINT: there are not any negative tests for this feature because it
		// HINT+ does not involve any forms.
	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to show old announcements as an authenticated principal.

		Collection<Announcement> announcements;
		Date deadline;
		String query;

		super.signIn("employer1", "employer1");
		deadline = MomentHelper.deltaFromMoment(MomentHelper.getBaseMoment(), -1, ChronoUnit.MONTHS);
		announcements = this.repository.findManyAnnouncementsBeforeDeadline(deadline);
		for (final Announcement announcement : announcements) {
			query = String.format("id=%d", announcement.getId());
			super.request("/authenticated/announcement/show", query);
			super.checkPanicExists();
		}
		super.signOut();
	}

}
