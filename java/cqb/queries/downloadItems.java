
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
import cqb.db.JsonRep;
import cqb.svc.*;
import cc.util.CCDBException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author hari-work
 */
public class downloadItems {

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

    public void getItems(int supplierId) throws Exception {

        deleteItems(supplierId);
        deleteItemsJson(supplierId);

        try {

            JSONObject itemsObj = QueryHelper.getData(supplierId, "items");
            JSONArray items = (JSONArray) itemsObj.get("results");

            LogObj.logln("# of Items : " + items.size(), Log.PINFO);

            try {
                for (int i = 0; i < items.size(); i++) {

                    JSONObject itemObj = (JSONObject) items.get(i);
                    createItem.create(supplierId, itemObj);
                }

            } catch (Exception e) {
                // 401
                LogObj.logln("Exception message: " + e.getMessage(), Log.PINFO);
                LogObj.log(e, Log.PFATAL);
                throw e;
            }

        } catch (Exception e1) {
            LogObj.loglnT("Exception while downloading items: " + e1.getMessage(), Log.PFATAL);
            LogObj.flushBuffer();
        }

    }

    private void deleteItems(int supplierId) throws SQLException {
        try {
            LogObj.loglnT("Deleting items of Supplier with Id : " + supplierId, Log.PFATAL);
            cqb.db.Item item = new cqb.db.Item();
            item.delete("Where SupplierId=" + supplierId);

        } catch (CCDBException e) {
            LogObj.loglnT("Unable to delete Items of Supplier with Id : " + supplierId, Log.PFATAL);
            LogObj.log(e, Log.PFATAL);
        }
    }

    private void deleteItemsJson(int supplierId) throws SQLException {
        try {
            LogObj.loglnT("Deleting Json values of items of Supplier with Id : " + supplierId, Log.PFATAL);
            JsonRep rep = new JsonRep();
            rep.delete("Where SupplierId=" + supplierId + " AND EntityName='Item'");

        } catch (CCDBException e) {
            LogObj.loglnT("Unable to delete Json values of items of Supplier with Id : " + supplierId, Log.PFATAL);
            LogObj.log(e, Log.PFATAL);
        }
    }

}
