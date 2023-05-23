package cqb.queries;

import cc.util.CCDBException;
import java.io.IOException;

import org.json.simple.*;

import cc.util.*;
import cqb.svc.*;
import cqb.db.JsonRep;

import java.sql.SQLException;
import javax.servlet.http.HttpSession;

public class downloadTaxRates {

    static Log LogObj = null;

    static {
        try {
            LogObj = Log.getReference("cqb");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getTaxRates(int supplierId) throws Exception, IOException {

        JSONObject ratesObj = QueryHelper.getData(supplierId, "taxRates");
        JSONArray rates = (JSONArray) ratesObj.get("results");

        LogObj.logln("# of taxRates : " + rates.size(), Log.PINFO);

        try {

            deleteTaxRates(supplierId);
            deleteTaxRatesJson(supplierId);

            for (int i = 0; i < rates.size(); i++) {

                JSONObject rateObj = (JSONObject) rates.get(i);
                createTaxRate.createTaxRateObj(supplierId, rateObj);
            }

        } catch (Exception e) {
            // 401
            LogObj.logln("Exception message: " + e.getMessage(), Log.PINFO);
            LogObj.log(e, Log.PFATAL);
            throw e;
        }

        cqb.op.LastUpdatedOp.setLastUpdated(supplierId, "TaxRate");
    }

    private void deleteTaxRates(int supplierId) throws SQLException {
        try {
            LogObj.loglnT("Deleting taxRates of Supplier with Id : " + supplierId, Log.PFATAL);
            cqb.db.TaxRate rate = new cqb.db.TaxRate();
            rate.delete("Where SupplierId=" + supplierId);

        } catch (CCDBException e) {
            LogObj.loglnT("Unable to delete TaxRates of Supplier with Id : " + supplierId, Log.PFATAL);
            LogObj.log(e, Log.PFATAL);
        }
    }

    private void deleteTaxRatesJson(int supplierId) throws SQLException {
        try {
            LogObj.loglnT("Deleting Json values of rates of Supplier with Id : " + supplierId, Log.PFATAL);
            JsonRep rep = new JsonRep();
            rep.delete("Where SupplierId=" + supplierId + " AND EntityName='TaxRate'");

        } catch (CCDBException e) {
            LogObj.loglnT("Unable to delete Json values of rates of Supplier with Id : " + supplierId, Log.PFATAL);
            LogObj.log(e, Log.PFATAL);
        }
    }

}
