package cqb.db;

/**
 *
 * @author hari-work
 */
import cc.db.*;
import java.math.BigDecimal;
import java.sql.SQLException;

public class Buyer extends Entity {

    protected DBIntegerField SupplierId;

    protected DBStringField BuyerId;
    protected DBStringField CustomerName;
    protected DBStringField ContactName;
    protected DBStringField EmailAddress;
    protected DBStringField Phone;

    protected DBStringField BillName;
    protected DBStringField BillStreet;
    protected DBStringField BillCity;
    protected DBStringField BillState;
    protected DBStringField BillZip;

    protected DBStringField ShipName;
    protected DBStringField ShipStreet;
    protected DBStringField ShipCity;
    protected DBStringField ShipState;
    protected DBStringField ShipZip;

    protected DBDecimalField TaxRate;
    //protected DBStringField ShipAddr;

    protected static final String TABLE_NAME = "Buyer_T";

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    /**
     * Constructor
     */
    public Buyer() {

        super();

        LoadExceptionString = "ErrorCode=ERROR_INVALID_BUYER&ErrorMsg="
                + "No Buyer exists in Buyer_T with the given key";

        SupplierId = new DBIntegerField("SupplierId");
        KeyFieldsList.add(SupplierId);

        BuyerId = new DBStringField("BuyerId");
        KeyFieldsList.add(BuyerId);

        CustomerName = new DBStringField("CustomerName");
        FieldsList.add(CustomerName);

        ContactName = new DBStringField("ContactName");
        FieldsList.add(ContactName);

        EmailAddress = new DBStringField("EmailAddress");
        FieldsList.add(EmailAddress);

        Phone = new DBStringField("Phone");
        FieldsList.add(Phone);

        BillName = new DBStringField("BillName");
        FieldsList.add(BillName);

        BillStreet = new DBStringField("BillStreet");
        FieldsList.add(BillStreet);

        BillCity = new DBStringField("BillCity");
        FieldsList.add(BillCity);

        BillState = new DBStringField("BillState");
        FieldsList.add(BillState);

        BillZip = new DBStringField("BillZip");
        FieldsList.add(BillZip);

        ShipName = new DBStringField("ShipName");
        FieldsList.add(ShipName);

        ShipStreet = new DBStringField("ShipStreet");
        FieldsList.add(ShipStreet);

        ShipCity = new DBStringField("ShipCity");
        FieldsList.add(ShipCity);

        ShipState = new DBStringField("ShipState");
        FieldsList.add(ShipState);

        ShipZip = new DBStringField("ShipZip");
        FieldsList.add(ShipZip);

        TaxRate = new DBDecimalField("TaxRate");
        FieldsList.add(TaxRate);

    }
    /**
     * InsertCallStr related cache and methods. Purpose : To provide a static
     * cache for the LoadCallStr for this entity object to avoid rebuilding the
     * string for every single load of the entity object and accessors.
     *
     */
    static String ms_InsertCallStrCached = null;

    @Override
    protected String getInsertCallStrCached() {
        return ms_InsertCallStrCached;
    }

    @Override
    protected void setInsertCallStrCached(String InsertCallStrCached) {
        ms_InsertCallStrCached = InsertCallStrCached;
    }
    /**
     * LoadCallStr related cache and methods. Purpose : To provide a static
     * cache for the LoadCallStr for this entity object to avoid rebuilding the
     * string for every single load of the entity object and accessors.
     *
     */
    static String ms_LoadCallStrCached = null;

    @Override
    protected String getLoadCallStrCached() {
        return ms_LoadCallStrCached;
    }

    @Override
    protected void setLoadCallStrCached(String LoadCallStrCached) {
        ms_LoadCallStrCached = LoadCallStrCached;
    }

    /**
     * Accessors for the objects representing various DB Fields.
     *
     */
    public String getBuyerId() {
        return BuyerId.getValue();
    }

    public void setBuyerId(String BuyerId) {
        this.BuyerId.setValue(BuyerId);
    }

    public int getSupplierId() {
        return SupplierId.getValue();
    }

    public void setSupplierId(int SupplierId) {
        this.SupplierId.setValue(SupplierId);
    }

    public String getCustomerName() {
        return CustomerName.getValue();
    }

    public void setCustomerName(String CustomerName) {
        this.CustomerName.setValue(CustomerName);
    }

    public String getContactName() {
        return ContactName.getValue();
    }

    public void setContactName(String ContactName) {
        this.ContactName.setValue(ContactName);
    }

    public String getPhone() {
        return Phone.getValue();
    }

    public void setPhone(String Phone) {
        this.Phone.setValue(Phone);
    }

    public String getBillName() {
        return BillName.getValue();
    }

    public void setBillName(String BillName) {
        this.BillName.setValue(BillName);
    }

    public String getBillZip() {
        return BillZip.getValue();
    }

    public void setBillZip(String BillZip) {
        this.BillZip.setValue(BillZip);
    }

    public String getBillStreet() {
        return BillStreet.getValue();
    }

    public void setBillStreet(String BillStreet) {
        this.BillStreet.setValue(BillStreet);
    }

    public String getBillCity() {
        return BillCity.getValue();
    }

    public void setBillCity(String BillCity) {
        this.BillCity.setValue(BillCity);
    }

    public String getBillState() {
        return BillState.getValue();
    }

    public void setBillState(String BillState) {
        this.BillState.setValue(BillState);
    }

    public String getShipName() {
        return ShipName.getValue();
    }

    public void setShipName(String ShipName) {
        this.ShipName.setValue(ShipName);
    }

    public String getShipZip() {
        return ShipZip.getValue();
    }

    public void setShipZip(String ShipZip) {
        this.ShipZip.setValue(ShipZip);
    }

    public String getShipStreet() {
        return ShipStreet.getValue();
    }

    public void setShipStreet(String ShipStreet) {
        this.ShipStreet.setValue(ShipStreet);
    }

    public String getShipCity() {
        return ShipCity.getValue();
    }

    public void setShipCity(String ShipCity) {
        this.ShipCity.setValue(ShipCity);
    }

    public String getShipState() {
        return ShipState.getValue();
    }

    public void setShipState(String ShipState) {
        this.ShipState.setValue(ShipState);
    }

    public String getEmailAddress() {
        return EmailAddress.getValue();
    }

    public void setEmailAddress(String EmailAddress) {
        this.EmailAddress.setValue(EmailAddress);
    }

    public BigDecimal getTaxRate() {
        return TaxRate.getValue();
    }

    public void setTaxRate(BigDecimal TaxRate) {
        this.TaxRate.setValue(TaxRate);
    }

    public String getRateDetail() {
        String strTaxRate = "" + TaxRate.getValue();
        String RateDetail = "0.00";
        if (!strTaxRate.equals("0.00")) {
            RateDetail = "Rate from QBD for the Buyer:" + strTaxRate;
        }

        return RateDetail;
    }

    public String getBillAddress() {
        String Address = BillStreet.getValue();
        if (!BillStreet.getValue().isEmpty()) {
            Address += ", ";
        }
        Address += BillCity.getValue();
        if (!BillCity.getValue().isEmpty()) {
            Address += ", ";
        }

        Address += BillState.getValue();

        Address += ", " + BillZip.getValue();
        return Address;

    }

    public String getShipAddress() {

        String Address = ShipStreet.getValue();
        if (!ShipStreet.getValue().isEmpty()) {
            Address += ", ";
        }
        Address += ShipCity.getValue();
        if (!ShipCity.getValue().isEmpty()) {
            Address += ", ";
        }

        Address += ShipState.getValue();

        Address += ", " + ShipZip.getValue();
        return Address;

    }

    /* public String getBillAddress() {
        String Address = "";
        if (BillStreet.getValue().isEmpty()) {
            return Address;
        } else {
            Address = BillStreet.getValue();
            if (BillCity.getValue().isEmpty()) {
                return Address;
            } else {
                Address += ", " + BillCity.getValue();
                if (BillState.getValue().isEmpty()) {
                    return Address;
                } else {
                    Address += ", " + BillState.getValue();
                    if (BillZip.getValue().isEmpty()) {
                        return Address;
                    } else {
                        Address += ", " + BillZip.getValue();
                        return Address;
                    }
                }

            }

        }
    }*/
 /*
    public String getShipAddress() {
        String Address = "";
        if (ShipStreet.getValue().isEmpty()) {
            return Address;
        } else {
            Address = ShipStreet.getValue();
            if (ShipCity.getValue().isEmpty()) {
                return Address;
            } else {
                Address += ", " + ShipCity.getValue();
                if (ShipState.getValue().isEmpty()) {
                    return Address;
                } else {
                    Address += ", " + ShipState.getValue();
                    if (ShipZip.getValue().isEmpty()) {
                        return Address;
                    } else {
                        Address += ", " + ShipZip.getValue();
                        return Address;
                    }
                }

            }

        }
    }*/
    public static Buyer getObject(int supplierId, String buyerId)
            throws SQLException {
        Buyer Obj = new Buyer();

        Obj.setSupplierId(supplierId);
        Obj.setBuyerId(buyerId);

        Obj.load();

        return (Obj);
    }

}
