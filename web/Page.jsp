<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<html>
  <head>
    <title>Page</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
  </head>
  <body>
    <jsp:include page="Header.jsp"/>
    <br />
    <table cellspacing="2" cellpadding="0" border="0">
      <tbody>
        <tr>
          <td>Title:</td>
          <td>${page.title}</td>
        </tr>
        <tr>
          <td>Info:</td>
          <td>${page.info}</td>
        </tr>
        <tr>
          <td>Username:</td>
          <td>${page.username}</td>
        </tr>
      </tbody>
    </table>
    <br />
    ${likeBlock}
            
  </body>
</html>


