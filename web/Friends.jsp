<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<html>
  <head>
    <title>Friends</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
  </head>
  <body>
    <jsp:include page="Header.jsp"/>
    <h2>Friends</h2> 
            
    <c:forEach var="profile" items="${profiles}">
      Username: <a href="Profile?username=${profile.username}">${profile.username}</a><br />
      First Name: ${profile.firstName}<br />
      Last Name: ${profile.lastName}<br />
      <br /><br />
    </c:forEach>
  </body>
</html>


