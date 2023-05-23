/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cqb.svc;

import java.sql.*;
import cc.util.*;
import static java.lang.System.*;
import cqb.data.*;
import java.util.*;
//import cqb.db.Batch;

/**
 * Copyright Capital Commerce, LLC
 *
 * @author Hari Vellanki
 *
 */
public class DBSvc {

    /**
     * DBNameSpace indicates the namespace value to be used to read the
     * DBConnectionManager property file (db.prop). The properties used by the
     * DBConnectionManager to access a database are grouped by the NameSpace.
     *
     */
    protected static String DBNameSpace = null;
    /**
     * Instance of the DBConnectionManager class which provides connection
     * pooling.
     */
    static final DBConnectionManager ConnMgr = DBConnectionManager.getInstance();
    /**
     * Props object which provides access to both the File based and DB based
     * properties (stored in the Property_T table) of an application are read.
     */
    protected static Props PropsObj = null;
    /**
     * Static Log object, which is initialized once.
     */
    protected static Log LogObj = null;
    static final String AppSpace = "cqb";

    static {

        try {
            PropsObj = Props.getReference(AppSpace);
            LogObj = Log.getReference(AppSpace);
            DBNameSpace = PropsObj.getRequiredFileProperty("db.NameSpace");
        } catch (Exception e) {
            err.println("IOException while trying to initialize App :" + AppSpace);
            e.printStackTrace(err);
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    static String GetInvoiceNumbersStr = "{Call USP_OP_GetInvoiceNumbers(?, ?, ?, ?, ?)}";

    public static BuyerInvoiceInfo getInvoiceNumbers(int SupplierId, String BuyerId) throws SQLException {
        try {
            return getInvoiceNumbers(SupplierId, BuyerId, false);
        } catch (SQLException e) {// try with a Validated Connection, as most of SQLExceptions are due to connection issues
            LogObj.Filelogln("Retrying getInvoiceNumbers with a Validated connection", Log.PFATAL);
            return getInvoiceNumbers(SupplierId, BuyerId, true);
        }
    }

    /**
     *
     * @param StoreId
     * @return
     * @throws SQLException
     */
    public static BuyerInvoiceInfo getInvoiceNumbers(int SupplierId, String BuyerId, boolean ValidateConn) throws SQLException {

        CallableStatement Stmt = null;

        Connection Conn = getConnection(ValidateConn);
        try {
            Stmt = Conn.prepareCall(GetInvoiceNumbersStr);
            Stmt.setInt(1, SupplierId);
            Stmt.setString(2, BuyerId);
            Stmt.registerOutParameter(3, java.sql.Types.INTEGER);
            Stmt.registerOutParameter(4, java.sql.Types.INTEGER);
            Stmt.registerOutParameter(5, java.sql.Types.INTEGER);
            Stmt.execute();

            BuyerInvoiceInfo info = new BuyerInvoiceInfo();
            info.numInvoices = Stmt.getInt(3); // return total number ofinvoices
            info.numPastDue = Stmt.getInt(4);
            info.numPaid = Stmt.getInt(5);
            Stmt.close();
            return info;

        } catch (SQLException sqle) {
            LogObj.logln(Log.PFATAL);
            LogObj.logln("SQL Exception while executing :" + GetInvoiceNumbersStr,
                    Log.PFATAL);
            LogObj.log("Text from SQLException :" + sqle.getMessage(), Log.PFATAL);
            LogObj.log(sqle, Log.PFATAL);

            if (Stmt != null) {
                Stmt.close();
            }
            throw sqle;
        } finally {
            ConnMgr.freeConnection(DBNameSpace, Conn);
        }
    }

    /**
     *
     * @param NameSpace
     * @param ValidateConn
     * @return
     * @throws SQLException
     */
    public static Connection getConnection(String NameSpace, boolean ValidateConn) throws SQLException {

        Connection Conn = ConnMgr.getConnection(NameSpace, ValidateConn);
        if (Conn == null) {
            String ErrorString = "Error - Unable to connect to DB";
            throw new SQLException(ErrorString);
        }
        return Conn;
    }

    public static List getPmts(int supplierId) throws SQLException {
        cqb.db.Pmt pmt = new cqb.db.Pmt();
        return pmt.loadList("Where SupplierId=" + supplierId);
    }

    public static List getItems(int supplierId) throws SQLException {
        cqb.db.Item item = new cqb.db.Item();
        return item.loadList("Where SupplierId=" + supplierId);
    }

    public static List getAccounts(int supplierId) throws SQLException {
        cqb.db.Account acct = new cqb.db.Account();
        return acct.loadList("Where SupplierId=" + supplierId);
    }

    /*
    public static List getNotes(int supplierId) throws SQLException {
        cqb.db.CreditNote note = new cqb.db.CreditNote();
        return note.loadList("Where SupplierId=" + supplierId);
    }
     */
    /**
     *
     * @return @throws SQLException
     */
    public static Connection getConnection(boolean ValidateConn) throws SQLException {
        return getConnection(DBNameSpace, ValidateConn);
    }
}
