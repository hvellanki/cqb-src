/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cqb.svc;

import cc.util.Log;
import cc.util.Props;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import java.math.*;

import java.io.*;
import java.util.*;
import javax.servlet.http.*;

//import com.intuit.ipp.data.*;
//import com.intuit.ipp.services.*;
/**
 *
 * @author hari-work
 */
public class SvcUtil {

    static Log LogObj = null;
    static Props PropsObj = null;

    static {
        try {
            LogObj = Log.getReference("ms");
            PropsObj = Props.getReference("ms");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static byte[] calcHmacSha256(byte[] secretKey, byte[] message) {
        byte[] hmacSha256 = null;
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey, "HmacSHA256");
            mac.init(secretKeySpec);
            hmacSha256 = mac.doFinal(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to calculate hmac-sha256", e);
        }
        return hmacSha256;
    }

    public static String getCalculatedHash(String secretKey, String msg) {
        byte[] hmacSha256 = calcHmacSha256(secretKey.getBytes(), msg.getBytes());

        String base64HmacSha256 = Base64.getEncoder().encodeToString(hmacSha256);
       
        return base64HmacSha256;
    }

    public static int getDaysPast(long PastEpochDate) {
        long CurrentEpochDate = Calendar.getInstance().getTimeInMillis();
        long diffInMillis = CurrentEpochDate - PastEpochDate;
        int DaysPast = (int) (diffInMillis / (1000 * 60 * 60 * 24));
        return DaysPast;

    }

    public static String createMemo(String From, String Msg) {
        return "(" + getCurrentDateTime() + " EDT) " + " :" + Msg;
    }

    public static String getCurrentDateTime() {
        Date myDate = new Date();

        SimpleDateFormat mdyFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        mdyFormat.setTimeZone(TimeZone.getTimeZone("America/New_York"));
        return mdyFormat.format(myDate);
    }

    public static String getCurrentDate() {
        Date myDate = new Date();

        SimpleDateFormat mdyFormat = new SimpleDateFormat("MM/dd/yyyy");
        return mdyFormat.format(myDate);
    }
    
    // Converts MM/DD/YYYY to YYYY-MM-DD
    public static String getISODate(String DateStr){
        int indexM = DateStr.indexOf("/");
        String MM = DateStr.substring(0, indexM);
        if (MM.length() == 1){
            MM = "0" + MM;
        }
        
        int indexD = DateStr.indexOf("/", indexM+1);
        String DD = DateStr.substring(indexM+1, indexD);
        if (DD.length() == 1){
            DD = "0" + DD;
        }
        
        String YYYY = DateStr.substring(indexD+1);
        
        return YYYY + "-" + MM + "-" + DD;
        
    }
    
    public static String getISODateTime(String DateStr){
        
        return getISODate(DateStr)+ "T00:00:00Z";
        
    }

    public static String getBody(HttpServletRequest request) throws IOException {

        StringBuilder stringBuilder = new StringBuilder();

        try (BufferedReader bufferedReader = request.getReader();) {

            char[] charBuffer = new char[128];
            int bytesRead = -1;
            while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                stringBuilder.append(charBuffer, 0, bytesRead);
            }

        } catch (IOException ex) {
            throw ex;
        }
        return stringBuilder.toString();
    }
    
    public static BigDecimal getSafeAmount(Double Amount){
        if(Amount == null){
            return new BigDecimal(0.0);
        }else{
            return new BigDecimal(Amount);
        }
    }

    /*
    public static void printJSON(QueryResult queryResult) {
        if (!queryResult.getEntities().isEmpty() && queryResult.getEntities().size() > 0) {

            List objsList = queryResult.getEntities();
            LogObj.loglnT("Number of objects returned " + objsList.size(), Log.PFATAL);

            String jsonInString = "";
            for (int i = 0; i < objsList.size(); i++) {
                ObjectMapper mapper = new ObjectMapper();
                try {
                    jsonInString += mapper.writeValueAsString(objsList.get(i));

                } catch (Exception e) {
                    LogObj.loglnT("Exception while getting json " + e, Log.PFATAL);

                }

            }

            LogObj.loglnT("JSON returned :" + jsonInString, Log.PFATAL);

        }

    }
     */
    // return in M.DD.HHMM format
    /*   public static String getDocNumberT() {
        Calendar calendarObj = Calendar.getInstance();

        String CurrentMonth = "" + (calendarObj.get(Calendar.MONTH) + 1);

        String CurrentDay = "" + calendarObj.get(Calendar.DAY_OF_MONTH);

        String CurrentHour
                = (Integer.valueOf(calendarObj.get(Calendar.HOUR_OF_DAY))).toString();
        if (CurrentHour.length() == 1) {
            CurrentHour = "0" + CurrentHour;
        }

        String CurrentMinute = Integer.valueOf(calendarObj.get(Calendar.MINUTE)).toString();
        if (CurrentMinute.length() == 1) {
            CurrentMinute = "0" + CurrentMinute;
        }
        return CurrentMonth + "." + CurrentDay + "." + CurrentHour + CurrentMinute;

    }*/
    // Format is date: /Date(1636761600000+0000)/
    public static Date getJavaDate(String dateStr) {
        if (dateStr == null) {
            return null;
        }
        int startTimestamp = 6;
        int end = dateStr.indexOf('+');
        String epochTime = dateStr.substring(startTimestamp, end);
        return new Date(Long.parseLong(epochTime));
    }
/*    
    public static String getXeroDate(String DateStr){
        Date jDate = Date.
    } 
*/    
}
