<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.List"%>
<%	List siteList = (List) request.getAttribute("siteList");%>

<html>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/SiteLayout.css">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Sites</title>
</head>

<html>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/SiteLayout.css">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Sites</title>
</head>

<body>
	Sites<BR>
	<form action="SiteController" method="post">
		<table class="tg">
			<tr>
				<td>
					<input type="image" src="Down-48.png" alt="Submit" width="15px"
					height="15px" value="SORTDOWN.siteName" name="SORTDOWN.siteName">
					Site name 
					<input type="image" src="Up-48.png" alt="Submit"
					width="15px" height="15px" value="SORTUP.siteName"
					name="SORTUP.siteName">
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
			<c:forEach items="${siteList}" var="site">
				<tr>
					<td><c:out value="${site.siteName}" /></td>
					<td><c:out value="${site.description}" /></td>
					<td><c:out value="${site.location}" /></td>
					<td><c:out value="${site.longitude}" /></td>
					<td><c:out value="${site.latitude}" /></td>
					<td><c:out value="${site.admin}" /></td>
					<td class="tg-yw4l"><input type="submit" value="Del"
						name=DEL.${site.siteName}></td>
					<td class="tg-yw4l"><input type="submit" value="Edit"
						name=ED.${site.siteName}></td>
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
