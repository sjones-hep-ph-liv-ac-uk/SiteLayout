<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/SiteLayout.css">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>New Cluster</title>
</head>
<body>
	New Cluster
	<form action="NewClusterController" method="post">
		Cluster Name : <input type="text" name="clusterName"> <BR>
		Cluster Description : <input type="text" name="descr"> <BR>
		<input type="submit" />
	</form>
</body>
</html>
