<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.basingwerk.sldb.mvc.model.ClusterSet"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	ClusterSet clusterSet = (ClusterSet) request.getAttribute("clusterSet");
%>

<html>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/SiteLayout.css">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Edit ClusterSet</title>
</head>
<body>
	Edit ClusterSet
	<form action="EditClusterSetController" method="post">
		ClusterSet name : <input type="text" name="clusterSetName" readonly value="${clusterSet.clusterSetName}"> <BR> 
        Description : <input type="text" name="description" value="${clusterSet.description}"> <BR>
        Location : <input type="text" name="location" value="${clusterSet.location}"> <BR>
        Longitude : <input type="text" name="longitude" value="${clusterSet.longitude}"> <BR>
        Latitude : <input type="text" name="latitude" value="${clusterSet.latitude}"> <BR>
        Admin : <input type="text" name="admin" value="${clusterSet.admin}"> <BR>
                <input type="submit" value="Submit" />
	</form>
</body>
</html>
