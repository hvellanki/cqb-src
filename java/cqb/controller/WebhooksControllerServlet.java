/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cqb.controller;

import cqb.svc.SvcUtil;
import cqb.db.*;
//import cqb.svc.WebhookSvc;
import cc.util.*;
import cqb.svc.createInvoice;
import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author hari-work
 */
@WebServlet(name = "WebhooksControllerServlet", urlPatterns = {"/webhook"})
public class WebhooksControllerServlet extends HttpServlet {

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
/*
        {
   "CompanyId":"1ccc96a5-5726-4194-88b7-89a727cf24d9",
   "ClientId":"c9e35ad1-63ac-49cc-b918-53644041abef",
   "ClientName":"Bill360 Inc.",
   "DataConnectionId":"dc69625f-e7cc-4dfc-b926-0cc6a5297926",
   "RuleId":"2aff2a87-1d29-4b34-ba34-fa035bdf213e",
   "RuleType":"Push Operation Status Changed()",
   "AlertId":"1fde417b-757d-4425-bd70-9c51a1caa2cd",
   "Message":"invoices triggered notification for PushOperationStatusChanged at 2023-05-01T03:02:54.1820474Z",
   "Data":{
      "dataType":"invoices",
      "status":"Success",
      "pushOperationKey":"6cac182d-aa9b-4e25-b655-a0058dfb86b5"
   }
}
        */
        LogObj.loglnT("WebHooksController called by Codat. ", Log.PINFO);
        try {
            String webHookStr = SvcUtil.getBody(request);
            
            JSONObject webHookJson = (JSONObject) JSONValue.parse(webHookStr);
            String companyId = (String)webHookJson.get("CompanyId");

            Supplier supplierObj = new Supplier();
            
            supplierObj = (Supplier)supplierObj.loadObject("Where CompanyId = '" + companyId + "'");
           
            processHook(webHookJson, supplierObj.getSupplierId());
            
            response.setStatus(HttpServletResponse.SC_OK);

        } catch (Exception e) {
            LogObj.log(e, Log.PFATAL);
            LogObj.loglnT("Exception in WebHooksControllerServlet: " + e.getMessage(), Log.PFATAL);
            LogObj.flushBuffer();
            response.setStatus(HttpServletResponse.SC_OK);
        }
        response.flushBuffer();
    }
    
    private void processHook(JSONObject webHookJson, int supplierId) throws Exception{
        JSONObject dataJson = (JSONObject)webHookJson.get("Data");
        String dataType = (String)dataJson.get("dataType");     
        
        if(dataType.equals("invoices")){
            createInvoice.updatePushedInvoice(dataJson, supplierId);
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
