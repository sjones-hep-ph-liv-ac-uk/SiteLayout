<%@ page import="com.basingwerk.sldb.mvc.model.ClusterSet"%>
<%@ page import="java.util.List"%>
<% List clusterSetList = (List) request.getAttribute("clusterSetList"); %>

<html>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/SiteLayout.css">

<head>
  <title>Select Cluster Set</title>
</head>
<body>
  Select Cluster Set
  <form action="SelectClusterSetController" method="post">
    Cluster set : <select name='clusterSetList'>
      <c:forEach items="${clusterSetList}" var="s">
        <option value="${s.clusterSetName}" selected="selected" >${s.clusterSetName}</option>
      </c:forEach>
    </select>
    <br>
    <input type="submit" value="Submit" />
  </form>
</body>
</html>
