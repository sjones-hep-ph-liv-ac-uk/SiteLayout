<%@ page import="java.util.List"%>

<% List clusterList = (List)request.getAttribute("clusterList");%>

<html>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/SiteLayout.css">

<head>

<title>Cluster</title>
</head>
<body>
	Cluster

	<form action="ClusterController" method="post">

		<table class="tg">
			<tr>
				<td>
					<input type="image" src="Down-48.png" alt="Submit" width="15px"
					height="15px" value="SORTDOWN.clusterName" name="SORTDOWN.clusterName">
				Cluster name
					<input type="image" src="Up-48.png" alt="Submit"
					width="15px" height="15px" value="SORTUP.clusterName"
					name="SORTUP.clusterName">
				</td>
				<td>
					<input type="image" src="Down-48.png" alt="Submit" width="15px"
					height="15px" value="SORTDOWN.descr" name="SORTDOWN.descr">
				Description
					<input type="image" src="Up-48.png" alt="Submit"
					width="15px" height="15px" value="SORTUP.descr"
					name="SORTUP.descr">
				</td>
				
				<td>
					<input type="image" src="Down-48.png" alt="Submit" width="15px"
					height="15px" value="SORTDOWN.clusterSet" name="SORTDOWN.clusterSet">
				Cluster set name
					<input type="image" src="Up-48.png" alt="Submit"
					width="15px" height="15px" value="SORTUP.clusterSet" name="SORTUP.clusterSet">
				</td>
				
				<td></td>
				<td></td>
			</tr>
			<c:set var="index" value="-1" scope="page" />
			<c:forEach items="${clusterList}" var="cluster">
			    <c:set var="index" value="${index + 1}" scope="page"/>
				<tr>
					<td><c:out value="${cluster.clusterName}" /></td>
					<td><c:out value="${cluster.descr}" /></td>
					<td><c:out value="${cluster.clusterSet.clusterSetName}" /></td>
					<td class="tg-yw4l"><input type="submit" value="Del"
						name=DEL.${cluster.clusterName}></td>
					<td class="tg-yw4l"><input type="submit" value="Edit"
						name=ED.${index}></td>  <!--  ${cluster.clusterName} -->
				</tr>
			</c:forEach>
			<tr>
				<td><input type="submit" value="Back" name="Back" /></td>
				<td><input type="submit" value="New" name="New" /></td>
				<td><input type="submit" value="Refresh" name="Refresh" /></td>
				<td></td>
				<td></td>
			</tr>

		</table>
	</form>
</body>
</html>
