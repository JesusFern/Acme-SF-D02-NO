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
<%@taglib prefix="acme-jobs" tagdir="/WEB-INF/tags"%>

<acme:form>
	<acme:input-textbox code="authenticated.user-account.form.label.username" path="username" readonly="true"/>
	<acme:input-password code="authenticated.user-account.form.label.password" path="password"/>
	<acme:input-password code="authenticated.user-account.form.label.confirmation" path="confirmation"/>
	
	<acme:input-textbox code="authenticated.user-account.form.label.name" path="identity.name"/>
	<acme:input-textbox code="authenticated.user-account.form.label.surname" path="identity.surname"/>
	<acme:input-email code="authenticated.user-account.form.label.email" path="identity.email"/>
	<acme-jobs:input-phone code="authenticated.user-account.form.label.phone" path="identity.phone" placeholder="+34 954 123456"/>
	 
	<acme:submit code="authenticated.user-account.form.button.update" action="/authenticated/user-account/update"/>
</acme:form>
