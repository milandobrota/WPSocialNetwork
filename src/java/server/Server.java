/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.*;
import java.net.*;

public class Server {

  public static final int TCP_PORT = 9999;

  public static void main(String[] args) {
    try {
      ServerSocket ss = new ServerSocket(TCP_PORT);
      System.out.println("Server running...");
      while (true) {
        Socket sock = ss.accept();

        ServerThread st = new ServerThread(sock);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

}
