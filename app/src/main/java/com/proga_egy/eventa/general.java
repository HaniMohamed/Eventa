package com.proga_egy.eventa;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.HttpEntity;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


/**
 * Created by Hani Hussein on 4/23/2016.
 */
public class general extends AppCompatActivity {

    // flag for Internet connection status
    Boolean isInternetPresent = false;

    // Connection detector class
    ConnectionDetector cd;


    HttpPost httppost;
    StringBuffer buffer;
    HttpResponse response;
    HttpClient httpclient;



    ImageView srchbtn;
    EditText srch;

    EventsCustomAdapter adapter;
    ListView listEvents;
    ArrayList<EventsItems> records;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_layout);


        srch = (EditText) findViewById(R.id.srch);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN); //Prevent keyboard opening automatically


        // creating connection detector class instance
        cd = new ConnectionDetector(getApplicationContext());


        // get Internet status
        isInternetPresent = cd.isConnectingToInternet();

        listEvents = (ListView)findViewById(R.id.content);
        getList(); //get list contents

        //On List Item click
        listEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(general.this, EventContent.class);
                startActivity(intent);

            }
        });


        //On keyboard's done button click action
        srch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {

                    search(srch.getText().toString()); //searching

                    handled = true;
                }
                return handled;
            }
        });


        srchbtn = (ImageView) findViewById(R.id.srchbtn);
        //search button click action
        srchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search(srch.getText().toString()); //searching
            }
        });


        //floating menu and it's background action
        final FrameLayout frameLayout = (FrameLayout) findViewById(R.id.frame_layout);
        frameLayout.getBackground().setAlpha(0);
        final FloatingActionsMenu fabMenu = (FloatingActionsMenu) findViewById(R.id.fab_menu);
        fabMenu.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                frameLayout.getBackground().setAlpha(200);
                frameLayout.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        fabMenu.collapse();
                        return true;
                    }
                });
            }

            @Override
            public void onMenuCollapsed() {
                frameLayout.getBackground().setAlpha(0);
                frameLayout.setOnTouchListener(null);
            }
        });


        //refresh floating button action
        FloatingActionButton fabRefresh = (FloatingActionButton)findViewById(R.id.fab_refresh);
        fabRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabMenu.collapse();
                Toast.makeText(general.this, "Refreshing", Toast.LENGTH_SHORT).show();
                getList(); //get list contents

            }
        });


    }



    //Getting list of Events
    public void getList(){
        // check for Internet status
        if (isInternetPresent) {
            // Internet Connection is Present
            records = new ArrayList<EventsItems>();
            adapter = new EventsCustomAdapter(getApplicationContext(), R.layout.generalitem, R.id.headline, records);
            listEvents.setAdapter(adapter);

            BackTask bt = new BackTask();
            bt.execute();

        }
    }


    //background process to make a request to server and list product information
    private class BackTask extends AsyncTask<Void, Void, Void> {
        protected void onPreExecute() {
            super.onPreExecute();


        }

        protected Void doInBackground(Void... params) {

            InputStream is = null;
            String result = "";
            try {

                httpclient = new DefaultHttpClient();
                httppost = new HttpPost("http://koraalife.com/getEvents.php");
                response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
            // Get our response as a String.
                is = entity.getContent();

            } catch (Exception e) {


                Log.e("ERROR", e.getMessage());

            }

        //convert response to string
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                result = sb.toString();
            } catch (Exception e) {
                Log.e("ERROR", "Error converting result " + e.toString());

            }

            //parse json data
            try {
            // Remove unexpected characters that might be added to beginning of the string
                result = result.substring(result.indexOf("["));
                JSONArray jArray = new JSONArray(result);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    EventsItems p = new EventsItems();

                    p.setType(json_data.getString("type"));
                    p.setImage(json_data.getString("img_url"));
                    p.setTitle(json_data.getString("headline"));
                    p.setShDesc(json_data.getString("shrt_desc"));
                    p.setLnDesc(json_data.getString("long_desc"));
                    p.setHost(json_data.getString("host"));
                    p.setDate(json_data.getString("date"));
                    p.setPlace(json_data.getString("place"));
                    p.setPriority(json_data.getInt("priority"));


                    Calendar c = Calendar.getInstance();
                    System.out.println("Current time => "+c.getTime());

                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    String formattedDate = df.format(c.getTime());


                    if (json_data.getString("type").equals("general")){

                        records.add(0,p);
                    }


                }


            } catch (Exception e) {
                Log.e("ERROR", "Error pasting data " + e.toString());


            }

            return null;
        }


        protected void onPostExecute(Void result) {


            Log.e("size", records.size() + "");
            adapter.notifyDataSetChanged(); //notify the ListView to get new records

        }

    }



    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;


        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
            bmImage.setScaleType(ImageView.ScaleType.FIT_XY);

        }
    }



    //search method
    public void search(String s){
        //hide keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        Toast.makeText(general.this, "Searching for: "+s + "..!", Toast.LENGTH_SHORT).show();

    }


}
