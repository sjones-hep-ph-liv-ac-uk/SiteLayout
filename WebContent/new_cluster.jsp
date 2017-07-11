<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.basingwerk.sldb.mvc.model.ClusterSet"%>
<%@ page import="java.util.List"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<% 	List clusterSetList = (List) request.getAttribute("clusterSetList"); %>

<html>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/SiteLayout.css">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>New Cluster</title>

<script>

function isNumber(value) {
  return !isNaN(value) ;
}

function isInt(value) {
  return !isNaN(value) && parseInt(Number(value)) == value
    && !isNaN(parseInt(value, 10));
}
function validateForm() {
  var clusterName = document.forms["NewClusterForm"]["clusterName"].value;
  if (clusterName == "") {
    alert("Name must be filled out");
    return false;
  }
  var descr = document.forms["NewClusterForm"]["descr"].value;
  if (descr == "") {
    alert("Description must be filled out");
    return false;
  }
}
</script>

</head>
<body>
	New Cluster
	<form name="NewClusterForm" action="NewClusterController" method="post" onsubmit="return validateForm()">
		Cluster Name : <input type="text" name="clusterName"> <BR>
		Cluster Description : <input type="text" name="descr"> <BR>
		Cluster set : <select name='clusterSetList'>
			<c:forEach items="${clusterSetList}" var="s">
			  <option value="${s.clusterSetName}">${s.clusterSetName}</option>
			</c:forEach>
		</select>
		<BR>
		<input type="submit" value="Submit" />
	</form>
</body>
</html>
