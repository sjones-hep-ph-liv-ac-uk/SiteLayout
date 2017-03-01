<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.List"%>
<% List clusterList = (List)request.getAttribute("clusterList");%>

<html>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/SiteLayout.css">
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Cluster</title>
</head>
<body>
	Cluster

	<form action="ClusterController" method="post">

		<table class="tg">
			<tr>
				<td>
					<input type="image" src="Down-48.png" alt="Submit" width="15px"
					height="15px" value="SORTDOWN.clusterName" name="SORTDOWN.clusterName">
				Cluster name
					<input type="image" src="Up-48.png" alt="Submit"
					width="15px" height="15px" value="SORTUP.clusterName"
					name="SORTUP.clusterName">
				</td>
				<td>
					<input type="image" src="Down-48.png" alt="Submit" width="15px"
					height="15px" value="SORTDOWN.descr" name="SORTDOWN.descr">
				Description
					<input type="image" src="Up-48.png" alt="Submit"
					width="15px" height="15px" value="SORTUP.descr"
					name="SORTUP.descr">
				</td>
				<td></td>
				<td></td>
			</tr>

			<c:forEach items="${clusterList}" var="cluster">
				<tr>
					<td><c:out value="${cluster.cluster}" /></td>
					<td><c:out value="${cluster.descr}" /></td>
					<td class="tg-yw4l"><input type="submit" value="Del"
						name=DEL.${cluster.cluster}></td>
					<td class="tg-yw4l"><input type="submit" value="Edit"
						name=ED.${cluster.cluster}></td>
				</tr>
			</c:forEach>
			<tr>
				<td><input type="submit" value="Back" name="Back" /></td>
				<td><input type="submit" value="New" name="New" /></td>
				<td></td>
				<td></td>
			</tr>

		</table>
	</form>
</body>
</html>
