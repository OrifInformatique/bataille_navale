/**
 * Created by ToRe on 01.09.2017.
 */

public class Player {
    // Attributes
    public String name;
    public String color;
    public Grid playerGrid;

    // Constructors
    Player(){
        this.name = "None";
        this.color = "None";
        this.playerGrid = new Grid();
    }

    Player(String Name,String Color){
        this.name = Name;
        this.color = Color;
        this.playerGrid = new Grid(this.name);
    }
}
