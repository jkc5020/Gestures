package com.example.grocerystore2.userInterface;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.grocerystore2.R;

public class MainActivity extends AppCompatActivity {
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.NewOrderButton);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                openActivity2();
            }
        });

    }
    public void openActivity2(){
        Intent intent = new Intent(this, StartOrder.class);
        startActivity(intent);
    }
}