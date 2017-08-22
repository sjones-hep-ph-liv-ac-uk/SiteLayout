<%@ page import="com.basingwerk.sldb.mvc.model.ServiceNode"%>
<%@ page import="java.util.List"%>

<html>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/SiteLayout.css">

<head>
  <title>Edit Service Node</title>
<script>
function isNumber(value) {
  return !isNaN(value) ;
}

function isInt(value) {
  return !isNaN(value) && parseInt(Number(value)) == value
    && !isNaN(parseInt(value, 10));
}

function validateForm() {
  var hostname = document.forms["EditServiceNodeForm"]["hostname"].value;
  if (hostname == "") {
    alert("hostname must be filled out");
    return false;
  }
  var cpu = document.forms["EditServiceNodeForm"]["cpu"].value;
  if (! isInt(cpu)) {
    alert("cpu must be integer");
    return false;
  }
  var mem = document.forms["EditServiceNodeForm"]["mem"].value;
  if (! isInt(mem)) {
    alert("mem must be integer");
    return false;
  }
  var os = document.forms["EditServiceNodeForm"]["os"].value;
  if (os == "") {
    alert("os must be filled out");
    return false;
  }
  var kernel = document.forms["EditServiceNodeForm"]["kernel"].value;
  if (kernel == "") {
    alert("kernel must be filled out");
    return false;
  }
  var service = document.forms["EditServiceNodeForm"]["service"].value;
  if (service == "") {
    alert("service must be filled out");
    return false;
  }
  var comment = document.forms["EditServiceNodeForm"]["comment"].value;
  if (comment == "") {
    alert("comment must be filled out");
    return false;
  }
}

</script>
</head>
<body>
  Edit Service Node
  
  <form name="EditServiceNodeForm" action="EditServiceNodeController"
    method="post" onsubmit="return validateForm()">

    hostname : <input type="text" name="hostname" readonly value="${serviceNode.hostname}"><br>
    hostSystemName : <select name='hostSystemList'>
      <c:forEach items="${hostSystemList}" var="hostSystem">
         <option value="${hostSystem.hostname}"
         ${hostSystem.hostname == serviceNode.hostSystem.hostname  ? 'selected="selected"' : ''}
            >${hostSystem.hostname}</option>
      </c:forEach>
    </select><br>
    clusterName : <select name='clusterList'>
      <c:forEach items="${clusterList}" var="cluster">
         <option value="${cluster.clusterName}"
              ${cluster.clusterName == serviceNode.cluster.clusterName ? 'selected="selected"' : ''}         
           >${cluster.clusterName}</option>
      </c:forEach>
    </select><br>
    cpu : <input type="text" name="cpu" value="${serviceNode.cpu}"> <br>
    mem : <input type="text" name="mem" value="${serviceNode.mem}"> <br>
    os : <input type="text" name="os" value="${serviceNode.os}"> <br>
    kernel : <input type="text" name="kernel" value="${serviceNode.kernel}"> <br>
    service : <input type="text" name="service" value="${serviceNode.service}"> <br>
    comment : <input type="text" name="comment" value="${serviceNode.comment}"> <br>
    <br> <input type="submit" value="Submit" />
  </form>
</body>
</html>
