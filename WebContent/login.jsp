<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/SiteLayout.css">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<title>Login</title>
</head>
<body>
	Login
	<form action="LoginController" method="post">
		Database name : <input type="text" name="database"> <BR>
		User name : <input type="text" name="username"> <BR>
		Password : <input type="password" name="password"> <BR> <input
			type="submit" />
	</form>
</body>
</html>

