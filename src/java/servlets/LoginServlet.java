/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import models.Profile;

/**
 *
 * @author milandobrota
 */
public class LoginServlet extends HttpServlet {

  public static final int TCP_PORT = 9999;
  String hostname = "localhost";
  public static String SEPARATOR = "#_%";

  /** 
   * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
   * @param request servlet request
   * @param response servlet response
   * @throws ServletException if a servlet-specific error occurs
   * @throws IOException if an I/O error occurs
   */
  protected void processRequest(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    response.setContentType("text/html;charset=UTF-8");
    ServletContext ctx = getServletConfig().getServletContext();
    request.setAttribute("error", "");
    HttpSession session = request.getSession(true);
    PrintWriter out = response.getWriter();
    
    String username = (String)session.getAttribute("username");
    if(username == null){
      // login
      
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
        PrintWriter cmdOut =
                new PrintWriter(
                new BufferedWriter(
                new OutputStreamWriter(
                sock.getOutputStream())), true);
        
        BufferedReader sysIn = new BufferedReader(
                new InputStreamReader(System.in));
        
        username = request.getParameter("username");
        String password = request.getParameter("password");
        
        String resp;
        String list = "";
        
        cmdOut.println("user_login" + SEPARATOR + username + SEPARATOR + password);
        
        // retrieve response
        while (!(resp = in.readLine()).equals("END")) {
          list += resp + "\n";
        }
        
        if(list.indexOf("OK") >= 0) {
          session.setAttribute("username", username);
          if(list.indexOf("PAGE") >= 0) {
            session.setAttribute("type", "Page");
            response.sendRedirect("Page");
          } else {
            session.setAttribute("type", "Profile");
            response.sendRedirect("Profile");
          }
        } else {
          response.sendRedirect("Login.jsp");
        }
        
        in.close();
        cmdOut.close();
        sock.close();
      } catch (UnknownHostException e1) {
        e1.printStackTrace();
      } catch (IOException e2) {
        e2.printStackTrace();
      }
    } else {
      out.println("Logged in already");
    }    
  }
  
  
  // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
  /**
   * Handles the HTTP <code>GET</code> method.
   * @param request servlet request
   * @param response servlet response
   * @throws ServletException if a servlet-specific error occurs
   * @throws IOException if an I/O error occurs
   */
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    processRequest(request, response);
  }
  
  /**
   * Handles the HTTP <code>POST</code> method.
   * @param request servlet request
   * @param response servlet response
   * @throws ServletException if a servlet-specific error occurs
   * @throws IOException if an I/O error occurs
   */
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    processRequest(request, response);
  }
  
  /**
   * Returns a short description of the servlet.
   * @return a String containing servlet description
   */
  @Override
  public String getServletInfo() {
    return "Short description";
  }// </editor-fold>
}
