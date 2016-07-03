package com.superjb.mycolloc;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle("My Coloc'");

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);



        final EditText etEmail = (EditText) findViewById(R.id.etEmail);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        final Button bLogin = (Button) findViewById(R.id.bLogin);
        final TextView registerLink = (TextView) findViewById(R.id.tvRegisterHere);


        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });



        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = etEmail.getText().toString();
                final String password = etPassword.getText().toString();


                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);

                            boolean success = jsonResponse.getBoolean("success");

                            if(success)
                            {
                                int idUser = jsonResponse.getInt("id_user");
                                int idColoc = jsonResponse.getInt("id_coloc");
                                String pseudo = jsonResponse.getString("user");

                                if (idColoc!=0)
                                {
                                    String nomColoc = jsonResponse.getString("nom_coloc");
                                    Intent intentLogin = new Intent(LoginActivity.this, MenuActivity.class);
                                    intentLogin.putExtra("idUser", idUser);
                                    intentLogin.putExtra("idColoc",idColoc);
                                    intentLogin.putExtra("pseudo",pseudo);
                                    intentLogin.putExtra("nomColoc",nomColoc);

                                    LoginActivity.this.startActivity(intentLogin);

                                }
                                else
                                {
                                    Intent intentLoginForHomeLess = new Intent(LoginActivity.this, HomeLessActivity.class);
                                    intentLoginForHomeLess.putExtra("idUser", idUser);
                                    intentLoginForHomeLess.putExtra("pseudo",pseudo);
                                    LoginActivity.this.startActivity(intentLoginForHomeLess);
                                }
                            }
                            else
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
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

                LoginRequest loginRequest = new LoginRequest(email, password, responseListener );
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);

            }
        });



    }
}
