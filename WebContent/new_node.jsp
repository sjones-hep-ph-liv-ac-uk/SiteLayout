<%@ page import="com.basingwerk.sldb.mvc.model.Node"%>
<%@ page import="java.util.List"%>

<html>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/SiteLayout.css">

<head>
<title>New Node</title>

<script>
function isNumber(value) {
  return !isNaN(value) ;
}

function isInt(value) {
  return !isNaN(value) && parseInt(Number(value)) == value
    && !isNaN(parseInt(value, 10));
}

function validateForm() {
  var nodeName = document.forms["NewNodeForm"]["nodeName"].value;
  if (nodeName == "") {
    alert("Node name must be filled out");
    return false;
  }
}
</script>
</head>
<body>
	New Node
	<form name="NewNodeForm" action="NewNodeController"
		method="post" onsubmit="return validateForm()">

		Node name : <input type="text" name="nodeName"> <BR>
		Node description : <input type="text" name="nodeDescription"> <BR>
		Node set : <select name='nodeSetList'>
			<c:forEach items="${nodeSetList}" var="ns">
 			  <option value="${ns.nodeSetName}">${ns.nodeSetName}</option>
			</c:forEach>
		</select> </select><BR> 
		State : <select name='nodeStateList'>
			<c:forEach items="${nodeStateList}" var="st">
				<option value="${st.state}">${st.state}</option>
			</c:forEach>
		</select><BR> <input type="submit" value="Submit" />
	</form>
</body>
</html>
