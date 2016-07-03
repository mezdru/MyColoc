package com.superjb.mycolloc;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class CalendrierCreerEvenementActivity extends AppCompatActivity {

    private String id_coloc;
    String etat="1";
    private String id_membre;
    String date_event;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendrier_creer_evenement);

        Intent intentResponse = getIntent();
        id_coloc = intentResponse.getStringExtra("idColoc");
        id_membre = intentResponse.getStringExtra("idUser");




        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            date_event = extras.getString("laDate");
        }
        TextView textViewDate = (TextView) findViewById(R.id.textViewDate);
        textViewDate.setText(date_event);
        setTitle("Créer votre evenement");
        Button bregister = (Button) findViewById(R.id.bregister);
        bregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("myColoc", "test register !");
                EditText ettitre = (EditText) findViewById(R.id.ettitre);
                EditText etdetails = (EditText) findViewById(R.id.etdetails);
                String nom = ettitre.getText().toString();
                String details = etdetails.getText().toString();
                Log.i("myColoc", nom);
                Log.i("myColoc", date_event);
                Log.i("myColoc", details);
                Log.i("myColoc", id_coloc);
                Log.i("myColoc", etat);
                Log.i("myColoc", id_membre);
                if (nom.length() > 0) {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if (success) {
                                    Intent intent = new Intent(CalendrierCreerEvenementActivity.this, CalendrierActivity.class);
                                    intent.putExtra("idColoc",Integer.parseInt(id_coloc));
                                    intent.putExtra("idUser",Integer.parseInt(id_membre));
                                    startActivity(intent);
                                    finish();
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(CalendrierCreerEvenementActivity.this);
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


                    CalendrierEventRequest eventRequest = new CalendrierEventRequest(nom, date_event, details, id_coloc, etat,id_membre, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(CalendrierCreerEvenementActivity.this);
                    queue.add(eventRequest);

                }
                else {
                    Context context = getApplicationContext();
                    CharSequence text = "rentrez un titre";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }

            }
        });
    }


}

