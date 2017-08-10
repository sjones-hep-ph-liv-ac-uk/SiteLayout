<%@ page import="com.basingwerk.sldb.mvc.model.ClusterSet"%>

<% ClusterSet clusterSet = (ClusterSet) request.getAttribute("clusterSet"); %>

<html>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/SiteLayout.css">

<head>
<title>Edit ClusterSet</title>
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
  var clusterName = document.forms["EditClusterSetForm"]["clusterSetName"].value;
  if (clusterName == "") {
    alert("Name must be filled out");
    return false;
  }
  var description = document.forms["EditClusterSetForm"]["description"].value;
  if (description == "") {
    alert("Description must be filled out");
    return false;
  }
  var location = document.forms["EditClusterSetForm"]["location"].value;
  if (location == "") {
    alert("Location must be filled out");
    return false;
  }
  var longitude = document.forms["EditClusterSetForm"]["longitude"].value;
  if (! isNumber(longitude)) {
    alert("Longitude must be number");
    return false;
  }
  var latitude = document.forms["EditClusterSetForm"]["latitude"].value;
  if (! isNumber(latitude)) {
    alert("Latitude must be number");
    return false;
  }
  var admin = document.forms["EditClusterSetForm"]["admin"].value;
  if (admin == "") {
    alert("Admin must be filled out");
    return false;
  }
}
</script>

</head>
<body>
	Edit ClusterSet
	<form name="EditClusterSetForm" action="EditClusterSetController" method="post" onsubmit="return validateForm()">
		ClusterSet name : <input type="text" name="clusterSetName" readonly value="${clusterSet.clusterSetName}"> <BR> 
        Description : <input type="text" name="description" value="${clusterSet.description}"> <BR>
        Location : <input type="text" name="location" value="${clusterSet.location}"> <BR>
        Longitude : <input type="text" name="longitude" value="${clusterSet.longitude}"> <BR>
        Latitude : <input type="text" name="latitude" value="${clusterSet.latitude}"> <BR>
        Admin : <input type="text" name="admin" value="${clusterSet.admin}"> <BR>
                <input type="submit" value="Submit" />
	</form>
</body>
</html>

