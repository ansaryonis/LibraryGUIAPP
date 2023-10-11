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
public class SilverConsumer extends CustomerStatus{
private String statustxt;

public SilverConsumer(){
    statustxt="Silver";

}

@Override
public String currentStatus(){
    return statustxt;
} // this override currentstatus used to be below changestatus, if it doesnt run u can try putting it back below

@Override
public void changeStatus(Customer c){
    c.setState(new GoldConsumer());
}

}
