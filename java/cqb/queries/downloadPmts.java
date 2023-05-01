package cqb.queries;

import cqb.svc.*;
import cqb.db.*;
import cc.util.CCDBException;
import java.io.IOException;

import cc.util.*;

import java.sql.SQLException;
import javax.servlet.http.HttpSession;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class downloadPmts {

    static Log LogObj = null;

    static {
        try {
            LogObj = Log.getReference("ms");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    ///customerPaymentJournals(0b4d7ebd-d36f-ec11-bb7f-6045bd8e50f8)/customerPayments
    public void getPmts(HttpSession session) throws Exception, IOException {

        int supplierId = (Integer) session.getAttribute("SupplierId");
        JSONObject pmtsObj = QueryHelper.getData(supplierId, "payments");
        JSONArray pmts = (JSONArray) pmtsObj.get("results");

        LogObj.logln("# of pmts : " + pmts.size(), Log.PINFO);

        try {

            deletePmts(supplierId);
            deletePmtsJson(supplierId);

            for (int i = 0; i < pmts.size(); i++) {

                JSONObject pmtObj = (JSONObject) pmts.get(i);
                createPmt.create(pmtObj, supplierId);
            }

        } catch (Exception e) {
           
            LogObj.logln("Exception message: " + e.getMessage(), Log.PINFO);
            LogObj.log(e, Log.PFATAL);
        }
        cqb.op.LastUpdatedOp.setLastUpdated(supplierId, "Pmt");
    }

    private void deletePmts(int supplierId) throws SQLException {
        try {
            LogObj.loglnT("Deleting pmts of Supplier with Id : " + supplierId, Log.PFATAL);
            cqb.db.Pmt pmt = new cqb.db.Pmt();
            pmt.delete("Where SupplierId=" + supplierId);

        } catch (CCDBException e) {
            LogObj.loglnT("Unable to delete Pmts of Supplier with Id : " + supplierId, Log.PFATAL);
            LogObj.log(e, Log.PFATAL);
        }
    }

    private void deletePmtsJson(int supplierId) throws SQLException {
        try {
            LogObj.loglnT("Deleting Json values of pmts of Supplier with Id : " + supplierId, Log.PFATAL);
            JsonRep rep = new JsonRep();
            rep.delete("Where SupplierId=" + supplierId + " AND EntityName='Payment'");

        } catch (CCDBException e) {
            LogObj.loglnT("Unable to delete Json values of pmts of Supplier with Id : " + supplierId, Log.PFATAL);
            LogObj.log(e, Log.PFATAL);
        }
    }

}
