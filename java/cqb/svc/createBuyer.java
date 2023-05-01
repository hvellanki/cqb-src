/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cqb.svc;

import cqb.db.Buyer;
import cc.util.Log;

import java.io.IOException;
import java.sql.SQLException;
import cqb.data.SafeResp;
import java.math.BigDecimal;

import org.json.simple.*;

/**
 *
 * @author hari-work
 */
public class createBuyer {

    static Log LogObj = null;

    static {
        try {
            LogObj = Log.getReference("ms");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void create(JSONObject buyerJson, int supplierId) throws SQLException {

        try {
            Buyer buyerObj = new Buyer();

            setData(buyerObj, buyerJson, supplierId);

            buyerObj.create();

            createJsonRep.createJsonRep(supplierId, "Buyer", buyerObj.getBuyerId(), buyerJson.toJSONString());

        } catch (Exception e) {
            LogObj.loglnT("Exception while processing buyer: " + buyerJson, Log.PINFO);
            LogObj.log(e, Log.PFATAL);
            LogObj.flushBuffer();
            throw e;
        }
    }

    /*
    public static void update(JSONObject buyerJson, String buyerId, int supplierId) throws SQLException {

        //BuyerSvc.deleteBuyer(buyerId);
        BuyerSvc.deleteBuyerJson(buyerId);

        Buyer buyerObj = Buyer.getObject(supplierId, buyerId);
        setData(buyerObj, buyerJson, supplierId);    

        createJsonRep.createJsonRep(supplierId, "Buyer", buyerObj.getBuyerId(), buyerJson.toJSONString());
        buyerObj.update();
    }*/
    private static void setData(Buyer buyerObj, JSONObject buyerJson, int supplierId) {
        SafeResp RespObj = new SafeResp(buyerJson);
        buyerObj.setSupplierId(supplierId);
        /*
            "id": "150000-933272658",
      "customerName": "Abercrombie, Kristy",
      "contactName": "Kristy Abercrombie",
      "emailAddress": "kristy@samplename.com",
     
      "phone": "415-555-6579",
      "addresses": [
        {
          "type": "Delivery",
          "line1": "Kristy Abercrombie",
          "line2": "5647 Cypress Hill Rd",
          "city": "Bayshore",
          "region": "CA",
          "postalCode": "94326"
        },
        {
          "type": "Billing",
          "line1": "Kristy Abercrombie",
          "line2": "5647 Cypress Hill Rd",
          "city": "Bayshore",
          "region": "CA",
          "postalCode": "94326"
        }
      ],
      "contacts": [
        {
          "name": "Abercrombie, Kristy",
          "email": "kristy@samplename.com",
          "phone": [
            {
              "number": "415-555-6579",
              "type": "Primary"
            }
          ],
          "status": "Active"
        },
        {
          "name": "Kristy Abercrombie",
          "phone": [],
          "status": "Active"
        },
        {
          "name": "Steve Darcangelo",
          "phone": [],
          "status": "Active"
        }
      ],
      "status": "Active",
      "modifiedDate": "2023-03-21T15:45:05Z",
      "sourceModifiedDate": "2023-12-16T05:06:42",
      
         */
        try {
            buyerObj.setBuyerId(RespObj.get("id"));

            buyerObj.setCustomerName(RespObj.get("customerName"));
            buyerObj.setContactName(RespObj.get("contactName"));

            buyerObj.setEmailAddress(RespObj.get("emailAddress"));

            buyerObj.setPhone(RespObj.get("phone"));
            
            buyerObj.setTaxRate(BigDecimal.ZERO);

            JSONArray addresses = (JSONArray) RespObj.getObject("addresses");

            if (addresses != null) {
                JSONObject addr1 = (JSONObject) addresses.get(0);

                if (addr1 != null) {
                    setAddress(addr1, buyerObj);
                    if (addresses.size() > 1) {
                        JSONObject addr2 = (JSONObject) addresses.get(1);

                        if (addr2 != null) {
                            setAddress(addr2, buyerObj);
                        }
                    }
                }
            }

        } catch (Exception e) {
            LogObj.loglnT("Exception while processing buyer: " + buyerJson, Log.PFATAL);
            LogObj.log(e, Log.PFATAL);
            throw e;
        }

        //buyerObj.setUpdatedDate((String)RespObj.get("modifiedDate"));
    }

    private static void setAddress(JSONObject addr, Buyer buyerObj) {
        String contactType = (String) addr.get("type");
        if (contactType.equals("Delivery")) {
            buyerObj.setShipName((String) addr.get("line1"));
            buyerObj.setShipStreet((String) addr.get("line2"));
            buyerObj.setShipCity((String) addr.get("city"));
            buyerObj.setShipState((String) addr.get("region"));
            buyerObj.setShipZip((String) addr.get("postalCode"));
        } else {
            buyerObj.setBillName((String) addr.get("line1"));
            buyerObj.setBillStreet((String) addr.get("line2"));
            buyerObj.setBillCity((String) addr.get("city"));
            buyerObj.setBillState((String) addr.get("region"));
            buyerObj.setBillZip((String) addr.get("postalCode"));
        }
    }

}
