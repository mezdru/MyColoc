package com.superjb.mycolloc;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
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
import org.w3c.dom.Text;

public class SettingsPseudEmailActivity extends AppCompatActivity {

    private String msg = "Erreur : Mot de passe incorrect";
    private String msg2 = "Etes vous sûr de vouloir changer votre pseudo en : ";
    private int idUserColoc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_pseud_email);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        final TextView tvPseudo = (TextView) findViewById(R.id.tvPseudo);
        TextView tvEmail = (TextView) findViewById(R.id.tvEmail);
        ImageView ivAvatar = (ImageView) findViewById(R.id.ivAvatar);
        TextView tvTitreEmailPseudo = (TextView) findViewById(R.id.titrePseudoEmail);

        final EditText etNewPseudoEmail = (EditText) findViewById(R.id.etEmailPseudo);
        final EditText etPassword = (EditText) findViewById(R.id.etMotDePasse);

        Button bChanger = (Button) findViewById(R.id.bChanger);

        Intent intentResponse = getIntent();
        final int idUser = intentResponse.getIntExtra("idUser",0);
        final String pseudo = intentResponse.getStringExtra("pseudo");
        final int avatar = intentResponse.getIntExtra("avatar",R.drawable.avatar1);
        final String email = intentResponse.getStringExtra("email");
        final int etat = intentResponse.getIntExtra("etat",0);
        final int idColoc = intentResponse.getIntExtra("idColoc",0);
        final String nomColoc = intentResponse.getStringExtra("nomColoc");


        tvPseudo.setText(pseudo);
        tvEmail.setText(email);
        ivAvatar.setImageResource(avatar);


        if (etat==3 || etat==2 || etat==7)
        {
            if (etat==3)
            {
                tvTitreEmailPseudo.setText("Votre pseudo actuel : "+pseudo);
               etNewPseudoEmail.setHint("Votre nouveau pseudo");
                bChanger.setText("Changer mon pseudo");
                setTitle("Changer mon pseudo");
                idUserColoc=idUser;
            }
            else if (etat==7)
            {
                tvTitreEmailPseudo.setText(nomColoc);
                etNewPseudoEmail.setHint("Nouveau nom de Coloc'");
                bChanger.setText("Changer le nom de ma Coloc'");
                etPassword.setHint("Mot de Passe de Coloc'");
                setTitle("Changer le nom de ma Coloc'");
                msg = "Erreur : Mot de passe incorrect ou nom de Coloc' déjà utilisé";
                msg2 = "Etes vous sûr de vouloir changer votre nom de Coloc' en : ";
                idUserColoc=idColoc;
            }
            else
            {
                tvTitreEmailPseudo.setText("Votre email actuel : "+email);
                etNewPseudoEmail.setHint("Votre nouvel email");
                etNewPseudoEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                bChanger.setText("Changer mon email");
                setTitle("Changer mon Email");
                msg = "Erreur : Mot de passe incorrect ou email déjà utilisé";
                msg2 = "Etes vous sûr de vouloir changer votre email en : ";
                idUserColoc=idUser;
            }


            bChanger.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    final String pseudoEmail = etNewPseudoEmail.getText().toString();

                    if (pseudoEmail.length()!=0)
                    {

                        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsPseudEmailActivity.this);
                        builder.setMessage(msg2+pseudoEmail)
                                .setNegativeButton("non",null)
                                .setPositiveButton("oui", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {

                                        //Envoie php pour recevoir les notes
                                        Response.Listener<String> responseListenerReceive = new Response.Listener<String>(){


                                            @Override

                                            public void onResponse(String responseReceive) {
                                                try {

                                                    JSONObject jsonResponseReceive = new JSONObject(responseReceive);

                                                    boolean successReceive = jsonResponseReceive.getBoolean("success");
                                                    if (successReceive)
                                                    {
                                                        MenuActivity.getInstance().finish();
                                                        SettingActivity.getInstance().finish();

                                                        Intent myIntent = new Intent(SettingsPseudEmailActivity.this, MenuActivity.class);
                                                        myIntent.putExtra("idColoc",idColoc);
                                                        myIntent.putExtra("idUser",idUser);
                                                        if (etat==3)
                                                        {
                                                            myIntent.putExtra("pseudo",pseudoEmail);
                                                            myIntent.putExtra("nomColoc",nomColoc);
                                                        }else if (etat==7)
                                                        {
                                                            myIntent.putExtra("pseudo",pseudo);
                                                            myIntent.putExtra("nomColoc",pseudoEmail);
                                                        }
                                                        else
                                                        {
                                                            myIntent.putExtra("pseudo",pseudo);
                                                            myIntent.putExtra("nomColoc",nomColoc);
                                                        }
                                                        startActivity(myIntent);
                                                        finish();
                                                    }
                                                    else{
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsPseudEmailActivity.this);
                                                        builder.setMessage(msg)
                                                                .setNegativeButton("OK",null)
                                                                .create()
                                                                .show();
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(SettingsPseudEmailActivity.this);
                                                    builder.setMessage("Echec du chargement, verifiez votre connexion internet")
                                                            .setNegativeButton("Retry",null)
                                                            .create()
                                                            .show();
                                                }
                                            }
                                        };
                                        Log.i(pseudoEmail,"etat"+etat);
                                        SettingsPseudEmailRequest settingsPseudEmailRequest = new SettingsPseudEmailRequest (idUserColoc, etat, pseudoEmail, etPassword.getText().toString(), responseListenerReceive );
                                        RequestQueue queue = Volley.newRequestQueue(SettingsPseudEmailActivity.this);
                                        queue.add(settingsPseudEmailRequest);

                                    }
                                })
                                .create()
                                .show();
                    }
                    else
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsPseudEmailActivity.this);
                        builder.setMessage("Merci de remplir tout les champs disponibles")
                                .setNegativeButton("OK",null)
                                .create()
                                .show();
                    }
                }

            });


        }
        else
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(SettingsPseudEmailActivity.this);
            builder.setMessage("Echec du chargement de la page (pseudo ou email ou nomColoc) ?")
                    .setNegativeButton("Pas cool...", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .create()
                    .show();
        }
    }
}
