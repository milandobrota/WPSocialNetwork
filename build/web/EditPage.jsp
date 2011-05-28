<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<html>
  <head>
    <title>Edit Page</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
  </head>
  <body>
    <jsp:include page="Header.jsp"/>
    <h2>Edit Page</h2> 
            
    <form action="UpdateInfo" method="post">
      <table cellspacing="2" cellpadding="0" border="0">
        <tbody>
          <tr>
          <tr>
            <td>Info:</td>
            <td><textarea name="info" size="27" >${page.info}</textarea></td>
          </tr>
          <tr>
            <td>&nbsp;</td>
            <td><input type="submit" value="Submit"></td>
          </tr>
        </tbody>
      </table>
    </form>
  </body>
</html>


