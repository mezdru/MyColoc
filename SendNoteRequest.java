package com.superjb.mycolloc;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jean-Bryce on 07/06/2016.
 */
public class SendNoteRequest extends StringRequest
{
    private static final String SEND_NOTE_REQUEST_URL = "http://writer-pro.fr/liste_app.php";
    private Map<String, String> params;

    public SendNoteRequest(String id_coloc, String note, String pseudo, Response.Listener<String> listener){
        super(Method.POST, SEND_NOTE_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("id_coloc", id_coloc);
        params.put("texte", note);
        params.put("pseudo",pseudo);
        params.put("etat", "0");
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }


}
