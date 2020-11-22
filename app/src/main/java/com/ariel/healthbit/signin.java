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

import com.ariel.healthbit.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class signin extends AppCompatActivity
{
    public static boolean emailvalidity(String email) //check validity of an email
    {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }
   EditText email;
   EditText password;
   Toolbar toolbar;
   Button back,login;
   TextView signup;
   FirebaseAuth ref=FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        email=(EditText)findViewById(R.id.signin_email);
        password=(EditText)findViewById(R.id.signin_password);

        toolbar = (Toolbar) findViewById(R.id.toolbarSignIn);
        setSupportActionBar(toolbar);

        back = (Button) findViewById(R.id.backSignIn); //back to the activity main
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view)
            {
                Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(myIntent);
            }

        });

        login = (Button) findViewById(R.id.login); //login to the app with email and password
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

                //connection to db
                ref.signInWithEmailAndPassword(TextEmail,Textpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful()) //check if the connection was successful
                        {
                            Toast.makeText(signin.this, "hello", Toast.LENGTH_SHORT).show();
                            Intent myIntent = new Intent(getApplicationContext(), MainProfile.class);
                            startActivity(myIntent);
                        }
                        else
                        {
                            Toast.makeText(signin.this, "not work", Toast.LENGTH_SHORT).show();
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) //toolbar definition
    {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
}