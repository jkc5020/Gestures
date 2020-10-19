package com.example.diceroller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val rollButton: Button = findViewById(R.id.button)
        rollButton.setOnClickListener {
            rollDice()
        }
    }

    private fun rollDice() {
        val dice = Dice(6)
        val diceRoll = dice.roll()
        val diceImage: ImageView = findViewById(R.id.imageView)
        when(diceRoll){
            1 ->diceImage.setImageResource(R.drawable.capture1)
            2 ->diceImage.setImageResource(R.drawable.capture2)
            3 ->diceImage.setImageResource(R.drawable.capture3)
            4 ->diceImage.setImageResource(R.drawable.capture4)
            5 ->diceImage.setImageResource(R.drawable.capture5)
            6 ->diceImage.setImageResource(R.drawable.capture6)
        }
        diceImage.contentDescription = diceRoll.toString()
    }
}
class Dice(val numSides: Int)
{
    fun roll(): Int{
        return (1..numSides).random()

    }
}