/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author milandobrota
 */
public class Admin {

  public static String SEPARATOR = "#_%";

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

  
  public synchronized static boolean login(String username, String password){
    List<Admin> all = all();
    for (int i = 0; i < all.size(); i++) {
      if (all.get(i).getUsername().equals(username) && all.get(i).getPassword().equals(password)) return true;
    }
    return false;
  }

  public synchronized static List<Admin> all(){
    try{
      List<Admin> admins = new ArrayList<Admin>();
      FileInputStream fstream = new FileInputStream("admins.csv");
      DataInputStream in = new DataInputStream(fstream);
      BufferedReader br = new BufferedReader(new InputStreamReader(in));
      
      String strLine;
      //Read File Line By Line
      while ((strLine = br.readLine()) != null)   {
        // Print the content on the console
        String[] attrs = strLine.split(SEPARATOR);
        Admin admin = new Admin();
        admin.setUsername(attrs[0]);
        admin.setPassword(attrs[1]);

        admins.add(admin);
      }

      //Close the input stream
      in.close();
      return admins;
    }catch (Exception e){//Catch exception if any
      System.out.println("Error: " + e.getMessage());
      return null;
    }
  }
  
}
