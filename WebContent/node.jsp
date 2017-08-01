<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.List"%>
<%
	List nodeList = (List) session.getAttribute("nodeList");
%>

<html>
<link rel="stylesheet" href="${pageContext.request.contextPath}/SiteLayout.css">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Nodes </title>
</head>
<body>
	Nodes

	<form action="NodeController" method="post">
		<table class="tg">
			<tr>
				<td>
					<input type="image" src="Down-48.png" alt="Submit" width="15px"
					height="15px" value="SORTDOWN.nodeName" name="SORTDOWN.nodeName">
				Node name
					<input type="image" src="Up-48.png" alt="Submit" width="15px"
					height="15px" value="SORTUP.nodeName" name="SORTUP.nodeName">
				</td>
				<td>
					<input type="image" src="Down-48.png" alt="Submit" width="15px"
					height="15px" value="SORTDOWN.nodeState" name="SORTDOWN.nodeState">
				Node state
					<input type="image" src="Up-48.png" alt="Submit" width="15px"
					height="15px" value="SORTUP.nodeState" name="SORTUP.nodeState">
				</td>
				<td>
					<input type="image" src="Down-48.png" alt="Submit" width="15px"
					height="15px" value="SORTDOWN.description" name="SORTDOWN.description">
			Description	
					<input type="image" src="Up-48.png" alt="Submit" width="15px"
					height="15px" value="SORTUP.description" name="SORTUP.description">
				</td>
				<td>
					<input type="image" src="Down-48.png" alt="Submit" width="15px"
					height="15px" value="SORTDOWN.nodeSet" name="SORTDOWN.nodeSet">
				Node set
					<input type="image" src="Up-48.png" alt="Submit" width="15px"
					height="15px" value="SORTUP.nodeSet" name="SORTUP.nodeSet">
				</td>
				<td></td>
				<td></td>
				<td></td>
				<%-- <td></td>--%>
			</tr>
			<c:set var="index" value="-1" scope="page" />
			<c:forEach items="${nodeList}" var="node">
			    <c:set var="index" value="${index + 1}" scope="page"/>
				<tr>
					<td><c:out value="${node.nodeName}" /></td>
					<td><c:out value="${node.nodeState.state}" /></td>
					<td><c:out value="${node.description}" /></td>
					<td><c:out value="${node.nodeSet.nodeSetName}" /></td>
					<td><input type="checkbox" name="choices" value="CHK.${node.nodeName}"> </input></td>
					<td class="tg-yw4l"><input type="submit" value="Del" name=DEL.${node.nodeName}></td>
					<td class="tg-yw4l"><input type="submit" value="Edit" name=ED.${index}></td> 
					<%--<td class="tg-yw4l"><input type="submit" value="Toggle" name=TGL.${index}></td> --%> 
				</tr>
			</c:forEach>
			<tr>
				<td><input type="submit" value="Back" name="Back" /></td>
				<td><input type="submit" value="New" name="New" /></td>
				<td><input type="submit" value="Refresh" name="Refresh" /></td>
				<td></td>
				<td><input type="submit" value="Toggle" name="Toggle" /></td>
				<td></td>
				<td></td>
				<%-- <td></td> --%>
			</tr>
		</table>
	</form>
</body>
</html>
