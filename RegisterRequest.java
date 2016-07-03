package com.superjb.mycolloc;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jean-Bryce on 31/05/2016.
 */
public class RegisterRequest extends StringRequest {

    private static final String REGISTER_REQUEST_URL = "http://writer-pro.fr/app_inscription.php";
    private Map<String, String> params;

    public RegisterRequest(String pseudo, String email, String ddn, String mdp, String URIConstellation, String ClefAdministrateur, boolean checkConstellation, Response.Listener<String> listener){
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("pseudo", pseudo);
        params.put("email", email);
        params.put("ddn", ddn);
        params.put("mdp",mdp);
        if (checkConstellation)
        {
            params.put("URIConstellation", URIConstellation);
            params.put("ClefAdministrateur",ClefAdministrateur);
            params.put("etat","2");
        }
        else
        {
            params.put("etat","1");
        }
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}