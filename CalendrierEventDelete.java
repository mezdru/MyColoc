package com.superjb.mycolloc;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jean-Bryce on 23/06/2016.
 */
public class CalendrierEventDelete extends StringRequest {
    private static final String DELETE_REQUEST_URL = "http://writer-pro.fr/app_evenement.php";
    private Map<String, String> params;

    public CalendrierEventDelete(String id_coloc, String etat,String id, Response.Listener<String> listener){
        super(Request.Method.POST, DELETE_REQUEST_URL, listener,null);
        params=new HashMap<>();
        params.put("id_coloc",id_coloc);
        params.put("etat",etat);
        params.put("id",id);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
