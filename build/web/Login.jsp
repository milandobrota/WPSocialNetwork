<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<html>
  <head>
    <title>Login</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
  </head>
  <body>
    <jsp:include page="Header.jsp"/>
    <h2>Login</h2> 
            
    <form action="Login" method="post">
      <table cellspacing="2" cellpadding="0" border="0">
        <tbody>
          <tr>
            <td>Username:</td>
            <td><input type="text" name="username" size="25"></td>
          </tr>
          <tr>
            <td>Password:</td>
            <td><input type="password" name="password" size="25"></td>
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


