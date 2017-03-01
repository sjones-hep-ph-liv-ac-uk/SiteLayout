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
<title>Node Type</title>
</head>

<body>
	Node Type

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
					width="15px" height="15px" value="SORTDOWN.memPerSlot"
					name="SORTDOWN.memPerSlot"> GB per node <input type="image"
					src="Up-48.png" alt="Submit" width="15px" height="15px"
					value="SORTUP.memPerSlot" name="SORTUP.memPerSlot"></td>
				<td></td>
				<td></td>
			</tr>
			<c:forEach items="${nodeTypeList}" var="nodeType">
				<tr>
					<td><c:out value="${nodeType.nodeTypeName}" /></td>
					<td><c:out value="${nodeType.cpu}" /></td>
					<td><c:out value="${nodeType.slot}" /></td>
					<td><c:out value="${nodeType.hs06PerSlot}" /></td>
					<td><c:out value="${nodeType.memPerSlot}" /></td>
					<td class="tg-yw4l"><input type="submit" value="Del"
						name=DEL.${nodeType.nodeTypeName}></td>
					<td class="tg-yw4l"><input type="submit" value="Edit"
						name=ED.${nodeType.nodeTypeName}></td>
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
<!-- <style type="text/css">
.tg  {border-collapse:collapse;border-spacing:0;}
.tg td{font-family:Arial, sans-serif;font-size:14px;padding:10px 5px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal;}
.tg th{font-family:Arial, sans-serif;font-size:14px;font-weight:normal;padding:10px 5px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal;}
.tg .tg-yw4l{vertical-align:top}
</style> -->
