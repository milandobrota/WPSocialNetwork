<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<html>
  <head>
    <title>New Status</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
  </head>
  <body>
    <jsp:include page="Header.jsp"/>
    <h2>New Status</h2> 
    <form action="CreateStatus" method="post">
      <textarea name="text" size="27"></textarea><br />
      <input type="submit" value="Submit" />
    </form>
  </body>
</html>


