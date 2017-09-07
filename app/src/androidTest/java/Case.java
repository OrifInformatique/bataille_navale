/**
 * Created by ToRe on 01.09.2017.
 */

public class Case {
    // Attributes
    private boolean touched;
    private String ship;

    // Constructors
    public Case(){
        this.touched = false;
        this.ship = "None";
    }
    
    // Setters
    public void setTouched(boolean setHited){
        this.touched = setHited;
    }
    
    public void setShip(String setShip){
        this.ship = setShip;
    }
    
    // Getters
    public boolean getTouched(){
        return this.touched;
    }
    
    public String getShip(){
        return this.ship;
    }
}
