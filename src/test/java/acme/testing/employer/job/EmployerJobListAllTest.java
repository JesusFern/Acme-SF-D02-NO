/*
 * EmployerJobListAllTest.java
 *
 * Copyright (C) 2012-2024 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.testing.employer.job;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.TestHarness;

public class EmployerJobListAllTest extends TestHarness {

	@ParameterizedTest
	@CsvFileSource(resources = "/employer/job/list-all-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int recordIndex, final String reference, final String title, final String deadline) {
		// HINT: this test signs in as an employer, lists all of the jobs, 
		// HINT+ and then checks that the listing shows the expected data.

		super.signIn("employer1", "employer1");

		super.clickOnMenu("Employer", "List all jobs");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(recordIndex, 0, reference);
		super.checkColumnHasValue(recordIndex, 1, deadline);
		super.checkColumnHasValue(recordIndex, 2, title);

		super.signOut();
	}

	@Test
	public void test200Negative() {
		// HINT: there are not any negative tests for this feature because it is a listing
		// HINT+ that does not involve entering any data in any forms.
	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to list all of the jobs using 
		// HINT+ inappropriate roles.

		super.checkLinkExists("Sign in");
		super.request("/employer/job/list-all");
		super.checkPanicExists();

		super.signIn("administrator", "administrator");
		super.request("/employer/job/list-all");
		super.checkPanicExists();
		super.signOut();

		super.signIn("worker1", "worker1");
		super.request("/employer/job/list-all");
		super.checkPanicExists();
		super.signOut();
	}

}
