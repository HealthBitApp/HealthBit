package com.ariel.healthbit;

import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class User
{
    private String userID;
    private String name;
    private String lname;
    private Details details;


    public User() {}
    public User(String userID,String name,String lname)
    {
        this.userID=userID;
        this.name=name;
        this.lname=lname;
    }


    public void setusertodb()
    {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("users");
        ref.child(this.userID).child("name").setValue(this.name);
        ref.child(this.userID).child("lastname").setValue(this.lname);
    }
}
