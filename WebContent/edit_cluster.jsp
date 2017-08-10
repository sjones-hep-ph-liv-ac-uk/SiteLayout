<%@ page import="com.basingwerk.sldb.mvc.model.Cluster"%>
<%@ page import="com.basingwerk.sldb.mvc.model.ClusterSet"%>
<%@ page import="java.util.List"%>

<% 	List clusterSetList = (List) request.getAttribute("clusterSetList"); %>
<%	Cluster cluster = (Cluster) request.getAttribute("cluster"); %>

<html>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/SiteLayout.css">

<head>
<title>Edit Cluster</title>
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
  var clusterName = document.forms["EditClusterForm"]["clusterName"].value;
  if (clusterName == "") {
    alert("Name must be filled out");
    return false;
  }
  var descr = document.forms["EditClusterForm"]["descr"].value;
  if (descr == "") {
    alert("Description must be filled out");
    return false;
  }
}
</script>

</head>
<body>
	Edit Cluster
	<form name="EditClusterForm" action="EditClusterController" method="post" onsubmit="return validateForm()">
		Cluster Name : <input type="text" name="clusterName" readonly
			value="${cluster.clusterName}"> <BR> 
		Cluster Description : <input type="text" name="descr" value="${cluster.descr}"> <BR>
		Cluster set : <select name='clusterSetList'>
			<c:forEach items="${clusterSetList}" var="s">
				<option value="${s.clusterSetName}"
					${s.clusterSetName == cluster.clusterSet.clusterSetName ? 'selected="selected"' : ''}>${s.clusterSetName}</option>
			</c:forEach>
		</select>
		<BR>
		<input type="submit" value="Submit" />
	</form>
</body>
</html>
