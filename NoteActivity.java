package com.superjb.mycolloc;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NoteActivity extends AppCompatActivity {
    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;

    /*Intent intentLogin = getIntent();
    final int idColoc = intentLogin.getIntExtra("idColoc",0);
    final String pseudo = intentLogin.getStringExtra("pseudo");*/


    /*Intent intentResponse = getIntent();
    private String pseudo = intentResponse.getStringExtra("pseudo");
    private int idColoc = intentResponse.getIntExtra("idColoc",0);*/

    //final int idColoc = 20;
    //final String pseudo = "plop";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        setTitle("Note");

        Intent intentResponse = getIntent();
        final int idColoc = intentResponse.getIntExtra("idColoc",0);



        lvItems = (ListView) findViewById(R.id.lvItems);
        items = new ArrayList<String>();
        itemsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);


        //Envoie php pour recevoir les notes
        Response.Listener<String> responseListenerReceive = new Response.Listener<String>(){


            @Override

            public void onResponse(String responseReceive) {
                try {

                    JSONObject jsonResponseReceive = new JSONObject(responseReceive);
                    Log.i("ici","1");

                    boolean successReceive = jsonResponseReceive.getBoolean("success");
                    Log.i(String.valueOf(successReceive),"2");
                    if (successReceive)
                    {
                        int lenth = jsonResponseReceive.length()-1;
                        int i=0;
                        while (i<lenth)
                        {
                            String text = jsonResponseReceive.getJSONObject(i+"").getString("texte");
                            itemsAdapter.add(text);
                            i++;
                        }

                    }
                    else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(NoteActivity.this);
                        builder.setMessage("Echec de la reception des notes")
                                .setNegativeButton("Retry",null)
                                .create()
                                .show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    AlertDialog.Builder builder = new AlertDialog.Builder(NoteActivity.this);
                    builder.setMessage("Echec de la reception des notes, verifiez votre connexion internet")
                            .setNegativeButton("Retry",null)
                            .create()
                            .show();
                }
            }
        };

        ReceiveNoteRequest receiveNoteRequest = new ReceiveNoteRequest (idColoc+"", responseListenerReceive );
        RequestQueue queue = Volley.newRequestQueue(NoteActivity.this);
        queue.add(receiveNoteRequest);
        //Fin de l'envoie php


        setupListViewListener();


    }




    private void setupListViewListener() {

        Intent intentResponse = getIntent();
        final int idColoc = intentResponse.getIntExtra("idColoc",0);

        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter,
                                                   final View item, final int pos, long id) {


                        AlertDialog.Builder builder = new AlertDialog.Builder(NoteActivity.this);
                        builder.setMessage("Voulez vous vraiment supprimer cette note ?")
                                .setNegativeButton("Cancel",null)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {


                                        //Envoie php pour delete les notes
                                        Response.Listener<String> responseListenerDelete = new Response.Listener<String>(){


                                            @Override
                                            public void onResponse(String responseDelete) {
                                                try {

                                                    JSONObject jsonResponseDelete = new JSONObject(responseDelete);

                                                    boolean successDelete = jsonResponseDelete.getBoolean("success");
                                                    if (successDelete)
                                                    {
                                                        // Remove the item within array at position
                                                        items.remove(pos);
                                                        // Refresh the adapter
                                                        itemsAdapter.notifyDataSetChanged();
                                                        // Return true consumes the long click event (marks it handled)

                                                    }
                                                    else{
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(NoteActivity.this);
                                                        builder.setMessage("Echec de la suppression de la note")
                                                                .setNegativeButton("Retry",null)
                                                                .create()
                                                                .show();
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(NoteActivity.this);
                                                    builder.setMessage("Echec de la suppression de la note, verifiez votre connexion internet")
                                                            .setNegativeButton("Retry",null)
                                                            .create()
                                                            .show();
                                                }


                                            }
                                        };

//probleme idColoc
                                        DeleteNoteRequest deleteNoteRequest = new DeleteNoteRequest (items.get(pos).toString(), idColoc+"", responseListenerDelete );
                                        RequestQueue queue = Volley.newRequestQueue(NoteActivity.this);
                                        queue.add(deleteNoteRequest);
                                        //Fin de l'envoie php

                                    }
                                })
                                .create()
                                .show();

                        return true;
                    }

                });
    }



    public void onAddItem(View v) {
        final EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        final String itemText = etNewItem.getText().toString();

        Intent intentResponse = getIntent();
        final String pseudo = intentResponse.getStringExtra("pseudo");
        final int idColoc = intentResponse.getIntExtra("idColoc",0);


        //envoie au php
        Response.Listener<String> responseListenerSend = new Response.Listener<String>(){

            @Override
            public void onResponse(String responseSend) {
                try {
                    JSONObject jsonResponse = new JSONObject(responseSend);
                    boolean successSend = jsonResponse.getBoolean("success");

                    if (successSend)
                    {
                        itemsAdapter.add(itemText);
                        etNewItem.setText("");
                    }
                    else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(NoteActivity.this);
                        builder.setMessage("Echec de l'envoie de la note")
                                .setNegativeButton("Retry",null)
                                .create()
                                .show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    AlertDialog.Builder builder = new AlertDialog.Builder(NoteActivity.this);
                    builder.setMessage("Echec de l'envoie de la note, verifiez votre connexion internet")
                            .setNegativeButton("Retry",null)
                            .create()
                            .show();
                }
            }
        };

        SendNoteRequest sendNotesRequest = new SendNoteRequest(idColoc+"", itemText, pseudo, responseListenerSend );
        RequestQueue queue = Volley.newRequestQueue(NoteActivity.this);
        queue.add(sendNotesRequest);
        //fin de l'envoie


    }



}
