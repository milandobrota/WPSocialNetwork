<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<html>
  <head>
    <title>Search</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
  </head>
  <body>
    <jsp:include page="Header.jsp"/>
    <h2>Search</h2> 
            
    <h3>Profile</h3>
    <form action="Search" method="post">
      First or Last Name <input type="text" name="firstOrLastName" size="27" />
      <input type="submit" value="Submit" />
    </form>

    <h3>Page</h3>
    <form action="Search" method="post">
      Title <input type="text" name="title" size="27" />
      <input type="submit" value="Submit" />
    </form>

    <c:forEach var="profile" items="${profiles}">
      Username: <a href="Profile?username=${profile.username}">${profile.username}</a><br />
      First Name: ${profile.firstName}<br />
      Last Name: ${profile.lastName}<br />
      <br />
    </c:forEach>

    <c:forEach var="page" items="${pages}">
      Title: <a href="Page?username=${page.username}">${page.title}</a><br />
      Username: ${page.username}<br />
      Info: ${page.info}<br />
      <br />
    </c:forEach>
  </body>
</html>


