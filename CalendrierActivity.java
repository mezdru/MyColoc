package com.superjb.mycolloc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class CalendrierActivity extends AppCompatActivity {
    private String id_coloc;
    String etat;
    Date date;
    int length;
    ArrayList events = new ArrayList();
    ArrayList ids = new ArrayList();
    ArrayList noms = new ArrayList();
    ArrayList id_colocs = new ArrayList();
    ArrayList id_membres = new ArrayList();
    ArrayList detailss = new ArrayList();
    private String id_user="1";

    String sDate;
    private boolean undo = false;
    private CaldroidFragment caldroidFragment;
    private CaldroidFragment dialogCaldroidFragment;

    private void setCustomResourceForDates() {
        Calendar cal = Calendar.getInstance();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendrier);
        setTitle("Calendrier");

        Intent intentResponse = getIntent();
        id_coloc = intentResponse.getIntExtra("idColoc",0)+"";
        id_user = intentResponse.getIntExtra("idUser",0)+"";


        final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");

        caldroidFragment = new CaldroidFragment();
        if (savedInstanceState != null) {
            caldroidFragment.restoreStatesFromKey(savedInstanceState,
                    "CALDROID_SAVED_STATE");
        }
        // If activity is created from fresh
        else {
            Bundle args = new Bundle();
            Calendar cal = Calendar.getInstance();
            args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
            args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
            args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
            args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);

            caldroidFragment.setArguments(args);
        }

        setCustomResourceForDates();

        // Attach to the activity
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar1, caldroidFragment);
        t.commit();

        // Setup listener
        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {
                SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
                String laDate = dateformat.format(date);
                int i = 0;
                System.out.println(events.size());
                while (i < events.size()) {
                    System.out.println(i);
                    System.out.println(laDate);
                    System.out.println(events.get(i));
                    if (laDate.equals((String) events.get(i))) {
                        Context context = getApplicationContext();
                        System.out.println("je suis dans ...");
                        String id = (String) ids.get(i);
                        String id_coloc = (String) id_colocs.get(i);
                        String details = (String) detailss.get(i);
                        String nom = (String) noms.get(i);
                        String id_membre = (String) id_membres.get(i);
                        System.out.println(id+","+id_coloc+","+id_membre+","+nom+","+details+","+laDate);
                        AlertDialog.Builder builder = new AlertDialog.Builder(CalendrierActivity.this);
                        builder.setTitle("votre événement:")
                                .setMessage("Titre:   " + nom+"\n"+"Details:   "+details +"\n"+"Date:   "+laDate +"\n"+"Organisateur:   "+id_membre )
                                .setNegativeButton("ok", null)
                                .create()
                                .show();
                        i=events.size()+1;
                    }
                    else {
                        System.out.println("je suis dans .. la bouucle");
                        i++;
                        System.out.println(i);
                    }

                }
                if (i==events.size()){
                    System.out.println("je suis");
                    Intent mySuperIntent = new Intent(CalendrierActivity.this, CalendrierCreerEvenementActivity.class);
                    mySuperIntent.putExtra("laDate", laDate);
                    mySuperIntent.putExtra("idColoc",id_coloc);
                    mySuperIntent.putExtra("idUser", id_user);


                    startActivity(mySuperIntent);
                    finish();
                }


            }


            public void onLongClickDate(final Date date, View view) {
                etat = "3";
                SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
                String sDate = dateformat.format(date);
                int i = 0;
                while (i < events.size()) {
                    if (sDate.equals((String) events.get(i))) {
                        System.out.println("on est dans le lard");
                        String id = (String) ids.get(i);
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");
                                    if (success) {
                                        caldroidFragment.refreshView();
                                        Intent intent = getIntent();
                                        finish();
                                        startActivity(intent);
                                        System.out.println("on");
                                    } else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(CalendrierActivity.this);
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
                        CalendrierEventDelete eventDelete = new CalendrierEventDelete(id_coloc, etat, id, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(CalendrierActivity.this);
                        queue.add(eventDelete);
                        i++;

                    } else {
                        System.out.println(i);
                        i++;
                    }


                }
            }




        };
        caldroidFragment.setCaldroidListener(listener);

        etat="2";


        final Bundle state = savedInstanceState;
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    Boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        JSONObject jsonResponseReceive = new JSONObject(response);
                        boolean successReceive = jsonResponseReceive.getBoolean("success");
                        if (successReceive)
                        {
                            length = jsonResponse.length()-1;
                            int i=0;
                            while (i<length)
                            {
                                Log.i("myColoc", "hé mec ! tu tourne !");
                                String nom = jsonResponse.getJSONObject(i + "").getString("nom");
                                String date_event = jsonResponse.getJSONObject(i + "").getString("date_event");
                                String details = jsonResponse.getJSONObject(i + "").getString("details");
                                String id_membre = jsonResponse.getJSONObject(i + "").getString("id_membre");
                                String id = jsonResponse.getJSONObject(i + "").getString("id");
                                String id_coloc = jsonResponse.getJSONObject(i + "").getString("id_coloc");
                                Log.i("myColoc", nom + "," + date_event + "," + details + "," + id_membre + "," + id + "," + id_coloc);
                                System.out.println(i);
                                if (date_event.length()<10){
                                    if(date_event.length()<9){
                                        String jour =  date_event.substring(7,8);
                                        String mois = date_event.substring(5,6);
                                        String year = date_event.substring(0,4);
                                        mois = "0"+mois;
                                        jour = "0"+jour;
                                        System.out.println(jour);
                                        System.out.println(mois);
                                        System.out.println(year);
                                        date_event = year + "-" + mois +"-"+jour;
                                        System.out.println(date_event);
                                    }
                                    else if (date_event.length()==9){
                                        String jourtest = date_event.substring(7,8);
                                        String moistest = date_event.substring(5,6);
                                        if (moistest.equals("0")) {
                                            String jour = date_event.substring(8, 9);
                                            jour = "0" + jour;
                                            String mois = date_event.substring(5, 7);
                                            String year = date_event.substring(0, 4);
                                            date_event = year + "-" + mois + "-" + jour;
                                        }
                                        else  if (jourtest.equals("0")){
                                            String mois = date_event.substring(5,6);
                                            mois = "0" + mois;
                                            String jour = date_event.substring(7,9);
                                            String year = date_event.substring(0,4);
                                            date_event = year + "-" + mois + "-" + jour;
                                        }
                                    }
                                }
                                events.add(date_event);
                                ids.add(id);
                                noms.add(nom);
                                id_membres.add(id_membre);
                                detailss.add(details);
                                id_colocs.add(id_coloc);
                                try {
                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                    Date date = format.parse((String)events.get(i));
                                    System.out.println(date);
                                    ColorDrawable pink = new ColorDrawable(Color.MAGENTA);
                                    caldroidFragment.setBackgroundDrawableForDate(pink,date);
                                    caldroidFragment.setTextColorForDate(R.color.caldroid_white, date);
                                    caldroidFragment.refreshView();
                                } catch (ParseException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }

                                i++;
                            }

                        }
                        Log.i("myColoc", "load ça marche!");
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(CalendrierActivity.this);
                        builder.setMessage("le chargement a échoué.")
                                .setNegativeButton("retry", null)
                                .create()
                                .show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        CalendrierEventChercher eventchercher = new CalendrierEventChercher(id_coloc, etat,responseListener);
        RequestQueue queue = Volley.newRequestQueue(CalendrierActivity.this);
        queue.add(eventchercher);


    }

    /**
     * Save current states of the Caldroid here
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);

        if (caldroidFragment != null) {
            caldroidFragment.saveStatesToKey(outState, "CALDROID_SAVED_STATE");
        }

        if (dialogCaldroidFragment != null) {
            dialogCaldroidFragment.saveStatesToKey(outState,
                    "DIALOG_CALDROID_SAVED_STATE");
        }
    }

}
