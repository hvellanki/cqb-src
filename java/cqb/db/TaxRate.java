package cqb.db;

/**
 *
 * @author hari-work
 */
import cc.db.*;
import java.math.BigDecimal;
import java.sql.SQLException;

public class TaxRate extends Entity {

    protected DBIntegerField SupplierId;
    protected DBStringField TaxRateId;
    protected DBStringField TaxRateName;
    protected DBDecimalField TaxRateValue;

    protected DBStringField UpdatedDate;

    protected static final String TABLE_NAME = "TaxRate_T";

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    /**
     * Constructor
     */
    public TaxRate() {

        super();

        LoadExceptionString = "ErrorCode=ERROR_INVALID_TAXRATE&ErrorMsg="
                + "No TaxRate exists in the TaxRate table with the given key";

        SupplierId = new DBIntegerField("SupplierId");
        KeyFieldsList.add(SupplierId);

        TaxRateName = new DBStringField("TaxRateName");
        KeyFieldsList.add(TaxRateName);

        TaxRateId = new DBStringField("TaxRateId");
        FieldsList.add(TaxRateId);

        TaxRateValue = new DBDecimalField("TaxRateValue");
        FieldsList.add(TaxRateValue);

        UpdatedDate = new DBStringField("UpdatedDateTime");
        FieldsList.add(UpdatedDate);

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
    public String getUpdatedDate() {
        return UpdatedDate.getValue();
    }

    public void setUpdatedDate(String UpdatedDate) {
        this.UpdatedDate.setValue(UpdatedDate);
    }

    public BigDecimal getTaxRateValue() {
        return TaxRateValue.getValue();
    }

    public void setTaxRateValue(BigDecimal TaxRateValue) {
        this.TaxRateValue.setValue(TaxRateValue);
    }

    public Integer getSupplierId() {
        return SupplierId.getValue();
    }

    public void setSupplierId(Integer SupplierId) {
        this.SupplierId.setValue(SupplierId);
    }

    public String getTaxRateId() {
        return TaxRateId.getValue();
    }

    public void setTaxRateId(String TaxRateId) {
        this.TaxRateId.setValue(TaxRateId);
    }

    public String getTaxRateName() {
        return TaxRateName.getValue();
    }

    public void setTaxRateName(String TaxRateName) {
        this.TaxRateName.setValue(TaxRateName);
    }

    public static TaxRate getObject(int supplierId, String name)
            throws SQLException {
        TaxRate Obj = new TaxRate();

        Obj.setSupplierId(supplierId);
        Obj.setTaxRateName(name);
        Obj.load();

        return (Obj);
    }

}
