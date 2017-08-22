<%@ page import="com.basingwerk.sldb.mvc.model.ClusterSet"%>
<%@ page import="java.util.List"%>
<% List clusterSetList = (List) request.getAttribute("clusterSetList"); %>

<html> 
  <link rel="stylesheet" href="${pageContext.request.contextPath}/SiteLayout.css">
<head> <title>New Cluster</title> 

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
  var clusterName = document.forms["NewClusterForm"]["clusterName"].value;
  if (clusterName == "") {
    alert("Name must be filled out");
    return false;
  }
  var descr = document.forms["NewClusterForm"]["descr"].value;
  if (descr == "") {
    alert("Description must be filled out");
    return false;
  }
}
</script>

</head>
<body>
  New Cluster
  <form name="NewClusterForm" action="NewClusterController" method="post" onsubmit="return validateForm()">
    Cluster Name : <input type="text" name="clusterName"> <br>
    Cluster Description : <input type="text" name="descr"> <br>
    Cluster set : <select name='clusterSetList'>
      <c:forEach items="${clusterSetList}" var="s">
        <option value="${s.clusterSetName}">${s.clusterSetName}</option>
      </c:forEach>
    </select>
    <br>
    <input type="submit" value="Submit" />
  </form>
</body>
</html>
