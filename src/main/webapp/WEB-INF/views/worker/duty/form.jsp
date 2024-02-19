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
	<acme:input-textbox code="worker.duty.form.label.title" path="title"/>
	<acme:input-textarea code="worker.duty.form.label.description" path="description"/>
	<acme:input-double code="worker.duty.form.label.workLoad" path="workLoad"/>
	<acme:input-url code="worker.duty.form.label.moreInfo" path="moreInfo"/>			
</acme:form>

