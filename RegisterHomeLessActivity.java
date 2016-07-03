package com.superjb.mycolloc;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterHomeLessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_home_less);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent intent = getIntent();
        final int idUser = intent.getIntExtra("idUser",0);
        final String pseudo = intent.getStringExtra("pseudo");

        final EditText colocName = (EditText) findViewById(R.id.etColocName);
        final EditText password = (EditText) findViewById(R.id.etPassword);
        final EditText repeatPassword = (EditText) findViewById(R.id.etRepeatPassword);
        final Button registerColoc = (Button) findViewById(R.id.bRegister);
        final CheckBox cbPresencePC = (CheckBox) findViewById(R.id.checkBox);

        registerColoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String nomColoc = colocName.getText().toString();
                final String mdp = password.getText().toString();
                final String repeatMdp = repeatPassword.getText().toString();
                final Boolean presencepc = cbPresencePC.isChecked();


                if(!repeatMdp.equals(mdp))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterHomeLessActivity.this);
                    builder.setMessage("Register Failed, you have two different password")
                            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    password.setText("");
                                    repeatPassword.setText("");
                                }
                            })
                            .create()
                            .show();
                }
                else if (nomColoc.length()==0 || mdp.length()==0 ){
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterHomeLessActivity.this);
                    builder.setMessage("Merci de remplir tout les champs disponibles.")
                            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    password.setText("");
                                    repeatPassword.setText("");
                                }
                            })
                            .create()
                            .show();

                }
                else if (mdp.length()<5) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterHomeLessActivity.this);
                    builder.setMessage("Votre mot de passe doit faire au moins 5 caratères")
                            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    password.setText("");
                                    repeatPassword.setText("");
                                }
                            })
                            .create()
                            .show();
                }
                else
                {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");


                                if (success) {

                                    Intent intent = new Intent(RegisterHomeLessActivity.this, MenuActivity.class);
                                    intent.putExtra("idUser", idUser);
                                    int idColoc = jsonResponse.getInt("id_coloc");
                                    intent.putExtra("idColoc",idColoc);
                                    intent.putExtra("pseudo",pseudo);
                                    intent.putExtra("nomColoc",nomColoc);
                                    RegisterHomeLessActivity.this.startActivity(intent);
                                    finish();





                                } else {

                                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterHomeLessActivity.this);
                                    builder.setMessage("Erreur: le nom de votre coloc est déjà utilisé")
                                            .setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    password.setText("");
                                                    repeatPassword.setText("");
                                                    colocName.setText("");
                                                }
                                            })
                                            .create()
                                            .show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    RegisterHomeLessRequest registerHomeLessRequest = new RegisterHomeLessRequest(nomColoc, mdp, idUser, presencepc, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(RegisterHomeLessActivity.this);
                    queue.add(registerHomeLessRequest);

                }


            }
        });
    }
}