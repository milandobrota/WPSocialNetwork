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
public class Status {

  public static String SEPARATOR = "#_%";

  public Status(){
    text = "";
  }

  protected String text;

  /**
   * Get the value of text
   *
   * @return the value of text
   */
  public String getText() {
    return text;
  }

  /**
   * Set the value of text
   *
   * @param text new value of text
   */
  public void setText(String text) {
    this.text = text;
  }

  protected Integer profileId;

  /**
   * Get the value of profileId
   *
   * @return the value of profileId
   */
  public Integer getProfileId() {
    return profileId;
  }

  /**
   * Set the value of profileId
   *
   * @param profileId new value of profileId
   */
  public void setProfileId(Integer profileId) {
    this.profileId = profileId;
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

  public String calculateUsername() {
    return Profile.findById(profileId).getUsername();
  }

  protected void setUsername(String username){
    this.username = username;
  }


  public String toCSV() {
    return Integer.toString(profileId) + SEPARATOR + text + SEPARATOR + calculateUsername();
  }

  public static Status fromCSV(String strLine) {
    String[] attrs = strLine.split(SEPARATOR);
    Status status = new Status();
    status.setProfileId(Integer.parseInt(attrs[0]));
    status.setText(attrs[1]);
    status.setUsername(attrs[2]);
    return status;
  }
  
  
  public boolean save() {
    List<Status> all = Status.all();
    all.add(this);
    return bulkSave(all);
  }

//  public boolean destroy() {
//    List<Page> all = Page.all();
//    for(int i = 0; i < all.size(); i++) {
//      if(all.get(i).getId() == this.id) all.remove(all.get(i));
//    }
//    return bulkSave(all);
//  }

  public synchronized static boolean bulkSave(List<Status> statuses) {
    try {
      BufferedWriter out = new BufferedWriter(new FileWriter("statuses.csv"));
      for(int i = 0; i < statuses.size(); i++) {
        out.write(statuses.get(i).toCSV());
        out.write('\n');
      }
      out.close();
    } catch (IOException e) {
      return false;
    }
    return true;

  }

  public synchronized static Status findLastByProfileId(int profileId){
    List<Status> all = all();
    for(int i = all.size() - 1; i >= 0; i--) {
      if(profileId == all.get(i).getProfileId().intValue()) return all.get(i);
    }
    return null;
  }

  public synchronized static List<Status> findByIds(List<Integer> profileIds){
    List<Status> res = new ArrayList<Status>();
    List<Status> all = all();
    for(int i = all.size() - 1; i >= 0; i--) {
      for(int j = 0; j < profileIds.size(); j++) {
        if(profileIds.get(j).intValue() == all.get(i).getProfileId().intValue()){
          res.add(all.get(i));
        }
      }
    }
    return res;
  }
  
  public synchronized static List<Status> all(){
    try{
      List<Status> statuses = new ArrayList<Status>();
      FileInputStream fstream = new FileInputStream("statuses.csv");
      DataInputStream in = new DataInputStream(fstream);
      BufferedReader br = new BufferedReader(new InputStreamReader(in));
      
      String strLine;
      //Read File Line By Line
      while ((strLine = br.readLine()) != null)   {
        // Print the content on the console
        statuses.add(Status.fromCSV(strLine));
      }
      //Close the input stream
      in.close();
      return statuses; 
    }catch (Exception e){//Catch exception if any
      System.err.println("Error: " + e.getMessage());
      return null;
    }
  }
  
  public static void main(String[] args) {
    Status status = new Status();
    status.setProfileId(1);
    status.setText("My new status");
    status.save();

    List<Status> statuses = Status.all();
    System.out.println(statuses.get(0).getText());
  }
  

}

  