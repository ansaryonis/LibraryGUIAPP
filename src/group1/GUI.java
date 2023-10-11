/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package group1;
/**
 *
 * @authors 
 * Anjelo Gana |501085972
 * Rafid Karim |501107858
 * Ansar Yonis |501089549
 */

/****************************************************************************************/

//Import of Fx libraries for animations, scenes, buttons, etc for Interface
import java.io.IOException;
import javafx.application.Application;//launches program and fx libraries
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.Scene;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

//Program Commences
public class GUI extends Application {//CHANGE EXTENSION BASED ON FILENAMe
    //Instances for user and owner
    private Customer customer = null;
    private owner owner = null;
    
    //Instances for user usage and pricing of books
    private double totalPrice;
    private int loadDataCount = 0;

    //Comments below reference fx libraries
    @Override
    public void start(Stage initialStage) {
        //controls labels
        Label greetings = new Label("Welcome to the Online BookStore");
        Label idOfUser =new Label("User ID");
        Label passwrd = new Label("Password");
        
        //controls colouring of UI
        greetings.setStyle("-fx-text-fill: white; -fx-font-size: 20;");
        idOfUser.setStyle("-fx-text-fill: white; -fx-font-size: 18px;");
        passwrd.setStyle("-fx-text-fill: white; -fx-font-size: 18px;");

        //provides text fields
        TextField txt1=new TextField();  
        TextField txt2=new TextField();
        
        //Interactive button for signing in
        Button signIn = new Button("Sign In");
        signIn.setMaxWidth(125);

        //The outline of the UI
        GridPane layOut = new GridPane();
        layOut.addRow(0, greetings);
        layOut.addRow(1,idOfUser, txt1);  
        layOut.addRow(2, passwrd, txt2);  
        layOut.addRow(3, signIn);
        
        //layout of offset
        layOut.setPadding(new Insets(150, 25, 25,100));
        layOut.setHgap(25);
        layOut.setVgap(25);
        Scene scene=new Scene(layOut,750,600);
        layOut.setStyle("-fx-background-color: blue");
        initialStage.setScene(scene);
        initialStage.setResizable(false);
        initialStage.setTitle("Online Public Bookstore");  
        initialStage.show();
        
        //Layout of username and password input
        signIn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                String userName = txt1.getText();
                String password = txt2.getText();
                scan(userName, password, initialStage);
            }
        });
        close(initialStage);//allows program exit with saving information
    }
    
    @Override
    public void stop(){ //when 'X' is clicked, this method is called before termination
        System.out.println("Stage is closing");
        try { //filewriter throws exception
            owner.save(); //save the current customer and owner data
        }
        catch (IOException ex) {
            System.out.println("Error: Saving Data");
        }
        System.out.println("Saved Data");
    }
    
    public void scan(String name, String pass, Stage initialStage){
        System.out.println(name + " " + pass);
        if(loadDataCount ==0){  //only load data once.
            owner = owner.getOwnerDetails();  //only one owner instance and load data once
            try {
                owner.load();  //load customer and books previous data.
            } 
            catch (IOException ex) {
                ex.getStackTrace();
            }
            loadDataCount+=1;
        }
    if(name.equals("admin") && pass.equals("admin")){ //check is it owner?
            ownerStage(initialStage);   //owner main screen
            return;
    }
        else{ //check if its a cutomer, and get current customer instance
            customer = customerScan (name, pass, owner.getCustomers());
            if (customer == null){
                System.out.println("Invalid Username or Password");
                Alert invalidUser = new Alert(Alert.AlertType.ERROR);
                invalidUser.setContentText("Invalid username or password.");
                invalidUser.show();
                return;
            }
            else{
                System.out.println("Customer Username is: " + customer.getUsername());
                customerLayout(initialStage);
            }
        }
    }
    
    public Customer customerScan (String name, String pass, ObservableList<Customer> customers){
        //loops customerlist scanning for new inputted customers
        for(Customer c: customers ){
            if(c.getUsername().equals(name) && c.getPassword().equals(pass)){//if it matches return the customer
                return c;
            }
        }
        return null;//else return null
    }
    public void customerLayout(Stage initialStage){
        ObservableList<books> bookss = owner.getBooks();
        Label label = new Label ("Welcome " + customer.getUsername() + ". You have " + customer.getPoints() + " points. Your Status is " + customer.getStatus().currentStatus() + ".");
        label.setMaxWidth(Double.MAX_VALUE);
        AnchorPane.setLeftAnchor(label, 0.0);
        AnchorPane.setRightAnchor(label, 0.0);
        label.setAlignment(Pos.CENTER);
        
        //UI layout
        TableColumn<books, String> bookNameColumn = new TableColumn<>("Book Name");
        bookNameColumn.setMinWidth(266);
        bookNameColumn.setCellValueFactory(new PropertyValueFactory<>("book"));

        TableColumn<books, Double> bookPriceColumn = new TableColumn<>("Book Price");
        bookPriceColumn.setMinWidth(266);
        bookPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
       
        TableColumn<books, String> selectColumn = new TableColumn<>("Select");
        selectColumn.setCellValueFactory(new PropertyValueFactory<>("check"));
       
        TableView<books> table = new TableView<>();
        table.setItems(bookss);
        table.getColumns().addAll(bookNameColumn, bookPriceColumn, selectColumn);
       
        //Buy, Redeem, and logout button
        Button buy = new Button ("Buy");
        Button redeem = new Button("Redeem Points & Buy");
        Button logout = new Button ("Logout");
       
        //Grid layout initialization
        GridPane grid = new GridPane();
        grid.addRow(0, buy,redeem,logout);
        grid.setPadding(new Insets(10,10,10,10));  //outter padding
        grid.setHgap(10);  //padding between each element
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);
       
        //App Label
        VBox root = new VBox();
        root.getChildren().addAll(label,table,grid);
        Scene scene = new Scene(root);  
        initialStage.setScene(scene);
        initialStage.setTitle("BookStore");  
        initialStage.show();
   
        //Logout method
        logout.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                start(initialStage);
            }
        });
        //Buy method
        buy.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                totalPrice = 0;
                totalPrice = bookWithCash();
                purchaseLayout(initialStage, totalPrice);
            }
        });
       
        //Buy method
        redeem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                totalPrice = 0;
                totalPrice = bookWithPoint();
                purchaseLayout(initialStage, totalPrice);
            }
        });
        close(initialStage); //if 'X' is clicked, save data for reusage
    }
    //purchase with cash method
    public double bookWithCash(){
    double totalPrice = 0;      
    for(books b: owner.getBooks()){
        if(b.getCheck().isSelected()){
            totalPrice = totalPrice+b.getPrice();
        }
    }
    customer.BuyWithCash(totalPrice);
    return totalPrice;
    }
    //purchase with points method
    public double bookWithPoint(){
        double totalPrice = 0;      
        for(books b: owner.getBooks()){
            if(b.getCheck().isSelected()){
                totalPrice = totalPrice+b.getPrice();
            }
        }
        totalPrice = customer.BuyWithPoints(totalPrice);
        return totalPrice;
    }
    //buyscreen method
    public void purchaseLayout(Stage initialStage,double totalPrice){
       //Top Label
       Label label = new Label ("\n\n\nTotal Cost: " +totalPrice+ ". \nYou have " + customer.getPoints() + " points. \nYour Status is " + customer.getStatus().currentStatus() + ".");
       label.setMaxWidth(Double.MAX_VALUE);
       AnchorPane.setLeftAnchor(label, 0.0);
       AnchorPane.setRightAnchor(label, 0.0);
       label.setAlignment(Pos.CENTER);

       //Logout button
       Button logout = new Button ("Logout");

       GridPane grid = new GridPane();
       grid.setPadding(new Insets(10,10,10,10));  //outter padding
       grid.addRow(10,logout);
       grid.setHgap(10);  //padding between each element
       grid.setVgap(10);
       grid.setAlignment(Pos.CENTER);

       //UI title
       VBox root = new VBox();
       root.getChildren().addAll(label,grid);
       Scene scene = new Scene(root,300,300);
       initialStage.setScene(scene);
       initialStage.setTitle("BookStore");
       initialStage.show();

       //Logout method
       logout.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                start(initialStage);
            }
        });
        close(initialStage); //if 'X' is clicked in top right, save data before close
    }
    //buttons for other functions on UI
    public void ownerStage(Stage initialStage){
        Button books = new Button("Books");
        Button customer = new Button("Customer");
        Button logout = new Button("Logout");  
        GridPane root = new GridPane();  
        root.addRow(0, customer);  
        root.addRow(1, books);  
        root.addRow(2, logout);
        
        //dimensions of buttons
        books.setMaxWidth(200);
        customer.setMaxWidth(200);
        logout.setMaxWidth(200);
        
        //top right bottom left offset
        //center options
        root.setPadding(new Insets(200, 10, 10,325));
        root.setHgap(10); //hor and ver gap between each elements
        root.setVgap(25);
        Scene scene=new Scene(root,750,600);
        root.setStyle("-fx-background-color: blue");
        initialStage.setScene(scene);
        initialStage.setTitle("BookStore");  
        initialStage.show();
        
        customer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ownerCustomerScreen(initialStage); //add customer screen
            }
        });
        books.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                showBooksLayout(initialStage); //renders book screen
            }
        });
        logout.setOnAction(new EventHandler<ActionEvent>() { //logout
            public void handle(ActionEvent event) {
                start(initialStage); //main screen
            }
        });
        close(initialStage); //if 'X' is clicked in top right, save data before close
    }
    
    public void showBooksLayout(Stage initialStage){
        ObservableList<books> books = owner.getBooks(); //gets current books list
       
        //Name Column
        TableColumn<books, String> nameColumn = new TableColumn<>("Book Name");
        nameColumn.setMinWidth(350);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("book"));

        //Price Column
        TableColumn<books, Double> priceColumn = new TableColumn<>("Book Price");
        priceColumn.setMinWidth(150);
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

       //Input book name
        TextField inputName = new TextField();
        inputName.setPromptText("Name");
        inputName.setMinWidth(100);
        Label bookName = new Label("Name: ");
        bookName.setPadding(new Insets(5,0,0,0)); //padding inside the label
       
        //Input book price
        TextField inputPrice = new TextField();
        inputPrice.setPromptText("Price");
        inputPrice.setMinWidth(100);
        Label bookPrice = new Label("Price: ");
        bookPrice.setPadding(new Insets(5,0,0,0));
       
        Button addBookButton = new Button("Add");
        Button deleteBookButton = new Button("Delete");
        Button backButton = new Button("Back");

        HBox hBox1 = new HBox();
        hBox1.setPadding(new Insets(25, 25, 15, 25));
        hBox1.setSpacing(10);
        hBox1.getChildren().addAll(bookName, inputName,bookPrice, inputPrice, addBookButton);
       
        HBox hBox2 = new HBox();
        hBox2.setPadding(new Insets(0, 25, 25, 20));
        hBox2.setSpacing(10);
        hBox2.getChildren().addAll(deleteBookButton, backButton);
 
        TableView<books> booksTable = new TableView<>();
        booksTable.setItems(books);
        booksTable.getColumns().addAll(nameColumn, priceColumn);

        VBox vBox = new VBox();
        vBox.getChildren().addAll(booksTable, hBox1, hBox2);

        Scene scene = new Scene(vBox);
        initialStage.setScene(scene);
        initialStage.show();
        
        addBookButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String bookName = inputName.getText();
                String bookPrice = inputPrice.getText();
                try{
                    if(newBook(bookName, Double.parseDouble(bookPrice), books)){
                        books b = new books(Double.parseDouble(bookPrice), bookName);
                        booksTable.getItems().add(b);
                        return;
                    }
                    Alert duplicateBookAlert = new Alert(Alert.AlertType.INFORMATION);
                    duplicateBookAlert.setContentText("Invalid Input");
                    duplicateBookAlert.show();
                   
                }
                catch(NumberFormatException e){
                    Alert invalidInputAlert = new Alert(Alert.AlertType.ERROR);
                    invalidInputAlert.setContentText("Invalid Input.");
                    invalidInputAlert.show();  
                }
                catch (Exception e){
                    System.out.println(e);
                }
                finally {
                    inputName.clear();
                    inputPrice.clear();  
                }
            }
        });
        
        deleteBookButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ObservableList<books> allBooks, selectedBook;
                allBooks = booksTable.getItems();
                selectedBook = booksTable.getSelectionModel().getSelectedItems();
                selectedBook.forEach(allBooks::remove);
            }
        });
        
        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ownerStage(initialStage);  //go to ownerStage      
            }
        });
        close(initialStage);
    }
    
    public void ownerCustomerScreen(Stage initialStage){
        ObservableList<Customer> customers= owner.getCustomers(); //get current books list
       
        //username Col
        TableColumn<Customer, String> usernameColumn= new TableColumn<>("Username");//create username col
        usernameColumn.setMinWidth(266); //min col width
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username")); //(tie the data to col)get the values from obslist and all "username" var values
       
        //password col
        TableColumn<Customer, String> passwordColumn= new TableColumn<>("Password");//create password col
        passwordColumn.setMinWidth(266);//min col width
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));//get the values from obslist and all "password" var values
       
        //points col
        TableColumn<Customer, Double> pointsColumn= new TableColumn<>("Points"); //create points col
        pointsColumn.setMinWidth(266);//min col width
        pointsColumn.setCellValueFactory(new PropertyValueFactory<>("points"));//get the values from obslist and all "points" var values
       
        //table
        TableView<Customer> table = new TableView<>(); //create tableview obj
        table.setItems(customers); //set items to customers list
        table.getColumns().addAll(usernameColumn, passwordColumn, pointsColumn); //add the cols to the table
       
        //Username Textfield
        TextField addUsername = new TextField(); //password textfield
        addUsername.setPromptText("Username"); //placeholder text
        addUsername.setMinWidth(300); //min width
        Label usernameLabel = new Label("Username");
       
        //Password Textfield
        TextField addPassword = new TextField();
        addPassword.setPromptText("Password");
        addPassword.setMinWidth(300);
        Label passwordLabel = new Label("Password");
       
        //Add buttom
        Button add = new Button ("Add");
        //Delete button
        Button delete = new Button("Delete");
        //Back button
        Button back = new Button ("Back");
       
        GridPane grid = new GridPane();
        grid.addRow(0, usernameLabel,addUsername, passwordLabel,addPassword, add); //add all the fields and button in one row
        grid.addRow(1,delete,back); //delete and back button
        grid.setPadding(new Insets(10,10,10,10));  //outter padding
        grid.setHgap(10);  //hor and ver between each element
        grid.setVgap(10);
       
        //parent pane
        VBox root = new VBox();
        root.getChildren().addAll(table, grid); //add table and gridpane
       
        Scene scene = new Scene(root);  //scene
        initialStage.setScene(scene);  //main window scene
        initialStage.setTitle("BookStore");  //window title
        initialStage.show(); //show main window
        
        add.setOnAction(new EventHandler<ActionEvent>() { //add customer button
            @Override
            public void handle(ActionEvent event) {
                String name = addUsername.getText(); //entered username paramter
                String password = addPassword.getText(); //entered password parameter
                if(validateCustomerUsername(name,password,customers)){ //Check if customer with same exist or not, if not then add it
                    //create customer obj
                    Customer c = new Customer(name,password, 0); //create new customer onj
                    c.setState(new SilverConsumer());  //new customer is silver
                    table.getItems().add(c); //add new customer obj to the table
                    owner.setCustomers(table.getItems()); //set customerlist list to the current table list
                    addUsername.clear(); //clear addUsername textfield from user input
                    addPassword.clear(); //clear addPassword textfield from user input
                }else{ //error, customer username already exist
                    Alert a = new Alert(Alert.AlertType.ERROR); //alert box
                    a.setContentText("Invalid Input"); //alert box content(message)
                    a.show();  //show the alert box
                    addUsername.clear(); //clear addUsername textfield from user input
                    addPassword.clear(); //clear addPassword textfield from user input
                }
            }
        });
        
        delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ObservableList<Customer> allCustomers, selectedCustomer; //temp lists
                allCustomers = table.getItems(); //get all table item
                selectedCustomer = table.getSelectionModel().getSelectedItems(); //get the selected item(obj)
                selectedCustomer.forEach(allCustomers::remove); //delete the selected customer from the list
                owner.setCustomers(table.getItems()); //set customerlist list to the current table list
            }
        });
        
        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                owner.setCustomers(table.getItems()); //set customerlist list to the current table list
                //table.getItems().clear(); //clear table (dont do it, since i copy table to list, it will over ride the list (have to deep copy then)).
                ownerStage(initialStage);  //go to ownerStage
               
            }
        });
        close(initialStage); //if 'X' is clicked in top right, save data before close
    }
    
    public boolean newBook(String bookName, double bookPrice, ObservableList<books> books){
        if(bookName.equals("")){
            return false;
        }
        if(bookPrice <=0){
            return false;
        }
        for(books book: books){
            // && book.getBookPrice() == bookPrice
            if(book.getBook().equals(bookName)){
                return false;
            }
        }
        return true; //true if book does not exist already
    }
    public boolean validateCustomerUsername(String name,String password, ObservableList<Customer> cust){
        if(name.equals("") || password.equals("")){  //if empty field then false
            return false;
        }
        for(Customer c: cust){ //loop through all the customers list
            if(c.getUsername().equals(name)){ //if username already exist then false
                return false;
            }
        }
        return true; //true if username does not exist
    }
    
    public void close(Stage initialStage){
        initialStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(final WindowEvent event) {
                //System.out.println("Saved Data");
                initialStage.close(); //close main window
            }
        });
    }
        
    public static void main(String[] args) {
        launch(args);
    }
    
}
