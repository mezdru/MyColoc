package com.superjb.mycolloc;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jean-Bryce on 07/06/2016.
 */
public class EtatChangerSoldeRequest extends StringRequest
{
    private static final String REQUEST_URL = "http://writer-pro.fr/app_etat.php";
    private Map<String, String> params;

    public EtatChangerSoldeRequest(int idUser , int idColoc, float finalArgentMis, int etat, Response.Listener<String> listener){

        super(Method.POST, REQUEST_URL, listener, null);
        params = new HashMap<>();


        params.put("etat", etat+"");
        params.put("id_coloc",idColoc+"");


        params.put("id_user",idUser+"");

        params.put("final_argent_mise",finalArgentMis+"");
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

}

