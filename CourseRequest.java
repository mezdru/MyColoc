package com.superjb.mycolloc;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jean-Bryce on 21/06/2016.
 */
public class CourseRequest extends StringRequest {
    private static final String COURSE_REQUEST_URL = "http://writer-pro.fr/app_course.php";
    private Map<String, String> params;

    public CourseRequest(int etat, int idColoc, String texte, String pseudo, Response.Listener<String> listener){
        super(Method.POST, COURSE_REQUEST_URL, listener, null);

        params = new HashMap<>();
        params.put("etat", etat+"");
        params.put("id_coloc",idColoc+"");
        params.put("texte", texte);
        params.put("pseudo",pseudo);

    }
    @Override
    public Map<String, String> getParams() {
        return params;
    }

}
