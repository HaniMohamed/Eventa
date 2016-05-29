package com.proga_egy.eventa;


/**
 * Created by Hani Hussein on 4/23/2016.
 */

public class EventsItems {
    private String type;
    private String img_url;
    private String headline;
    private String shrt_desc;
    private String long_desc;
    private String host;
    private String date;
    private String place;
    private int priority;
    public void setType(String type){this.type=type;}
    public void setImage(String img_url){this.img_url=img_url;}
    public void setTitle(String headline){this.headline=headline;}
    public void setShDesc(String shrt_desc){this.shrt_desc=shrt_desc;}
    public void setLnDesc(String long_desc){this.long_desc=long_desc;}
    public void setHost(String host){this.host=host;}
    public void setDate(String date){this.date=date;}
    public void setPriority(int priority){this.priority=priority;}
    public void setPlace(String place){this.place=place;}
    public String getType(){return type;}
    public String getImage(){return img_url;}
    public String getTitle(){return headline;}
    public String getShDesc(){return shrt_desc;}
    public String getLnDesc(){return long_desc;}
    public String getHost(){return host;}
    public String getDate(){return date;}
    public String getPlace(){return place;}
    public int getPriority(){return priority;}

}

