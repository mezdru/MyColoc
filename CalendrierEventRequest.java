package com.superjb.mycolloc;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jean-Bryce on 23/06/2016.
 */
public class CalendrierEventRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "http://writer-pro.fr/app_evenement.php";
    private Map<String, String> params;

    public CalendrierEventRequest(String nom, String date_event, String details,String id_coloc, String etat,String id_membre, Response.Listener<String> listener){
        super(Request.Method.POST, REGISTER_REQUEST_URL, listener,null);
        params=new HashMap<>();
        params.put("nom",nom);
        params.put("date_event",date_event);
        params.put("details",details);
        params.put("id_coloc",id_coloc);
        params.put("etat",etat);
        params.put("id_membre",id_membre);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
