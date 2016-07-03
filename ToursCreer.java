package com.superjb.mycolloc;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class ToursCreer extends AppCompatActivity {

    String id_coloc="2";
    String etat="1";
    String date_event;
    String jour_vaisselle="";
    String jour_menage="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tours_creer);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            date_event = extras.getString("leJour");
            System.out.println(date_event);
            if (date_event.equals("lun.")){
                date_event="lundi";
            }
            if (date_event.equals("mar.")){
                date_event="mardi";
            }
            if (date_event.equals("mer.")){
                date_event="mercredi";
            }
            if (date_event.equals("jeu.")){
                date_event="jeudi";
            }
            if (date_event.equals("ven.")){
                date_event="vendredi";
            }
            if (date_event.equals("sam.")){
                date_event="samedi";
            }
            if (date_event.equals("dim.")){
                date_event="dimanche";
            }
        }


        TextView textViewDate = (TextView) findViewById(R.id.textViewDate);
        textViewDate.setText(date_event);
        setTitle("Créer votre evenement");
        Button bregister = (Button) findViewById(R.id.bregister);
        bregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CheckBox checkBoxM = (CheckBox) findViewById(R.id.checkM);
                if (checkBoxM.isChecked()==true) {
                    jour_menage=date_event;

                }
                else {
                    jour_menage="rien";
                }

                final CheckBox checkBoxV = (CheckBox) findViewById(R.id.checkV);
                if (checkBoxV.isChecked()==true) {
                    jour_vaisselle=date_event;
                }
                else {
                    jour_vaisselle="rien";
                }
                Log.i("myColoc", "test register !");
                EditText etcolocataire = (EditText) findViewById(R.id.etcolocataire);
                String pseudo = etcolocataire.getText().toString();
                System.out.println(jour_vaisselle);
                System.out.println(jour_menage);
                Log.i("myColoc", pseudo);
                Log.i("myColoc", id_coloc);
                Log.i("myColoc", etat);
                if (pseudo.length() > 0) {
                    if (checkBoxM.isChecked()==true||checkBoxV.isChecked()==true) {
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");
                                    System.out.println(success);
                                    if (success) {
                                        Intent intent = new Intent(ToursCreer.this, ToursActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(ToursCreer.this);
                                        builder.setMessage("l'évènement n'a pas pu être creer")
                                                .setNegativeButton("retry", null)
                                                .create()
                                                .show();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();

                                }

                            }
                        };


                        ToursBDDRequest eventRequest = new ToursBDDRequest(id_coloc, jour_vaisselle, pseudo, jour_menage, etat, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(ToursCreer.this);
                        queue.add(eventRequest);

                    } else {
                        Context context = getApplicationContext();
                        CharSequence text = "cochez au moins une case";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                }
                else {
                    Context context = getApplicationContext();
                    CharSequence text = "rentrez un pseudo";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }

            }
        });
    }
}
