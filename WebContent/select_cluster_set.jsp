<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.basingwerk.sldb.mvc.model.Site"%>
<%@ page import="java.util.List"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<% 	List siteList = (List) request.getAttribute("siteList"); %>


<html>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/SiteLayout.css">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Select Site</title>
</head>
<body>
	Select Site
	<form action="SelectSiteController" method="post">
		Site : <select name='siteList'>
			<c:forEach items="${siteList}" var="s">
				<option value="${s.siteName}" selected="selected" >${s.siteName}</option>
			</c:forEach>
		</select>
		<BR>
		<input type="submit" value="Submit" />
	</form>
</body>
</html>
