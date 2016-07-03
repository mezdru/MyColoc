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

public class CourseActivity extends AppCompatActivity {

    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        setTitle("Course");

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
                        AlertDialog.Builder builder = new AlertDialog.Builder(CourseActivity.this);
                        builder.setMessage("Echec de la reception des courses")
                                .setNegativeButton("Retry",null)
                                .create()
                                .show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    AlertDialog.Builder builder = new AlertDialog.Builder(CourseActivity.this);
                    builder.setMessage("Echec de la reception des courses, verifiez votre connexion internet")
                            .setNegativeButton("Retry",null)
                            .create()
                            .show();
                }
            }
        };

        //to modifie
        CourseRequest courseRequest0 = new CourseRequest (0, idColoc, "null", "null", responseListenerReceive );
        RequestQueue queue = Volley.newRequestQueue(CourseActivity.this);
        queue.add(courseRequest0);
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


                        AlertDialog.Builder builder = new AlertDialog.Builder(CourseActivity.this);
                        builder.setMessage("Voulez vous vraiment supprimer cette course ?")
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
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(CourseActivity.this);
                                                        builder.setMessage("Echec de la suppression de la course")
                                                                .setNegativeButton("Retry",null)
                                                                .create()
                                                                .show();
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(CourseActivity.this);
                                                    builder.setMessage("Echec de la suppression de la course, verifiez votre connexion internet")
                                                            .setNegativeButton("Retry",null)
                                                            .create()
                                                            .show();
                                                }


                                            }
                                        };

                                        CourseRequest courseRequest2 = new CourseRequest (2, idColoc, items.get(pos).toString(), "null", responseListenerDelete );
                                        RequestQueue queue = Volley.newRequestQueue(CourseActivity.this);
                                        queue.add(courseRequest2);

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
                        AlertDialog.Builder builder = new AlertDialog.Builder(CourseActivity.this);
                        builder.setMessage("Echec de l'envoie de la course")
                                .setNegativeButton("Retry",null)
                                .create()
                                .show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    AlertDialog.Builder builder = new AlertDialog.Builder(CourseActivity.this);
                    builder.setMessage("Echec de l'envoie de la course, verifiez votre connexion internet")
                            .setNegativeButton("Retry",null)
                            .create()
                            .show();
                }
            }
        };

        CourseRequest courseRequest1 = new CourseRequest (1, idColoc, itemText, pseudo, responseListenerSend );
        RequestQueue queue = Volley.newRequestQueue(CourseActivity.this);
        queue.add(courseRequest1);

        //fin de l'envoie


    }


}
