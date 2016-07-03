package com.superjb.mycolloc;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jean-Bryce on 15/06/2016.
 */
public class SettingRequest extends StringRequest {

    private static final String REQUEST_URL = "http://writer-pro.fr/app_setting.php";
    private Map<String, String> params;

    public SettingRequest(int id_user, int etat, int id_coloc, Response.Listener<String> listener){
        super(Request.Method.POST, REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("id_user", id_user+"");
        params.put("id_coloc",id_coloc+"");
        params.put("etat", etat+"");
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }


}
