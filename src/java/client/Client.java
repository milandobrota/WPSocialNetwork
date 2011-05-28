package client;

import java.io.*;
import java.net.*;

public class Client {

  public static final int TCP_PORT = 9999;

  public static void main(String[] args) {
    String hostname = "localhost";

    try {
      // odredi adresu racunara sa kojim se povezujemo
      InetAddress addr = InetAddress.getByName(hostname);

      // otvori socket prema drugom racunaru
      Socket sock = new Socket(addr, TCP_PORT);

      // inicijalizuj ulazni stream
      BufferedReader in =
        new BufferedReader(
          new InputStreamReader(
            sock.getInputStream()));

      // inicijalizuj izlazni stream
      PrintWriter out =
        new PrintWriter(
          new BufferedWriter(
            new OutputStreamWriter(
              sock.getOutputStream())), true);

      BufferedReader sysIn = new BufferedReader(
                    new InputStreamReader(System.in));


      String command;
      String response;
      String list;
      
      System.out.println("Client ready (type \"help\" for list of commands):");
      System.out.print(">");

      while(!(command = sysIn.readLine()).equals("logout")){
        list = "";
        out.println(command);

        // retrieve response
        while (!(response = in.readLine()).equals("END")) {
          list += response + "\n";
        }
        System.out.println(list);
        System.out.print(">");
      }
      
      
      // zatvori konekciju
      in.close();
      out.close();
      sock.close();
    } catch (UnknownHostException e1) {
      e1.printStackTrace();
    } catch (IOException e2) {
      e2.printStackTrace();
    }
  }

}
