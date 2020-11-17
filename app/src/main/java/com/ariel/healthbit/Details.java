package com.ariel.healthbit;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

public class Details
{
    private double height;
    private double weight;
    private Date datebirth;
    private String gender;

    public Details()
    {
    }
    public Details(double height,double weight, Date datebirth,String gender)
    {
        this.height=height;
        this.weight=weight;
        this.datebirth=datebirth;
        this.gender=gender;
    }
    public Details (Details d)
    {
        this.height=d.height;
        this.weight=d.weight;
        this.gender=d.gender;
        this.datebirth=d.datebirth;
    }

    public void setdetailstodb(String userID)
    {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("users");
        ref.child(userID).child("details").child("height").setValue(this.height);
        ref.child(userID).child("details").child("weight").push().setValue(this.weight);
        ref.child(userID).child("details").child("datebirth").child("day").setValue(datebirth.getDate());
        ref.child(userID).child("details").child("datebirth").child("month").setValue(datebirth.getMonth());
        ref.child(userID).child("details").child("datebirth").child("year").setValue(datebirth.getYear());
        ref.child(userID).child("details").child("gender").setValue(gender);
    }


}
