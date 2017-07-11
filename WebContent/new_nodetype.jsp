<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/SiteLayout.css">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>New Nodetype</title>

<script>

function isNumber(value) {
  return !isNaN(value) ;
}

function isInt(value) {
  return !isNaN(value) && parseInt(Number(value)) == value
    && !isNaN(parseInt(value, 10));
}
function validateForm() {
  var nodeTypeName = document.forms["EditNodeTypeForm"]["nodeTypeName"].value;
  if (nodeTypeName == "") {
    alert("Name must be filled out");
    return false;
  }

  var cpu = document.forms["EditNodeTypeForm"]["cpu"].value;
  if (! isInt(cpu)) {
    alert("Cpus per node must be a whole number");
    return false;
  }
  var slot = document.forms["EditNodeTypeForm"]["slot"].value;
  if (! isInt   (slot)) {
    alert("Slots per node must be a whole number");
    return false;
  }
  var hs06PerSlot = document.forms["EditNodeTypeForm"]["hs06PerSlot"].value;
  if (! isNumber(hs06PerSlot)) {
    alert("HS06 per slot must be a number");
    return false;
  }
  var memPerNode = document.forms["EditNodeTypeForm"]["memPerNode"].value;
  if (! isInt   (memPerNode)) {
    alert("GB per node node must be a whole number");
    return false;
  }
}
</script>


</head>
<body>
	New Nodetype

        <form name="NewNodeTypeForm" action="NewNodeTypeController" method="post" onsubmit="return validateForm()">
		Nodetype name : <input type="text" name="nodeTypeName"> <BR>
		CPUs per node : <input type="text" name="cpu"> <BR> 
                Slots per node : <input type="text" name="slot"> <BR> 
                HS06 per slot : <input type="text" name="hs06PerSlot"> <BR> 
                GBs per node : <input type="text" name="memPerNode"> <BR> 
                               <input type="submit" value="Submit" />
	</form>
</body>
</html>

