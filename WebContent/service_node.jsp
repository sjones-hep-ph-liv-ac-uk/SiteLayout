<%@ page import="java.util.List"%>
<% List serviceNodeList = (List) session.getAttribute("serviceNodeList"); %>

<html> 
  <link rel="stylesheet" href="${pageContext.request.contextPath}/SiteLayout.css">
<head> <title>Service nodes</title> </head>

<body>
	Service nodes

	<form action="ServiceNodeController" method="post">
		<table class="tg">
			<tr>

				<td>
					<input type="image" src="Down-48.png" alt="Submit" width="15px"
					height="15px" value="SORTDOWN.hostname" name="SORTDOWN.hostname">
				hostname
					<input type="image" src="Up-48.png" alt="Submit" width="15px"
					height="15px" value="SORTUP.hostname" name="SORTUP.hostname">
				</td>

				<td>
					<input type="image" src="Down-48.png" alt="Submit" width="15px"
					height="15px" value="SORTDOWN.hostSystemName" name="SORTDOWN.hostSystem">
				hostSystemName
					<input type="image" src="Up-48.png" alt="Submit" width="15px"
					height="15px" value="SORTUP.hostSystemName" name="SORTUP.hostSystem">
				</td>

				<td>
					<input type="image" src="Down-48.png" alt="Submit" width="15px"
					height="15px" value="SORTDOWN.clusterName" name="SORTDOWN.cluster">
				clusterName
					<input type="image" src="Up-48.png" alt="Submit" width="15px"
					height="15px" value="SORTUP.clusterName" name="SORTUP.cluster">
				</td>

				<td>
					<input type="image" src="Down-48.png" alt="Submit" width="15px"
					height="15px" value="SORTDOWN.cpu" name="SORTDOWN.cpu">
				cpu
					<input type="image" src="Up-48.png" alt="Submit" width="15px"
					height="15px" value="SORTUP.cpu" name="SORTUP.cpu">
				</td>

				<td>
					<input type="image" src="Down-48.png" alt="Submit" width="15px"
					height="15px" value="SORTDOWN.mem" name="SORTDOWN.mem">
				mem
					<input type="image" src="Up-48.png" alt="Submit" width="15px"
					height="15px" value="SORTUP.mem" name="SORTUP.mem">
				</td>

				<td>
					<input type="image" src="Down-48.png" alt="Submit" width="15px"
					height="15px" value="SORTDOWN.os" name="SORTDOWN.os">
				os
					<input type="image" src="Up-48.png" alt="Submit" width="15px"
					height="15px" value="SORTUP.os" name="SORTUP.os">
				</td>

				<td>
					<input type="image" src="Down-48.png" alt="Submit" width="15px"
					height="15px" value="SORTDOWN.kernel" name="SORTDOWN.kernel">
				kernel
					<input type="image" src="Up-48.png" alt="Submit" width="15px"
					height="15px" value="SORTUP.kernel" name="SORTUP.kernel">
				</td>
				
				<td>
					<input type="image" src="Down-48.png" alt="Submit" width="15px"
					height="15px" value="SORTDOWN.service" name="SORTDOWN.service">
				service
					<input type="image" src="Up-48.png" alt="Submit" width="15px"
					height="15px" value="SORTUP.service" name="SORTUP.service">
				</td>
				
				<td>
					<input type="image" src="Down-48.png" alt="Submit" width="15px"
					height="15px" value="SORTDOWN.comment" name="SORTDOWN.comment">
				comment
					<input type="image" src="Up-48.png" alt="Submit" width="15px"
					height="15px" value="SORTUP.comment" name="SORTUP.comment">
				</td>
				<td></td>
				<td></td>
				
			</tr>
			<c:set var="index" value="-1" scope="page" />
			<c:forEach items="${serviceNodeList}" var="serviceNode">
			    <c:set var="index" value="${index + 1}" scope="page"/>
				<tr>
					<td><c:out value="${serviceNode.hostname}" /></td>
					<td><c:out value="${serviceNode.hostSystem.hostname}" /></td>
					<td><c:out value="${serviceNode.cluster.clusterName}" /></td>
					<td><c:out value="${serviceNode.cpu}" /></td>
					<td><c:out value="${serviceNode.mem}" /></td>
					<td><c:out value="${serviceNode.os}" /></td>
					<td><c:out value="${serviceNode.kernel}" /></td>
					<td><c:out value="${serviceNode.service}" /></td>
					<td><c:out value="${serviceNode.comment}" /></td>
					<td class="tg-yw4l"><input type="submit" value="Del" name=DEL.${serviceNode.hostname}></td>
					<td class="tg-yw4l"><input type="submit" value="Edit" name=ED.${index}></td> 
				</tr>
			</c:forEach>
			<tr>
				<td><input type="submit" value="Back" name="Back" /></td>
				<td><input type="submit" value="New" name="New" /></td>
				<td><input type="submit" value="Refresh" name="Refresh" /></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
			</tr>
		</table>
	</form>
</body>
</html>
