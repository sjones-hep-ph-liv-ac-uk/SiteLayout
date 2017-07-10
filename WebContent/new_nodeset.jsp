<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.basingwerk.sldb.mvc.model.NodeSet"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.List"%>
<%
    List nodeTypeList = (List) request.getAttribute("nodeTypeList");
	List clusterList = (List) request.getAttribute("clusterList");
%>
<!-- <c:set var="baseline" value="BASELINE" /> -->

<html>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/SiteLayout.css">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>New Node Set</title>
<script>
	function isInt(value) {
		return !isNaN(value) && parseInt(Number(value)) == value
				&& !isNaN(parseInt(value, 10));
	}
	function validateForm() {
		var nodeSetName = document.forms["NodeSetController"]["nodeSetName"].value;
		if (nodeSetName == "") {
			alert("Node set name must be filled out");
			return false;
		}
		var nodeCount = document.forms["NodeSetController"]["nodeCount"].value;
		if (! isInt(nodeCount)) {
			alert("Node count must be an integer value");
			return false;
		}
	}
</script>
</head>
<body>
	New Node Set
	<form name="NodeSetController" action="NewNodeSetController"
		method="post" onsubmit="return validateForm()">

		Node set name : <input type="text" name="nodeSetName"> <BR>
		Node count : <input type="text" name="nodeCount"> <BR>
		Node type : <select name='nodeTypeList'>
			<c:forEach items="${nodeTypeList}" var="nt">
				<c:if test="${nt.nodeTypeName != 'BASELINE'}">
					<option value="${nt.nodeTypeName}">${nt.nodeTypeName}</option>
				</c:if>
			</c:forEach>
		</select> </select><BR> Cluster : <select name='clusterList'>
			<c:forEach items="${clusterList}" var="cn">
				<option value="${cn.clusterName}">${cn.clusterName}</option>
			</c:forEach>
		</select><BR> <input type="submit" value="Submit" />
	</form>
</body>
</html>
