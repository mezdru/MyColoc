package com.superjb.mycolloc;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jean-Bryce on 07/06/2016.
 */
public class DeleteNoteRequest extends StringRequest
{
    private static final String DELETE_NOTE_REQUEST_URL = "http://writer-pro.fr/liste_app.php";
    private Map<String, String> params;

    public DeleteNoteRequest(String texte, String id_coloc, Response.Listener<String> listener){

        super(Method.POST, DELETE_NOTE_REQUEST_URL, listener, null);

        params = new HashMap<>();
        params.put("texte", texte);
        params.put("id_coloc",id_coloc);
        params.put("etat", "3");
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

}

