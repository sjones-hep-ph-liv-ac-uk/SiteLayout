<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
	

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/SiteLayout.css">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<title>Login</title>

<script>

function isNumber(value) {
  return !isNaN(value) ;
}

function isInt(value) {
  return !isNaN(value) && parseInt(Number(value)) == value
    && !isNaN(parseInt(value, 10));
}
function validateForm() {
  var database = document.forms["LoginForm"]["database"].value;
  if (database == "") {
    alert("DB Name must be filled out");
    return false;
  }
  var username = document.forms["LoginForm"]["username"].value;
  if (username == "") {
    alert("User Name must be filled out");
    return false;
  }
  
  var password = document.forms["LoginForm"]["password"].value;
  if (password == "") {
    alert("Password must be filled out");
    return false;
  }  
}
</script>


</head>
<body>
	Login
	
	<form name="LoginForm" action="LoginController" method="post" onsubmit="return validateForm()">
		Database name : <input type="text" name="database"> <BR>
		User name : <input type="text" name="username"> <BR>
		Password : <input type="password" name="password"> <BR> <input type="submit" value="Submit" />
	</form>
</body>
</html>

