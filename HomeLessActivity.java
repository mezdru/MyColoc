package com.superjb.mycolloc;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class HomeLessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_less);

        final Intent intentLoginForHomeLess = getIntent();
        final int idUser = intentLoginForHomeLess.getIntExtra("idUser",0);
        final String pseudo = intentLoginForHomeLess.getStringExtra("pseudo");


        final EditText colocName = (EditText) findViewById(R.id.etPseudo);
        final EditText password = (EditText) findViewById(R.id.etPassword);
        final Button login = (Button) findViewById(R.id.bLogin);
        final TextView registerLink = (TextView) findViewById(R.id.tvRegister);

        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentHomeLess = new Intent(HomeLessActivity.this, RegisterHomeLessActivity.class);
                intentHomeLess.putExtra("idUser", idUser);
                intentHomeLess.putExtra("pseudo",pseudo);
                HomeLessActivity.this.startActivity(intentHomeLess);
                finish();
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String nomColoc = colocName.getText().toString();
                final String mdp = password.getText().toString();


                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);

                            boolean success = jsonResponse.getBoolean("success");

                            if(success)
                            {
                                int idColoc = jsonResponse.getInt("id_coloc");
                                String nomColoc = jsonResponse.getString("nom_colo");


                                Intent intentLogin = new Intent(HomeLessActivity.this, MenuActivity.class);
                                intentLogin.putExtra("idUser", idUser);
                                intentLogin.putExtra("idColoc",idColoc);
                                intentLogin.putExtra("pseudo",pseudo);
                                intentLogin.putExtra("nomColoc",nomColoc);

                                HomeLessActivity.this.startActivity(intentLogin);
                                finish();


                            }
                            else
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(HomeLessActivity.this);
                                builder.setMessage("Login Failed")
                                        .setNegativeButton("Retry",null)
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                HomeLessRequest homeLessRequest = new HomeLessRequest(nomColoc, mdp, idUser, responseListener );
                RequestQueue queue = Volley.newRequestQueue(HomeLessActivity.this);
                queue.add(homeLessRequest);

            }
        });



    }
}
