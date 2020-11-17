package com.ariel.healthbit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.ariel.healthbit.R;

public class store extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarSTORE);
        setSupportActionBar(toolbar);
        Button back = (Button) findViewById(R.id.backStore);
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(getApplicationContext(), MainProfile.class);
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