
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cqb.queries;

import cqb.svc.*;

import cqb.svc.createJsonRep;
import cqb.db.Buyer;
import cqb.db.JsonRep;
import cc.util.*;

import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author hari-work
 */
public class downloadBuyers {

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

    /**
     * Sample QBO API call using OAuth2 tokens
     *
     * @param session
     * @return
     */

    private int supplierId;
    public void getBuyers(HttpSession session) throws SQLException, Exception {

        supplierId = (Integer) session.getAttribute("SupplierId");

        deleteBuyers(supplierId);
        deleteBuyerJson(supplierId);

        try {

            JSONObject buyersObj = QueryHelper.getData(supplierId, "customers");
            JSONArray buyers = (JSONArray) buyersObj.get("results");

            LogObj.logln("# of buyers : " + buyers.size(), Log.PINFO);

            processBuyers(buyers);

        } catch (Exception e1) {
            LogObj.loglnT("Exception while downloading buyers: " + e1.getMessage(), Log.PFATAL);
            LogObj.log(e1, Log.PFATAL);
        }

    }

    private void processBuyers(JSONArray buyers) throws SQLException, Exception {
        LogObj.loglnT("Number of buyers returned " + buyers.size(), Log.PFATAL);

        if (buyers.size() > 0) {

            for (int i = 0; i < buyers.size(); i++) {
                JSONObject customer = (JSONObject) buyers.get(i);
                createBuyer.create(customer, supplierId);
            }
        }

    }

    private void deleteBuyers(int supplierId) throws SQLException {
        try {
            LogObj.loglnT("Deleting buyers of Supplier with Id : " + supplierId, Log.PFATAL);
            Buyer buyer = new Buyer();
            buyer.delete("Where SupplierId='" + supplierId + "'");

        } catch (CCDBException e) {
            LogObj.loglnT("Unable to delete buyers of Supplier with Id : " + supplierId, Log.PFATAL);
            LogObj.log(e, Log.PFATAL);
        }
    }

    private void deleteBuyerJson(int supplierId) throws SQLException {
        try {
            LogObj.loglnT("Deleting Json values of buyers of Supplier with Id : " + supplierId, Log.PFATAL);
            JsonRep rep = new JsonRep();
            rep.delete("Where SupplierId='" + supplierId + "' AND EntityName='Buyer'");

        } catch (CCDBException e) {
            LogObj.loglnT("Unable to delete Json values of buyers of Supplier with Id : " + supplierId, Log.PFATAL);
            LogObj.log(e, Log.PFATAL);
        }
    }
}
