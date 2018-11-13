package com.example.philip.demoappl.View;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.estimote.internal_plugins_api.cloud.proximity.ProximityAttachment;
import com.example.philip.demoappl.model.Vendor;

import com.example.philip.demoappl.R;
import com.example.philip.demoappl.model.Vendor;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class hideVendorView {

    public Vendor v;
    public Activity activity;

    //Constructor
    public hideVendorView(Activity _activity){
        this.activity = _activity;
    }

    public void Display(){

        TextView textVendorName = (TextView) this.activity.findViewById(R.id.textVendorName);
        TextView textLblVendorName = (TextView) this.activity.findViewById(R.id.textLblVendorName);
        TextView textVendorCategory = (TextView) this.activity.findViewById(R.id.textVendorCategory);
        TextView textLblVendorCategory = (TextView) this.activity.findViewById(R.id.textLblVendorCategory);
        TextView textVendorSubCategory = (TextView) this.activity.findViewById(R.id.textVendorSubCategory);
        TextView textLblVendorSubCategory = (TextView) this.activity.findViewById(R.id.textLblVendorSubCategory);
        TextView textVendorDescription = (TextView) this.activity.findViewById(R.id.textVendorDescription);
        TextView textLblVendorDescription = (TextView) this.activity.findViewById(R.id.textLblVendorDescription);
        ImageButton btnSendBusinessCard = (ImageButton) this.activity.findViewById(R.id.btnSendBusinessCard);
        ImageButton btnAddFavourite = (ImageButton) this.activity.findViewById(R.id.btnAddFavourite);
        RatingBar avgRatingBar = (RatingBar) this.activity.findViewById(R.id.avgRatingBar);
        RatingBar userRatingBar = (RatingBar) this.activity.findViewById(R.id.userRatingBar);
        TextView textuserRating = (TextView) this.activity.findViewById(R.id.textuserRating);
        TextView textAvgRating = (TextView) this.activity.findViewById(R.id.textAvgRating);

        //set visibility
        textVendorName.setVisibility(View.INVISIBLE);
        textLblVendorName.setVisibility(View.INVISIBLE);
        textVendorCategory.setVisibility(View.INVISIBLE);
        textLblVendorCategory.setVisibility(View.INVISIBLE);
        textVendorSubCategory.setVisibility(View.INVISIBLE);
        textLblVendorSubCategory.setVisibility(View.INVISIBLE);
        textVendorDescription.setVisibility(View.INVISIBLE);
        textLblVendorDescription.setVisibility(View.INVISIBLE);
        btnSendBusinessCard.setVisibility(View.INVISIBLE);
        btnAddFavourite.setVisibility(View.INVISIBLE);
        avgRatingBar.setVisibility(View.INVISIBLE);
        userRatingBar.setVisibility(View.INVISIBLE);
        textAvgRating.setVisibility(View.INVISIBLE);
        textuserRating.setVisibility(View.INVISIBLE);

}




public class VendorView {





        //Button btnSendBusinessCard = (Button) this.activity.findViewById(R.id.btnSendBusinessCard);
        //Button btnAddFavourite = (Button) this.activity.findViewById(R.id.btnAddFavourite);
    }
}
