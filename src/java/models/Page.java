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
public class Page {

  public static String SEPARATOR = "#_%";

  public Page() {
    active = false;
    info = "";
    username = "";
    password = "";
    title = "";
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
  
  protected String title;
  
  /**
   * Get the value of title
   *
   * @return the value of title
   */
  public String getTitle() {
    return title;
  }
  
  /**
   * Set the value of title
   *
   * @param title new value of title
   */
  public void setTitle(String title) {
    this.title = title;
  }
  
  protected String info;
  
  /**
   * Get the value of info
   *
   * @return the value of info
   */
  public String getInfo() {
    return info;
  }
  
  /**
   * Set the value of info
   *
   * @param info new value of info
   */
  public void setInfo(String info) {
    this.info = info;
  }
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
  
  public String toCSV() {
    return Integer.toString(id) + SEPARATOR + title + SEPARATOR + username +
      SEPARATOR + password + SEPARATOR + info + SEPARATOR + active;
  }

  public static Page fromCSV(String strLine){
    String[] attrs = strLine.split(SEPARATOR);
    Page page = new Page();
    page.setId(Integer.parseInt(attrs[0]));
    page.setTitle(attrs[1]);
    page.setUsername(attrs[2]);
    page.setPassword(attrs[3]);
    page.setInfo(attrs[4]);
    boolean active = (attrs[5].equals("true")) ? true : false;
    page.setActive(active);
    return page;
  }
  
  
  public synchronized boolean save() {
    boolean newRecord = (id == null) ? true : false;
    List<Page> all = Page.all();
    if(newRecord) {
      this.id = all.size() + 1;
      all.add(this);
    }else{
      for(int i = 0; i < all.size(); i++) {
        if(all.get(i).getId().intValue() == this.id.intValue()) {
          Page existing = all.get(i);
          existing.username = this.username;
          existing.active = this.active;
          existing.info = this.info;
          existing.password = this.password;
          existing.title = this.title;
          break;
        }
      }
    }
    
    return bulkSave(all);
  }

//  public boolean destroy() {
//    List<Page> all = Page.all();
//    for(int i = 0; i < all.size(); i++) {
//      if(all.get(i).getId() == this.id) all.remove(all.get(i));
//    }
//    return bulkSave(all);
//  }

  public synchronized static boolean bulkSave(List<Page> pages) {
    try {
      BufferedWriter out = new BufferedWriter(new FileWriter("pages.csv"));
      for(int i = 0; i < pages.size(); i++) {
        out.write(pages.get(i).toCSV());
        out.write('\n');
      }
      out.close();
    } catch (IOException e) {
      return false;
    }
    return true;

  }

  public synchronized static List<Page> active(){
    List<Page> active = new ArrayList<Page>();
    List<Page> all = all();
    for(int i = 0; i < all.size(); i++) {
      if(all.get(i).isActive()) active.add(all.get(i));
    }
    return active;
  }
  
  public synchronized static List<Page> all(){
    try{
      List<Page> pages = new ArrayList<Page>();
      FileInputStream fstream = new FileInputStream("pages.csv");
      DataInputStream in = new DataInputStream(fstream);
      BufferedReader br = new BufferedReader(new InputStreamReader(in));
      
      String strLine;
      //Read File Line By Line
      while ((strLine = br.readLine()) != null)   {
        pages.add(Page.fromCSV(strLine));
      }
      //Close the input stream
      in.close();
      return pages;
    }catch (Exception e){//Catch exception if any
      System.err.println("Error: " + e.getMessage());
      return null;
    }
  }

  public String prettyPrint(){
    String response = "";
    response = response + "title    : " + title    + '\n';
    response = response + "username : " + username + '\n';
    response = response + "info     : " + info     + '\n';
    response = response + "active   : " + active   + '\n';
    return response;
  }

  public synchronized static Page findByUsername(String username){
    List<Page> all = all();
    for(int i = 0; i < all.size(); i++) {
      if(username.equals(all.get(i).getUsername())) return all.get(i);
    }
    return null;
  }

  public synchronized static List<Page> findByTitle(String title){
    List<Page> res = new ArrayList<Page>();
    List<Page> all = all();
    for(int i = 0; i < all.size(); i++) {
      if(title.equalsIgnoreCase(all.get(i).getTitle())) res.add(all.get(i));
    }
    return res;
  }

  public synchronized static Page findById(Integer id){
    List<Page> all = all();
    for(int i = 0; i < all.size(); i++) {
      if(id.equals(all.get(i).getId())) return all.get(i);
    }
    return null;
  }

  public synchronized static Page login(String username, String password){
    List<Page> all = all();
    for(int i = 0; i < all.size(); i++) {
      if(username.equals(all.get(i).getUsername()) && password.equals(all.get(i).getPassword())) return all.get(i);
    }
    return null;
  }
  
  public static void main(String[] args) {
    Page page = new Page();
    page.setId(2);
    page.setActive(true);
    page.setInfo("infoo");
    page.setPassword("password");
    page.setTitle("title3");
    page.setUsername("milance");
    page.save();

    List<Page> pages = Page.all();
    System.out.println(pages.get(0).isActive());
  }
  

}
