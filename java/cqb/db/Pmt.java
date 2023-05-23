package cqb.db;

/**
 *
 * @author hari-work
 */
import cc.db.*;
import java.sql.SQLException;
import java.math.*;

public class Pmt extends Entity {

    protected DBIntegerField SupplierId;
    protected DBStringField PmtId;
    protected DBStringField PmtRefNum;

    protected DBStringField LinkedTxId;
    protected DBStringField LinkedTxType;
    protected DBStringField AcctId;
    protected DBStringField AcctName;
    protected DBStringField BuyerId;
    protected DBStringField BuyerName;
    protected DBStringField PmtDate;
    protected DBStringField UpdatedDate;

    protected DBDecimalField TotalAmount;

    protected DBStringField PushKey;

    protected DBStringField Status;

    protected static final String TABLE_NAME = "Payment_T";

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    /**
     * Constructor
     */
    public Pmt() {

        super();

        LoadExceptionString = "ErrorCode=ERROR_INVALID_INVOICELINEITEM&ErrorMsg="
                + "No Pmt exists in the Payment table with the given key";

        SupplierId = new DBIntegerField("SupplierId");
        KeyFieldsList.add(SupplierId);

        PmtId = new DBStringField("PmtId");
        KeyFieldsList.add(PmtId);

        PmtRefNum = new DBStringField("PmtRefNum");
        FieldsList.add(PmtRefNum);

        LinkedTxId = new DBStringField("LinkedTxId");
        FieldsList.add(LinkedTxId);

        LinkedTxType = new DBStringField("LinkedTxType");
        FieldsList.add(LinkedTxType);

        AcctId = new DBStringField("AcctId");
        FieldsList.add(AcctId);

        AcctName = new DBStringField("AcctName");
        FieldsList.add(AcctName);

        BuyerId = new DBStringField("BuyerId");
        FieldsList.add(BuyerId);

        BuyerName = new DBStringField("BuyerName");
        FieldsList.add(BuyerName);

        PmtDate = new DBStringField("PmtDate");
        FieldsList.add(PmtDate);

        UpdatedDate = new DBStringField("UpdatedDate");
        FieldsList.add(UpdatedDate);

        TotalAmount = new DBDecimalField("TotalAmount");
        FieldsList.add(TotalAmount);

        PushKey = new DBStringField("PushKey");
        FieldsList.add(PushKey);

        Status = new DBStringField("Status");
        FieldsList.add(Status);

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
    public String getPmtId() {
        return PmtId.getValue();
    }

    public void setPmtId(String PmtId) {
        this.PmtId.setValue(PmtId);
    }

    public String getLinkedTxType() {
        return LinkedTxType.getValue();
    }

    public void setLinkedTxType(String LinkedTxType) {
        this.LinkedTxType.setValue(LinkedTxType);
    }

    public String getLinkedTxId() {
        return LinkedTxId.getValue();
    }

    public void setLinkedTxId(String LinkedTxId) {
        this.LinkedTxId.setValue(LinkedTxId);
    }

    public String getBuyerId() {
        return BuyerId.getValue();
    }

    public void setBuyerId(String BuyerId) {
        this.BuyerId.setValue(BuyerId);
    }

    public String getAcctId() {
        return AcctId.getValue();
    }

    public void setAcctId(String AcctId) {
        this.AcctId.setValue(AcctId);
    }

    public String getStatus() {
        return Status.getValue();
    }

    public void setStatus(String Status) {
        this.Status.setValue(Status);
    }

    public String getPushKey() {
        return PushKey.getValue();
    }

    public void setPushKey(String PushKey) {
        this.PushKey.setValue(PushKey);
    }

    public String getPmtRefNum() {
        return PmtRefNum.getValue();
    }

    public void setPmtRefNum(String PmtRefNum) {
        this.PmtRefNum.setValue(PmtRefNum);
    }

    public Integer getSupplierId() {
        return SupplierId.getValue();
    }

    public void setSupplierId(Integer SupplierId) {
        this.SupplierId.setValue(SupplierId);
    }

    public BigDecimal getTotalAmount() {
        return TotalAmount.getValue();
    }

    public void setTotalAmount(BigDecimal Amount) {
        this.TotalAmount.setValue(Amount);
    }

    public String getUpdatedDate() {
        return UpdatedDate.getValue();
    }

    public void setUpdatedDate(String UpdatedDate) {
        this.UpdatedDate.setValue(UpdatedDate);
    }

    public String getPmtDate() {
        return PmtDate.getValue();
    }

    public void setPmtDate(String PmtDate) {
        this.PmtDate.setValue(PmtDate);
    }

    public String getBuyerName() {
        return BuyerName.getValue();
    }

    public void setBuyerName(String BuyerName) {
        this.BuyerName.setValue(BuyerName);
    }

    public String getAcctName() {
        return AcctName.getValue();
    }

    public void setAcctName(String AcctName) {
        this.AcctName.setValue(AcctName);
    }

    public static Pmt getObject(int supplierId, String PmtId)
            throws SQLException {
        Pmt Obj = new Pmt();

        Obj.setSupplierId(supplierId);
        Obj.setPmtId(PmtId);
        Obj.load();

        return (Obj);
    }

}
