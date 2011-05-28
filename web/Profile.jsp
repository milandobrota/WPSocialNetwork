<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<html>
  <head>
    <title>Profile</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
  </head>
  <body>
    <jsp:include page="Header.jsp"/>
    <br />
    <table cellspacing="2" cellpadding="0" border="0">
      <tbody>
        <tr>
          <td>Username:</td>
          <td>${profile.username}</td>
        </tr>
        <tr>
          <td>First Name:</td>
          <td>${profile.firstName}</td>
        </tr>
        <tr>
          <td>Last Name:</td>
          <td>${profile.lastName}</td>
        </tr>
        <tr>
          <td>Status:</td>
          <td>${currentStatus}</td>
        </tr>
      </tbody>
    </table>
    <br />
    <% if(session.getAttribute("type").equals("Profile")) { %>
    ${friendshipBlock}
    <% } %>
    <br />
    <a href="Wall?username=${profile.username}">Wall</a> |
    <a href="Liked?username=${profile.username}">Likes</a> |
    <a href="Friends?username=${profile.username}">Friends</a>
  </body>
</html>


