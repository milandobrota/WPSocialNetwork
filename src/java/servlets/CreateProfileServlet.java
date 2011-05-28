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

/**
 *
 * @author milandobrota
 */
public class CreateProfileServlet extends HttpServlet {

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
    response.setContentType("text/html;charset=UTF-8");
    ServletContext ctx = getServletConfig().getServletContext();
    HttpSession session = request.getSession(true);
    
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
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        
        String resp;
        String list = "";
        
        cmdOut.println("profile_new" + SEPARATOR + username + SEPARATOR + password + SEPARATOR + firstName + SEPARATOR + lastName);
        
        // retrieve response
        while (!(resp = in.readLine()).equals("END")) {
          list += resp + "\n";
        }

        out.println(list);
        
        if(list.indexOf("OK") >= 0) {
          session.setAttribute("username", username);
          response.sendRedirect("Profile?username=" + username);
        } else {
          response.sendRedirect("NewProfile.jsp");
        }
        
        
        // zatvori konekciju
        in.close();
        cmdOut.close();
        sock.close();
      } catch (UnknownHostException e1) {
        e1.printStackTrace();
      } catch (IOException e2) {
        e2.printStackTrace();
      }





















    try {
      /* TODO output your page here
      out.println("<html>");
      out.println("<head>");
      out.println("<title>Servlet CreateProfile</title>");  
      out.println("</head>");
      out.println("<body>");
      out.println("<h1>Servlet CreateProfile at " + request.getContextPath () + "</h1>");
      out.println("</body>");
      out.println("</html>");
       */
    } finally {      
      out.close();
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
