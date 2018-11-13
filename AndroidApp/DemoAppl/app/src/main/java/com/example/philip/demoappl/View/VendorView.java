package com.example.philip.demoappl.View;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.internal_plugins_api.cloud.proximity.ProximityAttachment;
import com.example.philip.demoappl.MainActivity;
import com.example.philip.demoappl.model.Vendor;

import com.example.philip.demoappl.R;
import com.example.philip.demoappl.model.Vendor;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

import static com.example.philip.demoappl.MainActivity.currentUser;

public class VendorView {

    public Vendor v;
    public Activity activity;

    //Constructor
    public VendorView(Vendor vendor, Activity _activity){
        this.v  = vendor;
        this.activity = _activity;
    }

    public void Display(){

        //Declare the text fields and buttons
        TextView textVendorName = (TextView) this.activity.findViewById(R.id.textVendorName);
        TextView textLblVendorName = (TextView) this.activity.findViewById(R.id.textLblVendorName);
        TextView textVendorCategory = (TextView) this.activity.findViewById(R.id.textVendorCategory);
        TextView textLblVendorCategory = (TextView) this.activity.findViewById(R.id.textLblVendorCategory);
        TextView textVendorSubCategory = (TextView) this.activity.findViewById(R.id.textVendorSubCategory);
        TextView textLblVendorSubCategory = (TextView) this.activity.findViewById(R.id.textLblVendorSubCategory);
        TextView textVendorDescription = (TextView) this.activity.findViewById(R.id.textVendorDescription);
        TextView textLblVendorDescription = (TextView) this.activity.findViewById(R.id.textLblVendorDescription);
        //declare buttons and listeners
        ImageButton btnSendBusinessCard = (ImageButton) this.activity.findViewById(R.id.btnSendBusinessCard);
        btnSendBusinessCard.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                currentUser.sendUserBusinessCard();
                Toast.makeText(view.getContext(), "i've been clicked ", Toast.LENGTH_SHORT).show();
            }
        });


        ImageButton btnAddFavourite = (ImageButton) this.activity.findViewById(R.id.btnAddFavourite);
        btnAddFavourite.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                //Toast.makeText(view.getContext(), "Your have added "  + v.getVendorName() + " to your favourites" , Toast.LENGTH_SHORT).show();
                //Add the current vendor stand number to the current users list of favourites
                currentUser.setFavourite(v.getVendorStand());
                //v.getVendorStand()
                Toast.makeText(view.getContext(), "Your favourites are "  + currentUser.getFavouritesAsString(), Toast.LENGTH_SHORT).show();
                //currentUser.getFavouritesAsString();
            }
        });

        //Declare rating bars and associated text
        RatingBar avgRatingBar = (RatingBar) this.activity.findViewById(R.id.avgRatingBar);
        RatingBar userRatingBar = (RatingBar) this.activity.findViewById(R.id.userRatingBar);
        TextView textuserRating = (TextView) this.activity.findViewById(R.id.textuserRating);
        TextView textAvgRating = (TextView) this.activity.findViewById(R.id.textAvgRating);

        //populate the text fields with vendor data
        textVendorName.setText(v.getVendorName());
        textVendorCategory.setText(v.getVendorCategory());
        textVendorSubCategory.setText(v.getVendorSubCategory());
        textVendorDescription.setText(v.getVendorDescription());

        //make the text fields and buttons visible
        textVendorName.setVisibility(View.VISIBLE);
        textLblVendorName.setVisibility(View.VISIBLE);
        textVendorCategory.setVisibility(View.VISIBLE);
        textLblVendorCategory.setVisibility(View.VISIBLE);
        textVendorSubCategory.setVisibility(View.VISIBLE);
        textLblVendorSubCategory.setVisibility(View.VISIBLE);
        textVendorDescription.setVisibility(View.VISIBLE);
        textLblVendorDescription.setVisibility(View.VISIBLE);
        btnAddFavourite.setVisibility(View.VISIBLE);
        btnSendBusinessCard.setVisibility(View.VISIBLE);
        avgRatingBar.setVisibility(View.VISIBLE);
        userRatingBar.setVisibility(View.VISIBLE);
        textAvgRating.setVisibility(View.VISIBLE);
        textuserRating.setVisibility(View.VISIBLE);
    }
}
