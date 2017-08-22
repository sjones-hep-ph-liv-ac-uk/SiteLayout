<%@ page import="java.util.List"%>
<% List installationList = (List) session.getAttribute("installationList"); %>

<html> 
  <link rel="stylesheet" href="${pageContext.request.contextPath}/SiteLayout.css">
<head> <title>Installation</title> </head>

<body>
  Installation

  <form action="InstallationController" method="post">
    <table class="tg">
      <tr>
        <td>
          <input type="image" src="Down-48.png" alt="Submit" width="15px"
          height="15px" value="SORTDOWN.service.serviceName" name="SORTDOWN.service.serviceName">
        Service name: 
          <input type="image" src="Up-48.png" alt="Submit" width="15px"
          height="15px" value="SORTUP.service.serviceName" name="SORTUP.service.serviceName">
        </td>
        <td>
          <input type="image" src="Down-48.png" alt="Submit" width="15px"
          height="15px" value="SORTDOWN.serviceNode.hostname" name="SORTDOWN.serviceNode.hostname">
        Service node hostname :
          <input type="image" src="Up-48.png" alt="Submit" width="15px"
          height="15px" value="SORTUP.serviceNode.hostname" name="SORTUP.serviceNode.hostname">
        </td>
        <td>
          <input type="image" src="Down-48.png" alt="Submit" width="15px"
          height="15px" value="SORTDOWN.softwareVersion" name="SORTDOWN.softwareVersion">
        Software version :
          <input type="image" src="Up-48.png" alt="Submit" width="15px"
          height="15px" value="SORTUP.softwareVersion" name="SORTUP.softwareVersion">
        </td>
        <td></td>
        <td></td>
      </tr>
      <c:set var="index" value="-1" scope="page" />
      <c:forEach items="${installationList}" var="installation">
          <c:set var="index" value="${index + 1}" scope="page"/>
        <tr>
          <td><c:out value="${installation.service.serviceName}" /></td>
          <td><c:out value="${installation.serviceNode.hostname}" /></td>
          <td><c:out value="${installation.softwareVersion}" /></td>
          <td class="tg-yw4l"><input type="submit" value="Del"
            name=DEL.${index}></td>
          <td class="tg-yw4l"><input type="submit" value="Edit"
            name=ED.${index}></td> 
        </tr>
      </c:forEach>
      <tr>
        <td><input type="submit" value="Back" name="Back" /></td>
        <td><input type="submit" value="New" name="New" /></td>
        <td><input type="submit" value="Refresh" name="Refresh" /></td>
        <td></td>
        <td></td>
      </tr>
    </table>
  </form>
</body>
</html>
