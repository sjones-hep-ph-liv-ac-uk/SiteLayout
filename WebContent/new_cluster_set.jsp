<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/SiteLayout.css">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>New Cluster Set</title>
</head>
<body>
	New Cluster Set
	<form action="NewClusterSetController" method="post">
		Cluster set name : <input type="text" name="clusterSetName"> <BR>
		Description : <input type="text" name="description"> <BR>
		Location : <input type="text" name="location"> <BR>
		Longitude : <input type="text" name="longitude"> <BR>
		Latitude : <input type="text" name="latitude"> <BR>
		Admin : <input type="text" name="admin"> <BR>
        <input type="submit" value="Submit" />
 	</form>
</body>
</html>

