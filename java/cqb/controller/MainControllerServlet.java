/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cqb.controller;

import cqb.queries.*;

import cqb.svc.LoginSvc;
import cqb.svc.SessionSvc;

import cc.db.Entity;
import cc.util.*;
import cqb.svc.QueryHelper;
import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

/**
 *
 * @author hari-work
 */
@WebServlet(name = "MainControllerServlet", urlPatterns
        = {"/login", "/verifyLogin", "/logout", "/refreshData", "/authorize", "/syncData", "/syncAllData", "/displayMsg", "/topMenu"})
public class MainControllerServlet extends HttpServlet {

    final String jspPathPrefix = "/WEB-INF/view";

    static Log LogObj = null;
    static Props PropsObj = null;

    static {
        try {
            Entity.setAppSpace("cqb");
            LogObj = Log.getReference("cqb");
            PropsObj = Props.getReference("cqb");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init(ServletConfig s) {
        try {
            Entity.setAppSpace("cqb");
            LogObj = Log.getReference("cqb");
            PropsObj = Props.getReference("cqb");
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

        HttpSession session = request.getSession();

        String userPath = request.getServletPath();

        LogObj.loglnT("MainController reached by userPath : " + userPath, Log.PINFO);
        String nextURL = "";
        try {
            if (userPath.equals("/login")) {
                nextURL = jspPathPrefix + "/login.jsp";
            } else if (userPath.equals("/verifyLogin")) {

                if (LoginSvc.doLogin(request, response)) {

                    String connectionId = (String) session.getAttribute("ConnectionId");
                    if (connectionId == null || connectionId.length() == 0) {
                        nextURL = jspPathPrefix + "/authURL.jsp";
                    } else {
                        nextURL = jspPathPrefix + "/topMenu.jsp";
                    }

                } else {
                    session.setAttribute("ErrorMsg", "UserId or Password invalid, please try again");
                    nextURL = jspPathPrefix + "/login.jsp";
                }
            } else if (userPath.equals("/logout")) {
                nextURL = jspPathPrefix + "/login.jsp";
                session.invalidate();
            } else if (userPath.equals("/refreshData")) {
                int supplierId = (Integer) session.getAttribute("SupplierId");
                getDataFromCodat(supplierId);
                session.setAttribute("DisplayMsg", "Bill360 has updated your account");
                nextURL = jspPathPrefix + "/displayMsg.jsp";

            } else if (userPath.equals("/syncAllData")) {
                int supplierId = (Integer) session.getAttribute("SupplierId");
                //QueryHelper.syncAllData(supplierId);
                QueryHelper.syncData(supplierId, "invoices");
                QueryHelper.syncData(supplierId, "items");
                QueryHelper.syncData(supplierId, "customers");
                QueryHelper.syncData(supplierId, "payments");
                session.setAttribute("DisplayMsg", "Bill360 is updating your account");
                nextURL = jspPathPrefix + "/displayMsg.jsp";

            } else if (userPath.equals("/syncData")) {
                int supplierId = (Integer) session.getAttribute("SupplierId");
                String dataType = request.getParameter("dataType");
                QueryHelper.syncData(supplierId, dataType);
                session.setAttribute("DisplayMsg", "Bill360 is updating your " + dataType);
                nextURL = jspPathPrefix + "/displayMsg.jsp";

            } /*else if (userPath.equals("/refreshData")) {

                getDataFromCodat(session);

                LogObj.loglnT("Finished downloading Buyer info  ", Log.PINFO);

                session.setAttribute("DisplayMsg", "Bill360 has updated your account");
                nextURL = jspPathPrefix + "/displayMsg.jsp";

            }*/ else if (userPath.equals("/topMenu")) {
                if (!SessionSvc.checkSession(session)) {
                    session.setAttribute("ErrorMsg", "Session expired or invalid, please login to continue");
                    nextURL = jspPathPrefix + "/login.jsp";
                } else {
                    nextURL = jspPathPrefix + "/topMenu.jsp?time=" + java.time.Instant.now().getEpochSecond();
                }
            } else if (userPath.equals("/displayMsg")) {
                nextURL = jspPathPrefix + "/displayMsg.jsp";
            }
        } catch (Exception e) {
            LogObj.log(e, Log.PFATAL);
            LogObj.loglnT("Exception in MainControllerServlet: " + e.getMessage(), Log.PFATAL);
            LogObj.flushBuffer();
            //String ErrorMsg = "Exception in MainControllerServlet: " + e.getMessage();
            //session.setAttribute("ErrorMsg", ErrorMsg);
            session.setAttribute("ErrorMsg", "System Error: Please try again later.");
            nextURL = jspPathPrefix + "/login.jsp";
        }

        // use RequestDispatcher to forward request internally
        //String url =  userPath + ".jsp";
        try {
            request.getRequestDispatcher(nextURL).forward(request, response);
        } catch (Exception ex) {
            LogObj.loglnT("Exception in Main Controller, could not forward  : " + ex.getMessage(), Log.PFATAL);
            LogObj.log(ex, Log.PFATAL);
            LogObj.flushBuffer();
        }
    }

    private void getDataFromCodat(int supplierId) throws Exception {

        downloadTaxRates svcRateObj = new downloadTaxRates();
        svcRateObj.getTaxRates(supplierId);

        downloadAccounts svcAcctObj = new downloadAccounts();
        svcAcctObj.getAccounts(supplierId);

        downloadItems svcItemsObj = new downloadItems();
        svcItemsObj.getItems(supplierId);

        downloadPmts svcPmtObj = new downloadPmts();
        svcPmtObj.getPmts(supplierId);

        downloadBuyers svcObj = new downloadBuyers();
        svcObj.getBuyers(supplierId);

        downloadInvoices svcInvoicesObj = new downloadInvoices();
        svcInvoicesObj.getInvoices(supplierId);

        LogObj.loglnT("Finished downloading Buyer info  ", Log.PINFO);

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
