<%@ page import="com.basingwerk.sldb.mvc.model.Installation"%>
<%@ page import="java.util.List"%>
<%  List serviceNodeList = (List) request.getAttribute("serviceNodeList"); %>
<%  List serviceList = (List) request.getAttribute("serviceList"); %>

<html>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/SiteLayout.css">

<head>
<title>New Installation</title>
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
  var softwareVersion = document.forms["NewInstallationForm"]["softwareVersion"].value;
  if (softwareVersion == "") {
    alert("Software version must be filled out");
    return false;
  }
}
</script>
</head>

<body>
  New Installation
  <form name="NewInstallationForm" action="NewInstallationController"
    method="post" onsubmit="return validateForm()">

    Service node : <select name='serviceNodeList'>
      <c:forEach items="${serviceNodeList}" var="sn">
        <option value="${sn.hostname}">${sn.hostname}</option>
      </c:forEach>
    </select><br> 
    Service : <select name='serviceList'>
      <c:forEach items="${serviceList}" var="s">
        <option value="${s.serviceName}">${s.serviceName}</option>
      </c:forEach>
    </select><br> 
    Software version : <input type="text" name="softwareVersion"> <br>
    </select><br> <input type="submit" value="Submit" />
  </form>
</body>
</html>
