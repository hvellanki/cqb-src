package cqb.db;

/**
 *
 * @author hari-work
 */
import cc.db.*;
import java.math.BigDecimal;
import java.sql.SQLException;

public class Account extends Entity {

    protected DBIntegerField SupplierId;
    protected DBStringField AccountId;
    protected DBStringField Name;
    protected DBStringField Type;
    protected DBDecimalField Balance;

    protected DBStringField UpdatedDate;

    protected static final String TABLE_NAME = "Account_T";

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    /**
     * Constructor
     */
    public Account() {

        super();

        LoadExceptionString = "ErrorCode=ERROR_INVALID_ACCOUNT&ErrorMsg="
                + "No Account exists in the Account table with the given key";

        SupplierId = new DBIntegerField("SupplierId");
        KeyFieldsList.add(SupplierId);

        AccountId = new DBStringField("AccountId");
        KeyFieldsList.add(AccountId);

        Name = new DBStringField("AccountName");
        FieldsList.add(Name);

        Type = new DBStringField("AccountType");
        FieldsList.add(Type);

        Balance = new DBDecimalField("Balance");
        FieldsList.add(Balance);

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

    public BigDecimal getBalance() {
        return Balance.getValue();
    }

    public void setBalance(BigDecimal Balance) {
        this.Balance.setValue(Balance);
    }

    public String getAccountName() {
        return Name.getValue();
    }

    public void setAccountName(String Name) {
        this.Name.setValue(Name);
    }

    public Integer getSupplierId() {
        return SupplierId.getValue();
    }

    public void setSupplierId(Integer SupplierId) {
        this.SupplierId.setValue(SupplierId);
    }

    public String getAccountType() {
        return Type.getValue();
    }

    public void setAccountType(String Type) {
        this.Type.setValue(Type);
    }

    public String getAccountId() {
        return AccountId.getValue();
    }

    public void setAccountId(String AccountId) {
        this.AccountId.setValue(AccountId);
    }

    public static Account getObject(int supplierId, String accountId)
            throws SQLException {
        Account Obj = new Account();

        Obj.setSupplierId(supplierId);
        Obj.setAccountId(accountId);
        Obj.load();

        return (Obj);
    }

}
