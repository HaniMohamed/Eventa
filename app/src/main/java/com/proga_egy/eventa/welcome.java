package com.proga_egy.eventa;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;

/**
 * Created by Hani Hussein on 4/25/2016.
 */
public class welcome extends AppCompatActivity {

    ImageView nxt;
    ImageView wel;
    ImageView start;
    ImageView circles;
    ImageView secondImg;
    Spinner univ;
    ImageView thirdImg;

    int x=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_layout);

        wel =(ImageView)findViewById(R.id.welcome);
        secondImg =(ImageView)findViewById(R.id.nd_img);
        thirdImg =(ImageView)findViewById(R.id.rd_img);
        nxt = (ImageView)findViewById(R.id.next);
        circles =(ImageView)findViewById(R.id.circles);
        start =(ImageView)findViewById(R.id.start);
        univ = (Spinner)findViewById(R.id.spinner);


        secondImg.setVisibility(View.INVISIBLE);
        univ.setVisibility(View.INVISIBLE);
        thirdImg.setVisibility(View.INVISIBLE);



        start.setEnabled(false);
        start.setAlpha(40);





        nxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                x++;

                if(x==1){
                    wel.setVisibility(View.INVISIBLE);
                    secondImg.setVisibility(View.VISIBLE);
                    univ.setVisibility(View.VISIBLE);
                    circles.setImageResource(R.drawable.nd);

                }else if(x==2){
                    secondImg.setVisibility(View.INVISIBLE);
                    univ.setVisibility(View.INVISIBLE);

                    thirdImg.setVisibility(View.VISIBLE);
                    nxt.setEnabled(false);
                    nxt.setAlpha(40);
                    circles.setImageResource(R.drawable.rd);
                    start.setEnabled(true);
                    start.setAlpha(1000);

                }


            }
        });





        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                SharedPreferences preferences = getSharedPreferences("PREFERENCE", MODE_PRIVATE);
                SharedPreferences.Editor edit= preferences.edit();

                edit.putString("university", univ.getSelectedItem().toString());
                edit.commit();


                //Make isFirstRun value = false in the  setting storing
                getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                        .edit()
                        .putBoolean("isFirstRun", false)
                        .apply();

                Intent intent = new Intent(welcome.this, MainActivity.class);
                startActivity(intent);
            }
        });




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
