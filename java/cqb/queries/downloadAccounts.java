package cqb.queries;

import cc.util.CCDBException;
import java.io.IOException;

import org.json.simple.*;

import cc.util.*;
import cqb.svc.*;
import cqb.db.JsonRep;

import java.sql.SQLException;
import javax.servlet.http.HttpSession;

public class downloadAccounts {

    static Log LogObj = null;

    static {
        try {
            LogObj = Log.getReference("cqb");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getAccounts(int supplierId) throws Exception, IOException {

        JSONObject acctsObj = QueryHelper.getData(supplierId, "accounts");
        JSONArray accts = (JSONArray) acctsObj.get("results");

        LogObj.logln("# of accounts : " + accts.size(), Log.PINFO);

        try {

            deleteAccts(supplierId);
            deleteAcctsJson(supplierId);

            for (int i = 0; i < accts.size(); i++) {

                JSONObject acctObj = (JSONObject) accts.get(i);
                createAccount.createAccountObj(supplierId, acctObj);
            }

        } catch (Exception e) {
            // 401
            LogObj.logln("Exception message: " + e.getMessage(), Log.PINFO);
            LogObj.log(e, Log.PFATAL);
            throw e;
        }

        cqb.op.LastUpdatedOp.setLastUpdated(supplierId, "Account");
    }

    private void deleteAccts(int supplierId) throws SQLException {
        try {
            LogObj.loglnT("Deleting accounts of Supplier with Id : " + supplierId, Log.PFATAL);
            cqb.db.Account acct = new cqb.db.Account();
            acct.delete("Where SupplierId=" + supplierId);

        } catch (CCDBException e) {
            LogObj.loglnT("Unable to delete Accounts of Supplier with Id : " + supplierId, Log.PFATAL);
            LogObj.log(e, Log.PFATAL);
        }
    }

    private void deleteAcctsJson(int supplierId) throws SQLException {
        try {
            LogObj.loglnT("Deleting Json values of accts of Supplier with Id : " + supplierId, Log.PFATAL);
            JsonRep rep = new JsonRep();
            rep.delete("Where SupplierId=" + supplierId + " AND EntityName='Account'");

        } catch (CCDBException e) {
            LogObj.loglnT("Unable to delete Json values of accts of Supplier with Id : " + supplierId, Log.PFATAL);
            LogObj.log(e, Log.PFATAL);
        }
    }

}
