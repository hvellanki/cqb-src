/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cqb.svc;

import cqb.db.*;

import java.sql.SQLException;

/**
 *
 * @author hari-work
 */
public class createJsonRep {
    
    public static void createJsonRep(int supplierId, String eName, String eId, String repVal) throws SQLException{
        
        JsonRep repObj = new JsonRep();
        
        repObj.setSupplierId(supplierId);
        repObj.setEId(eId);
        repObj.setEName(eName);
        repObj.setJsonValue(repVal);
        
        repObj.create();
        
    }
    
}
