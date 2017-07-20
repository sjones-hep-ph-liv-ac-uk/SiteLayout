<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.List"%>
<%	List nodeTypeList = (List) request.getAttribute("nodeTypeList");%>

<html>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/SiteLayout.css">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Node Types</title>
</head>

<body>
	Node Types
	<form action="NodeTypeController" method="post">
		<table class="tg">
			<tr>
				<td>
					<input type="image" src="Down-48.png" alt="Submit" width="15px"
					height="15px" value="SORTDOWN.nodeTypeName" name="SORTDOWN.nodeTypeName">
					Node type name 
					<input type="image" src="Up-48.png" alt="Submit"
					width="15px" height="15px" value="SORTUP.nodeTypeName"
					name="SORTUP.nodeTypeName">
				</td>
				<td><input type="image" src="Down-48.png" alt="Submit"
					width="15px" height="15px" value="SORTDOWN.cpu" name="SORTDOWN.cpu">
					CPUs per node <input type="image" src="Up-48.png" alt="Submit"
					width="15px" height="15px" value="SORTUP.cpu" name="SORTUP.cpu">
				</td>
				<td><input type="image" src="Down-48.png" alt="Submit"
					width="15px" height="15px" value="SORTDOWN.slot" name="SORTDOWN.slot">
					Slots per node <input type="image" src="Up-48.png" alt="Submit"
					width="15px" height="15px" value="SORTUP.slot" name="SORTUP.slot">
				</td>
				<td><input type="image" src="Down-48.png" alt="Submit"
					width="15px" height="15px" value="SORTDOWN.hs06PerSlot"
					name="SORTDOWN.hs06PerSlot"> HS06 per slot <input
					type="image" src="Up-48.png" alt="Submit" width="15px"
					height="15px" value="SORTUP.hs06PerSlot" name="SORTUP.hs06PerSlot"></td>
				<td><input type="image" src="Down-48.png" alt="Submit"
					width="15px" height="15px" value="SORTDOWN.memPerNode"
					name="SORTDOWN.memPerNode"> GB per node <input type="image"
					src="Up-48.png" alt="Submit" width="15px" height="15px"
					value="SORTUP.memPerNode" name="SORTUP.memPerNode"></td>
				<td></td>
				<td></td>
			</tr>
			<c:set var="index" value="-1" scope="page" />
			<c:forEach items="${nodeTypeList}" var="nodeType">
			    <c:set var="index" value="${index + 1}" scope="page"/>
				<tr>
					<td><c:out value="${nodeType.nodeTypeName}" /></td>
					<td><c:out value="${nodeType.cpu}" /></td>
					<td><c:out value="${nodeType.slot}" /></td>
					<td><c:out value="${nodeType.hs06PerSlot}" /></td>
					<td><c:out value="${nodeType.memPerNode}" /></td>
					<td class="tg-yw4l"><input type="submit" value="Del"
						name=DEL.${nodeType.nodeTypeName}></td>
					<td class="tg-yw4l"><input type="submit" value="Edit"
						name=ED.${index}></td>   <!--  ED.${nodeType.nodeTypeName} -->
				</tr>
			</c:forEach>
			<tr>
				<td><input type="submit" value="Back" name="Back" /></td>
				<td><input type="submit" value="New" name="New" /></td>
				<td><input type="submit" value="Refresh" name="Refresh" /></td>
				<td></td>
				<td></td>
			</tr>

		</table>
	</form>
</body>
</html>
