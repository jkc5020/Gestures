package com.example.grocerystore2.javaFiles;

public class Food {

    private String name;
    private double price;
    private int id;

    public Food(String name, double price, int id){
        this.name = name;
        this.price = price;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName(){
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String toString(){
        return "name: " + name + " price: $" + price + " id: " + id;
    }

}
