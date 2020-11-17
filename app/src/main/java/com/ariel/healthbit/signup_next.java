package com.ariel.healthbit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;
import java.util.Date;

public class signup_next extends AppCompatActivity
{
    Toolbar toolbar;
    Button register,back;
    EditText height,weight;
    RadioButton gend,male;
    RadioGroup genderR;
    DatePicker birthdate;
    TextView birthdate_textview;
    FirebaseAuth ref;
     @Override
    protected void onCreate(Bundle savedInstanceState)
     {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_next);

        toolbar = (Toolbar) findViewById(R.id.toolbarNextStep);
        setSupportActionBar(toolbar);

        back = (Button) findViewById(R.id.nextstep_back);
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view)
            {
                Intent myIntent = new Intent(getApplicationContext(), signup.class);
                startActivity(myIntent);
            }

        });

         height=(EditText)findViewById(R.id.nextstep_height);
         weight=(EditText)findViewById(R.id.nextstep_weight);
         birthdate=(DatePicker)findViewById(R.id.nextstep_birthdate);
         genderR=(RadioGroup) findViewById(R.id.nextstep_radioGroup);
         birthdate_textview=(TextView)findViewById(R.id.nextstep_birth);

        register = (Button) findViewById(R.id.nextstep_register);
        register.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                int year,month,day; //birth date initialize
                year=birthdate.getYear();
                month=birthdate.getMonth()+1;
                day=birthdate.getDayOfMonth();
                //check if the date are makes sense.(the age of the user is realible)
                if (year<=Calendar.getInstance().get(Calendar.YEAR)&&year>=Calendar.getInstance().get(Calendar.YEAR)-5)
                {
                    birthdate_textview.setError("select birth date");
                    return;
                }
                //check validity of the height value.
                String Textheight=height.getText().toString();
                double h;
                if(Textheight.isEmpty())
                {
                   height.setError("height is required!");
                    return;
                }
                else
                {
                    h=Double.parseDouble(Textheight);
                    if(h<3)
                    {
                        height.setError("use centimeters");
                        return;
                    }
                    if(h<55||h>250)
                    {
                        height.setError("invalid value");
                        return;
                    }

                }
                //check validity of weight value
                String Textweight=weight.getText().toString();
                double w;
                if(Textweight.isEmpty())
                {
                    weight.setError("weight is required!");
                    return;
                }
                else
                {
                    w=Double.parseDouble(Textweight);
                    if(w<30||w>400)
                    {
                        weight.setError("invalid value");
                        return;
                    }
                }

                String gender=" ";
                int select=genderR.getCheckedRadioButtonId();
                gend=(RadioButton)findViewById(select);
                male= (RadioButton) findViewById(R.id.nextstep_male);

                if (gend==null)
                {
                    male.setError("select gender!");
                    return;
                }
                else
                {
                    gender=gend.getText().toString();
                }

                Details d=new Details(h,w,new Date(year,month,day),gender);
                d.setdetailstodb(ref.getInstance().getCurrentUser().getUid());
                Intent myIntent = new Intent(getApplicationContext(), MainProfile.class);
                startActivity(myIntent);

            }

        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) //toolbar definition
    {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
}