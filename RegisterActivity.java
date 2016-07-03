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
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    private boolean check;
    private String msg;
    private boolean checkConstellation=false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        final EditText etPseudo = (EditText) findViewById(R.id.etPseudo);
        final EditText etEmail = (EditText) findViewById(R.id.etEmail);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        final EditText etRepeatPassword = (EditText) findViewById(R.id.etRepeatPassword);
        final EditText etDdn = (EditText) findViewById(R.id.etDdn);
        final EditText etURIConstellation = (EditText) findViewById(R.id.etURIConstellation);
        final EditText etClefAdministrateur = (EditText) findViewById(R.id.etClefAdministrateur);
        final Button bRegister = (Button) findViewById(R.id.bRegister);


        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String pseudo = etPseudo.getText().toString();
                final String email = etEmail.getText().toString();
                final String mdp = etPassword.getText().toString();
                final String repeatMdp = etRepeatPassword.getText().toString();
                final String ddn = etDdn.getText().toString();
                final String URIConstellation = etURIConstellation.getText().toString();
                final String ClefAdministrateur = etClefAdministrateur.getText().toString();


                check=false;
                if (!repeatMdp.equals(mdp))
                {
                    msg="Erreur : les mots de passes sont différents";
                }
                else if (pseudo.length()==0 || email.length()==0)
                {
                    msg="Merci de remplir tout les champs disponibles.";
                }
                else if (!goodBirthday(ddn))
                {
                    msg="Le format de votre date de naissance n'est pas conforme.";
                }
                else if (mdp.length()<5)
                {
                    msg="Votre Mot de passe doit contenir au moins 5 caractères";
                }
                else
                {
                    check =true;
                    msg = "Erreur #41";
                }

                if (etClefAdministrateur.length()!=0 && etURIConstellation.length()!=0)
                {
                    checkConstellation=true;
                }

                //fin du machin

                if (check)
                {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.i("test","-1");
                                JSONObject jsonResponse = new JSONObject(response);

                                Log.i("test","0");
                                boolean success = jsonResponse.getBoolean("success");
                                Log.i("test","0.5");

                                if (success) {
                                    int idUser = jsonResponse.getInt("id_user");
                                    if(checkConstellation)
                                    {
                                        Log.i("test","1");
                                        int idColoc = jsonResponse.getInt("id_coloc");
                                        Log.i("test","2");
                                        String nomColoc = jsonResponse.getString("nom_coloc");
                                        Log.i("test","3");
                                        Intent myintent = new Intent(RegisterActivity.this, MenuActivity.class);
                                        myintent.putExtra("idUser",idUser);
                                        myintent.putExtra("pseudo",pseudo);
                                        myintent.putExtra("idColoc",idColoc);
                                        myintent.putExtra("nomColoc",nomColoc);
                                        startActivity(myintent);
                                        finish();


                                    }else{
                                        Intent intent = new Intent(RegisterActivity.this, HomeLessActivity.class);
                                        intent.putExtra("idUser",idUser);
                                        intent.putExtra("pseudo",pseudo);
                                        RegisterActivity.this.startActivity(intent);
                                        finish();
                                    }

                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                    builder.setMessage("Cette adresse email est déjà utilisé")
                                            .setNegativeButton("Retry", null)
                                            .create()
                                            .show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    RegisterRequest registerRequest = new RegisterRequest(pseudo, email, ddn, mdp, URIConstellation, ClefAdministrateur, checkConstellation, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                    queue.add(registerRequest);

                }
                else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setMessage(msg)
                            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    etPassword.setText("");
                                    etRepeatPassword.setText("");
                                    if(!goodBirthday(ddn))
                                    {
                                        etDdn.setText("");
                                    }
                                }
                            })
                            .create()
                            .show();
                }


            }
        });


    }
    private boolean goodBirthday(String ddn)
    {
        boolean resultat = false;

        try
        {
            int year = Integer.parseInt(ddn.substring(0,4));
            int month = Integer.parseInt(ddn.substring(5,7));
            int day = Integer.parseInt(ddn.substring(8));


            if(ddn.length()==10 && ddn.charAt(4)== '-' && ddn.charAt(7)== '-' &&
                    year>1850 && year<2500 && month<=12 && month>=0 && day<=31 && day>=0)
            {
                resultat=true;
            }

        }
        catch (Exception e)
        {
            resultat=false;
        }

        return resultat;
    }



}



