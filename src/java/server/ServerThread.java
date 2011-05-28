package server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import models.Admin;
import models.Page;
import models.Profile;
import models.Status;


public class ServerThread extends Thread {

  public static String SEPARATOR = "#_%";

  /*
   * ----------ADMIN------------
   * admin_login <username> <password>
   * logout
   * profiles_all
   * pages_all
   * deactivate_profile <username>
   * deactivate_page <username>
   * profiles_by_username <username>
   * profiles_by_firstname <firstname>
   * profiles_by_lastname <lastname>
   * page_title <title>
   * profiles active
   * pages_active
   * help
   * 
   * ---------USER---------------
   * delimiter will be used instead of spaces for the user section
   * profile_new <username> <password> <firstname> <lastname>
   * page_new <username> <password> <title> <info>
   * user_login <username> <password>
   * status_new <username> <status>
   * status_for <username>
   * info_new <username> <info>
   * profiles_by_username <username>
   * profiles_by_firstname <firstname>
   * profiles_by_lastname <lastname>
   * profiles_by_first_or_lastname <first_or_lastname>
   * friend_requests_for <username>
   * pages_by_username <username>
   * pages_by_title <title>
   * friend_new <username> <friend_username>
   * friend_remove <username> <friend_username>
   * friend_status <username1> <username2>
   * like_status <username1> <username2>
   * friends <username>
   * like_new <username> <title>
   * likes <username>
   * friend_likes <username>
   * friends_friends <username>
   * wall_for <username>
   * 
   * 
   * 
   */
  public ServerThread(Socket sock) {
    this.sock = sock;
    this.loggedIn = false;
    try {
      in = new BufferedReader(
        new InputStreamReader(
          sock.getInputStream()));

      out = new PrintWriter(
        new BufferedWriter(
          new OutputStreamWriter(
            sock.getOutputStream())), true);
      // pokreni thread
      start();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private String execute(String fullCommand) {
    if(fullCommand.indexOf(SEPARATOR) >= 0) {
      // handle user request
      String[] commandWithArgs = fullCommand.split(SEPARATOR);
      
      String command = commandWithArgs[0];
      
      if(command.equals("profile_new")){
        if(createProfile(commandWithArgs[1], commandWithArgs[2], commandWithArgs[3], commandWithArgs[4]))
          return "OK";
        else
          return "ERROR";
        
      }else if(command.equals("page_new")){
        if(createPage(commandWithArgs[1], commandWithArgs[2], commandWithArgs[3], commandWithArgs[4]))
          return "OK";
        else
          return "ERROR";
        
      }else if(command.equals("user_login")){
        Object pageOrProfile;
        if((pageOrProfile = userLogin(commandWithArgs[1], commandWithArgs[2])) != null)
          return "OK " + (pageOrProfile.getClass().toString().indexOf("Page") > 0 ? "PAGE" : "PROFILE");
        else
          return "NO";
        
      }else if(command.equals("status_new")){
        if(createStatus(commandWithArgs[1], commandWithArgs[2]))
          return "OK";
        else
          return "ERROR";
        
      }else if(command.equals("status_for")){
        return (statusFor(commandWithArgs[1]));
        
      }else if(command.equals("info_new")){
        if(updateInfo(commandWithArgs[1], commandWithArgs[2]))
          return "OK";
        else
          return "ERROR";
        
      }else if(command.equals("profiles_by_firstname")){
        List<Profile> profiles = profileByFirstName(commandWithArgs[1]);
        String response = "";
        for(int i = 0; i < profiles.size(); i++) {
          response = response + profiles.get(i).toCSV();
          response = response + '\n';
        }
        return response;

      }else if(command.equals("profiles_by_username")){
        Profile profile = profileByUsername(commandWithArgs[1]);
        if(profile == null) return "NONE";
        return profile.toCSV();
        
      }else if(command.equals("profiles_by_lastname")){
        List<Profile> profiles = profileByLastName(commandWithArgs[1]);
        String res = "";
        for(int i = 0; i < profiles.size(); i++) {
          res = res + profiles.get(i).toCSV();
          res += '\n';
        }
        return res;
        
      }else if(command.equals("profiles_by_first_or_lastname")){
        List<Profile> profiles = profileByFirstOrLastName(commandWithArgs[1]);
        String res = "";
        for(int i = 0; i < profiles.size(); i++) {
          res = res + profiles.get(i).toCSV();
          res += '\n';
        }
        return res;

      }else if(command.equals("friend_requests_for")){
        List<Profile> profiles = friendRequestsFor(commandWithArgs[1]);
        String res = "";
        for(int i = 0; i < profiles.size(); i++) {
          res = res + profiles.get(i).toCSV();
          res += '\n';
        }
        return res;

      }else if(command.equals("pages_by_title")){
        List<Page> pages = pageByTitle(commandWithArgs[1]);
        String res = "";
        for(int i = 0; i < pages.size(); i++) {
          res = res + pages.get(i).toCSV();
          res += '\n';
        }
        return res;

      }else if(command.equals("pages_by_username")){
        Page page = pageByUsername(commandWithArgs[1]);
        return (page == null) ? "NONE" : page.toCSV();
        
      }else if(command.equals("friend_new")){
        if(createFriendship(commandWithArgs[1], commandWithArgs[2]))
          return "OK";
        else
          return "ERROR";

      }else if(command.equals("friend_remove")){
        if(removeFriendship(commandWithArgs[1], commandWithArgs[2]))
          return "OK";
        else
          return "ERROR";

      }else if(command.equals("friend_status")){
        return friendStatus(commandWithArgs[1], commandWithArgs[2]);
        
      }else if(command.equals("like_status")){
        return likeStatus(commandWithArgs[1], commandWithArgs[2]);
        
      }else if(command.equals("friends")){
        List<Profile> friends = friends(commandWithArgs[1]);
        String response = "";
        for(int i = 0; i < friends.size(); i++) {
          response = response + friends.get(i).toCSV();
          response = response + '\n';
        }
        return response;
        
      }else if(command.equals("like_new")){
        if(like(commandWithArgs[1], commandWithArgs[2]))
          return "OK";
        else
          return "ERROR";
        
      }else if(command.equals("likes")){
        List<Page> likes = likes(commandWithArgs[1]);
        String response = "";
        for(int i = 0; i < likes.size(); i++) {
          response = response + likes.get(i).toCSV();
          response = response + '\n';
        }
        return response;
        
      }else if(command.equals("friend_likes")){
        List<Page> likes = friendLikes(commandWithArgs[1]);
        String response = "";
        for(int i = 0; i < likes.size(); i++) {
          response = response + likes.get(i).toCSV();
          response = response + '\n';
        }
        return response;
        
      }else if(command.equals("friends_friends")){
        List<Profile> friends = friendsFriends(commandWithArgs[1]);
        String response = "";
        for(int i = 0; i < friends.size(); i++) {
          response = response + friends.get(i).toCSV();
          response = response + '\n';
        }
        return response;
        
      }else if(command.equals("wall_for")){
        List<Status> statuses = wallFor(commandWithArgs[1]);
        String response = "";
        for(int i = 0; i < statuses.size(); i++) {
          response = response + statuses.get(i).toCSV();
          response = response + '\n';
        }
        return response;
        
      }else{
        return "BAD COMMAND";
      }
    } else {
      // handle admin request
      /**
       * admin_login <username> <password>
       * logout
       * profiles_all
       * pages_all
       * deactivate_profile <username>
       * deactivate_page <username>
       * profiles_by_username <username>
       * profiles_by_firstname <firstname>
       * profiles_by_lastname <lastname>
       * profiles_by_first_or_lastname <first_or_lastname>
       * friend_requests_for <username>
       * page_title <title>
       * profiles active
       * pages_active
       * */
      if(fullCommand.startsWith("admin_login")) {
        String[] commandWithArgs = fullCommand.split(" ");
        if(login(commandWithArgs[1], commandWithArgs[2]))
          return "OK";
        else
          return "BAD COMBINATION";
        
      }else if(fullCommand.startsWith("logout")){
        logout();
        
      }else if(fullCommand.startsWith("profiles_all")){
        if(!loggedIn) return "Must be logged in in order to use this feature.";
        List<Profile> all = profiles();
        String response = "";
        for(int i = 0; i < all.size(); i++) {
          response = response + all.get(i).prettyPrint();
          response = response + '\n';
        }
        return response;
        
      }else if(fullCommand.startsWith("pages_all")){
        if(!loggedIn) return "Must be logged in in order to use this feature.";
        List<Page> all = pages();
        String response = "";
        for(int i = 0; i < all.size(); i++) {
          response = response + all.get(i).prettyPrint();
          response = response + '\n';
        }
        return response;
        
      }else if(fullCommand.startsWith("deactivate_profile")){
        if(!loggedIn) return "Must be logged in in order to use this feature.";
        String[] commandWithArgs = fullCommand.split(" ");
        deactivateProfile(commandWithArgs[1]);
        return "OK";
        
      }else if(fullCommand.startsWith("deactivate_page")){
        if(!loggedIn) return "Must be logged in in order to use this feature.";
        String[] commandWithArgs = fullCommand.split(" ");
        deactivatePage(commandWithArgs[1]);
        return "OK";
        
      }else if(fullCommand.startsWith("profiles_by_username")){
        // This is allowed for users to see
        // if(!loggedIn) return "Must be logged in in order to use this feature.";
        String[] commandWithArgs = fullCommand.split(" ");
        Profile profile = profileByUsername(commandWithArgs[1]);
        if(profile == null) return "NONE";
        return profile.prettyPrint();
        
      }else if(fullCommand.startsWith("profiles_by_firstname")){
        if(!loggedIn) return "Must be logged in in order to use this feature.";
        String[] commandWithArgs = fullCommand.split(" ");
        List<Profile> profiles = profileByFirstName(commandWithArgs[1]);
        String response = "";
        for(int i = 0; i < profiles.size(); i++) {
          response = response + profiles.get(i).prettyPrint();
          response = response + '\n';
        }
        return response;
        
      }else if(fullCommand.startsWith("profiles_by_lastname")){
        if(!loggedIn) return "Must be logged in in order to use this feature.";
        String[] commandWithArgs = fullCommand.split(" ");
        List<Profile> profiles = profileByLastName(commandWithArgs[1]);
        String response = "";
        for(int i = 0; i < profiles.size(); i++) {
          response = response + profiles.get(i).prettyPrint();
          response = response + '\n';
        }
        return response;
        
      }else if(fullCommand.startsWith("page_title")){
        if(!loggedIn) return "Must be logged in in order to use this feature.";
        String title = fullCommand.substring(11).trim();
        List<Page> pages = pageByTitle(title);
        String response = "";
        for(int i = 0; i < pages.size(); i++) {
          response = response + pages.get(i).prettyPrint();
          response = response + '\n';
        }
        return response;
        
      }else if(fullCommand.startsWith("profiles_active")){
        if(!loggedIn) return "Must be logged in in order to use this feature.";
        List<Profile> active = profilesActive();
        String response = "";
        for(int i = 0; i < active.size(); i++) {
          response = response + active.get(i).prettyPrint();
          response = response + '\n';
        }
        return response;
        
      }else if(fullCommand.startsWith("pages_active")){
        if(!loggedIn) return "Must be logged in in order to use this feature.";
        List<Page> active = pagesActive();
        String response = "";
        for(int i = 0; i < active.size(); i++) {
          response = response + active.get(i).prettyPrint();
          response = response + '\n';
        }
        return response;

      }else if(fullCommand.startsWith("help")){
        String response = "admin_login <username> <password>";
        response += '\n';
        response += "logout";
        response += '\n';
        response += "profiles_all";
        response += '\n';
        response += "pages_all";
        response += '\n';
        response += "deactivate_profile <username>";
        response += '\n';
        response += "deactivate_page <username>";
        response += '\n';
        response += "profiles_by_username <username>";
        response += '\n';
        response += "profiles_by_firstname <firstname>";
        response += '\n';
        response += "profiles_by_lastname <lastname>";
        response += '\n';
        response += "page_title <title>";
        response += '\n';
        response += "profiles active";
        response += '\n';
        response += "pages_active";
        response += '\n';
        response += "help";
        response += '\n';
        return response;
        
      } else {
        return "BAD COMMAND";
      }
    }
    return "SHOULD NOT HAPPEN";
  }

  public void run() {
    String response = "";
    try {
      String fullCommand = in.readLine();
      
      while(fullCommand != null && !fullCommand.startsWith("logout")){
        try {
          response = execute(fullCommand);
        } catch(java.lang.ArrayIndexOutOfBoundsException e){
          response = "BAD ARGUMENTS";
        }
        
        //send response
        out.println(response);
        out.println("END");
        
        fullCommand = in.readLine();
      }

      in.close();
      out.close();
      sock.close();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  
  
  //------------------ADMIN-------------------------------------
  //    admin_login <username> <password>
  public boolean login(String username, String password){
    loggedIn = Admin.login(username, password);
    return loggedIn;
  }

  //    logout
  public void logout(){
    try {
      in.close();
      out.close();
      sock.close();
    }catch(Exception e){
      e.printStackTrace();
    }
    
  }

  //    profiles_all
  public List<Profile> profiles(){
    return Profile.all();
  }

  //    pages_all
  public List<Page> pages(){
    return Page.all();
  }

  //    deactivate_profile <username>
  public void deactivateProfile(String username) {
    Profile profile = Profile.findByUsername(username);
    profile.setActive(false);
    profile.save();
  }

  //    deactivate_page <username>
  public void deactivatePage(String username) {
    Page page = Page.findByUsername(username);
    page.setActive(false);
    page.save();
  }

  //    profiles_by_username <username>
  public Profile profileByUsername(String username){
    return Profile.findByUsername(username);
  }

  //    page_by_username
  public Page pageByUsername(String username){
    return Page.findByUsername(username);
  }

  //    profiles_by_firstname <firstname>
  public List<Profile> profileByFirstName(String firstName){
    return Profile.findByFirstName(firstName);
  }

  //    profiles_by_lastname <lastname>
  public List<Profile> profileByLastName(String lastName){
    return Profile.findByLastName(lastName);
  }

  public List<Profile> profileByFirstOrLastName(String firstOrLastName){
    return Profile.findByFirstOrLastName(firstOrLastName);
  }

  //    friend_requests_for <username>
  public List<Profile> friendRequestsFor(String username) {
    List<Profile> res = new ArrayList<Profile>();
    Profile profile = Profile.findByUsername(username);
    if(profile == null) return res;
    return profile.getFriendRequests();
  }

  //    page_title <title>
  public List<Page> pageByTitle(String title){
    return Page.findByTitle(title);
  }
  
  //    profiles_active
  public List<Profile> profilesActive(){
    return Profile.active();
  }

  //    pages_active
  public List<Page> pagesActive(){
    return Page.active();
  }





  //    ---------USER---------------
  //    delimiter will be used instead of spaces for the user section


  //    profile_new <username> <password> <firstname> <lastname> <status>
  public boolean createProfile(String username, String password, String firstName, String lastName){
    Profile profileCheck = Profile.findByUsername(username);
    if(profileCheck != null) return false;

    Profile profile = new Profile();
    profile.setUsername(username);
    profile.setPassword(password);
    profile.setFirstName(firstName);
    profile.setLastName(lastName);
    profile.setActive(true);
    profile.save();

    return true;
  }

  //    page_new <username> <password> <title> <info>
  public boolean createPage(String username, String password, String title, String info) {
    Page pageCheck = Page.findByUsername(username);
    if(pageCheck != null) return false;

    Page page = new Page();
    page.setUsername(username);
    page.setPassword(password);
    page.setTitle(title);
    page.setInfo(info);
    page.setActive(true);
    return page.save();
  }

  //    user_login <username> <password>
  public Object userLogin(String username, String password) {
    Profile profile = Profile.login(username, password);
    if (profile != null) return profile;
    return Page.login(username, password);
  }

  //    status_new <username> <status>
  public boolean createStatus(String username, String text) {
    Profile profile = Profile.findByUsername(username);
    if(profile == null) return false;
    Status status = new Status();
    status.setProfileId(profile.getId());
    status.setText(text);
    return status.save();
  }

  //    status_for <username>
  public String statusFor(String username) {
    Profile profile = Profile.findByUsername(username);
    if(profile == null) return "";
    Status status = profile.currentStatus();
    return (status == null) ? "" : profile.currentStatus().getText();
  }

  //    info_new <username> <info>
  public boolean updateInfo(String username, String info){
    Page page = Page.findByUsername(username);
    if(page == null) return false;
    page.setInfo(info);
    return page.save();
  }

  //    profiles_by_firstname <firstname>
  public List<Profile> profilesByFirstName(String firstname) {
    return Profile.findByFirstName(firstname);
  }

  //    profiles_by_lastname <lastname>
  public List<Profile> profilesByLastName(String lastname) {
    return Profile.findByLastName(lastname);
  }

  //    pages_by_title <title>
  public List<Page> pagesByTitle(String title) {
    return Page.findByTitle(title);
  }

  //    friend_new <username> <friendusername>
  public boolean createFriendship(String username, String friendsUsername) {
    Profile profile = Profile.findByUsername(username);
    Profile friend = Profile.findByUsername(friendsUsername);
    boolean success = profile.addFriend(friend);
    if(success) profile.save();
    return success;
  }

  //    friend_remove <username> <friendusername>
  public boolean removeFriendship(String username, String friendsUsername) {
    Profile profile = Profile.findByUsername(username);
    Profile friend = Profile.findByUsername(friendsUsername);
    boolean success = profile.removeFriend(friend);
    if(success) profile.save();
    return success;
  }

  //    friend_status <username1> <username2>
  public String friendStatus(String username1, String username2) {
    Profile profile = Profile.findByUsername(username1);
    if(profile == null) return "NO SUCH USER";
    return profile.friendStatus(username2);
  }

  //    like_status <username1> <username2>
  public String likeStatus(String username1, String username2) {
    Profile profile = Profile.findByUsername(username1);
    if(profile == null) return "NO SUCH USER";
    return profile.likeStatus(username2);
  }

  //    friends <username>
  public List<Profile> friends(String username){
    Profile profile = Profile.findByUsername(username);
    return profile.getConfirmedFriends();
  }

  //    like_new <username> <title>
  public boolean like(String username, String pageUsername){
    Profile profile = Profile.findByUsername(username);
    Page page = Page.findByUsername(pageUsername);
    boolean resp = profile.like(page);
    profile.save();
    return resp;
  }

  //    likes <username>
  public List<Page> likes(String username) {
    Profile profile = Profile.findByUsername(username);
    return profile.getLikes();
  }

  //    friend_likes <username>
  public List<Page> friendLikes(String username) {
    Profile profile = Profile.findByUsername(username);
    return profile.getFriendLikes();
  }

  //    friends_friends <username>
  public List<Profile> friendsFriends(String username) {
    Profile profile = Profile.findByUsername(username);
    return profile.getFriendsFriends();
  }
  
  //    wall_for <username>
  public List<Status> wallFor(String username) {
    Profile profile = Profile.findByUsername(username);
    return profile.wall();
  }
  
  
  
  
  
  
  
  
  //------------------USER-------------------------------------
  
  
  
  private Socket sock;
  private BufferedReader in;
  private PrintWriter out;
  boolean loggedIn;
}
