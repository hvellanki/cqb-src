/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cqb.op;

import cc.util.Log;
import java.io.IOException;
import java.sql.*;

/**
 *
 * @author hari-work
 */
public class LastUpdatedOp {

    static Log LogObj = null;

    static {
        try {
            LogObj = Log.getReference("cqb");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setLastUpdated(int supplierId, String entity) throws SQLException {

        deleteLastUpdated(supplierId, entity);

        //LastUpdated upd = new LastUpdated();
        /*upd.setEntity(entity);
        upd.setSupplierId(supplierId);
        upd.setLastUpdatedTime("" + (new java.util.Date()));
        upd.create();*/
    }

    public static String getLastUpdatedTime(int supplierId, String entity) throws SQLException {
        String lastUpdated = "";
        try {
            // LastUpdated upd = LastUpdated.getObject(supplierId, entity);
            //lastUpdated = upd.getLastUpdatedTime();
        } catch (Exception e) {
            LogObj.loglnT("Exception when gettig lastUpdated : " + e.getMessage(), Log.PFATAL);
        }
        return lastUpdated;
    }

    private static void deleteLastUpdated(int supplierId, String entity) throws SQLException {

        LogObj.loglnT("Deleting last updated entry of Supplier with Id : " + supplierId + " and entity " + entity, Log.PFATAL);
        // LastUpdated upd = new LastUpdated();
        //upd.delete("Where SupplierId=" + supplierId + " AND Entity='" + entity + "'");

    }

}
