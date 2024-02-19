<%--
- list.jsp
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

<acme:list navigable="false">
	<acme:list-column code="any.shout.list.label.moment" path="moment" width="10%"/>
	<acme:list-column code="any.shout.list.label.author" path="author" width="10%"/>
	<acme:list-column code="any.shout.list.label.text" path="text" width="50%"/>
	<acme:list-column code="any.shout.list.label.moreInfo" path="moreInfo" width="30%"/>
</acme:list>

<acme:button code="any.shout.list.button.create" action="/any/shout/create"/>


