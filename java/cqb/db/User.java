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

public class User extends Entity {

    protected DBStringField UserId;
    protected DBStringField Password;
    protected DBIntegerField SupplierId;
    protected DBStringField UserType;
    protected DBIntegerField BuyerId;

    protected static final String TABLE_NAME = "User_T";

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    /**
     * Constructor
     */
    public User() {

        super();

        LoadExceptionString = "ErrorCode=ERROR_INVALID_USER&ErrorMsg="
                + "No User exists in the User table with the given key";

        UserId = new DBStringField("UserId");
        KeyFieldsList.add(UserId);

        Password = new DBStringField("Password");
        FieldsList.add(Password);

        SupplierId = new DBIntegerField("SupplierId");
        FieldsList.add(SupplierId);

        UserType = new DBStringField("UserType");
        FieldsList.add(UserType);

        BuyerId = new DBIntegerField("BuyerId");
        FieldsList.add(BuyerId);

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
    public String getUserId() {
        return UserId.getValue();
    }

    public void setUserId(String UserId) {
        this.UserId.setValue(UserId);
    }

    public String getUserType() {
        return UserType.getValue();
    }

    public void setUserType(String UserType) {
        this.UserType.setValue(UserType);
    }

    public String getPassword() {
        return Password.getValue();
    }

    public void setPassword(String Password) {
        this.Password.setValue(Password);
    }

    public Integer getSupplierId() {
        return SupplierId.getValue();
    }

    public void setSupplierId(Integer SupplierId) {
        this.SupplierId.setValue(SupplierId);
    }

    public Integer getBuyerId() {
        return BuyerId.getValue();
    }

    public void setBuyerId(Integer BuyerId) {
        this.BuyerId.setValue(BuyerId);
    }

    public static User getObject(String userId)
            throws SQLException {
        User Obj = new User();

        Obj.setUserId(userId);
        Obj.load();

        return (Obj);
    }

}
