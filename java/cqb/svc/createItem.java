/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cqb.svc;

import java.io.IOException;
import java.sql.SQLException;
import java.math.*;
import cc.util.*;
import cqb.data.SafeResp;

import org.json.simple.*;

/**
 *
 * @author hari-work
 */
public class createItem {

    static Log LogObj = null;

    static {
        try {
            LogObj = Log.getReference("ms");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     "id": "100000-933272656",
      "name": "Cabinets:Cabinet Pulls",
      "modifiedDate": "2023-03-25T14:47:27Z",
     
      "type": "Inventory",
      "isBillItem": true,
     
      "isInvoiceItem": true,
      "invoiceItem": {
        "description": "Cabinet Pulls",
        "unitPrice": 0,
        "accountRef": {
          "id": "1A0000-933270542",
          "name": "Materials Income"
        },
        "taxRateRef": {
          "id": "10000-999022286",
          "name": "Tax (TAX CODE)"
        }
      },
      
     */
    public static void create(int supplierId, JSONObject itemJson) throws Exception {

        LogObj.logln("Processing itemJSON: " + itemJson, Log.PINFO);

        SafeResp RespObj = new SafeResp(itemJson);
        try {
            cqb.db.Item itemObj = new cqb.db.Item();

            itemObj.setSupplierId(supplierId);
            itemObj.setItemId(RespObj.get("id"));

            itemObj.setName(RespObj.get("name"));
            itemObj.setType(RespObj.get("type"));

            JSONObject invItem = (JSONObject) RespObj.getObject("invoiceItem");
            if (invItem != null) {
                itemObj.setDescription((String) invItem.get("descripton"));

                if (invItem.get("unitPrice") != null) {
                    itemObj.setUnitPrice(new BigDecimal("" + invItem.get("unitPrice")));
                    itemObj.setDescription((String)invItem.get("description"));
                }

                JSONObject acctRef = (JSONObject) invItem.get("accountRef");
                if (acctRef != null) {
                    itemObj.setIncomeAccount((String) acctRef.get("name"));
                }

                JSONObject taxRef = (JSONObject) invItem.get("taxRateRef");
                if (taxRef != null) {
                    String taxName = (String) taxRef.get("name");
                    if (taxName.contains("Tax")) {
                        itemObj.setTaxable(true);
                    } else {
                        itemObj.setTaxable(false);
                    }
                }
            }

            itemObj.create();
            createJsonRep.createJsonRep(supplierId, "Item", itemObj.getItemId(), itemJson.toJSONString());
        } catch (Exception e) {
            LogObj.logln("Exception while processing item: " + itemJson, Log.PFATAL);
            LogObj.log(e, Log.PFATAL);
            throw e;
        }

    }

}
