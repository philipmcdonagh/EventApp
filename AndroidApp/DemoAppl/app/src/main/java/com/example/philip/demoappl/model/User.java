package com.example.philip.demoappl.model;

import android.companion.CompanionDeviceManager;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class User {

    private String userName;
    private String userId;
    private String userEmail;
    private String userLinkedin;
    private String userCompany;
    private ArrayList<Integer> favourites = new ArrayList<Integer>();

    public User () {}

    //constructor
    public User(String userName, String userId, String userEmail, String userCompany, String userLinkedin ) {
        this.userName = userName;
        this.userId = userId;
        this.userCompany = userCompany;
        this.userLinkedin = userLinkedin;
        this.userEmail = userEmail;
    }

    //getters
    public String getUserName() {
        return userName;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserEmail() {return userEmail;}

    public String getUserLinkedin() { return userLinkedin; }

    public String getUserCompany() { return userCompany; }

    public String getFavouritesAsString()
    {
        //This method adds all the favourites to a string for storage in the db
        String favouritesString = "";
        for(int i =0; i < favourites.size(); i++)
        {
          favouritesString = favouritesString + favourites.get(i) + ",";
        }
        //to get rid of the trailing comma at the end
        favouritesString = favouritesString.substring(0, favouritesString.length() - 1);

        return favouritesString;
    }

    public ArrayList<Integer> getFavourites() {return favourites; }

    //setters
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserId(String userId) {this .userId = userId;}

    public void setFavourite(Integer favourite){
        //check not already added to favourites to avoid duplication
        if(this.favourites.contains(favourite))
        {
            //do nothing
        }
        else {
            this.favourites.add(favourite);
        }
    }

    public void setUserEmail(String userEmail) { this.userEmail = userEmail;}

    public void setUserLinkedin(String userLinkedin) { this.userLinkedin = userLinkedin;}

    public void setUserCompany(String userCompany) { this.userCompany = userCompany;}

    //Method to serialize user business card data into json and store in db
    public void sendUserBusinessCard(){
        Log.i("mobius","BRAP");
        //Create object mapper

        ObjectMapper mapper = new ObjectMapper();       //create user obj


        //convert the user object to a string
        try{
            String jsonInString = mapper.writeValueAsString(this);
            Log.i("mobius2",jsonInString);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        //write the string to the vendor database
        //where vendor is x input


    }


}
