/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package group1;
/**
 *
 * @authors 
 * Anjelo Gana |501085972
 * Rafid Karim |501107858
 * Ansar Yonis |501089549
 */

//javafx libraries
import javafx.scene.control.CheckBox; //Imports CheckBox Class

public class books {
    private double price; //price of the book
    private CheckBox check; //what books being picked
    private String book; //the name of the book
    
    
    public books(double p, String b){ //Constructor
        this.check = new CheckBox();
        price = p;
        book = b;
    }
    
    public void setCheck(CheckBox c){ //setter method for whats checked
        check = c;
    }
    public CheckBox getCheck(){ //getter method for whats checked
        return check;
    }
    public void setPrice(double p){ //setter method for price
        price = p;
    }
    public double getPrice(){ //getter method for price
        return price;
    }
    
    public void setBook(String b){ //setter method for book
        book = b;
    }
    
    public String getBook(){ //getter method for book
        return book;
    }
}
