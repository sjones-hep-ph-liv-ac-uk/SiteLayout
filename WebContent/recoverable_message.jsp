<% String theMessage = (String) request.getAttribute("theMessage"); %>
<% String theJsp = (String) request.getAttribute("theJsp"); %>

<html> 
  <link rel="stylesheet" href="${pageContext.request.contextPath}/SiteLayout.css">
<head> <title>Message</title> </head>
<body>
	Message
	<BR> Some error happened. ${theMessage}
	<br>
	<a href="${theJsp}">Go someplace else ... </a>
</body>
</html>
