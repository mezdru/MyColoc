package com.superjb.mycolloc;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jean-Bryce on 31/05/2016.
 */
public class RegisterHomeLessRequest extends StringRequest {

    private static final String REGISTER_HOME_LESS_REQUEST_URL = "http://writer-pro.fr/colocation_app.php";
    private Map<String, String> params;

    public RegisterHomeLessRequest(String nomColoc, String mdp, int idUser, boolean presencePC, Response.Listener<String> listener){
        super(Method.POST, REGISTER_HOME_LESS_REQUEST_URL, listener, null);
        params = new HashMap<>();
        if(presencePC)
        {
            params.put("pot_commun", "oui");
        }else{
            params.put("pot_commun","non");
        }
        params.put("nom", nomColoc);
        params.put("mdp", mdp);
        params.put("id_user",idUser+"");
        params.put("etat","0");
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}