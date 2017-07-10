<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.HashMap"%>
<%@page import="com.basingwerk.sldb.mvc.model.NodeType"%>
<%@page import="com.basingwerk.sldb.mvc.model.NodeSetNodeTypeJoin"%>

<%
	List nodeTypeList = (List) request.getAttribute("nodeTypeList");
%>
<%
	NodeType baseline = (NodeType) request.getAttribute("baseline");
%>
<%
	List clusterList = (List) request.getAttribute("clusterList");
%>
<%
	HashMap joinMap = (HashMap) request.getAttribute("joinMap");
%>

<html>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/SiteLayout.css">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Reports</title>
</head>
<body>
	Report
	<br>

	<form action="main_screen.jsp" method="post">
		Node types:
		<table class="tg">
			<tr>
				<td>Node type name</td>
				<td>CPUs per node</td>
				<td>Slots per node</td>
				<td>HS06 per slot</td>
				<td>GB per node</td>
				<td>GB per slot</td>
				<td>Scale factor</td>
			</tr>
			<tr>
				<td><c:out value="${baseline.nodeTypeName}" /></td>
				<td><c:out value="${baseline.cpu}" /></td>
				<td><c:out value="${baseline.slot}" /></td>
				<td><c:out value="${baseline.hs06PerSlot}" /></td>
				<td><c:out value="${baseline.memPerNode}" /></td>
				<td><c:out value="undefined" /></td>
				<td>0.0</td>
			</tr>

			<c:forEach items="${nodeTypeList}" var="nodeType">
				<c:if test="${nodeType.nodeTypeName != 'BASELINE'}">
					<tr>
						<td><c:out value="${nodeType.nodeTypeName}" /></td>
						<td><c:out value="${nodeType.cpu}" /></td>
						<td><c:out value="${nodeType.slot}" /></td>
						<td><c:out value="${nodeType.hs06PerSlot}" /></td>
						<td><c:out value="${nodeType.memPerNode}" /></td>
						<td><fmt:formatNumber type="number" groupingUsed="0" maxFractionDigits="4" value="${nodeType.memPerNode / nodeType.slot}" /></td>
						<td><fmt:formatNumber type="number" groupingUsed="0" maxFractionDigits="4" value="${nodeType.hs06PerSlot / baseline.hs06PerSlot}" /></td>
					</tr>
				</c:if>
			</c:forEach>
		</table>

		<c:set var="clusterSetHs06" value="0.0" />
		<c:set var="clusterSetLogicalCpus" value="0" />

		<c:forEach items="${clusterList}" var="cluster">
			<br>
  Cluster:  ${cluster.clusterName}<br> 
			<c:set var="clusterHs06" value="0.0" />
			<c:set var="clusterPhysicalCpus" value="0" />
			<c:set var="clusterLogicalCpus" value="0" />
			<table class="tg">
				<tr>
					<td>Node Set</td>
					<td>Node Type</td>
					<td>Node Count</td>
					<td>CPUs in Node</td>
					<td>Slots per Node</td>
					<td>HS06 per Slot</td>
					<td>HS06</td>
				</tr>
				<c:forEach items="${joinMap[cluster.clusterName]}" var="joinrecord">
					<tr>
						<td><c:out value="${joinrecord.nodeSetName}" /></td>
						<td><c:out value="${joinrecord.nodeTypeName}" /></td>
						<td><c:out value="${joinrecord.nodeCount}" /></td>
						<td><c:out value="${joinrecord.cpu}" /></td>
						<td><c:out value="${joinrecord.slot}" /></td>
						<td><c:out value="${joinrecord.hs06PerSlot}" /></td>
						<td><fmt:formatNumber type="number" groupingUsed="0"
								maxFractionDigits="4"
								value="${joinrecord.hs06PerSlot * joinrecord.slot * joinrecord.nodeCount}" /></td>

						<c:set var="clusterHs06"
							value="${clusterHs06 +  joinrecord.hs06PerSlot * joinrecord.slot * joinrecord.nodeCount}" />
						<c:set var="clusterPhysicalCpus"
							value="${clusterPhysicalCpus + joinrecord.cpu * joinrecord.nodeCount}" />
						<c:set var="clusterLogicalCpus"
							value="${ clusterLogicalCpus + joinrecord.slot * joinrecord.nodeCount}" />
					</tr>
				</c:forEach>
			</table>

			<c:set var="clusterSetHs06" value="${clusterSetHs06 + clusterHs06}" />
			<c:set var="clusterSetLogicalCpus"
				value="${clusterSetLogicalCpus + clusterLogicalCpus}" />

  Cluster properties:
  <table class="tg">
				<tr>
					<td>HS06</td>
					<td><c:out value="" /> <fmt:formatNumber groupingUsed="0"
							type="number" maxFractionDigits="2" value="${clusterHs06}" /></td>
				</tr>
				<tr>
					<td>Physical CPUs</td>
					<td><c:out value="${clusterPhysicalCpus}" /></td>
				</tr>
				<tr>
					<td>Logical CPUs (slots)</td>
					<td><c:out value="${clusterLogicalCpus}" /></td>
				</tr>
				<tr>
					<td>Cores</td>
					<td><fmt:formatNumber type="number" groupingUsed="0"
							maxFractionDigits="3"
							value="${clusterLogicalCpus / clusterPhysicalCpus}" /></td>
				</tr>
				<tr>
					<td>Benchmark</td>
					<td><fmt:formatNumber type="number" groupingUsed="0"
							maxFractionDigits="3" value="${clusterHs06 / clusterLogicalCpus}" /></td>
				</tr>
				<tr>
					<td>CE_SI00/cpu_scaling_reference_si00</td>
					<td><fmt:formatNumber type="number" groupingUsed="0"
							maxFractionDigits="0"
							value="${clusterHs06 / clusterLogicalCpus * 250.0}" /></td>
				</tr>
				<tr>
					<td>Reference/benchmark_value</td>
					<td><fmt:formatNumber type="number" groupingUsed="0"
							maxFractionDigits="3" value="${baseline.hs06PerSlot * 250}" /></td>
				</tr>
			</table>
		</c:forEach>
		<br> Cluster set properties:
		<table class="tg">
			<tr>
				<td>Cluster set HS06</td>
				<td><c:out value="" /> <fmt:formatNumber groupingUsed="0"
						type="number" maxFractionDigits="2" value="${clusterSetHs06}" /></td>
			<tr>
				<td>Cluster set slots</td>
				<td><c:out value="" /> <fmt:formatNumber groupingUsed="0"
						type="number" maxFractionDigits="0" value="${clusterSetLogicalCpus}" /></td>
		</table>

		<input type="submit" value="Back" name="Back" />
	</form>
</body>
</html>
