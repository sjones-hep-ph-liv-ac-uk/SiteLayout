<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.basingwerk.sldb.mvc.model.NodeSet"%>
<%@page import="com.basingwerk.sldb.mvc.model.NodeType"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.List"%>
<%
	List nodeTypeList = (List) request.getAttribute("nodeTypeList");
%>
<%
	List clusterList = (List) request.getAttribute("clusterList");
%>
<%
	NodeSet nodeSet = (NodeSet) request.getAttribute("nodeSet");
%>

<html>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/SiteLayout.css">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Edit Nodeset</title>
</head>

<body>
	<form action="EditNodeSetController" method="post">
		Node set name : 
		   <input type="text" name="nodeSetName" readonly value="${nodeSet.nodeSetName}"> <BR> 
		Node count : 
		   <input type="text" name="nodeCount" value="${nodeSet.nodeCount}"> <BR>
		Node type : <select name='nodeTypeList'>
			<c:forEach items="${nodeTypeList}" var="nt">
				<!-- <option  value="${nt}">${nt}</option> -->
				<option value="${nt}"
					${nt == nodeSet.nodeType.nodeTypeName ? 'selected="selected"' : ''}>${nt}</option>
			</c:forEach>
		</select><BR> Cluster : <select name='clusterList'>
			<c:forEach items="${clusterList}" var="cn">
				<!--  <option value="${cn}">${cn}</option> -->
				<option value="${cn}"
					${cn == nodeSet.cluster.clusterName ? 'selected="selected"' : ''}>${cn}</option>
			</c:forEach>
		</select><BR> <input type="submit" value="Submit" />
	</form>
</body>
</html>

