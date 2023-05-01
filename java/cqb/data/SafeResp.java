/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cqb.data;

import org.json.simple.JSONObject;
import java.math.BigDecimal;

/**
 * Copyright Sterling Payment Technologies, LLC
 *
 * @author harivellanki
 */
public class SafeResp {

    JSONObject RespObj;

    public SafeResp(JSONObject RespObj) {
        this.RespObj = RespObj;
    }

    public String get(String Element) {
        String Response = "";
        Object Obj = RespObj.get(Element);
        if (Obj != null) {
            Response = "" + Obj;
        }
        return Response;
    }

    public Object getOrDefault(String Element, String Default) {
        return RespObj.getOrDefault(Element, Default);
    }

    public Object getObject(String Element) {
        return RespObj.get(Element);
    }

    public Object getDouble(String Element) {
        Double Response = 0.00;
        Object Obj = RespObj.get(Element);
        if (Obj != null) {
            Response = (Double) Obj;
        }
        return Response;
    }

    public BigDecimal getDecimal(String Element) {

        BigDecimal amt = null;
        try {
            String amtStr = "" +  RespObj.get(Element);
            if (amtStr != null) {
                amt = new BigDecimal(amtStr);
            }
        } catch (Exception e) {
              amt = new BigDecimal(0.00);
        }
        return amt;
    }
}
