<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.List"%>
<%	List clusterSetList = (List) request.getAttribute("clusterSetList");%>

<html>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/SiteLayout.css">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Cluster sets</title>
</head>

<body>
	Cluster sets<BR>
	<form action="ClusterSetController" method="post">
		<table class="tg">
			<tr>
				<td>
					<input type="image" src="Down-48.png" alt="Submit" width="15px"
					height="15px" value="SORTDOWN.clusterSetName" name="SORTDOWN.clusterSetName">
					Cluster set name 
					<input type="image" src="Up-48.png" alt="Submit"
					width="15px" height="15px" value="SORTUP.clusterSetName"
					name="SORTUP.clusterSetName">
				</td>
				<td>
					<input type="image" src="Down-48.png" alt="Submit" width="15px"
					height="15px" value="SORTDOWN.description" name="SORTDOWN.description">
					Desc 
					<input type="image" src="Up-48.png" alt="Submit"
					width="15px" height="15px" value="SORTUP.description"
					name="SORTUP.description">
				</td>
				<td>
					<input type="image" src="Down-48.png" alt="Submit" width="15px"
					height="15px" value="SORTDOWN.location" name="SORTDOWN.location">
					location 
					<input type="image" src="Up-48.png" alt="Submit"
					width="15px" height="15px" value="SORTUP.location"
					name="SORTUP.location">
				</td>
				<td>
					<input type="image" src="Down-48.png" alt="Submit" width="15px"
					height="15px" value="SORTDOWN.longitude" name="SORTDOWN.longitude">
					Longitude 
					<input type="image" src="Up-48.png" alt="Submit"
					width="15px" height="15px" value="SORTUP.longitude"
					name="SORTUP.longitude">
				</td>
				<td>
					<input type="image" src="Down-48.png" alt="Submit" width="15px"
					height="15px" value="SORTDOWN.latitude" name="SORTDOWN.latitude">
					Latitude 
					<input type="image" src="Up-48.png" alt="Submit"
					width="15px" height="15px" value="SORTUP.latitude"
					name="SORTUP.latitude">
				</td>
				<td>
					<input type="image" src="Down-48.png" alt="Submit" width="15px"
					height="15px" value="SORTDOWN.admin" name="SORTDOWN.admin">
					Admin contact 
					<input type="image" src="Up-48.png" alt="Submit"
					width="15px" height="15px" value="SORTUP.admin"
					name="SORTUP.admin">
				</td>
				<td></td>
				<td></td>
			</tr>
			<c:forEach items="${clusterSetList}" var="clusterSet">
				<tr>
					<td><c:out value="${clusterSet.clusterSetName}" /></td>
					<td><c:out value="${clusterSet.description}" /></td>
					<td><c:out value="${clusterSet.location}" /></td>
					<td><c:out value="${clusterSet.longitude}" /></td>
					<td><c:out value="${clusterSet.latitude}" /></td>
					<td><c:out value="${clusterSet.admin}" /></td>
					<td class="tg-yw4l"><input type="submit" value="Del"
						name=DEL.${clusterSet.clusterSetName}></td>
					<td class="tg-yw4l"><input type="submit" value="Edit"
						name=ED.${clusterSet.clusterSetName}></td>
				</tr>
			</c:forEach>
			<tr>
				<td><input type="submit" value="Back" name="Back" /></td>
				<td><input type="submit" value="New" name="New" /></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
			</tr>
		</table>
	</form>
</body>
</html>
