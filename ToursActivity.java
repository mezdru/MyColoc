package com.superjb.mycolloc;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Objects;


public class ToursActivity extends AppCompatActivity {

    private String id_coloc;
    String etat;
    Date date;
    int length;
    ArrayList tours_vaisselles = new ArrayList();
    ArrayList tours_menages = new ArrayList();
    ArrayList ids = new ArrayList();
    ArrayList pseudos = new ArrayList();
    ArrayList noms_tours = new ArrayList();
    ArrayList id_colocs = new ArrayList();
    ArrayList id_membres = new ArrayList();
    private String id_user;
    String sDate;
    String date3;
    String date4;
    int jour_entierm;
    int jour_entierv;
    Date datev;
    Date datem;
    private boolean undo = false;
    private CaldroidFragment caldroidFragment;
    private CaldroidFragment dialogCaldroidFragment;

    private void setCustomResourceForDates() {
        Calendar cal = Calendar.getInstance();


    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tours);


        Intent intentResponse = getIntent();
        id_coloc = intentResponse.getStringExtra("idColoc");
        id_user = intentResponse.getStringExtra("idUser");


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
            args.putInt(CaldroidFragment.START_DAY_OF_WEEK, CaldroidFragment.MONDAY);

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
                SimpleDateFormat dateformat = new SimpleDateFormat("EEE");
                String leJour = dateformat.format(date);
                System.out.println(leJour);
                int i = 0;
                if (leJour.equals("lun.")) {
                    leJour = "lundi";
                }
                if (leJour.equals("mar.")) {
                    leJour = "mardi";
                }
                if (leJour.equals("mer.")) {
                    leJour = "mercredi";
                }
                if (leJour.equals("jeu.")) {
                    leJour = "jeudi";
                }
                if (leJour.equals("ven.")) {
                    leJour = "vendredi";
                }
                if (leJour.equals("sam.")) {
                    leJour = "samedi";
                }
                if (leJour.equals("dim.")) {
                    leJour = "dimanche";
                }

                while (i < tours_menages.size()) {
                    System.out.println(i);
                    System.out.println(leJour);
                    System.out.println(tours_vaisselles.get(i));
                    System.out.println(tours_menages.get(i));
                    if (leJour.equals((String) tours_vaisselles.get(i)) ||leJour.equals((String) tours_menages.get(i))) {
                        Context context = getApplicationContext();
                        System.out.println("je suis dans ...");
                        String id = (String) ids.get(i);
                        String id_coloc = (String) id_colocs.get(i);
                        String tours_menage = (String) tours_menages.get(i);
                        String tours_vaisselle = (String) tours_vaisselles.get(i);
                        String pseudo = (String) pseudos.get(i);
                        System.out.println(id+","+id_coloc+","+pseudo+","+tours_menage+","+tours_vaisselle+","+leJour);
                        AlertDialog.Builder builder = new AlertDialog.Builder(ToursActivity.this);
                        builder.setTitle("votre événement:")
                                .setMessage("tour de ménage:    " + tours_menage +"\n"+"tours de vaiselle:   "+tours_vaisselle +"\n"+"jour:   "+leJour +"\n"+"c'est le tour de:   "+pseudo )
                                .setNegativeButton("ok", null)
                                .create()
                                .show();
                        i=tours_menages.size()+1;
                    }
                    else {
                        System.out.println("je suis dans .. la bouucle");
                        i++;
                        System.out.println(i);
                    }

                }
                if (i==tours_menages.size()){
                    System.out.println(leJour);
                    Intent mySuperIntent = new Intent(ToursActivity.this, ToursCreer.class);
                    mySuperIntent.putExtra("leJour", leJour);
                    mySuperIntent.putExtra("idColoc",id_coloc);
                    mySuperIntent.putExtra("idUser", id_user);
                    startActivity(mySuperIntent);
                    finish();
                }


            }
            public void onLongClickDate(final Date date, View view) {
                SimpleDateFormat dateformat = new SimpleDateFormat("EEE");
                String leJour = dateformat.format(date);
                System.out.println(leJour);
                etat="3";
                int i = 0;
                if (leJour.equals("lun.")) {
                    leJour = "lundi";
                }
                if (leJour.equals("mar.")) {
                    leJour = "mardi";
                }
                if (leJour.equals("mer.")) {
                    leJour = "mercredi";
                }
                if (leJour.equals("jeu.")) {
                    leJour = "jeudi";
                }
                if (leJour.equals("ven.")) {
                    leJour = "vendredi";
                }
                if (leJour.equals("sam.")) {
                    leJour = "samedi";
                }
                if (leJour.equals("dim.")) {
                    leJour = "dimanche";
                }
                while (i < tours_menages.size()) {
                    if (leJour.equals((String) tours_menages.get(i))||leJour.equals((String) tours_vaisselles.get(i))) {
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
                                        AlertDialog.Builder builder = new AlertDialog.Builder(ToursActivity.this);
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
                        ToursBDDDelete toursDelete = new ToursBDDDelete(id_coloc, etat, id, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(ToursActivity.this);
                        queue.add(toursDelete);
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
                            while (i<length) {
                                Log.i("myColoc", "hé mec ! tu tourne !");
                                String pseudo = jsonResponse.getJSONObject(i + "").getString("pseudo");
                                String jour_vaisselle = jsonResponse.getJSONObject(i + "").getString("jour_vaisselle");
                                String jour_menage = jsonResponse.getJSONObject(i + "").getString("jour_menage");
                                String id = jsonResponse.getJSONObject(i + "").getString("id");
                                String id_coloc = jsonResponse.getJSONObject(i + "").getString("id_coloc");
                                Log.i("myColoc", pseudo + "," + jour_menage + "," + jour_vaisselle + "," + id + "," + id_coloc);
                                tours_menages.add(jour_menage);
                                ids.add(id);
                                pseudos.add(pseudo);
                                tours_vaisselles.add(jour_vaisselle);
                                id_colocs.add(id_coloc);
                                if (jour_menage.equals("rien")) {
                                    jour_menage = "null";
                                    System.out.println("je ne suis rienm");
                                    System.out.println(jour_menage + ",");

                                }
                                if (jour_vaisselle.equals("rien")) {
                                    jour_vaisselle = "null";
                                    System.out.println("je ne suis rienv");
                                    System.out.println(jour_vaisselle);
                                }
                                if (jour_vaisselle.equals("lundi")) {
                                    jour_vaisselle = "lun.";
                                }
                                if (jour_vaisselle.equals("mardi")) {
                                    jour_vaisselle = "mar.";
                                }
                                if (jour_vaisselle.equals("mercredi")) {
                                    jour_vaisselle = "mer.";
                                }
                                if (jour_vaisselle.equals("jeudi")) {
                                    jour_vaisselle = "jeu.";
                                }
                                if (jour_vaisselle.equals("vendredi")) {
                                    jour_vaisselle = "ven.";
                                }
                                if (jour_vaisselle.equals("samedi")) {
                                    jour_vaisselle = "sam.";
                                }
                                if (jour_vaisselle.equals("dimanche")) {
                                    jour_vaisselle = "dim.";
                                }
                                if (jour_menage.equals("lundi")) {
                                    jour_menage = "lun.";
                                }
                                if (jour_menage.equals("mardi")) {
                                    jour_menage = "mar.";
                                }
                                if (jour_menage.equals("mercredi")) {
                                    jour_menage = "mer.";
                                }
                                if (jour_menage.equals("jeudi")) {
                                    jour_menage = "jeu.";
                                }
                                if (jour_menage.equals("vendredi")) {
                                    jour_menage = "ven.";
                                }
                                if (jour_menage.equals("samedi")) {
                                    jour_menage = "sam.";
                                }
                                if (jour_menage.equals("dimanche")) {
                                    jour_menage = "dim.";
                                }
                                SimpleDateFormat formatter = new SimpleDateFormat("EEE");
                                Date currentTime_1 = new Date();
                                String jour = formatter.format(currentTime_1);
                                SimpleDateFormat formatter2 = new SimpleDateFormat("dd-MM-yyyy");
                                Date currentTime_2 = new Date();
                                String jourcalcul = formatter2.format(currentTime_2);
                                String jourdetache = jourcalcul.substring(0, 2);
                                String mois = jourcalcul.substring(3, 5);
                                String year = jourcalcul.substring(6, 10);
                                int jour_entier = Integer.parseInt(jourdetache);
                                System.out.println(jour_entier);
                                System.out.println(jour_menage);
                                System.out.println(jour_vaisselle);
                                int cjour = 0;
                                int cv = 0;
                                int cm = 0;
                                int diffv = 0;
                                int diffm = 0;

                                if (jour.equals("lun.")) {
                                    cjour = 1;
                                }
                                if (jour.equals("mar.")) {
                                    cjour = 2;
                                }
                                if (jour.equals("mer.")) {
                                    cjour = 3;
                                }
                                if (jour.equals("jeu.")) {
                                    cjour = 4;
                                }
                                if (jour.equals("ven.")) {
                                    cjour = 5;
                                }
                                if (jour.equals("sam.")) {
                                    cjour = 6;
                                }
                                if (jour.equals("dim.")) {
                                    cjour = 7;
                                }
                                if (jour_menage != "null" && jour_menage != "rien") {
                                    if (jour_menage.equals("lun.")) {
                                        cm = 1;
                                    }
                                    if (jour_menage.equals("mar.")) {
                                        cm = 2;
                                    }
                                    if (jour_menage.equals("mer.")) {
                                        cm = 3;
                                    }
                                    if (jour_menage.equals("jeu.")) {
                                        cm = 4;
                                    }
                                    if (jour_menage.equals("ven.")) {
                                        cm = 5;
                                    }
                                    if (jour_menage.equals("sam.")) {
                                        cm = 6;
                                    }
                                    if (jour_menage.equals("dim.")) {
                                        cm = 7;
                                    }
                                    System.out.println("cm:" + cm);
                                    System.out.println("jour:");
                                    System.out.println("cjour:" + cjour);

                                    diffm = cjour - cm;
                                    System.out.println("diffm:" + diffm);


                                    if (diffm < 0) {
                                        jour_entierm = 0;
                                        jour_entierm = jour_entier - diffm;
                                        System.out.println(jour_entierm);

                                    }
                                    if (diffm > 0) {
                                        jour_entierm = 0;
                                        jour_entierm = jour_entier - diffm;
                                        System.out.println(jour_entierm);

                                    }
                                    if (diffm == 0) {
                                        jour_entierm = jour_entier;
                                    }
                                }


                                if (jour_vaisselle != "null" && jour_vaisselle != "rien") {
                                    if (jour_vaisselle.equals("lun.")) {
                                        cv = 1;
                                    }
                                    if (jour_vaisselle.equals("mar.")) {
                                        cv = 2;
                                    }
                                    if (jour_vaisselle.equals("mer.")) {
                                        cv = 3;
                                    }
                                    if (jour_vaisselle.equals("jeu.")) {
                                        cv = 4;
                                    }
                                    if (jour_vaisselle.equals("ven.")) {
                                        cv = 5;
                                    }
                                    if (jour_vaisselle.equals("sam.")) {
                                        cv = 6;
                                    }
                                    if (jour_vaisselle.equals("dim.")) {
                                        cv = 7;
                                    }

                                    diffv = cjour - cv;
                                    System.out.println("diffv:" + diffv);
                                    if (diffv < 0) {
                                        jour_entierv = 0;
                                        jour_entierv = jour_entier - diffv;
                                        System.out.println(jour_entierv);

                                    }
                                    if (diffv > 0) {
                                        jour_entierv = 0;
                                        jour_entierv = jour_entier - diffv;
                                        System.out.println(jour_entierv);

                                    }
                                    if (diffv == 0) {
                                        jour_entierv = jour_entier;
                                    }

                                }
                                if (jour_vaisselle != "null" && jour_vaisselle != "rien" && jour_menage != "null" && jour_menage != "rien") {
                                    String dayv = String.valueOf(jour_entierv);
                                    String jour_vaisselle_final = year + "-" + mois + "-" + dayv;
                                    String daym = String.valueOf(jour_entierm);
                                    String jour_menage_final = year + "-" + mois + "-" + daym;
                                    try {
                                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                        datev = format.parse(jour_vaisselle_final);
                                        datem = format.parse(jour_menage_final);
                                    } catch (ParseException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                    System.out.println(datev);
                                    ColorDrawable pink = new ColorDrawable(Color.GREEN);
                                    caldroidFragment.setBackgroundDrawableForDate(pink, datev);
                                    caldroidFragment.setTextColorForDate(R.color.caldroid_white, datev);
                                    caldroidFragment.refreshView();
                                } else {
                                    if (jour_vaisselle != "null" && jour_vaisselle != "rien") {
                                        String dayv = String.valueOf(jour_entierv);
                                        String jour_vaisselle_final = year + "-" + mois + "-" + dayv;
                                        try {
                                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                            datev = format.parse(jour_vaisselle_final);
                                        } catch (ParseException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }
                                        System.out.println(datev);
                                        ColorDrawable pink = new ColorDrawable(Color.YELLOW);
                                        caldroidFragment.setBackgroundDrawableForDate(pink, datev);
                                        caldroidFragment.setTextColorForDate(R.color.caldroid_white, datev);
                                        caldroidFragment.refreshView();
                                    }
                                    if (jour_menage != "null" && jour_menage != "rien") {
                                        String daym = String.valueOf(jour_entierm);
                                        String jour_menage_final = year + "-" + mois + "-" + daym;
                                        try {
                                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                            datem = format.parse(jour_menage_final);
                                        } catch (ParseException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }
                                        System.out.println(datem);
                                        ColorDrawable pink = new ColorDrawable(Color.MAGENTA);
                                        caldroidFragment.setBackgroundDrawableForDate(pink, datem);
                                        caldroidFragment.setTextColorForDate(R.color.caldroid_white, datem);
                                        caldroidFragment.refreshView();
                                    }

                                }

                                i++;
                            }

                        }
                        Log.i("myColoc", "load ça marche!");
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ToursActivity.this);
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
        ToursBDDChercher tourschercher = new ToursBDDChercher(id_coloc, etat,responseListener);
        RequestQueue queue = Volley.newRequestQueue(ToursActivity.this);
        queue.add(tourschercher);


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
