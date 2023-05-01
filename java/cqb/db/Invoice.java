package cqb.db;

/**
 *
 * @author hari-work
 */
import cc.db.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;

public class Invoice extends Entity {

    protected DBIntegerField SupplierId;
    protected DBStringField InvoiceId;
    protected DBStringField InvoiceNumber;
    protected DBStringField BuyerId;
    protected DBStringField BuyerName;

    protected DBStringField TxnDate;
    protected DBStringField PaidDate;

    protected DBStringField TaxExempt;
    protected DBStringField PrivateNote;

    protected DBStringField DueDate;

    protected DBDecimalField AmtDue;
    protected DBDecimalField TotalAmt;

    protected DBDecimalField Tax;

    protected DBDecimalField TaxRate;

    protected DBDecimalField SubTotal;

    protected DBDecimalField DiscountAmount;

    protected DBStringField PushKey;
    protected DBStringField Status;

    protected static final String TABLE_NAME = "Invoice_T";

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    /**
     * Constructor
     */
    public Invoice() {

        super();

        LoadExceptionString = "ErrorCode=ERROR_INVALID_INVOICE&ErrorMsg="
                + "No Invoice exists in Invoice_T with the given key";

        SupplierId = new DBIntegerField("SupplierId");
        KeyFieldsList.add(SupplierId);

        InvoiceId = new DBStringField("InvoiceId");
        KeyFieldsList.add(InvoiceId);

        InvoiceNumber = new DBStringField("InvoiceNumber");
        FieldsList.add(InvoiceNumber);

        BuyerId = new DBStringField("BuyerId");
        FieldsList.add(BuyerId);

        BuyerName = new DBStringField("BuyerName");
        FieldsList.add(BuyerName);

        Status = new DBStringField("Status");
        FieldsList.add(Status);

        TxnDate = new DBStringField("TxnDate");
        FieldsList.add(TxnDate);

        PaidDate = new DBStringField("PaidDate");
        FieldsList.add(PaidDate);

        PrivateNote = new DBStringField("PrivateNote");
        FieldsList.add(PrivateNote);

        DueDate = new DBStringField("DueDate");
        FieldsList.add(DueDate);

        AmtDue = new DBDecimalField("AmtDue");
        FieldsList.add(AmtDue);

        TotalAmt = new DBDecimalField("TotalAmt");
        FieldsList.add(TotalAmt);

        Tax = new DBDecimalField("Tax");
        FieldsList.add(Tax);

        TaxRate = new DBDecimalField("TaxRate");
        FieldsList.add(TaxRate);

        SubTotal = new DBDecimalField("SubTotal");
        FieldsList.add(SubTotal);

        TaxExempt = new DBStringField("TaxExempt");
        FieldsList.add(TaxExempt);

        DiscountAmount = new DBDecimalField("DiscountAmount");
        FieldsList.add(DiscountAmount);

        PushKey = new DBStringField("PushKey");
        FieldsList.add(PushKey);

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

    public int getSupplierId() {
        return SupplierId.getValue();
    }

    public void setSupplierId(int SupplierId) {
        this.SupplierId.setValue(SupplierId);
    }

    public String getInvoiceNumber() {
        return InvoiceNumber.getValue();
    }

    public void setInvoiceNumber(String InvoiceNumber) {
        this.InvoiceNumber.setValue(InvoiceNumber);
    }

    public String getBuyerId() {
        return BuyerId.getValue();
    }

    public void setBuyerId(String BuyerId) {
        this.BuyerId.setValue(BuyerId);
    }

    public String getBuyerName() {
        return BuyerName.getValue();
    }

    public void setBuyerName(String BuyerName) {
        this.BuyerName.setValue(BuyerName);
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

    public String getTxnDate() {
        return TxnDate.getValue();
    }

    public void setTxnDate(String TxnDate) {
        this.TxnDate.setValue(TxnDate);
    }

    public String getPaidDate() {
        return PaidDate.getValue();
    }

    public void setPaidDate(String PaidDate) {
        this.PaidDate.setValue(PaidDate);
    }

    public String getPrivateNote() {
        return PrivateNote.getValue();
    }

    public void setPrivateNote(String PrivateNote) {
        this.PrivateNote.setValue(PrivateNote);
    }

    public String getTaxExempt() {
        return TaxExempt.getValue();
    }

    public void setTaxExempt(String TaxExempt) {
        this.TaxExempt.setValue(TaxExempt);
    }

    public String getDueDate() {
        return DueDate.getValue();
    }

    public void setDueDate(String DueDate) {
        this.DueDate.setValue(DueDate);
    }

    public BigDecimal getAmountRcvd() {
        return TotalAmt.getValue().subtract(AmtDue.getValue()).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getSubTotal() {
        return SubTotal.getValue();
    }

    public void setSubTotal(BigDecimal SubTotal) {
        this.SubTotal.setValue(SubTotal);
    }

    public BigDecimal getDiscountAmount() {
        return DiscountAmount.getValue();
    }

    public void setDiscountAmount(BigDecimal DiscountAmount) {
        this.DiscountAmount.setValue(DiscountAmount);
    }

    public BigDecimal getTax() {
        return Tax.getValue();
    }

    public void setTax(BigDecimal Tax) {
        this.Tax.setValue(Tax);
    }

    public BigDecimal getTaxRate() {
        return TaxRate.getValue();
    }

    public void setTaxRate(BigDecimal TaxRate) {
        this.TaxRate.setValue(TaxRate);
    }

    public BigDecimal getTotalAmt() {
        return TotalAmt.getValue();
    }

    public void setTotalAmt(BigDecimal TotalAmt) {
        this.TotalAmt.setValue(TotalAmt);
    }

    public BigDecimal getAmtDue() {
        return AmtDue.getValue();
    }

    public void setAmtDue(BigDecimal AmtDue) {
        this.AmtDue.setValue(AmtDue);
    }

    public static Invoice getObject(int supplierId, String invoiceId)
            throws SQLException {
        Invoice Obj = new Invoice();

        Obj.setSupplierId(supplierId);
        Obj.setInvoiceId(invoiceId);
        Obj.load();

        return (Obj);
    }

}
