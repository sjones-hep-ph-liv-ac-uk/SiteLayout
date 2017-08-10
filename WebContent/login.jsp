<html>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/SiteLayout.css">
<head>

<title>Login</title>

<!-- <script src="valfuncs.js"></script> -->
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

