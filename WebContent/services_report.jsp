<%@ page import="java.util.List"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="com.basingwerk.sldb.mvc.model.ServiceNode"%>

<% List installationList  = (List) request.getAttribute("installationList"); %>

<html>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/SiteLayout.css">
<head>
<title>Services Report</title>
</head>

<body>
	Services  Report
	<br>
	<form action="main_screen.jsp" method="post">
		All service installations:
		<table class="tg">
		<td>Service Installation</td>
		<td>Service Node</td>
		<td>Cpus</td>
		<td>Mem</td>
		<td>Cluster</td>
		<td>VM Host System</td>
		
			<c:forEach items="${installationList}" var="installation">
					<tr>
						<td><c:out value="${installation.service.serviceName}" /></td>
						<td><c:out value="${installation.serviceNode.hostname}" /></td>
						<td><c:out value="${installation.serviceNode.cpu}" /></td>
						<td><c:out value="${installation.serviceNode.mem}" /></td>
						<td><c:out value="${installation.serviceNode.cluster.clusterName}" /></td>
						<td><c:out value="${installation.serviceNode.hostSystem.hostname}" /></td>
					</tr>
			</c:forEach>
		</table>
		<input type="submit" value="Back" name="Back" />
	</form>
</body>
</html>


<%-- 						<td><c:out value="${installation.cpu}" /></td>
						<td><c:out value="${installation.mem}" /></td>
						<td><c:out value="${installation.hs06PerSlot}" /></td>
						<td><c:out value="${installation.memPerNode}" /></td>
						<td><fmt:formatNumber type="number" groupingUsed="0"
								maxFractionDigits="4"
								value="${installation.memPerNode / installation.slot}" /></td>
						<td><fmt:formatNumber type="number" groupingUsed="0"
								maxFractionDigits="4"
								value="${installation.hs06PerSlot / baseline.hs06PerSlot}" /></td> --%>
