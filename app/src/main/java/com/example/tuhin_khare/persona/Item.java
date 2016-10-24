package com.example.tuhin_khare.persona;

/**
 * Created by Tuhin_Khare on 23/10/16.
 */

public class Item {

    String title;
    int eventImage;

    public Item(String title,int eventImage)
    {
        this.eventImage=eventImage;
        this.title=title;
    }
    public String getTitle()
    {
        return title;
    }
    public int getEventImage()
    {
        return eventImage;
    }

}
