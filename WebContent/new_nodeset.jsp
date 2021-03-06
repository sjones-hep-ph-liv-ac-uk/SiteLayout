<%@ page import="com.basingwerk.sldb.mvc.model.NodeSet"%>
<%@ page import="java.util.List"%>

<%  List nodeTypeList = (List) request.getAttribute("nodeTypeList");
    List clusterList = (List) request.getAttribute("clusterList");
%>

<html>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/SiteLayout.css">

<head>
<title>New Node Set</title>
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
  var nodeSetName = document.forms["NewNodeSetForm"]["nodeSetName"].value;
  if (nodeSetName == "") {
    alert("Node set name must be filled out");
    return false;
  }
}
</script>
</head>
<body>
  New Node Set
  <form name="NewNodeSetForm" action="NewNodeSetController"
    method="post" onsubmit="return validateForm()">

    Node set name : <input type="text" name="nodeSetName"> <br>
    Node type : <select name='nodeTypeList'>
      <c:forEach items="${nodeTypeList}" var="nt">
        <c:if test="${nt.nodeTypeName != 'BASELINE'}">
          <option value="${nt.nodeTypeName}">${nt.nodeTypeName}</option>
        </c:if>
      </c:forEach>
    </select> </select><br> Cluster : <select name='clusterList'>
      <c:forEach items="${clusterList}" var="cn">
        <option value="${cn.clusterName}">${cn.clusterName}</option>
      </c:forEach>
    </select><br> <input type="submit" value="Submit" />
  </form>
</body>
</html>
