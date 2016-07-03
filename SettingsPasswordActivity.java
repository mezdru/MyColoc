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
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class SettingsPasswordActivity extends AppCompatActivity {

    private int idUserColoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_password);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent intentResponse = getIntent();
        final int idUser = intentResponse.getIntExtra("idUser",0);
        final String pseudo = intentResponse.getStringExtra("pseudo");
        final int avatar = intentResponse.getIntExtra("avatar",R.drawable.avatar1);
        final String email = intentResponse.getStringExtra("email");
        final int etat = intentResponse.getIntExtra("etat",5);


        TextView tvPseudo = (TextView) findViewById(R.id.tvPseudo);
        TextView tvEmail = (TextView) findViewById(R.id.tvEmail);
        ImageView ivAvatar = (ImageView) findViewById(R.id.ivAvatar);
        final EditText etAncienMdp = (EditText) findViewById(R.id.etAncienMdp);
        final EditText etNewMdp = (EditText) findViewById(R.id.etNewMdp);
        final EditText etRepeatNewMdp = (EditText) findViewById(R.id.etRepeatNewMdp);
        final TextView tvNomColoc = (TextView) findViewById(R.id.tvnomColoc);
        Button bChangerMdp = (Button) findViewById(R.id.bChangerMdp);




        ivAvatar.setImageResource(avatar);
        tvPseudo.setText(pseudo);
        tvEmail.setText(email);

        if (etat==5)
        {
            setTitle("Mon mot de passe");
            tvNomColoc.setVisibility(View.INVISIBLE);
            idUserColoc = idUser;
        }
        else if (etat==6)
        {
            setTitle("Mot de passe de la Coloc");
            tvNomColoc.setText(intentResponse.getStringExtra("nomColoc"));
            etAncienMdp.setHint("Mot de Passe de la Coloc' actuel");
            etNewMdp.setHint("Nouveau Mot de Passe de Coloc'");
            bChangerMdp.setText("Changer le Mot de Passe de la Coloc'");
            idUserColoc = intentResponse.getIntExtra("idColoc",0);

        }



        bChangerMdp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (etAncienMdp.length()==0 || etNewMdp.length()==0 || etRepeatNewMdp.length()==0 )
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SettingsPasswordActivity.this);
                    builder.setMessage("Echec de Chargement, Veuillez remplir les trois champs disponible")
                            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    etAncienMdp.setText("");
                                    etNewMdp.setText("");
                                    etRepeatNewMdp.setText("");
                                }
                            })
                            .create()
                            .show();
                }
                else if (!etNewMdp.getText().toString().equals(etRepeatNewMdp.getText().toString()))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SettingsPasswordActivity.this);
                    builder.setMessage("Echec de Chargement, la répétition de votre mot de passe est incorrect")
                            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    etAncienMdp.setText("");
                                    etNewMdp.setText("");
                                    etRepeatNewMdp.setText("");
                                }
                            })
                            .create()
                            .show();
                }
                else
                {

                    AlertDialog.Builder builder = new AlertDialog.Builder(SettingsPasswordActivity.this);
                    builder.setMessage("Etes vous sûr de vouloir changer votre mot de passe ?")
                            .setNegativeButton("non",null)
                            .setPositiveButton("oui", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    //Envoie php
                                    Response.Listener<String> responseListenerReceive = new Response.Listener<String>(){


                                        @Override

                                        public void onResponse(String responseReceive) {
                                            try {

                                                JSONObject jsonResponseReceive = new JSONObject(responseReceive);

                                                boolean successReceive = jsonResponseReceive.getBoolean("success");
                                                if (successReceive)
                                                {
                                                    SettingActivity.getInstance().finish();
                                                    finish();
                                                }
                                                else{
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(SettingsPasswordActivity.this);
                                                    builder.setMessage("Votre ancien mot de passe est incorrect")
                                                            .setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    etAncienMdp.setText("");
                                                                    etNewMdp.setText("");
                                                                    etRepeatNewMdp.setText("");
                                                                }
                                                            })
                                                            .create()
                                                            .show();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsPasswordActivity.this);
                                                builder.setMessage("Echec du chargement, verifiez votre connexion internet")
                                                        .setNegativeButton("Retry",null)
                                                        .create()
                                                        .show();
                                            }
                                        }
                                    };
                                    Log.i(idUserColoc+"", etat+"");
                                    Log.i(etNewMdp.getText().toString(),etAncienMdp.getText().toString());
                                    SettingsPseudEmailRequest settingsPseudEmailRequest = new SettingsPseudEmailRequest (idUserColoc, etat, etNewMdp.getText().toString(), etAncienMdp.getText().toString(), responseListenerReceive );
                                    RequestQueue queue = Volley.newRequestQueue(SettingsPasswordActivity.this);
                                    queue.add(settingsPseudEmailRequest);


                                }
                            })
                            .create()
                            .show();

                }
            }
        });

    }
}
