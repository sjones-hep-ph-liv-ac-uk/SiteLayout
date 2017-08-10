<%@page import="com.basingwerk.sldb.mvc.model.Node"%>
<%@page import="com.basingwerk.sldb.mvc.model.NodeState"%>
<%@page import="com.basingwerk.sldb.mvc.model.NodeSet"%>

<%	Node  node = (Node) request.getAttribute("node"); %>
<%	NodeSet nodeSet = (NodeSet) request.getAttribute("nodeSet"); %>
<%	NodeState nodeState = (NodeState) request.getAttribute("nodeState"); %>

<html>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/SiteLayout.css">

<head>
<title>Edit Node</title>
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
  var nodeName = document.forms["EditNodeForm"]["nodeName"].value;
  if (nodeName == "") {
    alert("Node name must be filled out");
    return false;
  }
}
</script>

</head>

<body>
<%-- node is <%= node %> --%>

	<form name="EditNodeForm" action="EditNodeController" method="post" onsubmit="return validateForm()">
		Node  name : 
		   <input type="text" name="nodeName" readonly value="${node.nodeName}"> <BR> 
		Node description : 
		   <input type="text" name="nodeDescription" value="${node.description}"> <BR>
		Node set : <select name='nodeSetList'>
			<c:forEach items="${nodeSetList}" var="ns">
				<option value="${ns.nodeSetName}"
					${ns.nodeSetName == node.nodeSet.nodeSetName  ? 'selected="selected"' : ''}>${ns.nodeSetName}</option>
			</c:forEach>
		</select><BR> 
		State : <select name='nodeStateList'>
			<c:forEach items="${nodeStateList}" var="nodeState">
				<option value="${nodeState.state}"
					${nodeState.state == node.nodeState.state ? 'selected="selected"' : ''}>${nodeState.state}</option>
			</c:forEach>
		</select><BR> <input type="submit" value="Submit" />
	</form>
</body>
</html>
