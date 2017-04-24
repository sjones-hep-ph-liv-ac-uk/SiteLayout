<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.basingwerk.sldb.mvc.model.Site"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	Site site = (Site) request.getAttribute("site");
%>

<html>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/SiteLayout.css">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Edit Site</title>
</head>
<body>
	Edit Site
	<form action="EditSiteController" method="post">
		Site name : <input type="text" name="siteName" readonly value="${site.siteName}"> <BR> 
        Description : <input type="text" name="cpu" value="${site.description}"> <BR>
        Location : <input type="text" name="cpu" value="${site.location}"> <BR>
        Longitude : <input type="text" name="cpu" value="${site.longitude}"> <BR>
        Latitude : <input type="text" name="cpu" value="${site.latitude}"> <BR>
        Admin : <input type="text" name="cpu" value="${site.admin}"> <BR>
	</form>
</body>
</html>
