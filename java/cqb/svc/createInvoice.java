/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cqb.svc;

import cqb.data.SafeResp;

import cc.util.*;
import cqb.db.*;

import java.math.BigDecimal;

import java.io.IOException;
import java.math.RoundingMode;
import java.sql.SQLException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author hari-work
 */
public class createInvoice {

    static Log LogObj = null;

    static {
        try {
            LogObj = Log.getReference("cqb");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void update(JSONObject invoiceJson, String invoiceId, int supplierId) throws SQLException, Exception {

        //     InvoiceSvc.deleteInvoice(invoiceId);
        //    InvoiceSvc.deleteInvoiceJson(invoiceId);
        create(invoiceJson, supplierId);

    }

    /*
    {
      "id": "10",
      "invoiceNumber": "1002",
      "customerRef": {
        "id": "2",
        "companyName": "Bill's Windsurf Shop"
      },
     
      "issueDate": "2020-12-08T00:00:00",
      "dueDate": "2021-01-07T00:00:00",
      "modifiedDate": "2023-03-25T15:48:13Z",
      "sourceModifiedDate": "2021-03-24T19:56:01Z",
      "paidOnDate": "2020-12-29T00:00:00",
   
      "lineItems": [
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
      ],
   
      "totalDiscount": 0,
      "subTotal": 175, 
      "totalTaxAmount": 0,
            "additionalTaxPercentage":7.5,
      "totalAmount": 175,
      "amountDue": 0,
      "status": "Paid",
      "note": "Thank you for your business and have a great day!",      
    },
     */
    public static void create(JSONObject invoiceJson, int supplierId) throws SQLException, Exception {

        try {
            cqb.db.Invoice invoiceObj = new cqb.db.Invoice();

            setInvData(invoiceJson, invoiceObj, supplierId);
            invoiceObj.create();

            createJsonRep.createJsonRep(supplierId, "Invoice", invoiceObj.getInvoiceId(), invoiceJson.toJSONString());

        } catch (Exception e) {
            LogObj.logln("Exception while processing invoice: " + invoiceJson, Log.PINFO);
            LogObj.log(e, Log.PFATAL);
            throw e;
        }

    }

    public static void createPushedInvoice(JSONObject invoiceJson, JSONObject PushResp, int supplierId) throws SQLException, Exception {

        try {
            cqb.db.Invoice invoiceObj = new cqb.db.Invoice();

            String pushKey = (String) PushResp.get("pushOperationKey");
            String status = (String) PushResp.get("status");

            setInvData(invoiceJson, invoiceObj, supplierId);

            invoiceObj.setStatus(status);
            invoiceObj.setPushKey(pushKey);

            TimeUtil tUtil = new TimeUtil();
            invoiceObj.setInvoiceId("INV-" + tUtil.getDateTime());

            invoiceObj.create();

            JSONArray lineItems = (JSONArray) invoiceJson.get("lineItems");
            LogObj.logln("# of LineItems : " + lineItems.size(), Log.PINFO);
            processLineItems.process(lineItems, invoiceObj.getInvoiceId(), supplierId);

            createJsonRep.createJsonRep(supplierId, "Invoice", invoiceObj.getInvoiceId(), invoiceJson.toJSONString());

        } catch (Exception e) {
            LogObj.logln("Exception while processing invoice: " + invoiceJson, Log.PINFO);
            LogObj.log(e, Log.PFATAL);
            throw e;
        }

    }

    public static void updatePushedInvoice(JSONObject dataJson, int supplierId) throws SQLException, Exception {

        try {
            cqb.db.Invoice invoiceObj = new cqb.db.Invoice();

            String status = (String) dataJson.get("status");
            String pushKey = (String) dataJson.get("pushOperationKey");

            invoiceObj = (Invoice) invoiceObj.loadObject("Where PushKey='" + pushKey + "'");

            if (status.equalsIgnoreCase("Success")) {
                JSONObject pushJson = QueryHelper.getPushData(supplierId, pushKey);
                JSONObject invoiceJson = (JSONObject) pushJson.get("data");

                invoiceObj.delete();

                setInvData(invoiceJson, invoiceObj, supplierId);

                invoiceObj.create();
                setBuyerTaxRate(supplierId, invoiceObj);
                createJsonRep.createJsonRep(supplierId, "Invoice", invoiceObj.getInvoiceId(), invoiceJson.toJSONString());
                JSONArray lineItems = (JSONArray) invoiceJson.get("lineItems");
                LogObj.logln("# of LineItems : " + lineItems.size(), Log.PINFO);
                processLineItems.process(lineItems, invoiceObj.getInvoiceId(), supplierId);
            } else {
                invoiceObj.setStatus("Failed");
                invoiceObj.update();
            }

        } catch (Exception e) {
            LogObj.logln("Exception while processing pushed data: " + dataJson, Log.PINFO);
            LogObj.log(e, Log.PFATAL);
            throw e;
        }

    }

    private static void setBuyerTaxRate(int supplierId, Invoice invObj) throws SQLException, Exception {

        Buyer buyer = Buyer.getObject(supplierId, invObj.getBuyerId());
        buyer.setTaxRate(invObj.getTaxRate());
        buyer.update();
    }

    private static void setInvData(JSONObject invoiceJson, Invoice invoiceObj, int supplierId) {

        SafeResp RespObj = new SafeResp(invoiceJson);

        invoiceObj.setSupplierId(supplierId);
        invoiceObj.setInvoiceId(RespObj.get("id"));
        invoiceObj.setInvoiceNumber(RespObj.get("invoiceNumber"));

        invoiceObj.setAmtDue(RespObj.getDecimal("amountDue"));
        invoiceObj.setDiscountAmount(RespObj.getDecimal("totalDiscount"));
        invoiceObj.setSubTotal(RespObj.getDecimal("subTotal"));
        invoiceObj.setTax(RespObj.getDecimal("totalTaxAmount"));
        invoiceObj.setTaxRate(RespObj.getDecimal("additionalTaxPercentage"));
        invoiceObj.setTotalAmt(RespObj.getDecimal("totalAmount"));
        invoiceObj.setStatus(RespObj.get("status"));
        invoiceObj.setPrivateNote(RespObj.get("note"));
        invoiceObj.setTxnDate(RespObj.get("issueDate"));
        invoiceObj.setDueDate(RespObj.get("dueDate"));
        invoiceObj.setPaidDate(RespObj.get("paidOnDate"));

        JSONObject custObj = (JSONObject) RespObj.getObject("customerRef");
        SafeResp cust = new SafeResp(custObj);
        invoiceObj.setBuyerName(cust.get("companyName"));
        invoiceObj.setBuyerId(cust.get("id"));

    }

    private static BigDecimal getDiscount(BigDecimal UnitAmount, BigDecimal Quantity, BigDecimal DiscountRate) {
        BigDecimal Discount = UnitAmount.multiply(Quantity).multiply(DiscountRate.divide(new BigDecimal(100)));
        return Discount.setScale(2, RoundingMode.HALF_UP);
    }
    /*
    {
  "changes": [
    {
      "type": "Created",
      "recordRef": {
        "id": "2B225-1734318167",
        "dataType": "invoices"
      }
    }
  ],
  "data": {
    "id": "2B225-1734318167",
    "invoiceNumber": "1105",
    "customerRef": {
      "id": "940000-1071506775",
      "companyName": "Baker, Chris"
    },
    "salesOrderRefs": [],
    "issueDate": "2023-04-30T00:00:00.0000000",
    "dueDate": "2023-05-30T00:00:00.0000000",
    "modifiedDate": "2024-12-15T22:02:47.0000000",
    "sourceModifiedDate": "2024-12-15T22:02:47.0000000",
    "paidOnDate": null,
    "currency": "USD",
    "currencyRate": 1,
    "lineItems": [
      {
        "description": "standard interior brass hinge",
        "unitAmount": 35,
        "quantity": 1,
        "discountAmount": null,
        "subTotal": 35,
        "taxAmount": 2.7125,
        "totalAmount": 37.7125,
        "accountRef": null,
        "discountPercentage": null,
        "taxRateRef": {
          "id": "10000-999022286",
          "name": "Taxable Sales",
          "effectiveTaxRate": null
        },
        "itemRef": {
          "id": "450000-1071511428",
          "name": "Hardware:Brass hinges"
        },
        "trackingCategoryRefs": [],
        "tracking": null,
        "isDirectIncome": false
      }
    ],
    "paymentAllocations": [],
    "withholdingTax": [],
    "totalDiscount": 0,
    "subTotal": 35,
    "additionalTaxAmount": 2.71,
    "additionalTaxPercentage": 7.75,
    "totalTaxAmount": 2.71,
    "totalAmount": 37.71,
    "amountDue": 37.71,
    "discountPercentage": 0,
    "status": "Submitted",
    "note": null,
    "metadata": null,
    "supplementalData": null
  },
  "dataType": "invoices",
  "companyId": "1ccc96a5-5726-4194-88b7-89a727cf24d9",
  "pushOperationKey": "6cac182d-aa9b-4e25-b655-a0058dfb86b5",
  "dataConnectionKey": "dc69625f-e7cc-4dfc-b926-0cc6a5297926",
  "requestedOnUtc": "2023-05-01T03:02:23.9022235Z",
  "completedOnUtc": "2023-05-01T03:02:54.0328732Z",
  "status": "Success",
  "validation": {
    "errors": [],
    "warnings": [
      {
        "itemId": "InvoiceLineItem[0].TaxAmount",
        "message": "Failed to push to Invoice as InvoiceLineItem[0].TaxAmount cannot be mapped directly into QuickBooks Desktop and will only be used for validation purposes",
        "validatorName": "Invoice"
      },
      {
        "itemId": "InvoiceLineItem[0].TaxRateRef.EffectiveTaxRate",
        "message": "Failed to push to Invoice as InvoiceLineItem[0].TaxRateRef.EffectiveTaxRate cannot be mapped directly into QuickBooks Desktop and will only be used for validation purposes",
        "validatorName": "Invoice"
      }
    ]
  },
  "statusCode": 200
}
    }*/

}
