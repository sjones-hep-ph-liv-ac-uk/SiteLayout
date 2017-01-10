<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.basingwerk.sldb.mvc.model.NodeSet"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.List"%>
<%
	List nodeTypeList = (List) request.getAttribute("nodeTypeList");
%>
<%
	List clusterList = (List) request.getAttribute("clusterList");
%>
<!-- <c:set var="baseline" value="BASELINE" /> -->

<html>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/SiteLayout.css">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>New Node Set</title>
<body>
	New Node Set

	<form action="NewNodeSetController" method="post">

		Node set name : <input type="text" name="nodeSetName"> <BR>
		Node count : <input type="text" name="nodeCount"> <BR>
		Node type : <select name='nodeTypeList'>
			<c:forEach items="${nodeTypeList}" var="nt">
				<c:if test="${nt != 'BASELINE'}">
					<option value="${nt}">${nt}</option>
				</c:if>
			</c:forEach>
		</select> </select><BR> Cluster : <select name='clusterList'>
			<c:forEach items="${clusterList}" var="cn">
				<option value="${cn}">${cn}</option>
			</c:forEach>
		</select><BR> <input type="submit" />
	</form>
</body>
</html>
