/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cqb.svc;

import cqb.db.Supplier;
import cc.util.*;
import cqb.data.SafeResp;
import cqb.db.User;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.http.*;
import org.json.simple.*;

/**
 *
 * @author hari-work
 */
public class LoginSvc {

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

    public static boolean doLogin(HttpServletRequest request, HttpServletResponse response) throws SQLException, Exception {
        String userId = request.getParameter("UserId");
        String password = request.getParameter("Password");
        HttpSession session = request.getSession();
        try {

            if (userId != null) {
                User userObj = User.getObject(userId);
                if (password == null || !password.equals(userObj.getPassword())) {
                    LogObj.loglnT("Password mismatch for User :" + userId, Log.PFATAL);
                    return false;
                } else {
                    Supplier supplier = Supplier.getObject(userObj.getSupplierId());

                    session.setAttribute("UserId", userId);
                    session.setAttribute("SupplierId", userObj.getSupplierId());

                    session.setAttribute("CompanyId", supplier.getCompanyId());

                    if (supplier.getConnectionId().length() == 0) {
                        LogObj.loglnT("No Connection set for User :" + userId, Log.PFATAL);
                        LogObj.loglnT("Trying to download Connection for User :" + userId, Log.PFATAL);
                        setConnection(supplier);
                        setSupplier(supplier);
                        if (supplier.getConnectionId().length() == 0) { // if still no connectionId 
                            session.setAttribute("AuthURL", supplier.getAuthURL());
                        }
                    }

                    session.setAttribute("ConnectionId", supplier.getConnectionId());
                    session.setAttribute("SupplierName", supplier.getCompanyName());

                    if (userObj.getUserType().equalsIgnoreCase("Buyer")) {
                        session.setAttribute("UserType", "Buyer");
                        session.setAttribute("BuyerId", userObj.getBuyerId());
                    } else {
                        session.setAttribute("UserType", "Supplier");
                    }
                    return true;
                }
            } else {
                return false;
            }
        } catch (CCDBException | SQLException ex) {
            LogObj.log(ex, Log.PFATAL);
            LogObj.loglnT("SQLException or No User with this UserId:" + userId, Log.PFATAL);
            throw ex;
        }
    }

    /*
    {
  "results": [
    {
      "id": "dc69625f-e7cc-4dfc-b926-0cc6a5297926",
      "integrationId": "aff0f057-255f-42c4-8d4a-ae23b43e1615",
      "integrationKey": "pqsw",
      "sourceId": "19eefa32-58a8-4097-b95a-ef150bf24fa0",
      "platformName": "QuickBooks Desktop",
      "linkUrl": "https://link-api.codat.io/companies/1ccc96a5-5726-4194-88b7-89a727cf24d9/connections/dc69625f-e7cc-4dfc-b926-0cc6a5297926/start",
      "status": "Linked",
    }
  ],
 
  "totalResults": 1,
  
}
     */
    private static void setConnection(Supplier supplierObj) throws Exception {

        JSONObject resp = QueryHelper.getConnections(supplierObj.getSupplierId());

        if (resp == null || resp.get("totalResults") == null) {
            LogObj.loglnT("No connections.", Log.PFATAL);
            return;
        }
        long numConns = (Long) resp.get("totalResults");

        if (numConns > 0) { // Now connected so get and set the ConnectionId;
            JSONArray results = (JSONArray) resp.get("results");

            JSONObject connObj = (JSONObject) results.get(0);
            String connId = (String) connObj.get("id");
            supplierObj.setConnectionId(connId);
            supplierObj.update();
            LogObj.loglnT("Set new connection.", Log.PFATAL);
        }
    }

    private static void setSupplier(Supplier supplierObj) throws Exception {

        JSONObject company = QueryHelper.getData(supplierObj.getSupplierId(), "info");

        SafeResp RespObj = new SafeResp(company);
        /*
         {
  "companyName": "ACME Corporation",
  "companyLegalName": "ACME Corporation Ltd.",
  "addresses": [
    {
      "type": "Billing",
      "line1": "Warner House",
      "line2": "98 Theobald's Road",
      "city": "London",
      "region": "",
      "country": "United Kingdom",
      "postalcode": "WC1X 8WB"
    },
    {
      "type": "Unknown",
      "line1": "123 Sierra Way",
      "line2": "",
      "city": "San Pablo",
      "region": "CA",
      "country": "",
      "postalCode": "87999"
    }
  ],
  "phoneNumbers": [
    {
      "number": "010 1234 5678",
      "type": "Landline"
    }
  ],
}
         */

        try {

            supplierObj.setCompanyName(RespObj.get("companyName"));

            JSONArray phones = (JSONArray) RespObj.getObject("phoneNumbers");

            if (phones != null) {
                JSONObject phone1 = (JSONObject) phones.get(0);

                if (phone1 != null) {
                    supplierObj.setPhone((String) phone1.get("number"));
                }
            }

            JSONArray addresses = (JSONArray) RespObj.getObject("addresses");

            if (addresses != null) {
                JSONObject addr = (JSONObject) addresses.get(0);

                if (addr != null) {
                    String line2 = (String) addr.get("line2");
                    // Sometimes line1 may have Street address instead of name and line2 will be empty
                    if (line2 != null && line2.length() > 0) {

                        supplierObj.setCompanyStreet((String) addr.get("line2"));
                    } else {
                        supplierObj.setCompanyStreet((String) addr.get("line1"));
                    }
                    supplierObj.setCompanyCity((String) addr.get("city"));
                    supplierObj.setCompanyState((String) addr.get("region"));
                    supplierObj.setCompanyZip((String) addr.get("postalCode"));
                }
            }

            supplierObj.update();

        } catch (Exception e) {
            LogObj.loglnT("Exception while processing company: " + company, Log.PFATAL);
            LogObj.log(e, Log.PFATAL);
            throw e;
        }

    }
}
