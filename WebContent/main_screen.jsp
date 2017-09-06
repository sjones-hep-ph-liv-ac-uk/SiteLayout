<html>
<head>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/SiteLayout.css">
  <title>Site Layout Database</title>
</head>
<body>
  Welcome ${requestScope['user'].username}.
  <form action="MainScreenController" method="post">

    <table class="tg">
      <tr>
        <th class="tg-yw4l">Main Screen</th>
      </tr>
      <tr>
        <td class="tg-yw4l"><input type="submit"
          value="Edit cluster sets" name="SomeButton" /></td>
      </tr>
      <tr>
        <td class="tg-yw4l"><input type="submit"
          value="Edit node types" name="SomeButton" /></td>
      </tr>
      <tr>
        <td class="tg-yw4l"><input type="submit" value="Edit clusters"
          name="SomeButton" /></td>
      </tr>
      <tr>
        <td class="tg-yw4l"><input type="submit" value="Edit node sets"
          name="SomeButton" /></td>
      </tr>
      
<%--      <tr>
        <td class="tg-yw4l"><input type="submit" value="Edit nodes"
          name="SomeButton" /></td>
      </tr>  --%>
      
      <tr>
        <td class="tg-yw4l"><input type="submit" value="Edit service nodes"
          name="SomeButton" /></td>
      </tr>
      <tr>
        <td class="tg-yw4l"><input type="submit" value="Edit service installations"
          name="SomeButton" /></td>
      </tr>
      <tr>
        <td class="tg-yw4l"><input type="submit" value="Reports"
          name="SomeButton" /></td>
      </tr>
      <tr>
        <td class="tg-yw4l"><input type="submit" value="Logout"
          name="SomeButton" /></td>
      </tr>
    </table>
  </form>
</body>
</html>

