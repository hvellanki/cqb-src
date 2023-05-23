/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
public class createTaxRate {

    static Log LogObj = null;

    static {
        try {
            LogObj = Log.getReference("cqb");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    --"id": "TRG-360000-1071521681",
--      "name": "E. Bayshore/County",
--      "code": "E. Bayshore/County",
--      "effectiveTaxRate": 8.05,
--      "totalTaxRate": 8.05,
--      "modifiedDate": "2023-04-21T02:49:13Z",,
     */
    public static void createTaxRateObj(int supplierId, JSONObject acctJson) throws SQLException {

        SafeResp RespObj = new SafeResp(acctJson);
        try {
            cqb.db.TaxRate acctObj = new cqb.db.TaxRate();

            acctObj.setSupplierId(supplierId);
            acctObj.setTaxRateId(RespObj.get("id"));

            acctObj.setTaxRateName(RespObj.get("code"));

            acctObj.setTaxRateValue(RespObj.getDecimal("totalTaxRate"));
            acctObj.setUpdatedDate(RespObj.get("modifiedDate"));

            acctObj.create();

            createJsonRep.createJsonRep(supplierId, "TaxRate", acctObj.getTaxRateId(), acctJson.toJSONString());

        } catch (Exception e) {
            LogObj.logln("Exception while processing account: " + acctJson, Log.PINFO);
            LogObj.log(e, Log.PFATAL);
            throw e;
        }

    }

}
