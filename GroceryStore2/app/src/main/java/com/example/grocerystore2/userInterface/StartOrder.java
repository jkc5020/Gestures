package com.example.grocerystore2.userInterface;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.grocerystore2.R;

public class StartOrder extends AppCompatActivity {
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_order);
        button = (Button) findViewById(R.id.addItem);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity3();
            }
        });
    }
    public void openActivity3() {
        Intent intent = new Intent(this, AddItem.class);
        startActivity(intent);
    }
}