package com.proga_egy.eventa;


/**
 * Created by Hani Hussein on 4/23/2016.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;


public class EventsCustomAdapter extends ArrayAdapter<EventsItems> {

    int groupid;
    ArrayList<EventsItems> records;
    Context context;

    ImageView img;
    ImageView hostImg;
    TextView title;
    TextView host;
    TextView date;
    TextView shDesc;






    public EventsCustomAdapter(Context context, int vg, int id, ArrayList<EventsItems> records) {
        super(context, vg, id, records);
        this.context = context;
        groupid = vg;
        this.records = records;

    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View itemView = inflater.inflate(groupid, parent, false);


        img=(ImageView) itemView.findViewById(R.id.image);
        new DownloadImageTask(img)
                .execute(records.get(position).getImage());


        hostImg=(ImageView) itemView.findViewById(R.id.hostimg);
        new DownloadImageTask(hostImg)
                .execute("http://koraalife.com/admin/images/"+records.get(position).getHost());



        title = (TextView)itemView.findViewById(R.id.headline);
        title.setText(records.get(position).getTitle());

        host= (TextView)itemView.findViewById(R.id.host);
        host.setText(records.get(position).getHost());

        date= (TextView)itemView.findViewById(R.id.date);
        date.setText(records.get(position).getDate());

        shDesc= (TextView)itemView.findViewById(R.id.shDesc);
        shDesc.setText(records.get(position).getShDesc());





        return itemView;
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





}