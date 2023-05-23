/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cqb.svc;

import cc.util.Log;
import cc.util.*;
import cqb.db.Item;
import cqb.db.TaxRate;

import java.io.IOException;

import javax.servlet.http.*;
import java.math.BigDecimal;
import java.sql.SQLException;

import org.json.simple.*;

/**
 *
 * @author hari-work
 */
public class SaveInvoice {

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

    /*
    INVOICE:
{
    
   "dueDate":"2023-11-30T00:00:00",
   
   "subTotal":500.0,
   "lineItems":[
      {
         "totalAmount":35.00,
         "taxRateRef":{
            "name":"Tax (TAX CODE)",
            "id":"10000-999022286"
         },
         "itemRef":{
            "name":"Repairs",
            "id":"40000-933272655"
         },
         "quantity":10.0,
         "unitAmount":50.0,
         "subTotal":500.0        
      }
   ],
   "amountDue":450.0,
   "totalAmount":450.0,
   
   "customerRef":{
      "companyName":"Vitton, David",
      "id":"A70000-1197762972"
   },
   
   "invoiceNumber":"1-discounttest",

   "issueDate":"2023-04-25T00:00:00",
   "status":"Submitted"
}
     */
    @SuppressWarnings({"unchecked", "unchecked", "unchecked"})
    public String saveInvoice(HttpServletRequest req, HttpSession session, int supplierId) throws Exception {

        String dueDateStr = req.getParameter("DueDate");

        LogObj.logln("Received DueDate : " + dueDateStr, Log.PINFO);

        String InvDateStr = req.getParameter("InvoiceDate");

        String buyerId = req.getParameter("BuyerId");
        JSONObject custJson = new JSONObject();
        custJson.put("id", buyerId);

        //Buyer buyer = Buyer.getObject(supplierId, buyerId);
        JSONObject invJson = new JSONObject();

        invJson.put("dueDate", SvcUtil.getISODateTime(dueDateStr));
        invJson.put("issueDate", SvcUtil.getISODateTime(InvDateStr));
        invJson.put("customerRef", custJson);
        //invJson.put("totalAmountExcludingTax", new BigDecimal(req.getParameter("SubTotal")));
        //invJson.put("totalTaxAmount", new BigDecimal(req.getParameter("Tax")));
        invJson.put("totalAmount", new BigDecimal(req.getParameter("SubTotal")));
        invJson.put("amountDue", new BigDecimal(req.getParameter("Total")));

        invJson.put("subTotal", new BigDecimal(req.getParameter("SubTotal")));

        invJson.put("note", req.getParameter("Memo"));

        invJson.put("status", "Submitted");

        JSONArray lineItems = setLineItems(req, supplierId);

        invJson.put("lineItems", lineItems);

        LogObj.logln("JSON of Invoice being posted :" + invJson, Log.PHIGH);

        JSONObject PushResp = QueryHelper.postData(supplierId, "invoices", invJson.toString());

        LogObj.logln("Push Response received :" + PushResp, Log.PHIGH);

        createInvoice.createPushedInvoice(invJson, PushResp, supplierId);

        return "";
    }

    /*
   "lineItema":[{ 
            {
         "totalAmount":35.00,
         "taxRateRef":{
            "name":"Tax (TAX CODE)",
            "id":"10000-999022286"
         },
         "itemRef":{
            "name":"Repairs",
            "id":"40000-933272655"
         },
         "quantity":10.0,
         "unitAmount":50.0,
         "subTotal":500.0        
      }
        }
     */
    @SuppressWarnings("unchecked")
    private JSONArray setLineItems(HttpServletRequest req, int supplierId) throws SQLException {

        TaxRate NonTax = TaxRate.getObject(supplierId, "Non");
        TaxRate Taxable = TaxRate.getObject(supplierId, "Tax");

        String[] itemIds = req.getParameterValues("ItemId");

        String unitPrices[] = req.getParameterValues("Rate");

        String[] qtys = req.getParameterValues("Qty");
        String[] subTotals = req.getParameterValues("Amount");

        //String[] netAmounts = req.getParameterValues("NetAmount");
        String[] taxable = req.getParameterValues("Taxable");

        JSONArray lineItems = new JSONArray();
        for (int i = 0; i < itemIds.length; i++) {

            JSONObject lineItem = new JSONObject();
          //  Item item = Item.getObject(supplierId, itemIds[i]);

            JSONObject taxRef = new JSONObject();
            if (taxable[i].equalsIgnoreCase("tax")) {
                taxRef.put("id", Taxable.getTaxRateId());
            } else {
                taxRef.put("id", NonTax.getTaxRateId());
            }
            lineItem.put("taxRateRef", taxRef);

            JSONObject itemRef = new JSONObject();
            itemRef.put("id", itemIds[i]);
            lineItem.put("itemRef", itemRef);

            lineItem.put("quantity", new BigDecimal(qtys[i]));

            lineItem.put("unitAmount", new BigDecimal(unitPrices[i]));

            lineItem.put("subTotal", new BigDecimal(subTotals[i]));
            lineItem.put("totalAmount", new BigDecimal(subTotals[i]));
            //lineItem.put("sequence", i);

            lineItems.add(lineItem);
        }

        return lineItems;

    }

}
