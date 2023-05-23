/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cqb.db;

/**
 *
 * @author hari-work
 */
import cc.db.*;
import java.sql.SQLException;
import java.math.*;

public class LineItem extends Entity {

    /*
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
        },    */
    protected DBIntegerField SupplierId;
    protected DBStringField LineType;
    protected DBStringField InvoiceId;
    protected DBIntegerField LineNum;
    protected DBStringField ItemName;
    protected DBStringField Description;

    protected DBDecimalField UnitPrice;
    protected DBDecimalField Quantity;
    protected DBDecimalField SubTotal;
    protected DBDecimalField TaxAmount;
    protected DBDecimalField TotalAmount;

    protected DBStringField Taxable;

    protected static final String TABLE_NAME = "LineItem_T";

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    /**
     * Constructor
     */
    public LineItem() {

        super();

        LoadExceptionString = "ErrorCode=ERROR_INVALID_LINEITEM&ErrorMsg="
                + "No LineItem exists in the InvoiceLineItem table with the given key";

        SupplierId = new DBIntegerField("SupplierId");
        KeyFieldsList.add(SupplierId);

        LineType = new DBStringField("LineType");
        KeyFieldsList.add(LineType);

        InvoiceId = new DBStringField("InvoiceId");
        FieldsList.add(InvoiceId);

        LineNum = new DBIntegerField("LineNum");
        FieldsList.add(LineNum);

        ItemName = new DBStringField("ItemName");
        FieldsList.add(ItemName);

        Description = new DBStringField("Description");
        FieldsList.add(Description);

        UnitPrice = new DBDecimalField("UnitPrice");
        FieldsList.add(UnitPrice);

        Quantity = new DBDecimalField("Quantity");
        FieldsList.add(Quantity);

        SubTotal = new DBDecimalField("SubTotal");
        FieldsList.add(SubTotal);

        TaxAmount = new DBDecimalField("TaxAmount");
        FieldsList.add(TaxAmount);

        TotalAmount = new DBDecimalField("TotalAmount");
        FieldsList.add(TotalAmount);

        Taxable = new DBStringField("Taxable");
        FieldsList.add(Taxable);

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
    public String getInvoiceId() {
        return InvoiceId.getValue();
    }

    public void setInvoiceId(String InvoiceId) {
        this.InvoiceId.setValue(InvoiceId);
    }

    public String getLineType() {
        return LineType.getValue();
    }

    public void setLineType(String LineType) {
        this.LineType.setValue(LineType);
    }

    public Integer getLineNum() {
        return LineNum.getValue();
    }

    public void setLineNum(Integer LineNum) {
        this.LineNum.setValue(LineNum);
    }

    public Integer getSupplierId() {
        return SupplierId.getValue();
    }

    public void setSupplierId(Integer SupplierId) {
        this.SupplierId.setValue(SupplierId);
    }

    public String getItemName() {
        return ItemName.getValue();
    }

    public void setItemName(String ItemName) {
        this.ItemName.setValue(ItemName);
    }

    public String getTaxable() {
        return Taxable.getValue();
    }

    public void setTaxable(String Taxable) {
        this.Taxable.setValue(Taxable);
    }

    public boolean isTaxable() {
        if (Taxable.getValue().equalsIgnoreCase("TAX")) {
            return true;
        } else {
            return false;
        }
    }

    public String getDescription() {
        return Description.getValue();
    }

    public void setDescription(String Description) {
        this.Description.setValue(Description);
    }

    public BigDecimal getUnitPrice() {
        return UnitPrice.getValue();
    }

    public void setUnitPrice(BigDecimal UnitPrice) {
        this.UnitPrice.setValue(UnitPrice);
    }

    public BigDecimal getSubTotal() {
        return SubTotal.getValue();
    }

    public void setSubTotal(BigDecimal SubTotal) {
        this.SubTotal.setValue(SubTotal);
    }

    public BigDecimal getTaxAmount() {
        return TaxAmount.getValue();
    }

    public void setTaxAmount(BigDecimal TaxAmount) {
        this.TaxAmount.setValue(TaxAmount);
    }

    public BigDecimal getTotalAmount() {
        return TotalAmount.getValue();
    }

    public void setTotalAmount(BigDecimal TotalAmount) {
        this.TotalAmount.setValue(TotalAmount);
    }

    public BigDecimal getQuantity() {
        return Quantity.getValue();
    }

    public void setQuantity(BigDecimal Quantity) {
        this.Quantity.setValue(Quantity);
    }

    public static LineItem getObject(int supplierId, String lineType, String invoiceId, int LineNum)
            throws SQLException {
        LineItem Obj = new LineItem();

        Obj.setSupplierId(supplierId);
        Obj.setLineType(lineType);
        Obj.setInvoiceId(invoiceId);
        Obj.setLineNum(LineNum);
        Obj.load();

        return (Obj);
    }

}
