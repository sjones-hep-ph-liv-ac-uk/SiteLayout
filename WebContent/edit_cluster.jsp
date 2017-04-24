<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.basingwerk.sldb.mvc.model.Site"%>
<%@ page import="java.util.List"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<% 	List siteList = (List) request.getAttribute("siteList"); %>

<%	com.basingwerk.sldb.mvc.model.Cluster cluster = 
   (com.basingwerk.sldb.mvc.model.Cluster) request.getAttribute("cluster");
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
			value="${cluster.cluster}"> <BR> 
		Cluster Description : <input type="text" name="descr" value="${cluster.descr}"> <BR>
		Site : <select name='siteList'>
			<c:forEach items="${siteList}" var="s">
				<option value="${s}"
					${s == cluster.siteName ? 'selected="selected"' : ''}>${s}</option>
			</c:forEach>
		</select>
		<BR>
		<input type="submit" value="Submit" />
	</form>
</body>
</html>
