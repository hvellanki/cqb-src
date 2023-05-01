/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cqb.controller;

import cqb.db.Buyer;
import cqb.svc.*;
import cqb.op.*;
import cc.util.*;
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

/**
 *
 * @author hari-work
 */
@WebServlet(name = "BuyerActionsServlet",
        urlPatterns
        = {"/buyerAccounts", "/buyerProfile"})

public class BuyerActionsServlet extends HttpServlet {

    final String jspPathPrefix = "/WEB-INF/view";

    static Log LogObj = null;

    static {
        try {
            LogObj = Log.getReference("cqb");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userPath = request.getServletPath();

        HttpSession session = request.getSession();
        String nextURL = "";
        LogObj.loglnT("BuyerActions reached by userPath : " + userPath, Log.PINFO);
        try {
            if (!SessionSvc.checkSession(session)) {
                session.setAttribute("ErrorMsg", "Session expired or invalid, please login to continue");
                nextURL = jspPathPrefix + "/login.jsp";
            } else {
                if (userPath.equals("/buyerAccounts")) {
                    int supplierId = (Integer) session.getAttribute("SupplierId");
                    Buyer buyer = new Buyer();
                    List buyersList = buyer.loadList("Where SupplierId=" + supplierId + " Order by BuyerId asc");
                    request.setAttribute("BuyersList", buyersList);
                    nextURL = jspPathPrefix + "/buyerAccounts.jsp";
                } else if (userPath.equals("/buyerProfile")) {
                    int supplierId = (Integer) session.getAttribute("SupplierId");
                    String buyerId = request.getParameter("BuyerId");
                    Buyer buyer = Buyer.getObject(supplierId, buyerId);
                    session.setAttribute("BuyerObj", buyer);
                    /*String ASTOn = (String) session.getAttribute("ASTOn");
                    if (ASTOn.equals("N")) {
                        nextURL = jspPathPrefix + "/buyerProfileV2.jsp";
                    } else {*/
                        nextURL = jspPathPrefix + "/buyerProfile.jsp";
                    //}
                    // if checkout page is requested
                } else if (userPath.equals("/createBuyer")) {
                    Buyer buyer = new Buyer();
                    session.setAttribute("BuyerObj", buyer);
                    request.setAttribute("Action", "Create");
                    nextURL = jspPathPrefix + "/buyerInfo.jsp";
                } /*else if (userPath.equals("/refreshBuyer")) {
                    int supplierId = (Integer) session.getAttribute("SupplierId");
                    int buyerId = Integer.parseInt(request.getParameter("BuyerId"));
                    LogObj.loglnT("Refreshing buyer with Id : " + buyerId, Log.PFATAL);
                    BuyerDwnldSvc SvcObj = new BuyerDwnldSvc();
                    SvcObj.getNewBuyer(supplierId, buyerId);
                    Buyer buyer = Buyer.getObject(supplierId, buyerId);
                    session.setAttribute("BuyerObj", buyer);
                    nextURL = jspPathPrefix + "/buyerProfile.jsp";
                }  else if (userPath.equals("/saveBuyer")) {
                    int supplierId = (Integer) session.getAttribute("SupplierId");
                    SaveBuyer saveObj = new SaveBuyer();
                    int buyerId = saveObj.saveBuyer(request, supplierId);
                    request.setAttribute("BuyerId", buyerId);
                    nextURL = jspPathPrefix + "/createdBuyer.jsp";
                } */
            }
        } catch (Exception e) {
            LogObj.log(e, Log.PFATAL);
            LogObj.loglnT("Exception in BuyerActionsServlet: " + e.getMessage(), Log.PFATAL);
            LogObj.flushBuffer();
            session.setAttribute("DisplayMsg", "Error: Msg from Exception - " + e.getMessage());
            nextURL = jspPathPrefix + "/displayMsg.jsp";
            //session.setAttribute("ErrorMsg", "System Error: Please try again later.");
            //nextURL = jspPathPrefix + "/login.jsp";
        }

        // use RequestDispatcher to forward request internally
        //String url =  userPath + ".jsp";
        try {
            request.getRequestDispatcher(nextURL).forward(request, response);
        } catch (Exception ex) {
            LogObj.loglnT("Exception in BuyerActions Controller, could not forward  : " + ex.getMessage(), Log.PFATAL);
            LogObj.log(ex, Log.PFATAL);
        }
    }

// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
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
     *
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
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
