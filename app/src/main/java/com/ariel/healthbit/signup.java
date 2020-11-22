package com.ariel.healthbit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.function.LongConsumer;
import java.util.regex.Pattern;

public class signup extends AppCompatActivity
{
    public static boolean namevalidity(String s) //name and lastname check validity
    {
        for(int i=0;i<s.length();i++)
        {
            if(! Pattern.matches(".*[a-zA-Z]+.*[a-zA-Z]", s))
            {
                return false;
          }
        }
        return true;
    }

    public static boolean emailvalidity(String email) //check email validity
    {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

    Toolbar toolbar;
    Button back,nextstep;
    TextView login;
    EditText name,lastname,email,password,confirmpass;
    FirebaseAuth ref=FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        toolbar = (Toolbar) findViewById(R.id.toolbarNextStep);
        setSupportActionBar(toolbar);

        back = (Button) findViewById(R.id.nextstep_back); //move to main activity
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view)
            {
                Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(myIntent);
            }

        });

        name=(EditText)findViewById(R.id.signup_firstname);
        lastname=(EditText)findViewById(R.id.signup_lastname);
        email=(EditText)findViewById(R.id.signup_email);
        password=(EditText)findViewById(R.id.signup_password);
        confirmpass=(EditText)findViewById(R.id.signup_repassword);
        nextstep= (Button) findViewById(R.id.signup_nextstep);

        if(ref.getCurrentUser()!=null) //set text at screen if the user already have an account
        {
            Toast.makeText(signup.this, "you already have account", Toast.LENGTH_SHORT).show();
        }

        nextstep.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view)
            {
                //converts the inputs to strings.
                String TextName=name.getText().toString().trim();
                String TextLName=lastname.getText().toString().trim();
                String TextEmail=email.getText().toString().trim();
                String Textpassword=password.getText().toString().trim();
                String TextConfpass=confirmpass.getText().toString().trim();

                //name checks
                if (TextUtils.isEmpty(TextName))
                {
                    name.setError("name is required!");
                    return;
                }
                if(TextName.length()==1||!(namevalidity(TextName)))
                {
                    name.setError("invalid name");
                    return;
                }
                //last name checks
                if (TextUtils.isEmpty(TextLName))
                {
                    lastname.setError("last name is required!");
                    return;
                }
                if(TextLName.length()==1||!(namevalidity(TextLName)))
                {
                    lastname.setError("invalid last name");
                    return;
                }
                //email checks
                if (TextUtils.isEmpty(TextEmail))
                {
                    email.setError("email is required!");
                    return;
                }
                if (!(emailvalidity(TextEmail))&&TextEmail.length()>0)
                {
                    email.setError("email is invalid!");
                    return;
                }
                //password checks
                if (TextUtils.isEmpty(Textpassword))
                {
                    password.setError("password is required!");
                    return;
                }
                if (Textpassword.length()<6)
                {
                    password.setError("password must be at least 6 characters");
                    return;
                }
                //confirm password checks
                if (TextUtils.isEmpty(TextConfpass))
                {
                    confirmpass.setError("confirm your password!");
                    return;
                }
                if (!(Textpassword.equals(TextConfpass)))
                {
                    confirmpass.setError("password and confirm password should match!");
                    return;
                }
                //connection to db and create new user
                ref.fetchSignInMethodsForEmail(TextEmail).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) //check if email already exist
                    {
//                        boolean check=!task.getResult().getSignInMethods().isEmpty();
                        boolean check=false;
                        if(check)
                        {
                            email.setError("email is already exist");
                            return;
                        }
                    }
                });

                ref.createUserWithEmailAndPassword(TextEmail, Textpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) //checks if the connection was successful
                    {
                        if (task.isSuccessful()) {
                            User u = new User(TextName, TextLName,ref.getCurrentUser().getEmail()); //create a User's object
                            DatabaseReference ref1= FirebaseDatabase.getInstance().getReference("users");
                            ref1.child(ref.getCurrentUser().getUid()).setValue(u);
                            Intent myIntent = new Intent(getApplicationContext(), signup_next.class); //move to main menu actiivity
                            startActivity(myIntent);
                        } else {
                            Toast.makeText(signup.this, "not work", Toast.LENGTH_SHORT).show();
                        }
                    }
                });



            }

        });

        login= (TextView) findViewById(R.id.signup_loginBtn); // move to login activity
        login.setPaintFlags(login.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG); //set under line

        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view)
            {
                Intent myIntent = new Intent(getApplicationContext(), signin.class);
                startActivity(myIntent);
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
}