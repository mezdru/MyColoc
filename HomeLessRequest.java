package com.superjb.mycolloc;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jean-Bryce on 07/06/2016.
 */
public class HomeLessRequest extends StringRequest {

    private static final String HOME_LESS_REQUEST_URL = "http://writer-pro.fr/colocation_app.php";
    private Map<String, String> params;

    public HomeLessRequest(String nomColoc, String mdp, int idUser, Response.Listener<String> listener){
        super(Method.POST, HOME_LESS_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("nom", nomColoc);
        params.put("mdp", mdp);
        params.put("id_user",idUser+"");
        params.put("etat","1");
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
