package com.superjb.mycolloc;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class MenuActivity extends AppCompatActivity {

    static MenuActivity menuActivityFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);



        menuActivityFlag = this;

        Intent intentLogin = getIntent();
        final int idUser = intentLogin.getIntExtra("idUser",0);
        final int idColoc = intentLogin.getIntExtra("idColoc",0);
        final String pseudo = intentLogin.getStringExtra("pseudo");
        final String nomColoc = intentLogin.getStringExtra("nomColoc");
        //à changer bientôt, mais la flemme de le faire mtn
        //final String pseudo = "jb";


        Log.i("idUser",idUser+"");
        Log.i("idColoc",idColoc+"");

        setTitle(nomColoc +" : "+pseudo);


        ImageButton IbNote = (ImageButton) findViewById(R.id.bNotes);
        IbNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentNote = new Intent(MenuActivity.this, NoteActivity.class);
                intentNote.putExtra("idColoc",idColoc);
                intentNote.putExtra("pseudo",pseudo);
                startActivity(intentNote);
            }
        });


        ImageButton IbEtat = (ImageButton) findViewById(R.id.bEtat);
        IbEtat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentEtat = new Intent(MenuActivity.this, EtatActivity.class);
                intentEtat.putExtra("idColoc",idColoc);
                intentEtat.putExtra("idUser", idUser);
                intentEtat.putExtra("pseudo", pseudo);
                startActivity(intentEtat);
            }
        });


        ImageButton ibSettings = (ImageButton) findViewById(R.id.bHistorique);
        ibSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSettings = new Intent(MenuActivity.this, SettingActivity.class);
                intentSettings.putExtra("idColoc",idColoc);
                intentSettings.putExtra("idUser", idUser);
                intentSettings.putExtra("pseudo", pseudo);
                intentSettings.putExtra("nomColoc", nomColoc);
                startActivity(intentSettings);
            }
        });

        ImageButton ibCourses = (ImageButton) findViewById(R.id.bCourses);
        ibCourses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCourse = new Intent(MenuActivity.this, CourseActivity.class);
                intentCourse.putExtra("idColoc",idColoc);
                intentCourse.putExtra("pseudo",pseudo);
                startActivity(intentCourse);
            }
        });

        ImageButton ibCalendrier = (ImageButton) findViewById(R.id.bCalendrier);
        ibCalendrier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCalendrier = new Intent(MenuActivity.this, CalendrierActivity.class);
                intentCalendrier.putExtra("idColoc",idColoc);
                intentCalendrier.putExtra("idUser",idUser);
                startActivity(intentCalendrier);
            }
        });

        ImageButton ibTours = (ImageButton) findViewById(R.id.bTours);
        ibTours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intentTours = new Intent(MenuActivity.this, ToursActivity.class);
                intentTours.putExtra("idColoc",idColoc);
                intentTours.putExtra("idUser",idUser);
                startActivity(intentTours);*/
                Toast.makeText(getApplicationContext(), "Coming Soon :)", Toast.LENGTH_LONG).show();
            }
        });

    }

    public static MenuActivity getInstance(){
        return   menuActivityFlag;
    }

}
