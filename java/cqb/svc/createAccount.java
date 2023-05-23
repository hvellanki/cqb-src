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
public class createAccount {

    static Log LogObj = null;

    static {
        try {
            LogObj = Log.getReference("cqb");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    "id": "40000-933270541",
--      "name": "Accounts Receivable",
--      "type": "Asset",
--      "description": "Accounts Receivable",
--      "currentBalance": 93007.93,
--      "modifiedDate": "2023-03-21T15:44:48Z",
     */
    public static void createAccountObj(int supplierId, JSONObject acctJson) throws SQLException {

        SafeResp RespObj = new SafeResp(acctJson);
        try {
            cqb.db.Account acctObj = new cqb.db.Account();

            acctObj.setSupplierId(supplierId);
            acctObj.setAccountId(RespObj.get("id"));

            acctObj.setAccountName(RespObj.get("name"));
            acctObj.setAccountType(RespObj.get("type"));
            acctObj.setBalance(RespObj.getDecimal("currentBalance"));
            acctObj.setUpdatedDate(RespObj.get("modifiedDate"));

            acctObj.create();

            createJsonRep.createJsonRep(supplierId, "Account", acctObj.getAccountId(), acctJson.toJSONString());

        } catch (Exception e) {
            LogObj.logln("Exception while processing account: " + acctJson, Log.PINFO);
            LogObj.log(e, Log.PFATAL);
            throw e;
        }

    }

}
