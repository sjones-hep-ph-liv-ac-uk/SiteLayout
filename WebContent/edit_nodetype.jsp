<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.basingwerk.sldb.mvc.model.NodeType"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	NodeType nodeType = (NodeType) request.getAttribute("nodeType");
%>

<html>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/SiteLayout.css">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Edit Node Type</title>
</head>
<body>
	Edit Nodetype
	<form action="EditNodeTypeController" method="post">
		Nodetype name : <input type="text" name="nodeTypeName" readonly value="${nodeType.nodeTypeName}"> <BR> 
                CPU per node : <input type="text" name="cpu" value="${nodeType.cpu}"> <BR>
		Slots per node : <input type="text" name="slot" value="${nodeType.slot}"> <BR> 
                HS06 per slot : <input type="text" name="hs06PerSlot" value="${nodeType.hs06PerSlot}"> <BR> 
                GBs per node : <input type="text" name="memPerNode" value="${nodeType.memPerNode}"> <BR> 
                               <input type="submit" />
	</form>
</body>
</html>
