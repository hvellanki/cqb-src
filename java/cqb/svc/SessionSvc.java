/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cqb.svc;

import javax.servlet.http.*;

/**
 *
 * @author hari-work
 */
public class SessionSvc {

    public static boolean checkSession(HttpSession session) {
        if (session.getAttribute("UserId") == null || session.getAttribute("SupplierId") == null
                || session.getAttribute("UserType").equals("Buyer") ) {
            return false;
            //throw new IllegalStateException("Session expired or invalid, please login to continue");
        }
        return true;
    }
    
    public static boolean checkCustomerSession(HttpSession session) {
        if (session.getAttribute("UserId") != null && session.getAttribute("SupplierId") != null
                && session.getAttribute("UserType").equals("Buyer") ) {
            return true;
        } else {
            return false;
            //throw new IllegalStateException("Session expired or invalid, please login to continue");
        }
        
    }

}
