package com.superjb.mycolloc;



import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EtatActivity extends Activity {
    ListView listView;
    private float soldeCommun=0;
    private float monSolde=0;
    private int idAvatar=0;
    private float monArgentMis=0;
    private boolean potCommun;

    static EtatActivity etatActivityFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etat);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        etatActivityFlag = this;

        Intent intentResponse = getIntent();
        final int idColoc = intentResponse.getIntExtra("idColoc",0);
        final int idUser = intentResponse.getIntExtra("idUser",0);
        final String pseudo = intentResponse.getStringExtra("pseudo");

        setTitle("Etat : "+pseudo);
        listView = (ListView) findViewById(R.id.listView1);
        final TextView tvPotCommun = (TextView) findViewById(R.id.tvPotCommun);

        final ArrayList<EtatListItem> myList = new ArrayList<EtatListItem>();
        final EtatCustomListAdapter adapter = new EtatCustomListAdapter(this, myList);

        final Button bChangeSolde = (Button) findViewById(R.id.bChangerSolde);




        //Envoie php pour recevoir les notes
        Response.Listener<String> responseListenerReceive = new Response.Listener<String>(){
            @Override
            public void onResponse(String responseReceive) {
                try {

                    JSONObject jsonResponseReceive = new JSONObject(responseReceive);
                    boolean successReceive = jsonResponseReceive.getBoolean("success");

                    String reponsePotCommun = jsonResponseReceive.getString("pot_commun");

                    if (reponsePotCommun.equals("oui"))
                    {
                        potCommun=true;
                    }else if (reponsePotCommun.equals("non"))
                    {
                        potCommun=false;
                    }else
                    {
                        successReceive=false;
                    }

                    if (successReceive)
                    {

                        int lenth = jsonResponseReceive.length()-2;
                        float variable;
                        for (int i = 0; i < lenth; i++) {
                            variable = Float.parseFloat(jsonResponseReceive.getJSONObject(i + "").getString("argent_mise")) - Float.parseFloat(jsonResponseReceive.getJSONObject(i + "").getString("argent_due"));
                            myList.add(new EtatListItem(jsonResponseReceive.getJSONObject(i + "").getString("pseudo"),
                                    variable,chooseAvatar(jsonResponseReceive.getJSONObject(i + "").getString("avatar")),
                                    jsonResponseReceive.getJSONObject(i + "").getString("email")));
                            soldeCommun=soldeCommun+variable;
                            if (pseudo.equals(jsonResponseReceive.getJSONObject(i + "").getString("pseudo")))
                            {
                                monSolde=variable;
                                idAvatar=chooseAvatar(jsonResponseReceive.getJSONObject(i + "").getString("avatar"));
                                monArgentMis = Float.parseFloat(jsonResponseReceive.getJSONObject(i + "").getString("argent_mise"));


                            }
                        }
                        if (potCommun)
                        {
                            tvPotCommun.setText("Solde du Pot Commun : "+soldeCommun+" €");
                        }else {
                            tvPotCommun.setText("Il n'y a pas de pot commun dans cette coloc");
                            bChangeSolde.setText("Faire une dépense commune");
                        }


                        listView.setAdapter(adapter);
                        listView.setOnItemClickListener(adapter);
                    }
                    else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(EtatActivity.this);
                        builder.setMessage("Echec de la reception des données")
                                .setNegativeButton("Retry",null)
                                .create()
                                .show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    AlertDialog.Builder builder = new AlertDialog.Builder(EtatActivity.this);
                    builder.setMessage("Echec de la reception des données, verifiez votre connexion internet")
                            .setNegativeButton("Retry",null)
                            .create()
                            .show();
                }
            }
        };

        ReceiveEtatRequest receiveEtatRequest = new ReceiveEtatRequest (idColoc+"", responseListenerReceive );
        RequestQueue queue = Volley.newRequestQueue(EtatActivity.this);
        queue.add(receiveEtatRequest);
        //Fin de l'envoie php




        bChangeSolde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentEtatChangerSolde = new Intent(EtatActivity.this, EtatChangerSoldeActivity.class);
                intentEtatChangerSolde.putExtra("idColoc",idColoc);
                intentEtatChangerSolde.putExtra("idUser", idUser);
                intentEtatChangerSolde.putExtra("soldeCommun",soldeCommun);
                intentEtatChangerSolde.putExtra("pseudo",pseudo);
                intentEtatChangerSolde.putExtra("monSolde",monSolde);
                intentEtatChangerSolde.putExtra("idAvatar",idAvatar);
                intentEtatChangerSolde.putExtra("monArgentMis",monArgentMis);
                intentEtatChangerSolde.putExtra("potCommun",potCommun);
                startActivity(intentEtatChangerSolde);

            }
        });


    }

    public static EtatActivity getInstance(){
        return   etatActivityFlag;
    }

    public int chooseAvatar (String Avatar)
    {
        int result;
        switch (Avatar)
        {

            case "avatar1":
                result=R.drawable.avatar1;
                break;
            case "avatar2":
                result=R.drawable.avatar2;
                break;
            case "avatar3":
                result=R.drawable.avatar3;
                break;
            case "avatar4":
                result=R.drawable.avatar4;
                break;
            case "avatar5":
                result=R.drawable.avatar5;
                break;
            case "avatar6":
                result=R.drawable.avatar6;
                break;
            default:
                result=R.drawable.avatar1;
        }
        return result;

    }
}