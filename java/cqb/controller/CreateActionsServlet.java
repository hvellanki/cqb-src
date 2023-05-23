/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cqb.controller;

import cqb.data.InvoiceInfo;
import cqb.svc.*;
import cqb.db.*;
import cc.util.*;
import java.io.*;
import java.math.BigDecimal;
import java.util.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

/**
 *
 * @author hari-work
 */
@WebServlet(name = "CreateActionsServlet", urlPatterns = {"/voidInvoice", "/createInvoice", "/confirmInvoice", "/cancelInvoice",
    "/receivePayment", "/processPayment", "/saveInvoice", "/sendInvoice", "/createCompany", "/saveCompany"})

public class CreateActionsServlet extends HttpServlet {

    final String jspPathPrefix = "/WEB-INF/view";

    static Log LogObj = null;
    static Props PropsObj = null;

    static {
        try {
            cc.db.Entity.setAppSpace("cqb");
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
        String userPath = request.getServletPath();

        HttpSession session = request.getSession();
        String nextURL = "";
        LogObj.loglnT("CreateActions reached by userPath : " + userPath, Log.PINFO);
        try {
            if (!SessionSvc.checkSession(session)) {
                session.setAttribute("ErrorMsg", "Session expired or invalid, please login to continue");
                nextURL = jspPathPrefix + "/login.jsp";
            } else if (userPath.equals("/createInvoice")) {
                int supplierId = (Integer) session.getAttribute("SupplierId");
                Buyer currentBuyerObj = (Buyer) session.getAttribute("BuyerObj");
                List buyersList = currentBuyerObj.loadList("Where SupplierId=" + supplierId + " AND BuyerId <> '" + currentBuyerObj.getBuyerId()
                        + "' Order by CustomerName asc");
                buyersList.add(0, currentBuyerObj); // Make the currentBuyer show up first when creating the invoice.
                request.setAttribute("BuyersList", buyersList);
                Item item = new Item();
                List itemsList = item.loadList("Where SupplierId=" + supplierId + " AND (Type='Inventory' OR Type='Service')");
                request.setAttribute("ItemsList", itemsList);
                /*String ASTOn = (String) session.getAttribute("ASTOn");
                if (ASTOn.equals("N")) {
                    TaxCode code = new TaxCode();
                    List codesList = code.loadList("Where SupplierId=" + supplierId + " AND IsCustom='Y'");
                    request.setAttribute("CodesList", codesList);
                    nextURL = jspPathPrefix + "/createInvoiceV3.jsp";
                } else {*/
                nextURL = jspPathPrefix + "/createInvoice.jsp?time=" + java.time.Instant.now().getEpochSecond();
                //  }
            } else if (userPath.equals("/saveInvoice")) {
                int supplierId = (Integer) session.getAttribute("SupplierId");
                String buyerId = request.getParameter("BuyerId");
                SaveInvoice Obj = new SaveInvoice();
                Obj.saveInvoice(request, session, supplierId);
                Buyer buyer = Buyer.getObject(supplierId, buyerId);
                List<InvoiceInfo> infoList = BuyerSvc.getBuyerInvoices(supplierId, buyerId);
                request.setAttribute("InvoiceInfo", infoList);
                request.setAttribute("BuyerObj", buyer);
                request.setAttribute("B360Host", PropsObj.getProperty("B360Host"));
                nextURL = jspPathPrefix + "/buyerInvoices.jsp?time=" + java.time.Instant.now().getEpochSecond();
            } else if (userPath.equals("/receivePayment")) {
                int supplierId = (Integer) session.getAttribute("SupplierId");
                String invoiceId = request.getParameter("InvoiceId");
                BigDecimal Amount = new BigDecimal(request.getParameter("Amount"));
                Invoice invoice = Invoice.getObject(supplierId, invoiceId);
                Buyer buyer = Buyer.getObject(supplierId, invoice.getBuyerId());
                List itemsList = BuyerSvc.getItems(supplierId);
                request.setAttribute("ItemsList", itemsList);
                request.setAttribute("InvoiceObj", invoice);
                request.setAttribute("BuyerObj", buyer);
                request.setAttribute("Amount", Amount);
                nextURL = jspPathPrefix + "/receivePayment.jsp?time=" + java.time.Instant.now().getEpochSecond();
            } else if (userPath.equals("/createCompany")) {
                nextURL = jspPathPrefix + "/createCompany.jsp";
            } else if (userPath.equals("/saveCompany")) {
                Supplier supplierObj = SaveCompany.saveCompany(request, session);
                session.setAttribute("SupplierName", supplierObj.getCompanyName());
                session.setAttribute("CompanyId", supplierObj.getCompanyId());
                User userObj = new User();

                userObj = (User) userObj.loadObject("Where SupplierId=" + supplierObj.getSupplierId());
                session.setAttribute("UserId", userObj.getUserId());
                session.setAttribute("Password", userObj.getPassword());
                nextURL = jspPathPrefix + "/createdCompany.jsp";
            } else if (userPath.equals("/processPayment")) {
                int supplierId = (Integer) session.getAttribute("SupplierId");
                String invoiceId = request.getParameter("InvoiceId");
                BigDecimal Amount = new BigDecimal(request.getParameter("Amount"));
                SavePmt PmtObj = new SavePmt();
                PmtObj.savePmt(request, session, supplierId);

                String DisplayMsg = "Thank you for your Payment!";
                request.setAttribute("DisplayMsg", DisplayMsg);
                nextURL = jspPathPrefix + "/confirmAction.jsp";
            }

        } catch (Exception e) {
            LogObj.log(e, Log.PFATAL);
            LogObj.loglnT("Exception in CreateActionsServlet: " + e.getMessage(), Log.PFATAL);
            LogObj.flushBuffer();
            session.setAttribute("DisplayMsg", "Error: Msg from Exception - " + e.getMessage());
            nextURL = jspPathPrefix + "/displayMsg.jsp";
            //session.setAttribute("ErrorMsg", "System Error: Please try again later.");
            //nextURL = jspPathPrefix + "/login.jsp";
        }

        // use RequestDispatcher to forward request internally
        //String url =  userPath + ".jsp";
        try {
            if (nextURL != null) {
                request.getRequestDispatcher(nextURL).forward(request, response);
            }
        } catch (Exception ex) {
            LogObj.loglnT("Exception in CreateActions Controller, could not forward  : " + ex.getMessage(), Log.PFATAL);

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
