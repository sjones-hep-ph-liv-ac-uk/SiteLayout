<%@ page import="java.util.List"%>
<%@ page import="com.basingwerk.sldb.mvc.model.NodeSet"%>
<%@ page import="com.basingwerk.sldb.mvc.model.NodeType"%>

<%
  List nodeTypeList = (List) request.getAttribute("nodeTypeList");
%>
<%
  List clusterList = (List) request.getAttribute("clusterList");
%>
<%
  NodeSet nodeSet = (NodeSet) request.getAttribute("nodeSet");
%>

<html>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/SiteLayout.css">

<head>
<title>Edit Nodeset</title>
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
  var nodeSetName = document.forms["EditNodeSetForm"]["nodeSetName"].value;
  if (nodeSetName == "") {
    alert("Node set name must be filled out");
    return false;
  }
}
</script>

</head>

<body>
  <form name="EditNodeSetForm" action="EditNodeSetController" method="post" onsubmit="return validateForm()">
    Node set name : 
       <input type="text" name="nodeSetName" readonly value="${nodeSet.nodeSetName}"> <br> 
    Node type : <select name='nodeTypeList'>
      <c:forEach items="${nodeTypeList}" var="nt">
        
        <option value="${nt.nodeTypeName}"
          ${nt.nodeTypeName == nodeSet.nodeType.nodeTypeName ? 'selected="selected"' : ''}>${nt.nodeTypeName}</option>
      </c:forEach>
    </select><br> Cluster : <select name='clusterList'>
      <c:forEach items="${clusterList}" var="cn">
        <option value="${cn.clusterName}"
          ${cn.clusterName == nodeSet.cluster.clusterName ? 'selected="selected"' : ''}>${cn.clusterName}</option>
      </c:forEach>
    </select><br> <input type="submit" value="Submit" />
  </form>
</body>
</html>

