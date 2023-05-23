package cqb.svc;

import cc.util.CCDBException;
import java.io.IOException;

import cc.util.*;

import java.sql.SQLException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class processLineItems {

    static Log LogObj = null;

    static {
        try {
            LogObj = Log.getReference("cqb");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void process(JSONArray items, String invoiceId, int supplierId) throws SQLException {

        deleteItems(supplierId, invoiceId);

        try {
            for (int i = 0; i < items.size(); i++) {

                JSONObject itemObj = (JSONObject) items.get(i);
                createLineItem.create(supplierId, invoiceId, "SalesInvoice", itemObj, i);

            }

        } catch (Exception e) {
            // 401
            LogObj.logln("Exception message: " + e.getMessage(), Log.PINFO);
            LogObj.log(e, Log.PFATAL);
            throw e;
        }

    }

    private static void deleteItems(int supplierId, String invoiceId) throws SQLException {
        try {
            LogObj.loglnT("Deleting line items of Supplier with Id : " + supplierId + " AND InvoiceId :" + invoiceId, Log.PFATAL);
            cqb.db.LineItem item = new cqb.db.LineItem();
            item.delete("Where SupplierId=" + supplierId + " AND InvoiceId like '" + invoiceId + "%'");

        } catch (CCDBException e) {
            LogObj.loglnT("Unable to delete line Items of Supplier with Id : " + supplierId, Log.PFATAL);
            LogObj.log(e, Log.PFATAL);
        }
    }

}
