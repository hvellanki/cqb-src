/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cqb.controller;

import cqb.data.InvoiceInfo;
import cqb.data.BuyerInvoiceInfo;
import cqb.svc.DBSvc;
import cqb.svc.BuyerSvc;
import cqb.svc.SessionSvc;
import cqb.db.Account;
import cqb.db.JsonRep;
import cqb.db.Pmt;
import cqb.db.Item;
import cqb.db.Invoice;
import cqb.db.Buyer;

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
@WebServlet(name = "RptControllerServlet",
        urlPatterns
        = {"/supplierProfile", "/buyersInvoices", "/buyerInvoices", "/invoiceItems", "/showItems", "/showPmts",
            "/showAccounts", "/displayJson", "/tBI"})

public class RptControllerServlet extends HttpServlet {

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
        LogObj.loglnT("RptController reached by userPath : " + userPath, Log.PINFO);
        try {
            if (!SessionSvc.checkSession(session)) {
                session.setAttribute("ErrorMsg", "Session expired or invalid, please login to continue");
                nextURL = jspPathPrefix + "/login.jsp";
            } else {
                if (userPath.equals("/buyersInvoices")) { // List of buyers and number of invoices
                    int supplierId = (Integer) session.getAttribute("SupplierId");
                    List<BuyerInvoiceInfo> infoList = BuyerSvc.getBuyersInvoices(supplierId);
                    request.setAttribute("BuyersInvoicesInfo", infoList);
                    nextURL = jspPathPrefix + "/buyersInvoices.jsp";
                } else if (userPath.equals("/buyerInvoices")) { // List of Invoices for the buyer and some details for each invoice.
                    int supplierId = (Integer) session.getAttribute("SupplierId");
                    String buyerId = request.getParameter("BuyerId");
                    Buyer buyer = Buyer.getObject(supplierId, buyerId);
                    List<InvoiceInfo> infoList = BuyerSvc.getBuyerInvoices(supplierId, buyerId);
                    request.setAttribute("InvoiceInfo", infoList);
                    request.setAttribute("BuyerObj", buyer);
                    session.setAttribute("BuyerObj", buyer);
                    nextURL = jspPathPrefix + "/buyerInvoices.jsp?time=" + java.time.Instant.now().getEpochSecond();
                } else if (userPath.equals("/invoiceItems")) { // List of items foreach invoice.
                    int supplierId = (Integer) session.getAttribute("SupplierId");
                    String invoiceId = request.getParameter("InvoiceId");
                    Invoice invoice = Invoice.getObject(supplierId, invoiceId);
                    Buyer buyer = Buyer.getObject(supplierId, invoice.getBuyerId());
                    List itemsList = BuyerSvc.getInvoiceItems(supplierId, invoiceId);
                    request.setAttribute("ItemsList", itemsList);
                    request.setAttribute("InvoiceObj", invoice);
                    request.setAttribute("BuyerObj", buyer);
                    session.setAttribute("BuyerObj", buyer);
                    nextURL = jspPathPrefix + "/invoiceItems.jsp?time=" + java.time.Instant.now().getEpochSecond();
                } else if (userPath.equals("/showItems")) {
                    int supplierId = (Integer) session.getAttribute("SupplierId");
                    List<Item> itemList = DBSvc.getItems(supplierId);
                    request.setAttribute("ItemList", itemList);
                    nextURL = jspPathPrefix + "/showItems.jsp?time=" + java.time.Instant.now().getEpochSecond();
                } else if (userPath.equals("/showPmts")) {
                    int supplierId = (Integer) session.getAttribute("SupplierId");
                    List<Pmt> pmtList = DBSvc.getPmts(supplierId);
                    request.setAttribute("PmtList", pmtList);
                    nextURL = jspPathPrefix + "/showPmts.jsp?time=" + java.time.Instant.now().getEpochSecond();
                } else if (userPath.equals("/showAccounts")) {
                    int supplierId = (Integer) session.getAttribute("SupplierId");
                    List<Account> acctList = DBSvc.getAccounts(supplierId);
                    request.setAttribute("AccountList", acctList);
                    nextURL = jspPathPrefix + "/showAccounts.jsp?time=" + java.time.Instant.now().getEpochSecond();
                } else if (userPath.equals("/displayJson")) {
                    int supplierId = (Integer) session.getAttribute("SupplierId");
                    String EntityName = request.getParameter("EntityName");
                    String EntityId = request.getParameter("EntityId");
                    JsonRep repObj = JsonRep.getObject(supplierId, EntityName, EntityId);
                    request.setAttribute("JsonObj", repObj);
                    request.setAttribute("EntityName", EntityName);
                    nextURL = jspPathPrefix + "/displayJson.jsp?time=" + java.time.Instant.now().getEpochSecond();
                } else if (userPath.equals("/tBI")) {
                    nextURL = jspPathPrefix + "/tBI.jsp";
                }
            }
        } catch (Exception e) {
            LogObj.log(e, Log.PFATAL);
            LogObj.loglnT("Exception in RptControllerServlet: " + e.getMessage(), Log.PFATAL);
            LogObj.flushBuffer();
            //session.setAttribute("ErrorMsg", "System Error: Please try again later.");
            //nextURL = jspPathPrefix + "/login.jsp";
            session.setAttribute("DisplayMsg", "Error: Msg from Exception - " + e.getMessage());
            nextURL = jspPathPrefix + "/displayMsg.jsp";
        }

        // use RequestDispatcher to forward request internally
        //String url =  userPath + ".jsp";
        try {
            request.getRequestDispatcher(nextURL).forward(request, response);
        } catch (Exception ex) {
            LogObj.loglnT("Exception in Rpt Controller, could not forward  : " + ex.getMessage(), Log.PFATAL);
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
