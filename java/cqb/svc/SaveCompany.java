/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cqb.svc;

import cc.util.Log;
import cc.util.*;
import cqb.db.*;

import java.util.*;

import java.io.IOException;

import javax.servlet.http.*;

import org.json.simple.*;

/**
 *
 * @author hari-work
 */
public class SaveCompany {
    
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

    /*
    Company:
{
    
   "name":"XXXXXXXXXX",
    
    "description":"XXXXXXXXXX",
   
}
     */
    @SuppressWarnings({"unchecked", "unchecked", "unchecked"})
    public static Supplier saveCompany(HttpServletRequest req, HttpSession session) throws Exception {
        
        JSONObject companyJson = new JSONObject();        
        companyJson.put("name", req.getParameter("CompanyName"));
        
        companyJson.put("description", req.getParameter("Description"));
        
        String Password = req.getParameter("Password");
        
        String UserId = req.getParameter("UserId");
        
        User userObj = new User();
        
        List users = userObj.loadList("Where UserId='" + UserId + "'");
        if (users.size() > 0) {
            throw new CCDataException("The userId already exists - enter a different UserId");
        }
        
        LogObj.logln("JSON of Company being posted :" + companyJson, Log.PHIGH);
        
        JSONObject PushResp = QueryHelper.createCompany(companyJson.toString());
        
        LogObj.logln("Push Response received :" + PushResp, Log.PHIGH);
        
        Supplier supplierObj = createSupplier.create(UserId, Password, PushResp);
        
        QueryHelper.setSyncSettings(supplierObj.getCompanyId());
        
        return supplierObj;
    }
    
}
