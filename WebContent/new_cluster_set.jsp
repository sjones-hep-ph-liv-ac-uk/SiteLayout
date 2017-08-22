<html>
<link rel="stylesheet"
  href="${pageContext.request.contextPath}/SiteLayout.css">

<head>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/SiteLayout.css">
  <title>New Cluster Set</title>

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
  var clusterName = document.forms["NewClusterSetForm"]["clusterSetName"].value;
  if (clusterName == "") {
    alert("Name must be filled out");
    return false;
  }
  var description = document.forms["NewClusterSetForm"]["description"].value;
  if (description == "") {
    alert("Description must be filled out");
    return false;
  }
  var location = document.forms["NewClusterSetForm"]["location"].value;
  if (location == "") {
    alert("Location must be filled out");
    return false;
  }
  var longitude = document.forms["NewClusterSetForm"]["longitude"].value;
  if (! isNumber(longitude)) {
    alert("Longitude must be number");
    return false;
  }
  var latitude = document.forms["NewClusterSetForm"]["latitude"].value;
  if (! isNumber(latitude)) {
    alert("Latitude must be number");
    return false;
  }
  var admin = document.forms["NewClusterSetForm"]["admin"].value;
  if (admin == "") {
    alert("Admin must be filled out");
    return false;
  }
}
</script>

</head>
<body>
  New Cluster Set
  <form name="NewClusterSetForm" action="NewClusterSetController" method="post" onsubmit="return validateForm()">
    Cluster set name : <input type="text" name="clusterSetName"> <br>
    Description : <input type="text" name="description"> <br>
    Location : <input type="text" name="location"> <br>
    Longitude : <input type="text" name="longitude"> <br>
    Latitude : <input type="text" name="latitude"> <br>
    Admin : <input type="text" name="admin"> <br>
        <input type="submit" value="Submit" />
   </form>
</body>
</html>

