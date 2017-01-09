<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/SiteLayout.css">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<title>Site Layout Database</title>
</head>
<body>
	Welcome ${requestScope['user'].username}.
	<form action="MainScreenController" method="post">

		<table class="tg">
			<tr>
				<th class="tg-yw4l">Main Screen</th>
			</tr>
			<tr>
				<td class="tg-yw4l"><input type="submit"
					value="Edit node types" name="SomeButton" /></td>
			</tr>
			<tr>
				<td class="tg-yw4l"><input type="submit" value="Edit clusters"
					name="SomeButton" /></td>
			</tr>
			<tr>
				<td class="tg-yw4l"><input type="submit" value="Edit node sets"
					name="SomeButton" /></td>
			</tr>
			<tr>
				<td class="tg-yw4l"><input type="submit" value="Reports"
					name="SomeButton" /></td>
			</tr>
		</table>
	</form>
</body>
</html>

<!-- <style type="text/css">
.tg  {border-collapse:collapse;border-spacing:0;}
.tg td{font-family:Arial, sans-serif;font-size:14px;padding:10px 5px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal;}
.tg th{font-family:Arial, sans-serif;font-size:14px;font-weight:normal;padding:10px 5px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal;}
.tg .tg-yw4l{vertical-align:top}
</style> -->
