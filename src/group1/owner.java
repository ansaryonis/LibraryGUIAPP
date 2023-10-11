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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class owner {
    
    private String user;
    private String pw;
    private static owner inst = null;
    
    private ObservableList<Customer> cust; //or what ever Rafid names the customer file
    private ObservableList<books> bks;
    
    private owner(String u, String p){ //Owner Constructor, setting passwords & usernames
        user = u;
        pw = p;
        cust = FXCollections.observableArrayList(); //Initializing customers to be empty obeserable lists
        bks =  FXCollections.observableArrayList(); //Initializing books     to be empty obeserable lists
    }
    
    //Implementing a singleton design patter:
    public static owner getOwnerDetails(){
        if(inst == null){
            inst = new owner("admin", "admin");
        }
        return inst;
    }
    //Explanation:
    //The implementation ensures that only one instance (inst) exits out the application
    //This gives us to control and access to all the data from the program for customers and data,
    public ObservableList<Customer> getCustomers(){
        return cust;
    }
    public ObservableList<books> getBooks(){
        return bks;
    }
    
    public void setCustomers(ObservableList<Customer> cust){
        for (int i=0; i< cust.size(); i++){
            Customer c = cust.get(i);
            System.out.println(c.getUsername());
        }
        this.cust = cust;
    }
    
    public void save() throws IOException{
        File custtxt = new File("customers.txt");
        FileWriter custw = new FileWriter(custtxt);
        if (!custtxt.exists()){
            custtxt.createNewFile();
        }
        for (int i=0; i< cust.size(); i++){
            Customer c = cust.get(i);
            if(custtxt.exists()){
                custw.write(c.getUsername() + " "  + c.getPassword() + " " + c.getStatus().currentStatus() + " " + c.getPoints() + "\n");
            }
        }
        custw.close();

        custtxt = new File("books.txt");
        custw = new FileWriter(custtxt);
        if(!custtxt.exists()){
            custtxt.createNewFile();
        }
        for (int i=0; i< bks.size(); i++){
            books b = bks.get(i);
            if(custtxt.exists()){
                custw.write(b.getBook() + " " + "/" + " "  + b.getPrice() + "\n"); 
            }
        }
        custw.close();
    }
    
    public void load() throws IOException{
        File custtxt = new File("customers.txt");
        Scanner scan = new Scanner(custtxt);
        
        while(scan.hasNext()){
            String n = scan.next();
            String p = scan.next();
            String mem = scan.next(); //status of silver/gold
            double pts = Double.parseDouble(scan.next());
            
            Customer c = new Customer(n,p,pts);
            if(mem.equals("Silver")){
                c.setState(new SilverConsumer ());
            }
            else if(mem.equals("Gold")){
                c.setState(new GoldConsumer ());
            }
            cust.add(c);       
        }
        
        String temp;
        custtxt = new File("books.txt");
        scan = new Scanner(custtxt);
        while(scan.hasNext()){
            String n = scan.next();
            temp = scan.next();
            while(!temp.equals("/")){
                String temp2 = " ";
                n = n + temp2 + temp;
                temp = scan.next(); 
            }
            
            double p = Double.parseDouble(scan.next());
            System.out.println(n + " " + p);
            books b = new books(p,n);
            bks.add(b);            
        }
    }                
}
