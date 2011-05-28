/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author milandobrota
 */
public class Profile {

  public static String SEPARATOR = "#_%";

  // used when loading all profiles to avoid seeking
  private ArrayList<Integer> unconfirmedFriendIds;
  private ArrayList<Integer> likedIds;

  
  public Profile(){
    active = false;
    firstName = "";
    lastName = "";
    username = "";
    password = "";
    unconfirmedFriendIds = new ArrayList();
    likedIds = new ArrayList();
  }

  public ArrayList<Integer> getUnconfirmedFriendIds() {
    return unconfirmedFriendIds;
  }

  public void setUnconfirmedFriendIds(ArrayList<Integer> unconfirmedFriendIds) {
    this.unconfirmedFriendIds = unconfirmedFriendIds;
  }

  public ArrayList<Integer> getLikedIds() {
    return likedIds;
  }

  public void setLikedIds(ArrayList<Integer> likedIds) {
    this.likedIds = likedIds;
  }

	protected String username;


	/**
	 * Get the value of username
	 *
	 * @return the value of username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Set the value of username
	 *
	 * @param username new value of username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	protected String password;

	/**
	 * Get the value of password
	 *
	 * @return the value of password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Set the value of password
	 *
	 * @param password new value of password
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	protected String firstName;

	/**
	 * Get the value of firstName
	 *
	 * @return the value of firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Set the value of firstName
	 *
	 * @param firstName new value of firstName
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	protected String lastName;

	/**
	 * Get the value of lastName
	 *
	 * @return the value of lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Set the value of lastName
	 *
	 * @param lastName new value of lastName
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

//	protected String status;
//
//	/**
//	 * Get the value of status
//	 *
//	 * @return the value of status
//	 */
//	public String getStatus() {
//		return status;
//	}
//
//	/**
//	 * Set the value of status
//	 *
//	 * @param status new value of status
//	 */
//	public void setStatus(String status) {
//		this.status = status;
//	}
	protected boolean active;

	/**
	 * Get the value of isActive
	 *
	 * @return the value of isActive
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Set the value of isActive
	 *
	 * @param isActive new value of isActive
	 */
	public void setActive(boolean isActive) {
		this.active = isActive;
	}
	protected Integer id;

	/**
	 * Get the value of id
	 *
	 * @return the value of id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Set the value of id
	 *
	 * @param id new value of id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

  public synchronized List<Profile> getUnconfirmedFriends() {
    List<Profile> all = Profile.all();
    List<Profile> unconfirmedFriends = new ArrayList();
    for(int i = 0; i < unconfirmedFriendIds.size(); i++) {
      for(int j = 0; j < all.size(); j++) {
        if(unconfirmedFriendIds.get(i) == all.get(j).id.intValue()) {
          unconfirmedFriends.add(all.get(j));
        }
      }
    }
    return unconfirmedFriends;
  }

  public synchronized List<Profile> getConfirmedFriends() {
    List<Profile> confirmedFriends = new ArrayList<Profile>();
    List<Profile> unconfirmedFriends = getUnconfirmedFriends();

    for(int i = 0; i < unconfirmedFriends.size(); i++) {
      List<Integer> reverseIds = unconfirmedFriends.get(i).getUnconfirmedFriendIds();
      for(int j = 0; j < reverseIds.size(); j++) {
        if(getId().intValue() == reverseIds.get(j).intValue()) {
          confirmedFriends.add(unconfirmedFriends.get(i));
        }
      }
    }
    return confirmedFriends;
  }

  public synchronized List<Page> getLikes() {
    List<Page> all = Page.all();
    List<Page> likes = new ArrayList();
    for(int i = 0; i < likedIds.size(); i++) {
      for(int j = 0; j < all.size(); j++) {
        if(likedIds.get(i) == all.get(j).id.intValue()) {
          likes.add(all.get(j));
        }
      }
    }
    return likes;
  }

  public synchronized List<Profile> getFriendRequests() {
    List<Profile> resp = new ArrayList<Profile>();
    List<Profile> all = all();

    for(int i = 0; i < all.size(); i++) {
      if(all.get(i).getUnconfirmedFriendIds().contains(this.id) && !this.getUnconfirmedFriendIds().contains(all.get(i).getId()))
        resp.add(all.get(i));
    }
    return resp;
  }


  public boolean addFriend(Profile friend) {
    if(friend.getId() == null || friend.getId().intValue() == this.id.intValue()) return false;
    for(int i = 0; i < unconfirmedFriendIds.size(); i++) {
      if(friend.getId().intValue() == unconfirmedFriendIds.get(i).intValue()) return false;
    }
    unconfirmedFriendIds.add(friend.id);
    return true;
  }

  public boolean removeFriend(Profile friend) {
    if(friend.getId() == null || friend.getId().intValue() == this.id.intValue()) return false;
    for(int i = 0; i < unconfirmedFriendIds.size(); i++) {
      if(friend.getId().intValue() == unconfirmedFriendIds.get(i).intValue()) unconfirmedFriendIds.remove(unconfirmedFriendIds.get(i));
    }
    return true;
  }

  public boolean like(Page page) {
    if(page.getId() == null) return false;
    for(int i = 0; i < likedIds.size(); i++) {
      if(page.getId().intValue() == likedIds.get(i).intValue()) return false;
    }
    likedIds.add(page.id);
    return true;
  }


  public String toCSV() {
    return Integer.toString(id) + SEPARATOR + firstName + SEPARATOR + lastName + SEPARATOR +
            username + SEPARATOR + password + SEPARATOR + active + SEPARATOR + unconfirmedFriendsCSV() +
            SEPARATOR + likedCSV();
  }

  public static Profile fromCSV(String strLine) {
    String[] attrs = strLine.trim().split(SEPARATOR);
    Profile profile = new Profile();
    profile.setId(Integer.parseInt(attrs[0]));
    profile.setFirstName(attrs[1]);
    profile.setLastName(attrs[2]);
    profile.setUsername(attrs[3]);
    profile.setPassword(attrs[4]);
    boolean active = (attrs[5].equals("true")) ? true : false;
    profile.setActive(active);
    
    if(attrs[6].split(",") != null) {
      String[] friendIds = attrs[6].split(",");
      for(int i = 0; i < friendIds.length; i++)
        if(!friendIds[i].equals("empty")) {
          profile.unconfirmedFriendIds.add(Integer.parseInt(friendIds[i]));
        }
    }
    
    if(attrs[7].split(",") != null) {
      String[] likedIds = attrs[7].split(",");
      for(int i = 0; i < likedIds.length; i++)
        if(!likedIds[i].equals("empty")) {
          profile.likedIds.add(Integer.parseInt(likedIds[i]));
        }
    }
    return profile;
    
  }

  public String friendStatus(String username) {
    Profile possibleFriend = Profile.findByUsername(username);
    if(possibleFriend == null) return "NO";

    List<Profile> friends = this.getConfirmedFriends();
    for (int i = 0; i < friends.size(); i++) {
      if(friends.get(i).getUsername().equals(username)) return "YES";
    }

    for (int i = 0; i < unconfirmedFriendIds.size(); i++) {
      if(unconfirmedFriendIds.get(i).intValue() == possibleFriend.getId().intValue()) return "PENDING";
    }
    
    return "NO";
  }

  public String likeStatus(String username) {
    Page possibleLike = Page.findByUsername(username);
    if(possibleLike == null) return "NO";

    List<Page> likes = this.getLikes();
    for (int i = 0; i < likes.size(); i++) {
      if(likes.get(i).getUsername().equals(username)) return "YES";
    }
    return "NO";
  }

  public String unconfirmedFriendsCSV() {
    if(unconfirmedFriendIds.isEmpty()) return "empty";
    String res = "";
    for(int i = 0; i < unconfirmedFriendIds.size() - 1; i++) {
      res = res + unconfirmedFriendIds.get(i) + ",";
    }
    res = res + unconfirmedFriendIds.get(unconfirmedFriendIds.size() - 1);
    return res;
  }

  public String likedCSV() {
    if(likedIds.isEmpty()) return "empty";
    String res = "";
    for(int i = 0; i < likedIds.size() - 1; i++) {
      res = res + likedIds.get(i) + ",";
    }
    res = res + likedIds.get(likedIds.size() - 1);
    return res;
  }
  
  
  public synchronized boolean save() {
    boolean newRecord = (id == null) ? true : false;
    List<Profile> all = Profile.all();
    if(newRecord) {
      this.id = all.size() + 1;
      all.add(this);
    }else{
      for(int i = 0; i < all.size(); i++) {
        if(all.get(i).getId().intValue() == this.id.intValue()) {
          Profile existing = all.get(i);
          existing.firstName = this.firstName;
          existing.lastName = this.lastName;
          existing.username = this.username;
          existing.active = this.active;
          existing.password = this.password;
          existing.unconfirmedFriendIds = this.unconfirmedFriendIds;
          existing.likedIds = this.likedIds;
          break;
        }
      }
    }
    
    return bulkSave(all);
  }

//  public boolean destroy() {
//    List<Profile> all = Profile.all();
//    for(int i = 0; i < all.size(); i++) {
//      if(all.get(i).getId() == this.id) all.remove(all.get(i));
//    }
//    return bulkSave(all);
//  }

  public synchronized static boolean bulkSave(List<Profile> profiles) {
    try {
      BufferedWriter out = new BufferedWriter(new FileWriter("profiles.csv"));
      for(int i = 0; i < profiles.size(); i++) {
        out.write(profiles.get(i).toCSV());
        out.write('\n');
      }
      out.close();
    } catch (IOException e) {
      return false;
    }
    return true;

  }
  
  public synchronized static Profile findById(int id){
    List<Profile> all = all();
    for(int i = 0; i < all.size(); i++) {
      if(id == all.get(i).getId().intValue()) return all.get(i);
    }
    return null;
  }

  public Status currentStatus(){
    return Status.findLastByProfileId(id);
  }

  public List<Status> wall(){
    List<Integer> wallIds = new ArrayList<Integer>();
    List<Profile> friends = getConfirmedFriends();

    // add friend ids
    for(int i = 0; i < friends.size(); i++) {
      wallIds.add(friends.get(i).getId());
    }

    // add yourself
    wallIds.add(id);

    return Status.findByIds(wallIds);
    
  }

  public synchronized static List<Profile> active(){
    List<Profile> active = new ArrayList<Profile>();
    List<Profile> all = all();
    for(int i = 0; i < all.size(); i++) {
      if(all.get(i).isActive()) active.add(all.get(i));
    }
    return active;
  }
  
  public synchronized static List<Profile> all(){
    try{
      List<Profile> profiles = new ArrayList<Profile>();
      FileInputStream fstream = new FileInputStream("profiles.csv");
      DataInputStream in = new DataInputStream(fstream);
      BufferedReader br = new BufferedReader(new InputStreamReader(in));
      
      String strLine;
      //Read File Line By Line
      while ((strLine = br.readLine()) != null)   {
        profiles.add(Profile.fromCSV(strLine));
      }

      //Close the input stream
      in.close();
      return profiles;
    }catch (Exception e){//Catch exception if any
      System.out.println("Error: " + e.getMessage());
      return null;
    }
  }
  
  public synchronized static Profile findByUsername(String username){
    List<Profile> all = all();
    for(int i = 0; i < all.size(); i++) {
      if(username.equals(all.get(i).getUsername())) return all.get(i);
    }
    return null;
  }

  public synchronized static List<Profile> findByFirstName(String firstName){
    List<Profile> res = new ArrayList<Profile>();
    List<Profile> all = all();
    for(int i = 0; i < all.size(); i++) {
      if(firstName.equals(all.get(i).getFirstName())) res.add(all.get(i));
    }
    return res;
  }

  public synchronized static List<Profile> findByLastName(String lastName){
    List<Profile> res = new ArrayList<Profile>();
    List<Profile> all = all();
    for(int i = 0; i < all.size(); i++) {
      if(lastName.equals(all.get(i).getLastName())) res.add(all.get(i));
    }
    return res;
  }

  public synchronized static List<Profile> findByFirstOrLastName(String firstOrLastName){
    firstOrLastName = firstOrLastName.trim();
    List<Profile> res = new ArrayList<Profile>();
    List<Profile> all = all();
    for(int i = 0; i < all.size(); i++) {
      if(firstOrLastName.equalsIgnoreCase(all.get(i).getFirstName()) || firstOrLastName.equals(all.get(i).getLastName())) res.add(all.get(i));
    }
    return res;
  }

  public synchronized static Profile login(String username, String password){
    List<Profile> all = all();
    for(int i = 0; i < all.size(); i++) {
      if(username.equals(all.get(i).getUsername()) && password.equals(all.get(i).getPassword())) return all.get(i);
    }
    return null;
  }

  public synchronized List<Page> getFriendLikes(){
    List<Integer> resIds = new ArrayList<Integer>();
    List<Page> res = new ArrayList<Page>();
    List<Profile> friends = getConfirmedFriends();

    // gathering ids - no duplicates
    for(int i = 0; i < friends.size(); i++) {
      List<Integer> friendLikedIds = friends.get(i).getLikedIds();
      for (int j = 0; j < friendLikedIds.size(); j++) {
        if (!resIds.contains(friendLikedIds.get(j)))
          resIds.add(friendLikedIds.get(j));
      }
    }
    
    // getting pages
    for(int i = 0; i < resIds.size(); i++) {
      res.add(Page.findById(resIds.get(i)));
    }
    return res;
  }

  public synchronized List<Profile> getFriendsFriends(){
    List<Integer> resIds = new ArrayList<Integer>();
    List<Profile> res = new ArrayList<Profile>();
    List<Profile> friends = getConfirmedFriends();

    // gathering ids - no duplicates
    for(int i = 0; i < friends.size(); i++) {
      List<Profile> friendsFriends = friends.get(i).getConfirmedFriends();
      for (int j = 0; j < friendsFriends.size(); j++) {
        if (!resIds.contains(friendsFriends.get(j).getId()))
          resIds.add(friendsFriends.get(j).getId());
      }
    }
    
    // getting profiles
    for(int i = 0; i < resIds.size(); i++) {
      res.add(Profile.findById(resIds.get(i)));
    }
    return res;
  }

  public String prettyPrint(){
    String response = "";
    response = response + "username   : " + username  + '\n';
    response = response + "first name : " + firstName + '\n';
    response = response + "last name  : " + lastName  + '\n';
    response = response + "active     : " + active    + '\n';
    return response;
  }

  
  public static void main(String[] args) {
//    Profile profile = new Profile();
//    profile.setActive(true);
//    profile.setPassword("password");
//    profile.setUsername("milance");
//    profile.setFirstName("Milan");
//    profile.setLastName("Dobrota");
//    profile.save();
//
//    Profile profile2 = new Profile();
//    profile2.setActive(true);
//    profile2.setPassword("password");
//    profile2.setUsername("lana");
//    profile2.save();
//
//    Profile profile3 = new Profile();
//    profile3.setActive(true);
//    profile3.setPassword("password");
//    profile3.setUsername("lana");
//    profile3.save();
//
//    profile.addFriend(profile2);
//    profile.save();
//    Profile profile4 = Profile.findById(1);
//    profile4.addFriend(profile3);
//    profile4.save();
//
//    System.out.println(profile.prettyPrint());

    // List<Profile> profiles = Profile.findByFirstOrLastName("Milan\n");
    // System.out.println(profiles.get(0).getUsername());

    
    Profile a = Profile.findByUsername("lana");
    System.out.println(a.getConfirmedFriends().size());
    // System.out.println(a.getFriendRequests().get(0).getUsername());

    // System.out.println(profile.getFriendLikes().size());
    // System.out.println(profile.getFriendsFriends().size());
  }

  

}

