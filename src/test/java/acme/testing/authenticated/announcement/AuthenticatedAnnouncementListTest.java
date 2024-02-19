/*
 * AuthenticatedAnnouncementListTest.java
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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.TestHarness;

public class AuthenticatedAnnouncementListTest extends TestHarness {

	@ParameterizedTest
	@CsvFileSource(resources = "/authenticated/announcement/list-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int recordIndex, final String moment, final String status, final String title) {
		// HINT: this test authenticates as an employer and checks that he or
		// HINT+ she can display the expected announcements.

		super.signIn("employer1", "employer1");

		super.clickOnMenu("Authenticated", "List announcements");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(recordIndex, 0, moment);
		super.checkColumnHasValue(recordIndex, 1, status);
		super.checkColumnHasValue(recordIndex, 2, title);

		super.signOut();
	}

	@Test
	public void test200Negative() {
		// HINT: there is no negative test case for this listing, since it does not
		// HINT+ involve filling in any forms.
	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to request the list of announcements as
		// HINT+ an anonymous principal.

		super.checkLinkExists("Sign in");
		super.request("/authenticated/announcement/list");
		super.checkPanicExists();
	}

	// Ancillary methods ------------------------------------------------------

}
