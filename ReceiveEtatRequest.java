package com.superjb.mycolloc;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jean-Bryce on 13/06/2016.
 */
public class ReceiveEtatRequest extends StringRequest{



    private static final String RECEIVE_ETAT_REQUEST_URL = "http://writer-pro.fr/app_etat.php";
    private Map<String, String> params;

    public ReceiveEtatRequest(String id_coloc, Response.Listener<String> listener){

        super(Request.Method.POST, RECEIVE_ETAT_REQUEST_URL, listener, null);

        params = new HashMap<>();
        params.put("id_coloc", id_coloc);
        params.put("etat","1");
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

}

