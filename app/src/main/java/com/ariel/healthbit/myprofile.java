package com.ariel.healthbit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ariel.healthbit.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DecimalFormat;
import java.util.Random;

public class myprofile extends AppCompatActivity
{
    private double calculateBmi(double weight, double height) //calculate the BMI by weight&height
    {
        double bmiResult = weight / Math.pow(height/100, 2);
        return bmiResult;
    }
    Toolbar toolbar;
    TextView fullname,datebirth,height,email,bmi,weight;
    DatabaseReference refUser,refDetails;
    FirebaseAuth fb;
    private static final Random RANDOM = new Random();
    private LineGraphSeries<DataPoint> series;
    private int lastX = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myprofile);

        refUser= FirebaseDatabase.getInstance().getReference("users").child(fb.getInstance().getUid());
        refDetails= FirebaseDatabase.getInstance().getReference("users").child(fb.getInstance().getUid()).child("details");

        fullname=(TextView)findViewById(R.id.myprofile_fullname);
        datebirth=(TextView)findViewById(R.id.myprofile_date);
        email=(TextView)findViewById(R.id.myprofile_mail);
        height=(TextView)findViewById(R.id.myprofile_height);
        bmi=(TextView)findViewById(R.id.myprofile_BMI);
        weight=(TextView)findViewById(R.id.myprofile_weight);

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User post = dataSnapshot.getValue(User.class);
                fullname.setText("  Full Name: \n"+post.name+" "+post.lname);
                email.setText("  Email: \n"+post.email);
            }
            @Override
            public void onCancelled(DatabaseError databaseError)
            {
            }
        };
        refUser.addValueEventListener(postListener);

        ValueEventListener DetailsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Details det=dataSnapshot.getValue(Details.class);
                double h=det.height;
                double d =det.height;
                double w =det.weight;
                double BMIcalc=calculateBmi(w,h);
                DecimalFormat df2 = new DecimalFormat("#.##");
                bmi.setText(String.valueOf("my current \n BMI is \n \n" +df2.format(BMIcalc)));
                datebirth.setText("Birth Date: \n"+det.date);
                height.setText("Height: \n"+det.height);
                weight.setText("Weight: \n"+det.weight);
                GraphView graphview = (GraphView) findViewById(R.id.myprofile_graph);

                LineGraphSeries<DataPoint> bgseries= new LineGraphSeries<>(new DataPoint[]{

                        new DataPoint(1,3),
                        new DataPoint(2,0)


                });
                graphview.addSeries(bgseries);

                graphview.setBackgroundColor(getResources().getColor(R.color.button));
                graphview.getViewport().setScalable(true);
                graphview.getViewport().setScrollable(true);
                graphview.setTitle("Weight Tracker");
                graphview.setTitleColor(getResources().getColor(android.R.color.white));
                // legend
                bgseries.setTitle("My Weight");
                bgseries.setThickness(20);
                bgseries.setDrawBackground(true);
                bgseries.setBackgroundColor(R.color.button);

                graphview.getLegendRenderer().setVisible(true);
                graphview.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
            }
            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        };
        refDetails.addValueEventListener(DetailsListener);

        toolbar = (Toolbar) findViewById(R.id.toolbarMYPROFILE);
        setSupportActionBar(toolbar);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) //toolbar definition
    {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


}