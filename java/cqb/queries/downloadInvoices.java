
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cqb.queries;

import cc.util.Log;
import cc.util.Props;
import java.sql.SQLException;

import javax.servlet.http.HttpSession;

import java.io.IOException;
import cqb.db.LineItem;
import cqb.db.JsonRep;
import cqb.svc.*;
import cc.util.CCDBException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author hari-work
 */
public class downloadInvoices {

    static Log LogObj = null;
    static Props PropsObj = null;

    static {
        try {
            LogObj = Log.getReference("cqb");
            PropsObj = Props.getReference("cqb");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getInvoices(HttpSession session) throws Exception {

        int supplierId = (Integer) session.getAttribute("SupplierId");

        deleteInvoices(supplierId);

        deleteInvoiceJson(supplierId);

        try {

            JSONObject invoicesObj = QueryHelper.getData(supplierId, "invoices");
            JSONArray invoices = (JSONArray) invoicesObj.get("results");

            LogObj.logln("# of Invoices : " + invoices.size(), Log.PINFO);

            try {
                for (int i = 0; i < invoices.size(); i++) {

                    JSONObject invoiceObj = (JSONObject) invoices.get(i);
                    createInvoice.create(invoiceObj, supplierId);
                    LogObj.logln(Log.PINFO);
                    LogObj.logln(Log.PINFO);
                    String invoiceId = (String) invoiceObj.get("id");
                    JSONArray lineItems = (JSONArray) invoiceObj.get("lineItems");
                    LogObj.logln("# of LineItems : " + lineItems.size(), Log.PINFO);
                    processLineItems.process(lineItems, invoiceId, supplierId);

                }

            } catch (Exception e) {
                // 401
                LogObj.logln("Exception message: " + e.getMessage(), Log.PINFO);
                LogObj.log(e, Log.PFATAL);
                throw e;
            }

        } catch (Exception e1) {
            LogObj.loglnT("Exception while downloading invoices: " + e1.getMessage(), Log.PFATAL);
        }

    }

    private void deleteInvoices(int supplierId) throws SQLException {

        try {
            LogObj.loglnT("Deleting invoices of Supplier with Id : " + supplierId, Log.PFATAL);
            LineItem item = new LineItem();
            item.delete("Where SupplierId=" + supplierId);
            cqb.db.Invoice invoice = new cqb.db.Invoice();
            invoice.delete("Where SupplierId=" + supplierId );
        } catch (CCDBException e) {
            LogObj.loglnT("Unable to delete invoices of Supplier with Id : " + supplierId, Log.PFATAL);
            LogObj.log(e, Log.PFATAL);
        }
    }

    private void deleteInvoiceJson(int supplierId) throws SQLException {
        try {
            LogObj.loglnT("Deleting Json values of invoices of Supplier with Id : " + supplierId, Log.PFATAL);
            JsonRep rep = new JsonRep();
            rep.delete("Where SupplierId=" + supplierId + " AND EntityName='Invoice'");

        } catch (CCDBException e) {
            LogObj.loglnT("Unable to delete Json values of invoices of Supplier with Id : " + supplierId, Log.PFATAL);
            LogObj.log(e, Log.PFATAL);
        }
    }

}
