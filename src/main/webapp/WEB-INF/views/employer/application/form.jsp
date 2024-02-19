<%--
- form.jsp
-
- Copyright (C) 2012-2024 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="employer.application.form.label.reference" path="reference" readonly="true" />
	<acme:input-textbox code="employer.application.form.label.moment" path="moment" readonly="true" />
	<acme:input-textarea code="employer.application.form.label.statement" path="statement" readonly="true" />
	<acme:input-textarea code="employer.application.form.label.skills" path="skills" readonly="true" />
	<acme:input-textarea code="employer.application.form.label.qualifications" path="qualifications" readonly="true" />

	<acme:input-select path="status" code="employer.application.form.label.new-status" choices="${statuses}" readonly="${acme:anyOf(status, 'ACCEPTED|REJECTED')}" />
	
	<acme:submit
		test="${acme:anyOf(_command, 'show|update') && !acme:anyOf(status, 'ACCEPTED|REJECTED')}"
		code="employer.application.form.button.update"
		action="/employer/application/update" />
</acme:form>


