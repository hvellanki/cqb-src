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
import java.math.BigDecimal;

public class Item extends Entity {

    protected DBIntegerField SupplierId;
    protected DBStringField ItemId;
    protected DBStringField Name;
    protected DBStringField Description;

    protected DBStringField Type;

    protected DBStringField IncomeAccount;

    protected DBDecimalField UnitPrice;
    protected DBBooleanField Taxable;

    protected DBStringField TaxRateId;

    protected static final String TABLE_NAME = "Item_T";

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    /**
     * Constructor
     */
    public Item() {

        super();

        LoadExceptionString = "ErrorCode=ERROR_INVALID_ITEM&ErrorMsg="
                + "No Item exists in the Item table with the given key";

        SupplierId = new DBIntegerField("SupplierId");
        KeyFieldsList.add(SupplierId);

        ItemId = new DBStringField("ItemId");
        KeyFieldsList.add(ItemId);

        Name = new DBStringField("Name");
        FieldsList.add(Name);

        Description = new DBStringField("Description");
        FieldsList.add(Description);

        Type = new DBStringField("Type");
        FieldsList.add(Type);

        IncomeAccount = new DBStringField("IncomeAccount");
        FieldsList.add(IncomeAccount);

        UnitPrice = new DBDecimalField("UnitPrice");
        FieldsList.add(UnitPrice);

        Taxable = new DBBooleanField("Taxable");
        FieldsList.add(Taxable);

        TaxRateId = new DBStringField("TaxRateId");
        FieldsList.add(TaxRateId);

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
    public String getItemId() {
        return ItemId.getValue();
    }

    public void setItemId(String ItemId) {
        this.ItemId.setValue(ItemId);
    }

    public String getName() {
        return Name.getValue();
    }

    public void setName(String Name) {
        this.Name.setValue(Name);
    }

    public Integer getSupplierId() {
        return SupplierId.getValue();
    }

    public void setSupplierId(Integer SupplierId) {
        this.SupplierId.setValue(SupplierId);
    }

    public String getType() {
        return Type.getValue();
    }

    public void setType(String Type) {
        this.Type.setValue(Type);
    }

    public String getDescription() {
        return Description.getValue();
    }

    public void setDescription(String Description) {
        this.Description.setValue(Description);
    }

    public String getIncomeAccount() {
        return IncomeAccount.getValue();
    }

    public void setIncomeAccount(String IncomeAccount) {
        this.IncomeAccount.setValue(IncomeAccount);
    }

    public String getTaxable() {
        if (Taxable.getValue()) {
            return "Y";
        } else {
            return "N";
        }
    }

    public void setTaxable(Boolean Taxable) {
        if (Taxable != null) {
            this.Taxable.setValue(Taxable);
        }
    }

    public String getTaxRateId() {
        return TaxRateId.getValue();
    }

    public void setTaxRateId(String TaxRateId) {
        this.TaxRateId.setValue(TaxRateId);
    }

    public BigDecimal getUnitPrice() {
        return UnitPrice.getValue();
    }

    public void setUnitPrice(BigDecimal UnitPrice) {
        this.UnitPrice.setValue(UnitPrice);
    }

    public static Item getObject(int supplierId, String itemId)
            throws SQLException {
        Item Obj = new Item();

        Obj.setSupplierId(supplierId);
        Obj.setItemId(itemId);
        Obj.load();

        return (Obj);
    }

}
