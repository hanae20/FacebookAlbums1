package com.example.user.facebookalbums;

/**
 * Created by User on 01/11/2017.
 */

public class AlbumItem{
    private String id;
    private String name;
    public AlbumItem(String id,String name){
        this.id=id;
        this.name=name;
    }
    public String getId(){
        return id;
    }
    public void setId(String id){
        this.id=id;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name=name;
    }
    @Override
    public String toString(){
        return(this.name);
    }
}