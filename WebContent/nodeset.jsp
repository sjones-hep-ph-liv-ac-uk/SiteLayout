<%@ page import="java.util.List"%>
<% List nodeSetList = (List) session.getAttribute("nodeSetList"); %>

<html> 
  <link rel="stylesheet" href="${pageContext.request.contextPath}/SiteLayout.css">
<head> <title>Node Set</title> </head>

<body>
  Node Set

  <form action="NodeSetController" method="post">
    <table class="tg">
      <tr>
        <td>
          <input type="image" src="Down-48.png" alt="Submit" width="15px"
          height="15px" value="SORTDOWN.nodeSetName" name="SORTDOWN.nodeSetName">
        Node set name
          <input type="image" src="Up-48.png" alt="Submit" width="15px"
          height="15px" value="SORTUP.nodeSetName" name="SORTUP.nodeSetName">
        </td>
        <td>
          <input type="image" src="Down-48.png" alt="Submit" width="15px"
          height="15px" value="SORTDOWN.nodeType" name="SORTDOWN.nodeType">
        Node type
          <input type="image" src="Up-48.png" alt="Submit" width="15px"
          height="15px" value="SORTUP.nodeType" name="SORTUP.nodeType">
        </td>
        <td>
          <input type="image" src="Down-48.png" alt="Submit" width="15px"
          height="15px" value="SORTDOWN.nodeCount" name="SORTDOWN.nodeCount">
        Count
          <input type="image" src="Up-48.png" alt="Submit" width="15px"
          height="15px" value="SORTUP.nodeCount" name="SORTUP.nodeCount">
        </td>
        <td>
          <input type="image" src="Down-48.png" alt="Submit" width="15px"
          height="15px" value="SORTDOWN.cluster" name="SORTDOWN.cluster">
        Cluster
          <input type="image" src="Up-48.png" alt="Submit" width="15px"
          height="15px" value="SORTUP.cluster" name="SORTUP.cluster">
        </td>
        <td></td>
        <td></td>
        <td></td>
      </tr>
      <c:set var="index" value="-1" scope="page" />
      <c:forEach items="${nodeSetList}" var="nodeSet">
          <c:set var="index" value="${index + 1}" scope="page"/>
        <tr>
          <td><c:out value="${nodeSet.nodeSetName}" /></td>
          <td><c:out value="${nodeSet.nodeType.nodeTypeName}" /></td>
          <td><c:out value="${nodeSet.nodeCount}" /></td>
          <td><c:out value="${nodeSet.cluster.clusterName}" /></td>
          <td class="tg-yw4l"><input type="submit" value="Del" name=DEL.${nodeSet.nodeSetName}></td>
          <td class="tg-yw4l"><input type="submit" value="Edit" name=ED.${index}></td> 
          <td class="tg-yw4l"><input type="submit" value="View nodes" name=NODES.${nodeSet.nodeSetName}></td> 
        </tr>
      </c:forEach>
      <tr>
        <td><input type="submit" value="Back" name="Back" /></td>
        <td><input type="submit" value="New" name="New" /></td>
        <td><input type="submit" value="Refresh" name="Refresh" /></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
      </tr>
    </table>
  </form>
</body>
</html>
