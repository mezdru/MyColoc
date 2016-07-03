package com.superjb.mycolloc;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by flo on 07/06/2016.
 */
public class ToursBDDRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "http://writer-pro.fr/app_tours.php";
    private Map<String, String> params;

    public ToursBDDRequest(String id_coloc, String jour_vaisselle, String pseudo,String jour_menage, String etat, Response.Listener<String> listener){
        super(Method.POST, REGISTER_REQUEST_URL, listener,null);
        params=new HashMap<>();
        params.put("id_coloc",id_coloc);
        params.put("jour_vaisselle",jour_vaisselle);
        params.put("pseudo",pseudo);
        params.put("jour_menage",jour_menage);
        params.put("etat",etat);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
