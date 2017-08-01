<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.basingwerk.sldb.mvc.model.Node"%>
<%@page import="com.basingwerk.sldb.mvc.model.NodeState"%>
<%@page import="com.basingwerk.sldb.mvc.model.NodeSet"%>

<%-- @page import="com.basingwerk.sldb.mvc.model.NodeType" --%>

<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.List"%>

<%	Node  node = (Node) request.getAttribute("node"); %>
<%	NodeSet nodeSet = (NodeSet) request.getAttribute("nodeSet"); %>
<%	NodeState nodeState = (NodeState) request.getAttribute("nodeState"); %>

<html>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/SiteLayout.css">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Edit Node</title>
<script>

function isNumber(value) {
  return !isNaN(value) ;
}

function isInt(value) {
  return !isNaN(value) && parseInt(Number(value)) == value
    && !isNaN(parseInt(value, 10));
}
function validateForm() {
  var nodeName = document.forms["EditNodeForm"]["nodeName"].value;
  if (nodeName == "") {
    alert("Node name must be filled out");
    return false;
  }
}
</script>

</head>

<body>
<%-- node is <%= node %> --%>

	<form name="EditNodeForm" action="EditNodeController" method="post" onsubmit="return validateForm()">
		Node  name : 
		   <input type="text" name="nodeName" readonly value="${node.nodeName}"> <BR> 
		Node description : 
		   <input type="text" name="nodeDescription" value="${node.description}"> <BR>
		Node set : <select name='nodeSetList'>
			<c:forEach items="${nodeSetList}" var="ns">
				<option value="${ns.nodeSetName}"
					${ns.nodeSetName == node.nodeSet.nodeSetName  ? 'selected="selected"' : ''}>${ns.nodeSetName}</option>
			</c:forEach>
		</select><BR> 
		State : <select name='nodeStateList'>
			<c:forEach items="${nodeStateList}" var="nodeState">
				<option value="${nodeState.state}"
					${nodeState.state == node.nodeState.state ? 'selected="selected"' : ''}>${nodeState.state}</option>
			</c:forEach>
		</select><BR> <input type="submit" value="Submit" />
	</form>
</body>
</html>
