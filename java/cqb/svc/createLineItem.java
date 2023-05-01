package cqb.svc;

import java.io.IOException;
import java.sql.SQLException;
import cc.util.*;
import cqb.data.SafeResp;

import org.json.simple.*;

/**
 *
 * @author hari-work
 */
public class createLineItem {

    static Log LogObj = null;

    static {
        try {
            LogObj = Log.getReference("ms");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    {
          "description": "Weekly Gardening Service",
          "unitAmount": 35,
          "quantity": 4,
          "subTotal": 140,
          "taxAmount": 0,
          "totalAmount": 140,
          "accountRef": {
            "id": "45",
            "name": "Landscaping Services"
          },
          "taxRateRef": {
            "id": "NON",
            "name": "NON",
            "effectiveTaxRate": 0
          },
          "itemRef": {
            "id": "6",
            "name": "Gardening"
          },     
        },    
     */
    public static void create(int supplierId, String parentId, String parentType, JSONObject itemJson, int lineNum) throws SQLException {

        SafeResp RespObj = new SafeResp(itemJson);
        try {
            cqb.db.LineItem itemObj = new cqb.db.LineItem();

            itemObj.setSupplierId(supplierId);
            itemObj.setInvoiceId(parentId);
            itemObj.setLineNum(lineNum);
            itemObj.setLineType(parentType);

            itemObj.setDescription(RespObj.get("description"));
            itemObj.setUnitPrice(RespObj.getDecimal("unitAmount"));
            itemObj.setQuantity(RespObj.getDecimal("quantity"));
            itemObj.setTaxAmount(RespObj.getDecimal("taxAmount"));
            itemObj.setSubTotal(RespObj.getDecimal("subTotal"));
            itemObj.setTotalAmount(RespObj.getDecimal("totalAmount"));

            JSONObject taxObj = (JSONObject) RespObj.getObject("taxRateRef");
            if (taxObj != null) {
                SafeResp tax = new SafeResp(taxObj);
                itemObj.setTaxable(tax.get("name"));
            }

            JSONObject itemRefObj = (JSONObject) RespObj.getObject("itemRef");
            if (itemRefObj != null) {
                SafeResp item = new SafeResp(itemRefObj);
                itemObj.setItemName(item.get("name"));
            }

            itemObj.create();

        } catch (Exception e) {
            LogObj.logln("Exception while processing line item: " + itemJson, Log.PINFO);
            LogObj.log(e, Log.PFATAL);
            throw e;
        }

    }

}
