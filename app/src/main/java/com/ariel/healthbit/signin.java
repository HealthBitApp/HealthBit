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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

public class signin extends AppCompatActivity
{
    public static boolean emailvalidity(String email) //check validity of an email
    {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

    //objects
   EditText email;
   EditText password;
   Toolbar toolbar;
   Button login;
   TextView signup,forgotpassword;
   ProgressBar prog;
   FirebaseAuth ref=FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        //initialize
        email=(EditText)findViewById(R.id.signin_email);
        password=(EditText)findViewById(R.id.signin_password);
        toolbar = (Toolbar) findViewById(R.id.toolbarSignIn);
        setSupportActionBar(toolbar);
        prog=(ProgressBar) findViewById(R.id.signin_prog);
        setSupportActionBar(toolbar);


        login = (Button) findViewById(R.id.signin_login); //login to the app with email and password
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view)
            {
                String TextEmail=email.getText().toString().trim();
                String Textpassword=password.getText().toString().trim();
                //email checks
                if (TextUtils.isEmpty(TextEmail)) //check if the input exist
                {
                    email.setError("email is required!");
                    return;
                }
                if (!(emailvalidity(TextEmail))&&TextEmail.length()>0) //check validity of the input
                {
                    email.setError("email is invalid!");
                    return;
                }
                //password checks
                if (TextUtils.isEmpty(Textpassword)) //check if the input exist
                {
                    password.setError("password is required!");
                    return;
                }
                if (Textpassword.length()<6) //check vaility of password
                {
                    password.setError("password must be at least 6 characters");
                    return;
                }
                prog.setVisibility(View.VISIBLE);
                //connection to db
                ref.signInWithEmailAndPassword(TextEmail,Textpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful()) //check if the connection was successful
                        {
                            prog.setVisibility(View.GONE);
                            Intent myIntent = new Intent(getApplicationContext(), MainProfile.class);
                            myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(myIntent);
                            finish();
                        }
                        else
                        {
                            //2 cases: emails isnt exist or uncorrect password .
                            ref.fetchSignInMethodsForEmail(TextEmail).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                                @Override
                                public void onComplete(@NonNull Task<SignInMethodQueryResult> task) //check if email already exist
                                {
                              boolean check=!task.getResult().getSignInMethods().isEmpty();
                                    if(check)
                                    {
                                        prog.setVisibility(View.GONE);
                                        password.setError("this password is incorrect ,please retype your current password.");
                                        return;
                                    }
                                    else
                                    {
                                        prog.setVisibility(View.GONE);
                                        email.setError("email not exist");
                                    }
                                }
                            });
                        }

                    }
                });

            }

        });

        signup= (TextView) findViewById(R.id.signin_signupBtn); //move to signup activity
        signup.setPaintFlags(signup.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG); //put under line
        signup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view)
            {
                Intent myIntent = new Intent(getApplicationContext(), signup.class);
                startActivity(myIntent);
            }

        });
        forgotpassword=(TextView)findViewById(R.id.signin_forgotpasswod);
        forgotpassword.setPaintFlags(forgotpassword.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG); //put under line
        forgotpassword.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view)
            {
                Intent myIntent = new Intent(getApplicationContext(), forgotpassword.class);
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
    @Override
    public void onBackPressed()
    {
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(myIntent);
        return;
    }
}