
<html>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/SiteLayout.css">

<head>
<title>New Nodetype</title>

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

