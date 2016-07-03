package com.superjb.mycolloc;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jean-Bryce on 07/06/2016.
 */
public class ReceiveNoteRequest extends StringRequest {


    private static final String RECEIVE_NOTE_REQUEST_URL = "http://writer-pro.fr/liste_app.php";
    private Map<String, String> params;

    public ReceiveNoteRequest(String id_coloc, Response.Listener<String> listener){

        super(Method.POST, RECEIVE_NOTE_REQUEST_URL, listener, null);

        params = new HashMap<>();
        params.put("id_coloc", id_coloc);
        params.put("etat", "2");
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

}
