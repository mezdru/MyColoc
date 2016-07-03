package com.superjb.mycolloc;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.Image;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class EtatChangerSoldeActivity extends AppCompatActivity {

    private float finalArgentMis;
    private float finalCommun;
    private int etat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etat_changer_solde);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent intentResponse = getIntent();
        final int idColoc = intentResponse.getIntExtra("idColoc",0);
        final int idUser = intentResponse.getIntExtra("idUser",0);
        final String pseudo = intentResponse.getStringExtra("pseudo");
        final float soldeCommun = intentResponse.getFloatExtra("soldeCommun",0);
        final float monSolde = intentResponse.getFloatExtra("monSolde",0);
        final int idAvatar = intentResponse.getIntExtra("idAvatar",R.drawable.avatar1);
        final float monArgentMis = intentResponse.getFloatExtra("monArgentMis",0);
        final boolean potCommun = intentResponse.getBooleanExtra("potCommun", false);

        setTitle("Changer Mon Solde : "+pseudo);


        TextView tvPseudo = (TextView) findViewById(R.id.tvPseudo);
        TextView tvMonSolde = (TextView) findViewById(R.id.tvSolde);
        TextView tvPotCommun = (TextView) findViewById(R.id.tvPotCommun);
        ImageView ivAvatar = (ImageView) findViewById(R.id.ivAvatar);
        final EditText etDeposer = (EditText) findViewById(R.id.etDeposer);
        final EditText etRetirer = (EditText) findViewById(R.id.etRetirer);
        final EditText etDepenseCommune = (EditText) findViewById(R.id.etDepenseCommune);
        Button bEffectuer = (Button) findViewById(R.id.bEffectuer);




        tvPseudo.setText(pseudo);
        tvMonSolde.setText("Mon Solde : "+monSolde+" €");
        ivAvatar.setImageResource(idAvatar);
        if(potCommun)
        {
            tvPotCommun.setText("Pot Commun : "+soldeCommun+" €");
        }else{
            tvPotCommun.setText("Pas de Pot Commun");
            etRetirer.setVisibility(View.INVISIBLE);
            etDeposer.setVisibility(View.INVISIBLE);
        }


        bEffectuer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((etDeposer.length()!=0) ^ (etRetirer.length()!=0) ^(etDepenseCommune.length()!=0)) )
                {
                    boolean check = true;
                    etat = 2;
                    String msg;
                    if (etDeposer.length()!=0)
                    {
                        finalArgentMis=Float.parseFloat(etDeposer.getText().toString())+monArgentMis;
                        finalCommun= Float.parseFloat(etDeposer.getText().toString())+soldeCommun;
                        Float trueSolde = monSolde+Float.parseFloat(etDeposer.getText().toString());
                        msg = "Si vous acceptez la transaction, votre solde sera de "+trueSolde+"€. Et le Pot Commun disposera de "+finalCommun+" €.";
                    }
                    else if (etRetirer.length()!=0)
                    {
                        finalArgentMis=monArgentMis - Float.parseFloat(etRetirer.getText().toString());
                        finalCommun=soldeCommun-Float.parseFloat(etRetirer.getText().toString());
                        Log.i(monArgentMis+"",Float.parseFloat(etRetirer.getText().toString())+"");
                        Float trueSolde = monSolde-Float.parseFloat(etRetirer.getText().toString());
                        msg = "Si vous acceptez la transaction, votre solde sera de "+trueSolde+"€. Et le Pot Commun disposera de "+finalCommun+" €.";
                        if (trueSolde<0 || soldeCommun-Float.parseFloat(etRetirer.getText().toString())<0)
                        {
                            check=false;
                        }
                    }else{
                        finalArgentMis=Float.parseFloat(etDepenseCommune.getText().toString());
                        if (potCommun){
                            etat=3;
                            msg = "Attention, en effectuant un dépense commune avec le pot commun, vous baissez le solde de tous les colocs";
                            if(soldeCommun<=Float.parseFloat(etDepenseCommune.getText().toString()))
                            {
                                check=false;
                            }
                        }else{
                            etat=4;
                            msg = "Attention, vous avancez de l'argent à la coloc pour une dépense commune. Vous augmentez donc votre solde d'un peu moins de "+finalArgentMis+"€. Et vous baissez le solde des autres.";
                        }
                    }


                    if (check)
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(EtatChangerSoldeActivity.this);
                        builder.setMessage(msg)
                                .setNegativeButton("Annuler",null)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {

                                        //envoie php
                                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    JSONObject jsonResponse = new JSONObject(response);

                                                    boolean success = jsonResponse.getBoolean("success");

                                                    if (success) {

                                                        EtatActivity.getInstance().finish();

                                                        Intent myIntent = new Intent(EtatChangerSoldeActivity.this, EtatActivity.class);
                                                        myIntent.putExtra("idUser", idUser);
                                                        myIntent.putExtra("idColoc", idColoc);
                                                        myIntent.putExtra("pseudo", pseudo);
                                                        EtatChangerSoldeActivity.this.startActivity(myIntent);
                                                        finish();



                                                    } else {
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(EtatChangerSoldeActivity.this);
                                                        builder.setMessage("Erreur #42")
                                                                .setNegativeButton("réessayer", null)
                                                                .create()
                                                                .show();
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        };

                                        Log.i("final argent : ", finalArgentMis + "");
                                        Log.i("idUser ", idUser + "");
                                        EtatChangerSoldeRequest etatChangerSoldeRequest = new EtatChangerSoldeRequest(idUser, idColoc, finalArgentMis, etat, responseListener);
                                        RequestQueue queue = Volley.newRequestQueue(EtatChangerSoldeActivity.this);
                                        queue.add(etatChangerSoldeRequest);

                                        //fin envoie php

                                    }
                                })
                                .create()
                                .show();
                    }
                    else
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(EtatChangerSoldeActivity.this);
                        builder.setMessage("Les Soldes du pot commun ne permettent pas cette transaction.")
                                .setNegativeButton("OK", null)
                                .create()
                                .show();
                    }
                }
                else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(EtatChangerSoldeActivity.this);
                    builder.setMessage("Erreur. Merci de remplir qu'un seul champ.")
                            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    etDepenseCommune.setText("");
                                    etDeposer.setText("");
                                    etRetirer.setText("");
                                }
                            })
                            .create()
                            .show();
                }
            }
        });



    }
}
