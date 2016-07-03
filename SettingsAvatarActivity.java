package com.superjb.mycolloc;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class SettingsAvatarActivity extends AppCompatActivity {

    private int idUser;
    private String pseudo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_avatar);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent intentResponse = getIntent();
        idUser = intentResponse.getIntExtra("idUser",0);
        pseudo = intentResponse.getStringExtra("pseudo");
        final int avatar = intentResponse.getIntExtra("avatar",R.drawable.avatar1);
        final String email = intentResponse.getStringExtra("email");



        TextView tvPseudo = (TextView) findViewById(R.id.tvPseudo);
        TextView tvEmail = (TextView) findViewById(R.id.tvEmail);
        ImageView ivAvatar = (ImageView) findViewById(R.id.ivAvatar);

        ImageButton ibAvatar1 = (ImageButton) findViewById(R.id.ibAvatar1);
        ImageButton ibAvatar2 = (ImageButton) findViewById(R.id.ibAvatar2);
        ImageButton ibAvatar3 = (ImageButton) findViewById(R.id.ibAvatar3);
        ImageButton ibAvatar4 = (ImageButton) findViewById(R.id.ibAvatar4);
        ImageButton ibAvatar5 = (ImageButton) findViewById(R.id.ibAvatar5);
        ImageButton ibAvatar6 = (ImageButton) findViewById(R.id.ibAvatar6);

        setTitle("Changer mon avatar");

        ivAvatar.setImageResource(avatar);
        tvPseudo.setText(pseudo);
        tvEmail.setText(email);


        ibAvatar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avatarClick("avatar1");
            }
        });

        ibAvatar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avatarClick("avatar2");
            }
        });

        ibAvatar3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avatarClick("avatar3");
            }
        });

        ibAvatar4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avatarClick("avatar4");
            }
        });

        ibAvatar5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avatarClick("avatar5");
            }
        });

        ibAvatar6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avatarClick("avatar6");
            }
        });

    }


    public void avatarClick (final String numberAvatar)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsAvatarActivity.this);
        builder.setMessage("Voulez vous vraiment changer d'avatar avec celui-ci ?")
                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
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
                                        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsAvatarActivity.this);
                                        builder.setMessage("Echec, pas de chance ?")
                                                .setNegativeButton("Retry",null)
                                                .create()
                                                .show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    AlertDialog.Builder builder = new AlertDialog.Builder(SettingsAvatarActivity.this);
                                    builder.setMessage("Echec du chargement, verifiez votre connexion internet")
                                            .setNegativeButton("Retry",null)
                                            .create()
                                            .show();
                                }
                            }
                        };
                        // attention !! le fichierphp est a changer !!!
                        SettingsPseudEmailRequest settingsPseudEmailRequest = new SettingsPseudEmailRequest (idUser, 4, numberAvatar, "tu ne l'auras pas ! Haha", responseListenerReceive );
                        RequestQueue queue = Volley.newRequestQueue(SettingsAvatarActivity.this);
                        queue.add(settingsPseudEmailRequest);



                    }
                })
                .setNegativeButton("Non",null)
                .create()
                .show();




    }
}
