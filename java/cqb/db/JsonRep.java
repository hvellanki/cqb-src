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

public class JsonRep extends Entity {

    protected DBStringField EId;
    protected DBStringField EName;
    protected DBIntegerField SupplierId;
    protected DBStringField JsonValue;

    protected static final String TABLE_NAME = "JsonRep_T";

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    /**
     * Constructor
     */
    public JsonRep() {

        super();

        LoadExceptionString = "ErrorCode=ERROR_INVALID_JSONREP&ErrorMsg="
                + "No JsonRep exists in the JsonRep table with the given key";

        SupplierId = new DBIntegerField("SupplierId");
        KeyFieldsList.add(SupplierId);

        EName = new DBStringField("EntityName");
        KeyFieldsList.add(EName);

        EId = new DBStringField("EntityId");
        KeyFieldsList.add(EId);

        JsonValue = new DBStringField("JsonValue");
        FieldsList.add(JsonValue);

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
    public String getEId() {
        return EId.getValue();
    }

    public void setEId(String EId) {
        this.EId.setValue(EId);
    }

    public String getEName() {
        return EName.getValue();
    }

    public void setEName(String EName) {
        this.EName.setValue(EName);
    }

    public String getJsonValue() {
        return JsonValue.getValue();
    }

    public void setJsonValue(String JsonValue) {
        this.JsonValue.setValue(JsonValue);
    }

    public Integer getSupplierId() {
        return SupplierId.getValue();
    }

    public void setSupplierId(Integer SupplierId) {
        this.SupplierId.setValue(SupplierId);
    }

    public static JsonRep getObject(int SupplierId, String EName, String EId)
            throws SQLException {
        JsonRep Obj = new JsonRep();

        Obj.setSupplierId(SupplierId);
        Obj.setEName(EName);
        Obj.setEId(EId);
        Obj.load();

        return (Obj);
    }

}
