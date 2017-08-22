<%@ page import="com.basingwerk.sldb.mvc.model.Installation"%>
<%@ page import="java.util.List"%>

<html>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/SiteLayout.css">

<head>
<title>Edit Installation</title>
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
  var softwareVersion = document.forms["EditInstallationForm"]["softwareVersion"].value;
  if (softwareVersion == "") {
    alert("Software version must be filled out");
    return false;
  }
}
</script>
</head>

<body>
  Edit Installation
  <form name="EditInstallationForm" action="EditInstallationController"
    method="post" onsubmit="return validateForm()">

    Service : <input type="text" readonly disabled name="service" value="${installation.service.serviceName}"> <br>
    Service node : <input type="text" readonly disabled name="serviceNode" value="${installation.serviceNode.hostname}"> <br>
    Software version : <input type="text" name="softwareVersion" value="${installation.softwareVersion}"> <br>
    </select><br> <input type="submit" value="Submit" />
  </form>
</body>
</html>

