/*
 * EmployerDutyShowTest.java
 *
 * Copyright (C) 2012-2024 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.testing.employer.duty;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.jobs.Duty;
import acme.testing.TestHarness;

public class EmployerDutyShowTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	private EmployerDutyTestRepository repository;

	// Test methods -----------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/employer/duty/show-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int jobRecordIndex, final String reference, final int dutyRecordIndex, final String title, final String description, final String workLoad, final String moreInfo) {
		// HINT: this test signs in as an employer, lists his or her jobs, selects
		// HINT+ one of them and checks that it is as expected.

		super.signIn("employer1", "employer1");

		super.clickOnMenu("Employer", "List my jobs");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.clickOnListingRecord(jobRecordIndex);
		super.clickOnButton("Duties");
		super.checkListingExists();
		super.clickOnListingRecord(dutyRecordIndex);
		super.checkFormExists();

		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("description", description);
		super.checkInputBoxHasValue("workLoad", workLoad);
		super.checkInputBoxHasValue("moreInfo", moreInfo);

		super.signOut();
	}

	@Test
	public void test200Negative() {
		// HINT: there is no negative test case for this listing, since it does not
		// HINT+ involve filling in any forms.
	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to show a duty of a job that is in draft mode or
		// HINT+ not available, but was not published by the principal;

		Collection<Duty> duties;
		String param;

		duties = this.repository.findManyDutiesByEmployerUsername("employer2");
		for (final Duty duty : duties)
			if (duty.getJob().isDraftMode()) {
				param = String.format("id=%d", duty.getJob().getId());

				super.checkLinkExists("Sign in");
				super.request("/employer/duty/show", param);
				super.checkPanicExists();

				super.signIn("administrator", "administrator");
				super.request("/employer/duty/show", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("employer2", "employer2");
				super.request("/employer/duty/show", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("worker1", "worker1");
				super.request("/employer/duty/show", param);
				super.checkPanicExists();
				super.signOut();
			}
	}

}
