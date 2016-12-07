<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.List"%>
<%
	List nodeSetList = (List) request.getAttribute("nodeSetList");
%>

<html>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/SiteLayout.css">

<head>
Node Set

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Node Set</title>
</head>
<body>
	Node Set

	<form action="NodeSetController" method="post">
		<table class="tg">
			<tr>
				<td>Node set name</td>
				<td>Node type</td>
				<td>Count</td>
				<td>Cluster</td>
				<td></td>
				<td></td>
			</tr>
			<c:forEach items="${nodeSetList}" var="nodeSet">
				<tr>
					<td><c:out value="${nodeSet.nodeSetName}" /></td>
					<td><c:out value="${nodeSet.nodeTypeName}" /></td>
					<td><c:out value="${nodeSet.nodeCount}" /></td>
					<td><c:out value="${nodeSet.cluster}" /></td>
					<td class="tg-yw4l"><input type="submit" value="Del"
						name=DEL.${nodeSet.nodeSetName}/></td>
					<td class="tg-yw4l"><input type="submit" value="Edit"
						name=ED.${nodeSet.nodeSetName}/></td>
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
