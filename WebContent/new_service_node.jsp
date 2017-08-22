<%@ page import="com.basingwerk.sldb.mvc.model.Node"%>
<%@ page import="java.util.List"%>

<html>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/SiteLayout.css">

<head>
<title>New Service Node</title>
<script>
function isNumber(value) {
  return !isNaN(value) ;
}

function isInt(value) {
  return !isNaN(value) && parseInt(Number(value)) == value
    && !isNaN(parseInt(value, 10));
}

function validateForm() {
  var hostname = document.forms["NewServiceNodeForm"]["hostname"].value;
  if (hostname == "") {
    alert("hostname must be filled out");
    return false;
  }
  var cpu = document.forms["NewServiceNodeForm"]["cpu"].value;
  if (! isInt(cpu)) {
    alert("cpu must be integer");
    return false;
  }
  var mem = document.forms["NewServiceNodeForm"]["mem"].value;
  if (! isInt(mem)) {
    alert("mem must be integer");
    return false;
  }
  var os = document.forms["NewServiceNodeForm"]["os"].value;
  if (os == "") {
    alert("os must be filled out");
    return false;
  }
  var kernel = document.forms["NewServiceNodeForm"]["kernel"].value;
  if (kernel == "") {
    alert("kernel must be filled out");
    return false;
  }
  var service = document.forms["NewServiceNodeForm"]["service"].value;
  if (service == "") {
    alert("service must be filled out");
    return false;
  }
  var comment = document.forms["NewServiceNodeForm"]["comment"].value;
  if (comment == "") {
    alert("comment must be filled out");
    return false;
  }
}

</script>
</head>
<body>
  New Service Node
  
  <form name="NewServiceNodeForm" action="NewServiceNodeController"
    method="post" onsubmit="return validateForm()">

    hostname : <input type="text" name="hostname"><br>
    hostSystemName : <select name='hostSystemList'>
      <c:forEach items="${hostSystemList}" var="hostSystem">
         <option value="${hostSystem.hostname}">${hostSystem.hostname}</option>
      </c:forEach>
    </select><br>
    clusterName : <select name='clusterList'>
      <c:forEach items="${clusterList}" var="cluster">
         <option value="${cluster.clusterName}">${cluster.clusterName}</option>
      </c:forEach>
    </select><br>
    cpu : <input type="text" name="cpu"> <br>
    mem : <input type="text" name="mem"> <br>
    os : <input type="text" name="os"> <br>
    kernel : <input type="text" name="kernel"> <br>
    service : <input type="text" name="service"> <br>
    comment : <input type="text" name="comment"> <br>
    <br> <input type="submit" value="Submit" />
  </form>
</body>
</html>
