package cqb.svc;

import cc.util.Log;
import cc.util.*;
import cqb.db.*;

import java.io.IOException;

import javax.servlet.http.*;
import java.math.BigDecimal;
import java.sql.SQLException;

import org.json.simple.*;

/**
 *
 * @author hari-work
 */
public class SavePmt {

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
    /*
            
      "customerRef": {
        "id": "220000-933272658",
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
            }
          ]
        }
      ],
      "reference": "19650",
      
             
     */
    @SuppressWarnings({"unchecked", "unchecked", "unchecked"})
    public String savePmt(HttpServletRequest req, HttpSession session, int supplierId) throws Exception {

        String dueDateStr = req.getParameter("DueDate");

        LogObj.logln("Received DueDate : " + dueDateStr, Log.PINFO);

        String invoiceId = req.getParameter("InvoiceId");
        Invoice invObj = Invoice.getObject(supplierId, invoiceId);

        JSONObject pmtJson = new JSONObject();

        JSONObject custJson = new JSONObject();
        custJson.put("id", invObj.getBuyerId());
        pmtJson.put("customerRef", custJson);

        JSONObject acctJson = new JSONObject();
        Account acctObj = new Account();
        acctObj = (Account) acctObj.loadObject("Where AccountName='UnDeposited Funds'");
        acctJson.put("id", acctObj.getAccountId());
        pmtJson.put("accountRef", acctJson);

        pmtJson.put("totalAmount", new BigDecimal(req.getParameter("Amount")));

        pmtJson.put("date", SvcUtil.getCurrentISODateTime());

        //pmtJson.put("note", req.getParameter("Memo"));
        JSONArray lineItems = setLineItems(req, invoiceId, supplierId);

        pmtJson.put("lines", lineItems);

        LogObj.logln("JSON of Payment being posted :" + pmtJson, Log.PHIGH);

        JSONObject PushResp = QueryHelper.postData(supplierId, "payments", pmtJson.toString());

        LogObj.logln("Push Response received :" + PushResp, Log.PHIGH);

        createPmt.createPushedPmt(pmtJson, PushResp, supplierId);

        return "";
    }

    /*
  "lines": [
        {
          "amount": 14488.64,
          "links": [
            {
              "type": "Invoice",
              "id": "469E-1071530054",
              "amount": -14488.64,
            }
          ]
        }
      ],
     */
    @SuppressWarnings("unchecked")
    private JSONArray setLineItems(HttpServletRequest req, String invoiceId, int supplierId) throws SQLException {
        JSONArray lines = new JSONArray();
        JSONObject line = new JSONObject();

        JSONArray links = new JSONArray();
        JSONObject link = new JSONObject();
        link.put("type", "invoice");
        link.put("id", invoiceId);
        link.put("amount", new BigDecimal("-" + req.getParameter("Amount")));

        links.add(link);

        line.put("amount", new BigDecimal(req.getParameter("Amount")));

        line.put("links", links);

        lines.add(line);
        return lines;

    }

}
