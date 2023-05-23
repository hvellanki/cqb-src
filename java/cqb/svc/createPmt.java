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
import cqb.db.*;
import cqb.queries.downloadInvoices;
import org.json.simple.*;

/**
 *
 * @author hari-work
 */
public class createPmt {

    static Log LogObj = null;

    static {
        try {
            LogObj = Log.getReference("cqb");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void create(JSONObject pmtJson, int supplierId) throws SQLException, Exception {

        try {

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
            setPmtData(pmtJson, pmtObj, supplierId);

            pmtObj.setStatus("APPROVED");

            pmtObj.create();

            createJsonRep.createJsonRep(supplierId, "Payment", pmtObj.getPmtId(), pmtJson.toJSONString());

        } catch (Exception e) {
            LogObj.logln("Exception while processing payment: " + pmtJson, Log.PINFO);
            LogObj.log(e, Log.PFATAL);
            throw e;
        }
    }

    public static void createPushedPmt(JSONObject pmtJson, JSONObject PushResp, int supplierId) throws SQLException, Exception {

        try {
            cqb.db.Pmt pmtObj = new cqb.db.Pmt();

            SafeResp RespObj = new SafeResp(pmtJson);

            String pushKey = (String) PushResp.get("pushOperationKey");
            String status = (String) PushResp.get("status");

            setPmtData(pmtJson, pmtObj, supplierId);

            pmtObj.setStatus(status);
            pmtObj.setPushKey(pushKey);

            TimeUtil tUtil = new TimeUtil();
            pmtObj.setPmtId("PMT-" + tUtil.getDateTime());

            pmtObj.create();

            createJsonRep.createJsonRep(supplierId, "Payment", pmtObj.getPmtId(), pmtJson.toJSONString());

        } catch (Exception e) {
            LogObj.logln("Exception while processing pmt: " + pmtJson, Log.PINFO);
            LogObj.log(e, Log.PFATAL);
            throw e;
        }

    }

    public static void updatePushedPmt(JSONObject dataJson, int supplierId) throws SQLException, Exception {

        try {
            cqb.db.Pmt pmtObj = new cqb.db.Pmt();

            String status = (String) dataJson.get("status");
            String pushKey = (String) dataJson.get("pushOperationKey");

            pmtObj = (Pmt) pmtObj.loadObject("Where PushKey='" + pushKey + "'");

            pmtObj.setStatus(status);

            if (status.equalsIgnoreCase("Success")) {
                JSONObject pushJson = QueryHelper.getPushData(supplierId, pushKey);
                JSONObject pmtJson = (JSONObject) pushJson.get("data");

                pmtObj.delete();

                setPmtData(pmtJson, pmtObj, supplierId);
                pmtObj.create();

                createJsonRep.createJsonRep(supplierId, "Payment", pmtObj.getPmtId(), pmtJson.toJSONString());

                // Have to get updated invoice. Unfortunately no way to PULL only 1 invoice. Have to download  all.  
                QueryHelper.syncData(supplierId, "invoices");

            } else {
                pmtObj.update();
            }

        } catch (Exception e) {
            LogObj.logln("Exception while processing pushed data: " + dataJson, Log.PINFO);
            LogObj.log(e, Log.PFATAL);
            throw e;
        }

    }

    private static void setPmtData(JSONObject pmtJson, Pmt pmtObj, int supplierId) {

        pmtObj.setSupplierId(supplierId);

        SafeResp RespObj = new SafeResp(pmtJson);

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
        if (lines != null && lines.size() > 0) {
            JSONArray links = (JSONArray) (((JSONObject) lines.get(0)).get("links"));
            if (links != null && links.size() > 0) {
                JSONObject link = (JSONObject) links.get(0);
                pmtObj.setLinkedTxId((String) link.get("id"));
                pmtObj.setLinkedTxType((String) link.get("type"));
            }
        }

        pmtObj.setUpdatedDate(RespObj.get("lastModifiedDateTime"));

    }
}
