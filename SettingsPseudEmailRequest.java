package com.superjb.mycolloc;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jean-Bryce on 16/06/2016.
 */
public class SettingsPseudEmailRequest extends StringRequest {

    private static final String REQUEST_URL = "http://writer-pro.fr/app_setting.php";
    private Map<String, String> params;

    public SettingsPseudEmailRequest (int id_user, int etat, String newPseudoEmail, String motDePasse, Response.Listener<String> listener){
        super(Request.Method.POST, REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("id_user", id_user+"");
        params.put("etat", etat+"");
        params.put("new_pseudo_email",newPseudoEmail);
        params.put("mdp",motDePasse);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }


}
