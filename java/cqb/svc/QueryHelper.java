/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cqb.svc;

import cc.util.Log;
import cc.util.Props;

import okhttp3.*;

import java.io.IOException;
import cqb.db.Supplier;
import okhttp3.FormBody;
import org.json.simple.JSONArray;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author hari-work
 */
public class QueryHelper {

    static Log LogObj = null;
    static Props PropsObj = null;

    static {
        try {
            LogObj = Log.getReference("cqb");
            PropsObj = Props.getReference("cqb");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sample QBO API call using OAuth2 tokens
     *
     * @param session
     * @return OkHttpClient client = new OkHttpClient();
     *
     * Request request = new Request.Builder()
     * .url("https://api.codat.io/companies/1ccc96a5-5726-4194-88b7-89a727cf24d9/data/invoices?page=1")
     * .get() .addHeader("Content-Type", "application/json")
     * .addHeader("Authorization", "Basic
     * QWpPbGliSWl0VEE2emlnUzlqOUNtZEpiWFBmeHV6NGFIYkJtN3VDRw==") .build();
     *
     * Response response = client.newCall(request).execute();
     * ////////////////////////////////////////////////////////////////
     * HttpRequest request = HttpRequest.newBuilder()
     * .uri(URI.create("https://api.codat.io/companies/1ccc96a5-5726-4194-88b7-89a727cf24d9/data/invoices?page=1"))
     * .header("Content-Type", "application/json") .header("Authorization",
     * "Basic QWpPbGliSWl0VEE2emlnUzlqOUNtZEpiWFBmeHV6NGFIYkJtN3VDRw==")
     * .method("GET", HttpRequest.BodyPublishers.noBody()) .build();
     * HttpResponse<String> response = HttpClient.newHttpClient().send(request,
     * HttpResponse.BodyHandlers.ofString());
     * System.out.println(response.body());
     *
     */
    static String Header = PropsObj.getProperty("AuthHeader");

    public static JSONObject getData(int supplierId, String DataType) throws Exception {
        Supplier supplierObj = Supplier.getObject(supplierId);
        String companyId = supplierObj.getCompanyId();
        String url = PropsObj.getProperty("CodatAPIHost") + "/companies/" + companyId + "/data/" + DataType
                + "?pageSize=5000";

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", Header)
                .build();

        Response resp = client.newCall(request).execute();

        return (JSONObject) JSONValue.parse(resp.body().string());

    }

    public static JSONObject getConnections(int supplierId) throws Exception {
        Supplier supplierObj = Supplier.getObject(supplierId);
        String companyId = supplierObj.getCompanyId();
        String url = PropsObj.getProperty("CodatAPIHost") + "/companies/" + companyId + "/connections";

        LogObj.loglnT("URL for downloading connections :" + url, Log.PFATAL);

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", Header)
                .build();

        Response resp = client.newCall(request).execute();

        LogObj.loglnT("Response for downloading connections :" + resp, Log.PFATAL);

        return (JSONObject) JSONValue.parse(resp.body().string());

    }

    /*
    GET https://api.codat.io
/companies/{companyId}/push/{pushOperationKey}
     */
    public static JSONObject getPushData(int supplierId, String pushKey) throws Exception {
        Supplier supplierObj = Supplier.getObject(supplierId);
        String companyId = supplierObj.getCompanyId();
        String url = PropsObj.getProperty("CodatAPIHost") + "/companies/" + companyId + "/push/" + pushKey;

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", Header)
                .build();

        Response resp = client.newCall(request).execute();

        return (JSONObject) JSONValue.parse(resp.body().string());

    }

    /*
OkHttpClient client = new OkHttpClient();

Request request = new Request.Builder()
  .url("https://api.codat.io/companies/1ccc96a5-5726-4194-88b7-89a727cf24d9/data/queue/items?connectionId=dc69625f-e7cc-4dfc-b926-0cc6a5297926")
  .post(null)
  .addHeader("Content-Type", "application/json")
  .addHeader("Authorization", "Basic QWpPbGliSWl0VEE2emlnUzlqOUNtZEpiWFBmeHV6NGFIYkJtN3VDRw==")
  .build();

Response response = client.newCall(request).execute();
     */
    public static JSONObject syncData(int supplierId, String DataType) throws Exception {
        Supplier supplierObj = Supplier.getObject(supplierId);
        String companyId = supplierObj.getCompanyId();
        String connectionId = supplierObj.getConnectionId();
        String url = PropsObj.getProperty("CodatAPIHost") + "/companies/" + companyId + "/data/queue/" + DataType
                + "?connectionId=" + connectionId;

        LogObj.loglnT("Queing Pull operation for data  :" + DataType, Log.PFATAL);

        OkHttpClient client = new OkHttpClient();

        RequestBody dummy = new FormBody.Builder().add("param", "dummy").build();

        Request request = new Request.Builder()
                .url(url)
                .post(dummy)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", Header)
                .build();

        Response resp = client.newCall(request).execute();

        return (JSONObject) JSONValue.parse(resp.body().string());

    }

    /*
Request request = new Request.Builder()
  .url("https://api.codat.io/companies/companyId/data/all")
  .post(null)
  .addHeader("Content-Type", "application/json")
  .addHeader("Authorization", "Basic QWpPbGliSWl0VEE2emlnUzlqOUNtZEpiWFBmeHV6NGFIYkJtN3VDRw==")
  .build();

Response response = client.newCall(request).execute();
     */
    public static JSONObject syncAllData(int supplierId) throws Exception {
        Supplier supplierObj = Supplier.getObject(supplierId);
        String companyId = supplierObj.getCompanyId();
        String url = PropsObj.getProperty("CodatAPIHost") + "/companies/" + companyId + "/data/all";

        OkHttpClient client = new OkHttpClient();

        RequestBody dummy = new FormBody.Builder().add("param", "dummy").build();

        Request request = new Request.Builder()
                .url(url)
                .post(dummy)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", Header)
                .build();

        Response resp = client.newCall(request).execute();

        return (JSONObject) JSONValue.parse(resp.body().string());

    }

    /*OkHttpClient client = new OkHttpClient();

MediaType mediaType = MediaType.parse("application/json");
RequestBody body = RequestBody.create(mediaType, invJson);
Request request = new Request.Builder()
  .url("https://api.codat.io/companies/1ccc96a5-5726-4194-88b7-89a727cf24d9/connections/dc69625f-e7cc-4dfc-b926-0cc6a5297926/push/invoices")
  .post(body)
  .addHeader("Content-Type", "application/json")
  .addHeader("Authorization", "Basic QWpPbGliSWl0VEE2emlnUzlqOUNtZEpiWFBmeHV6NGFIYkJtN3VDRw==")
  .build();
   
     https://api.codat.io
/companies/{companyId}/connections/{connectionId}/push/invoices
     */
    public static JSONObject postData(int supplierId, String data, String dataJson) throws Exception {
        Supplier supplierObj = Supplier.getObject(supplierId);
        String companyId = supplierObj.getCompanyId();
        String connectionId = supplierObj.getConnectionId();
        String url = PropsObj.getProperty("CodatAPIHost") + "/companies/" + companyId + "/connections/" + connectionId + "/push/"
                + data;

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, dataJson);

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", Header)
                .build();

        Response resp = client.newCall(request).execute();

        return (JSONObject) JSONValue.parse(resp.body().string());

    }

    public static JSONObject createCompany(String dataJson) throws Exception {

        String url = PropsObj.getProperty("CodatAPIHost") + "/companies";

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, dataJson);

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", Header)
                .build();

        Response resp = client.newCall(request).execute();

        return (JSONObject) JSONValue.parse(resp.body().string());

    }

    /*
    https://api.codat.io/profile/syncSettings
    
    {
  "clientId": "367f7975-267b-439b-90c6-a6040ee680f3",
  "settings": [
    {
      "dataType": "invoices",
      "fetchOnFirstLink": true,
      "syncSchedule": 24,
    
      "syncFromWindow": 24,
      "monthsToSync": 24,
    }
  ],
  "overridesDefaults": true
}
     */
    public static void setSyncSettings(String companyId) throws IOException, Exception {
        String url = PropsObj.getProperty("CodatAPIHost") + "/profile/syncSettings";

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");

        JSONArray settings = new JSONArray();
        settings.add(getSetting("invoices"));
        settings.add(getSetting("customers"));
        settings.add(getSetting("items"));
        settings.add(getSetting("payments"));
        JSONObject dataJson = new JSONObject();
        dataJson.put("clientId", companyId);
        dataJson.put("overridesDefaults", new Boolean(true));
        dataJson.put("settings", settings);

        RequestBody body = RequestBody.create(mediaType, dataJson.toString());

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", Header)
                .build();

        Response resp = client.newCall(request).execute();

        LogObj.loglnT("Response for setSyncSettings :" + resp, Log.PFATAL);

        if (resp.code() > 299) {
            throw new Exception("failed to set SyncSettings, Error:" + resp.message());
        }

        //return (JSONObject) JSONValue.parse(resp.body().string());
    }

    /*{
      "dataType": "invoices",
      "fetchOnFirstLink": true,
      "syncSchedule": 24,
    
      "syncFromWindow": 24,
      "monthsToSync": 24,
    }*/
    private static JSONObject getSetting(String dataType) {
        JSONObject setting = new JSONObject();

        setting.put("dataType", dataType);
        setting.put("fetchOnFirstLink", new Boolean(true));
        setting.put("syncSchedule", new Integer(24));
        setting.put("syncFromWindow", new Integer(24));
        setting.put("monthsToSync", new Integer(24));

        return setting;

    }
}
