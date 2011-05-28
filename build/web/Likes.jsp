<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<html>
  <head>
    <title>Likes</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
  </head>
  <body>
    <jsp:include page="Header.jsp"/>
    <h2>Likes</h2> 
            
    <c:forEach var="page" items="${pages}">
      Username: <a href="Page?username=${page.username}">${page.username}</a><br />
      Title: ${page.title}<br />
      Info: ${page.info}<br />
      <br /><br />
    </c:forEach>
  </body>
</html>


