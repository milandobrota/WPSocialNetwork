<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<html>
  <head>
    <title>Wall</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
  </head>
  <body>
    <jsp:include page="Header.jsp"/>
    <h2>Wall</h2> 
            
    <c:forEach var="status" items="${statuses}">
      <a href="Profile?username=${status.username}">${status.username}</a><br />
      ${status.text}<br />
      <br /><br />
    </c:forEach>
  </body>
</html>


