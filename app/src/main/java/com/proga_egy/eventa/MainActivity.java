package com.proga_egy.eventa;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {


    ImageView gn;
    ImageView sc;
    ImageView fun;
    ImageView sp;
    ImageView more;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        checkFirstRun(); //Checking the first running




        gn = (ImageView)findViewById(R.id.gn);
        sc = (ImageView)findViewById(R.id.sc);
        fun = (ImageView)findViewById(R.id.fun);
        sp = (ImageView)findViewById(R.id.sp);
        more = (ImageView)findViewById(R.id.more);


        gn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,general.class);
                startActivity(intent);
            }
        });


    }


    public void checkFirstRun() {
        //Put the default value of isFirstRun
        boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isFirstRun", true);

        if (isFirstRun){

            //Showing welcome screen
            Intent intent = new Intent(MainActivity.this,welcome.class);
            startActivity(intent);


        }


    }


    //On back press action
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        moveTaskToBack(true);
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }


}
