package com.example.grocerystore2.javaFiles;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

/**
 * The inventory class stores all the food objects as a hashmap that is read in from a txt file
 */
public class Inventory {

    private HashMap<Integer, Food> myInventory;

    public Inventory(){
        this.myInventory = new HashMap<>(myInventory);

    }

    public String getName(int id){
        return myInventory.get(id).getName();
    }

    public double getPrice(int id){
        return myInventory.get(id).getPrice();
    }

    private void readFile() throws FileNotFoundException {
        try {
            File myFile = new File("..\\..\\..\\sampledata\\FoodItems.txt");
            Scanner myScanner = new Scanner(myFile);
            while(myScanner.hasNextLine()){
                String[] myLine = myScanner.nextLine().split(",");
                myInventory.put(Integer.parseInt(myLine[2]), new Food(myLine[0], Double.parseDouble(myLine[1]), Integer.parseInt(myLine[2])));


            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("file not found");
        }


    }

}
