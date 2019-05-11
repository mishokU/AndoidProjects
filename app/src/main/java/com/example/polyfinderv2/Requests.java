package com.example.polyfinderv2;

public class Requests {

    private String title, category, description, from, type, image, thumb_image;
    private long time;

    public Requests(){

    }

    public Requests(String title, String category, String description, String from, String type, long time, String image, String thumb_image ){
        this.title = title;
        this.category = category;
        this.description = description;
        this.from = from;
        this.type = type;
        this.time = time;
        this.image = image;
        this.thumb_image = thumb_image;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getTitle(){
        return title;
    }

    public void setCategory(String category){
        this.category = category;
    }

    public String getCategory(){
        return category;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getDescription(){
        return description;
    }


    public void setFrom(String from){
        this.from = from;
    }

    public String getFrom(){
        return from;
    }

    public void setType(String type){
        this.type = type;
    }

    public String getType(){
        return type;
    }

    public void setTime(long time){
        this.type = type;
    }

    public long getTime(){
        return time;
    }

    public void setImage(String image){
        this.image = image;
    }

    public String getImage(){
        return image;
    }

    public void setThumb_image(String thumb_image){
        this.thumb_image = thumb_image;
    }

    public String getThumb_image(){
        return thumb_image;
    }


}
