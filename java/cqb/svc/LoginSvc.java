/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cqb.svc;

import cqb.db.Supplier;
import cc.util.*;
import cqb.db.User;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.http.*;

/**
 *
 * @author hari-work
 */
public class LoginSvc {

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

    public static boolean doLogin(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        String userId = request.getParameter("UserId");
        String password = request.getParameter("Password");
        HttpSession session = request.getSession();
        try {

            if (userId != null) {
                User userObj = User.getObject(userId);
                if (password == null || !password.equals(userObj.getPassword())) {
                    LogObj.loglnT("Password mismatch for User :" + userId, Log.PFATAL);
                    return false;
                } else {
                    Supplier supplier = Supplier.getObject(userObj.getSupplierId());                   
                    
                    session.setAttribute("UserId", userId);
                    session.setAttribute("SupplierId", userObj.getSupplierId());
                    session.setAttribute("SupplierName", supplier.getCompanyName());
                    session.setAttribute("CompanyId", supplier.getCompanyId());
                    session.setAttribute("ConnectionId", supplier.getConnectionId());
                    
                    if(supplier.getConnectionId().length() ==0){
                        session.setAttribute("AuthURL", supplier.getAuthURL());
                    }
                    
                    if(userObj.getUserType().equalsIgnoreCase("Buyer")){
                        session.setAttribute("UserType", "Buyer");
                        session.setAttribute("BuyerId", userObj.getBuyerId());
                    }else{
                       session.setAttribute("UserType", "Supplier"); 
                    }
                    return true;
                }
            } else {
                return false;
            }
        } catch (CCDBException | SQLException ex) {
            LogObj.log(ex, Log.PFATAL);
            LogObj.loglnT("SQLException or No User with this UserId:" + userId, Log.PFATAL);
            throw ex;
        }
    }
}
