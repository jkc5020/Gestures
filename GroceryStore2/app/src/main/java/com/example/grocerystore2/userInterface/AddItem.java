package com.example.grocerystore2.userInterface;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.grocerystore2.R;

import java.io.FileNotFoundException;

public class AddItem extends AppCompatActivity {
    int id;
    EditText itemInput;
    Button submitButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        TextView confirmation = (TextView) findViewById(R.id.confirmationBox);
        try {
            Inventory inventory = new Inventory();
            itemInput = (EditText) findViewById(R.id.enterID);
            submitButton = (Button) findViewById(R.id.findItemButton);
            submitButton.setOnClickListener(new View.OnClickListener(){


                @Override
                public void onClick(View view) {
                    id = Integer.valueOf(itemInput.getText().toString());
                }
            });
            double itemPrice = inventory.retrievePrice(id);
            //if Item is not found variable is set to 0
            if(itemPrice!= 0){
                String idString = Double.toString(id);
                confirmation.setText(idString);
                Context context = getApplicationContext();
                CharSequence text = "Added";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
            }

        } catch (FileNotFoundException e) {
            confirmation.setText("Something went wrong");
            e.printStackTrace();
        }

    }
}

