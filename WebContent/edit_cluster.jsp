<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	com.basingwerk.sldb.mvc.model.Cluster cluster = (com.basingwerk.sldb.mvc.model.Cluster) request
			.getAttribute("cluster");
%>

<html>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/SiteLayout.css">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Edit Cluster</title>
</head>
<body>
	Edit Cluster
	<form action="EditClusterController" method="post">
		Cluster Name : <input type="text" name="clusterName" readonly
			value="${cluster.cluster}"> <BR> Cluster Description
		: <input type="text" name="descr" value="${cluster.descr}"> <BR>
		<input type="submit" />
	</form>
</body>
</html>
