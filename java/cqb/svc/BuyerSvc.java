/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cqb.svc;

import cqb.db.*;
import java.io.IOException;
import java.sql.SQLException;
import cc.util.*;
import java.util.*;
import java.text.*;
import cqb.data.*;
import java.math.BigDecimal;

/**
 *
 * @author hari-work
 */
public class BuyerSvc {

    static Log LogObj = null;

    static {
        try {
            LogObj = Log.getReference("cqb");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<BuyerInvoiceInfo> getBuyersInvoices(int supplierId) throws SQLException {
        Buyer buyer = new Buyer();
        List buyersList = buyer.loadList("Where SupplierId=" + supplierId + " Order by CustomerName asc");
        List<BuyerInvoiceInfo> buyerInvoices = new LinkedList<>();
        for (int i = 0; i < buyersList.size(); i++) {
            Buyer buyerObj = (Buyer) buyersList.get(i);
            BuyerInvoiceInfo info = DBSvc.getInvoiceNumbers(supplierId, buyerObj.getBuyerId());
            info.buyerName = buyerObj.getCustomerName();
            info.buyerId = buyerObj.getBuyerId();
            buyerInvoices.add(info);

        }
        return buyerInvoices;
    }

    public static List<InvoiceInfo> getBuyerInvoices(int supplierId, String buyerId) throws Exception {
        Invoice invoice = new Invoice();
        List invoicesList = invoice.loadList("Where SupplierId=" + supplierId + " and BuyerId = '" + buyerId + "' order by InvoiceNumber ASC");
        List<InvoiceInfo> buyerInvoices = new LinkedList<>();
        for (int i = 0; i < invoicesList.size(); i++) {
            Invoice invoiceObj = (Invoice) invoicesList.get(i);
            InvoiceInfo info = new InvoiceInfo();
            info.InvoiceId = invoiceObj.getInvoiceId();
            info.InvoiceNumber = invoiceObj.getInvoiceNumber();
            info.buyerId = buyerId;
            info.Amount = invoiceObj.getTotalAmt().toString();
            info.DueDate = invoiceObj.getDueDate();
            if (info.DueDate == null) {
                info.DueDate = "";
            }
            info.Balance = invoiceObj.getAmtDue().toString();

            Date dueDate = parseDate(invoiceObj.getDueDate());

            int DaysPast = SvcUtil.getDaysPast(dueDate.getTime());
            if (DaysPast > 0 && (invoiceObj.getAmtDue().compareTo(new BigDecimal("0.0")) == 1)) {
                info.DaysPastDue = DaysPast;
            } else {
                info.DaysPastDue = 0;
            }

            info.PmtDate = invoiceObj.getPaidDate();

            setInvoiceStatus(invoiceObj, info);

            buyerInvoices.add(info);

        }
        return buyerInvoices;
    }

    private static Date parseDate(String dbDate) throws ParseException {

        //dbDate = "2025-04-15T00:00:00";
        dbDate = dbDate.replace('T', ' ');
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = parser.parse(dbDate);
        return date;
    }

    private static void setInvoiceStatus(Invoice invoiceObj, InvoiceInfo info) {
        if (invoiceObj.getStatus().length() > 0) {
            info.Status = invoiceObj.getStatus();
        } else {
            if (StringUtil.isZeroAmt(info.Balance) && StringUtil.isZeroAmt(info.Amount)) {
                info.Status = "VOIDED";
            } else if (StringUtil.isZeroAmt(info.Balance)) {
                info.Status = "PAID";
            } else if (info.DaysPastDue > 0) {
                // if OverDue
                if (invoiceObj.getAmtDue().compareTo(invoiceObj.getTotalAmt()) == 1) {
                    // Balance != Total Amount
                    info.Status = "OverDue(Partially Paid)";
                } else { // Balance == Total Amount
                    info.Status = "OverDue";
                }
            } else {
                if (invoiceObj.getAmtDue().compareTo(invoiceObj.getTotalAmt()) != 0) {
                    // Balance != Total Amount
                    info.Status = "Partially Paid";
                }
            }
        }
        if (info.Status == null) {
            info.Status = "";
        }
    }

    public static String getInvoiceStatus(int supplierId, String invoiceId) {

        String InvoiceStatus = "";

        try {
            Invoice invoiceObj = Invoice.getObject(supplierId, invoiceId);

            int DaysPast = SvcUtil.getDaysPast(parseDate(invoiceObj.getDueDate()).getTime());
            int DaysPastDue = 0;
            if (DaysPast > 0 && (invoiceObj.getAmtDue().compareTo(new BigDecimal("0.0")) == 1)) {
                DaysPastDue = DaysPast;
            }
            String Balance = invoiceObj.getAmtDue().toString();
            String Amount = invoiceObj.getTotalAmt().toString();
            if (StringUtil.isZeroAmt(Balance) && StringUtil.isZeroAmt(Amount)) {
                InvoiceStatus = "VOIDED";
            } else if (StringUtil.isZeroAmt(Balance)) {
                InvoiceStatus = "PAID";
            } else if (DaysPastDue > 0) {
                // if OverDue
                if (invoiceObj.getAmtDue().compareTo(invoiceObj.getTotalAmt()) == 1) {
                    // Balance != Total Amount
                    InvoiceStatus = "OverDue(Partially Paid)";
                } else { // Balance == Total Amount
                    InvoiceStatus = "OverDue";
                }
            } else {
                if (invoiceObj.getAmtDue().compareTo(invoiceObj.getTotalAmt()) == 1) {
                    // Balance != Total Amount
                    InvoiceStatus = "Partially Paid";
                }
            }
            //InvoiceStatus = invoiceObj.getStatus();
        } catch (Exception e) {
            LogObj.logln("Exception while getting status for invoice :" + invoiceId, Log.PFATAL);
            //LogObj.log(e, Log.PFATAL);
        }
        return InvoiceStatus;
    }

    public static List getInvoiceItems(int supplierId, String invoiceId) throws SQLException {

        LineItem invoiceItem = new LineItem();
        List itemsList
                = invoiceItem.loadList("Where SupplierId=" + supplierId + " and InvoiceId = '" + invoiceId + "'  Order by LineNum ASC");
        return itemsList;
    }

    public static List getItems(int supplierId) throws SQLException {

        Item item = new Item();
        return item.loadList("Where SupplierId=" + supplierId);
    }

}
