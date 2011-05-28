<div>Welcome,
<% if(session.getAttribute("username") != null) { %>
    <a href="<%= session.getAttribute("type") %>"><%= session.getAttribute("username")%></a><br />
<%} else {%>
    Guest<br />
<% } %>
<% if(session.getAttribute("username") != null) { %>
    <% if((session.getAttribute("type")).equals("Page")) { %>
    <a href="EditInfo">Edit Info</a> | 
    <% }else { %> 
    <a href="Wall">My wall</a> |
    <a href="NewStatus.jsp">New status</a> | 
    <a href="Friends">Friends</a> |
    <a href="Liked">Likes</a> |
    <a href="FriendRequests">Friend requests</a> |
    <a href="FriendsFriends">Your friends know</a> |
    <a href="FriendsLiked">Your friends like</a> |
    <% } %>
    <a href="Search.jsp">Search</a> |
    <a href="Logout">Logout</a>
<% } else { %>
    <a href="Login">Login</a> |
    <a href="NewProfile.jsp">New Profile</a> |
    <a href="NewPage.jsp">New Page</a>
<% } %>
</div>
