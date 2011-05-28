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
import models.Page;

/**
 *
 * @author milandobrota
 */
public class PageServlet extends HttpServlet {

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
    PrintWriter out = response.getWriter();
    ServletContext ctx = getServletConfig().getServletContext();
    HttpSession session = request.getSession(true);
    
    String currentUsername = (String)session.getAttribute("username");
    if(currentUsername == null){
      response.sendRedirect("Login.jsp");
      return;
    }

    String username = currentUsername;
    if(request.getParameter("username") != null) username = (String)request.getParameter("username");
      
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
      
      
      String resp;
      String list = "";
      
      cmdOut.println("pages_by_username" + SEPARATOR + username);
      
      // retrieve response
      while (!(resp = in.readLine()).equals("END")) {
        list += resp + "\n";
      }

      Page page = Page.fromCSV(list);

      // like status
      String likeStatus;
      if(currentUsername.equals(username)){
        likeStatus = "YOU";
      }else{
        list = "";
        cmdOut.println("like_status" + SEPARATOR + currentUsername + SEPARATOR + username);
      
        // retrieve response
        while (!(resp = in.readLine()).equals("END")) {
          list += resp + "\n";
        }
        likeStatus = list;
      }

      String likeBlock = "";
      if(likeStatus.indexOf("YES") >= 0) {
        likeBlock = "You like this page.";
      }else if(likeStatus.indexOf("YOU") >=0) {
        likeBlock = "This is your page!";
      }else{
        likeBlock = "<a href='Like?username="+page.getUsername()+"'>Like</a>";
      }


      request.setAttribute("page", page);
      request.setAttribute("likeBlock", likeBlock);
      ctx.getRequestDispatcher("/Page.jsp").forward(request, response);
      
      // zatvori konekciju
      in.close();
      cmdOut.close();
      sock.close();
    } catch (UnknownHostException e1) {
      e1.printStackTrace();
    } catch (IOException e2) {
      e2.printStackTrace();
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
