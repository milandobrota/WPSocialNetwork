<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<html>
  <head>
    <title>Friend Requests</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
  </head>
  <body>
    <jsp:include page="Header.jsp"/>
    <h2>Friend Requests</h2> 
            
    <c:forEach var="profile" items="${profiles}">
      Username: <a href="Profile?username=${profile.username}">${profile.username}</a><br />
      First Name: ${profile.firstName}<br />
      Last Name: ${profile.lastName}<br />
      <a href="CreateFriendship?username=${profile.username}">Confirm</a>
      <a href="RemoveFriendship?username=${profile.username}">Deny</a>
      <br /><br />
    </c:forEach>
  </body>
</html>


