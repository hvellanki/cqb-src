package cqb.svc;

import java.io.IOException;
import java.sql.SQLException;
import cc.util.*;
import cqb.data.SafeResp;
import cqb.db.*;
import org.json.simple.*;

/**
 *
 * @author hari-work
 */
public class createSupplier {

    static Log LogObj = null;

    static {
        try {
            LogObj = Log.getReference("cqb");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
{
  "id": "e47fa7eb-8d65-4ff9-8d46-3a19be59ef78",
  "name": "Technicalium",
  "platform": "",
  "redirect": "https://link.codat.io/company/e47fa7eb-8d65-4ff9-8d46-3a19be59ef78",
  "dataConnections": [],
  "created": "2023-05-04T02:52:26.0427746Z"
}
     */
    public static Supplier create(String userId, String password, JSONObject respJson) throws SQLException, Exception {

        SafeResp RespObj = new SafeResp(respJson);
        try {
            cqb.db.Supplier supplierObj = new cqb.db.Supplier();

            //supplierObj.setSupplierId(supplierId);
            supplierObj.setCompanyId(RespObj.get("id"));

            supplierObj.setCompanyName(RespObj.get("name"));
            supplierObj.setAuthURL(RespObj.get("redirect"));

            supplierObj.create();

            supplierObj = (Supplier) supplierObj.loadObject("Where CompanyId='" + RespObj.get("id") + "'");

            User userObj = new User();
            userObj.setUserId(userId);
            userObj.setPassword(password);
            userObj.setSupplierId(supplierObj.getSupplierId());
            userObj.setUserType("Seller");
            userObj.create();

            return supplierObj;

        } catch (Exception e) {
            LogObj.logln("Exception while processing account: " + respJson, Log.PINFO);
            LogObj.log(e, Log.PFATAL);
            throw e;
        }

    }

}
