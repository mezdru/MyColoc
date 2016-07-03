package com.superjb.mycolloc;

import android.app.Activity;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SettingActivity extends Activity {

    private String email ="";
    private int avatar;
    static SettingActivity settingActivityFlag;

    ListView list;
    final String[] itemname ={
            "Mon Avatar",
            "Mon Pseudo",
            "Mon Email",
            "Mon Mot de Passe",
            "Mes Notifications",
            "Quitter cette Coloc'",
            "Nom de la Coloc'",
            "Pot Commun de la Coloc'",
            "Mot de Passe de la Coloc'"
    };

    Integer[] imgid={
            R.drawable.avatar,
            R.drawable.star,
            R.drawable.chat,
            R.drawable.padlock,
            R.drawable.upload,
            R.drawable.cancel,
            R.drawable.house,
            R.drawable.money_bag,
            R.drawable.download
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        settingActivityFlag = this;


        Intent intentResponse = getIntent();
        final int idUser = intentResponse.getIntExtra("idUser",0);
        final String pseudo = intentResponse.getStringExtra("pseudo");
        final int idColoc = intentResponse.getIntExtra("idColoc",0);
        final String nomColoc = intentResponse.getStringExtra("nomColoc");

        setTitle("Settings : "+pseudo);

        final ImageView iAvatar = (ImageView) findViewById(R.id.ivAvatar);
        final TextView tvPseudo = (TextView) findViewById(R.id.tvPseudo);
        final TextView tvEmail = (TextView) findViewById(R.id.tvEmail);
        final TextView tvColoc = (TextView) findViewById(R.id.tvnomColoc);

        /*TextView tvChangerPseudo = (TextView) findViewById(R.id.tvChangerPseudo);
        TextView tvCangerEmail = (TextView) findViewById(R.id.tvChangerEmail);
        TextView tvChangerAvatar = (TextView) findViewById(R.id.tvChangerAvatar);
        TextView tvChangerMdp = (TextView) findViewById(R.id.tvChangerMdp);

        TextView tvParametresNotifications = (TextView) findViewById(R.id.tvParametresNotification);
        */


        SettingCustomListAdapter adapter=new SettingCustomListAdapter(this, itemname, imgid);
        list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);


        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);

                    //c'est bien sucess et pas success, il y a la meme erreur dans le php
                    boolean success = jsonResponse.getBoolean("success");

                    if (success)
                    {
                        email=jsonResponse.getString("email");
                        tvEmail.setText(email);
                        tvPseudo.setText(pseudo);
                        tvColoc.setText(nomColoc);


                        avatar=chooseAvatar(jsonResponse.getString("avatar"));
                        iAvatar.setImageResource(avatar);
                    }
                    else
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                        builder.setMessage("Erreur de chargement de la page")
                                .setNegativeButton("OK", null)
                                .create()
                                .show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        SettingRequest settingRequest = new SettingRequest(idUser, 1, idColoc, responseListener);
        RequestQueue queue = Volley.newRequestQueue(SettingActivity.this);
        queue.add(settingRequest);




        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String Slecteditem= itemname[+position];

                switch (Slecteditem){
                    case "Mon Avatar":
                        Intent myIntentAvatar = new Intent(SettingActivity.this, SettingsAvatarActivity.class);
                        myIntentAvatar.putExtra("email",email);
                        myIntentAvatar.putExtra("pseudo",pseudo);
                        myIntentAvatar.putExtra("avatar",avatar);
                        myIntentAvatar.putExtra("idUser",idUser);
                        startActivity(myIntentAvatar);
                        break;

                    case "Mon Pseudo":
                        Intent myIntentPseudo = new Intent(SettingActivity.this, SettingsPseudEmailActivity.class);
                        myIntentPseudo.putExtra("etat",3);
                        myIntentPseudo.putExtra("email",email);
                        myIntentPseudo.putExtra("pseudo",pseudo);
                        myIntentPseudo.putExtra("avatar",avatar);
                        myIntentPseudo.putExtra("idUser",idUser);
                        myIntentPseudo.putExtra("idColoc",idColoc);
                        myIntentPseudo.putExtra("nomColoc",nomColoc);
                        startActivity(myIntentPseudo);
                        break;

                    case "Mon Email":
                        Intent myIntentEmail = new Intent(SettingActivity.this, SettingsPseudEmailActivity.class);
                        myIntentEmail.putExtra("etat",2);
                        myIntentEmail.putExtra("email",email);
                        myIntentEmail.putExtra("pseudo",pseudo);
                        myIntentEmail.putExtra("avatar",avatar);
                        myIntentEmail.putExtra("idUser",idUser);
                        myIntentEmail.putExtra("idColoc",idColoc);
                        myIntentEmail.putExtra("nomColoc",nomColoc);
                        startActivity(myIntentEmail);
                        break;

                    case "Mon Mot de Passe":
                        Intent myIntentMyPassword = new Intent(SettingActivity.this, SettingsPasswordActivity.class);
                        myIntentMyPassword.putExtra("email",email);
                        myIntentMyPassword.putExtra("pseudo",pseudo);
                        myIntentMyPassword.putExtra("avatar",avatar);
                        myIntentMyPassword.putExtra("idUser",idUser);
                        myIntentMyPassword.putExtra("etat",5);
                        startActivity(myIntentMyPassword);
                        break;

                    case "Mes Notifications":

                        ////////////////////                        // a modifier
                        final CharSequence[] items = {" Les Dépenses "," Les Notes ", " Les Courses ", " Les Paramètres ", " Le Calendrier ",
                        " Les Tours "};
// arraylist to keep the selected items
                        final ArrayList seletedItems=new ArrayList();

                        AlertDialog dialog = new AlertDialog.Builder(SettingActivity.this)
                                .setTitle("Accepter les Notifications lors des modifications sur : ")
                                .setMultiChoiceItems(items, null, new DialogInterface.OnMultiChoiceClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {
                                        if (isChecked) {
                                            // If the user checked the item, add it to the selected items
                                            seletedItems.add(indexSelected);
                                        } else if (seletedItems.contains(indexSelected)) {
                                            // Else, if the item is already in the array, remove it
                                            seletedItems.remove(Integer.valueOf(indexSelected));
                                        }
                                    }
                                }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                        //  Your code when user clicked on OK
                                        //  You can write the code  to save the selected item here
                                    }
                                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                        //  Your code when user clicked on Cancel
                                    }
                                }).create();
                        dialog.show();

//////////////////////                        //a mofifier !!



                            break;

                    case "Quitter cette Coloc'":
                        AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                        builder.setTitle("Quitter cette Coloc'")
                                .setMessage("Pour quitter votre Coloc', votre solde doit être à 0.")
                                .setMessage("Etes vous vraiment sûr de quitter cette Coloc' ?")
                                .setPositiveButton("oui", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {


//requette php quitter coloc
                                        Response.Listener<String> responseListenerquitter = new Response.Listener<String>() {

                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    JSONObject jsonResponse = new JSONObject(response);

                                                    boolean success = jsonResponse.getBoolean("success");

                                                    if (success)
                                                    {
                                                        MenuActivity.getInstance().finish();
                                                        Intent myIntentHomeLess = new Intent(SettingActivity.this, HomeLessActivity.class);
                                                        myIntentHomeLess.putExtra("idUser",idUser);
                                                        myIntentHomeLess.putExtra("pseudo",pseudo);
                                                        startActivity(myIntentHomeLess);
                                                        finish();
                                                    }
                                                    else
                                                    {
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                                                        builder.setMessage("Erreur : Votre solde n'est pas à 0")
                                                                .setNegativeButton("OK", null)
                                                                .create()
                                                                .show();
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        };

                                        SettingRequest settingRequestquitter = new SettingRequest(idUser, 8, idColoc, responseListenerquitter);
                                        RequestQueue queue = Volley.newRequestQueue(SettingActivity.this);
                                        queue.add(settingRequestquitter);
//fin php quitter coloc                                A MODIFIER


                                    }
                                })
                                .setNegativeButton("non", null)
                                .create()
                                .show();
                        break;

                    case "Nom de la Coloc'":
                        Intent myIntentNomColoc = new Intent(SettingActivity.this, SettingsPseudEmailActivity.class);
                        myIntentNomColoc.putExtra("etat",7);
                        myIntentNomColoc.putExtra("email",email);
                        myIntentNomColoc.putExtra("pseudo",pseudo);
                        myIntentNomColoc.putExtra("avatar",avatar);
                        myIntentNomColoc.putExtra("idUser",idUser);
                        myIntentNomColoc.putExtra("idColoc",idColoc);
                        myIntentNomColoc.putExtra("nomColoc",nomColoc);
                        startActivity(myIntentNomColoc);
                        break;

                    case "Pot Commun de la Coloc'":
////////////////////                        // a modifier
                        final CharSequence[] items2 = {" Avec un Pot Commun "," Sans Pot Commun "};
// arraylist to keep the selected items
                        final ArrayList seletedItems2=new ArrayList();

                        AlertDialog dialog2 = new AlertDialog.Builder(SettingActivity.this)
                                .setTitle("Comment effectuer les dépenses ?")
                                .setSingleChoiceItems(items2, -1, null )
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                        ListView lw = ((AlertDialog)dialog).getListView();
                                        int decision = lw.getCheckedItemPosition();

                                        if (decision==0)
                                        {
                                            decision=9;
                                        }
                                        else if (decision==1)
                                        {
                                            decision=10;
                                        }
                                        else
                                        {
                                            decision=-1;
                                        }



                                        //requette php pot commun coloc
                                        Response.Listener<String> responseListenerPotCommun = new Response.Listener<String>() {

                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    JSONObject jsonResponse = new JSONObject(response);

                                                    boolean success = jsonResponse.getBoolean("success");

                                                    if (success)
                                                    {
                                                        finish();
                                                    }
                                                    else
                                                    {
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                                                        builder.setMessage("Erreur : pas de chance")
                                                                .setNegativeButton("OK", null)
                                                                .create()
                                                                .show();
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        };

                                        SettingRequest settingRequestPotCommun = new SettingRequest(idUser, decision, idColoc, responseListenerPotCommun);
                                        RequestQueue queue = Volley.newRequestQueue(SettingActivity.this);
                                        queue.add(settingRequestPotCommun);
//fin php potcommin coloc                               php undone/uncheckde




                                    }
                                })
                                .setNegativeButton("Annulé", null)
                                .create();
                        dialog2.show();

//////////////////////                        //a mofifier !!
                        break;

                    case "Mot de Passe de la Coloc'":
                        Intent myIntentColocPassword = new Intent(SettingActivity.this, SettingsPasswordActivity.class);
                        myIntentColocPassword.putExtra("email",email);
                        myIntentColocPassword.putExtra("pseudo",pseudo);
                        myIntentColocPassword.putExtra("avatar",avatar);
                        myIntentColocPassword.putExtra("idUser",idUser);
                        myIntentColocPassword.putExtra("nomColoc",nomColoc);
                        myIntentColocPassword.putExtra("idColoc",idColoc);
                        myIntentColocPassword.putExtra("etat",6);
                        startActivity(myIntentColocPassword);
                        break;

                    default:
                        Toast.makeText(getApplicationContext(), "Erreur : "+ Slecteditem+" pas de chance ?", Toast.LENGTH_SHORT).show();
                        break;


                }
            }
        });


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

    public static SettingActivity getInstance(){
        return   settingActivityFlag;
    }
}
