package cqb.db;

/**
 *
 * @author hari-work
 */
import cc.db.*;
import java.sql.SQLException;

public class Supplier extends Entity {

    protected DBIntegerField SupplierId;

    protected DBStringField CompanyId;
     protected DBStringField ConnectionId;
    
    protected DBStringField CompanyName;
   

    protected DBStringField CompanyStreet;
    protected DBStringField CompanyCity;
    protected DBStringField CompanyState;
    protected DBStringField CompanyZip;
    protected DBStringField Email;
    protected DBStringField Phone;
    protected DBStringField AuthURL;

    protected static final String TABLE_NAME = "Supplier_T";

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    /**
     * Constructor
     */
    public Supplier() {

        super();

        LoadExceptionString = "ErrorCode=ERROR_INVALID_SUPPLIER&ErrorMsg="
                + "No Supplier exists in Supplier_T with the given key";

        SupplierId = new DBIntegerField("SupplierId");
        KeyFieldsList.add(SupplierId);

        CompanyId = new DBStringField("CompanyId");
        FieldsList.add(CompanyId);
        
        ConnectionId = new DBStringField("ConnectionId");
        FieldsList.add(ConnectionId);

        CompanyName = new DBStringField("CompanyName");
        FieldsList.add(CompanyName);


        CompanyStreet = new DBStringField("CompanyStreet");
        FieldsList.add(CompanyStreet);

        CompanyCity = new DBStringField("CompanyCity");
        FieldsList.add(CompanyCity);

        CompanyState = new DBStringField("CompanyState");
        FieldsList.add(CompanyState);

        CompanyZip = new DBStringField("CompanyZip");
        FieldsList.add(CompanyZip);

        Phone = new DBStringField("PrimaryPhone");
        FieldsList.add(Phone);

        Email = new DBStringField("Email");
        FieldsList.add(Email);
        
        AuthURL = new DBStringField("AuthURL");
        FieldsList.add(AuthURL);

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
    public int getSupplierId() {
        return SupplierId.getValue();
    }

    public void setSupplierId(int SupplierId) {
        this.SupplierId.setValue(SupplierId);
    }

  

    public String getCompanyId() {
        return CompanyId.getValue();
    }

    public void setCompanyId(String CompanyId) {
        this.CompanyId.setValue(CompanyId);
    }
    
    public String getConnectionId() {
        return ConnectionId.getValue();
    }

    public void setConnectionId(String ConnectionId) {
        this.ConnectionId.setValue(ConnectionId);
    }

 

    public String getCompanyName() {
        return CompanyName.getValue();
    }

    public void setCompanyName(String CompanyName) {
        this.CompanyName.setValue(CompanyName);
    }

   

    public String getCompanyStreet() {
        return CompanyStreet.getValue();
    }

    public void setCompanyStreet(String CompanyStreet) {
        this.CompanyStreet.setValue(CompanyStreet);
    }

    public String getCompanyCity() {
        return CompanyCity.getValue();
    }

    public void setCompanyCity(String CompanyCity) {
        this.CompanyCity.setValue(CompanyCity);
    }

    public String getCompanyState() {
        return CompanyState.getValue();
    }

    public void setCompanyState(String CompanyState) {
        this.CompanyState.setValue(CompanyState);
    }

    public String getCompanyZip() {
        return CompanyZip.getValue();
    }

    public void setCompanyZip(String CompanyZip) {
        this.CompanyZip.setValue(CompanyZip);
    }

    public String getPhone() {
        return Phone.getValue();
    }

    public void setPhone(String Phone) {
        this.Phone.setValue(Phone);
    }

    public String getEmail() {
        return Email.getValue();
    }

    public void setEmail(String Email) {
        this.Email.setValue(Email);
    }
    
    public String getAuthURL() {
        return AuthURL.getValue();
    }

    public void setAuthURL(String AuthURL) {
        this.AuthURL.setValue(AuthURL);
    }

    public static Supplier getObject(int supplierId)
            throws SQLException {
        Supplier Obj = new Supplier();

        Obj.setSupplierId(supplierId);
        Obj.load();

        return (Obj);
    }

}
