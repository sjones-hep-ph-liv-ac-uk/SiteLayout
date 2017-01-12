<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/SiteLayout.css">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>New Nodetype</title>
</head>
<body>
	New Nodetype
	<form action="NewNodeTypeController" method="post">
		Nodetype name : <input type="text" name="nodeTypeName"> <BR>
		CPUs per node : <input type="text" name="cpu"> <BR> 
                Slots per node : <input type="text" name="slot"> <BR> 
                HS06 per slot : <input type="text" name="hs06PerSlot"> <BR> 
                GBs per node : <input type="text" name="memPerNode"> <BR> 
                               <input type="submit" />
	</form>
</body>
</html>

