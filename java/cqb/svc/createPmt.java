/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cqb.svc;

import cqb.data.SafeResp;
import java.io.IOException;
import java.sql.SQLException;
import java.math.*;
import java.time.*;
import cc.util.*;
import org.json.simple.*;

/**
 *
 * @author hari-work
 */
public class createPmt {

    static Log LogObj = null;

    static {
        try {
            LogObj = Log.getReference("ms");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void create(JSONObject pmtJson, int supplierId) throws SQLException, Exception {

        try {
            SafeResp RespObj = new SafeResp(pmtJson);

            cqb.db.Pmt pmtObj = new cqb.db.Pmt();

            /*
            "id": "6915-1197775450",
      "customerRef": {
        "id": "220000-933272658",
        "companyName": "Melton, Johnny"
      },
      "accountRef": {
        "id": "80000-933270541",
        "name": "Undeposited Funds"
      },
      "totalAmount": 14488.64,
      "date": "2023-01-10T00:00:00",
      "note": "Check - 80000-933270541",
      "lines": [
        {
          "amount": 14488.64,
          "links": [
            {
              "type": "Invoice",
              "id": "469E-1071530054",
              "amount": -14488.64,
              "currencyRate": 1
            }
          ]
        }
      ],
      "modifiedDate": "2023-03-21T15:46:50Z",
      "sourceModifiedDate": "2023-12-16T05:06:11",
      "reference": "19650",
      
             */
            pmtObj.setSupplierId(supplierId);

            pmtObj.setPmtId(RespObj.get("id"));
            JSONObject custObj = (JSONObject) RespObj.getObject("customerRef");

            pmtObj.setBuyerId((String) custObj.get("id"));
            pmtObj.setBuyerName((String) custObj.get("companyName"));

            JSONObject acctObj = (JSONObject) RespObj.getObject("accountRef");

            pmtObj.setAcctId((String) acctObj.get("id"));
            pmtObj.setAcctName((String) acctObj.get("name"));
            
            pmtObj.setPmtRefNum(RespObj.get("reference"));

            pmtObj.setPmtDate((RespObj.get("date")));
            pmtObj.setTotalAmount(new BigDecimal(RespObj.get("totalAmount")));

            JSONArray lines = (JSONArray) RespObj.getObject("lines");
            if (lines != null) {
                JSONArray links = (JSONArray) (((JSONObject) lines.get(0)).get("links"));
                if (links != null) {
                    JSONObject link = (JSONObject) links.get(0);
                    pmtObj.setLinkedTxId((String) link.get("id"));
                    pmtObj.setLinkedTxType((String) link.get("type"));
                }
            }

            pmtObj.setUpdatedDate(RespObj.get("lastModifiedDateTime"));

            pmtObj.create();

            createJsonRep.createJsonRep(supplierId, "Payment", pmtObj.getPmtId(), pmtJson.toJSONString());

        } catch (Exception e) {
            LogObj.logln("Exception while processing payment: " + pmtJson, Log.PINFO);
            LogObj.log(e, Log.PFATAL);
            throw e;
        }
    }
}
