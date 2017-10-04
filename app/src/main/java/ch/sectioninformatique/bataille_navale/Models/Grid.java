package ch.sectioninformatique.bataille_navale.Models;

/**
 * Created by ToRe on 01.09.2017.
 */

public class Grid {
    // Attributes
    /**
     * That Grid's 10x10 Array of Cases
     */
    private Case[][] CasesGrid;
    /**
     * That Grid's Player.
     * May not be required actually.
     */
    //private Player player;
    /**
     * That Gird's vertical and horizontal dimensions
     */
    private final int gridLength = 10;

    // Constructors
    public Grid(){
        this.CasesGrid = new Case[this.gridLength][this.gridLength];

        int i = 0, j = 0;
        for (Case horizontal[]: CasesGrid) {
            i = 0;
            for (Case cases: horizontal) {
                CasesGrid[i][j] = new Case();
                i++;
            }
            j++;
        }

//        for(int i = 0; i < gridLength; i++){
//            for (int j = 0; j < gridLength;j++){
//                CasesGrid[i][j] = new Case();
//            }
//        }
        //this.player = null;
    }

//    public Grid(Player setPlayer){
//        this.CasesGrid = new Case[this.gridLength][this.gridLength];
//        //this.player = setPlayer;
//    }

    // Setters
//    public void setPlayer(Player setPlayer){
//        this.player = setPlayer;
//    }

    // Getters
    public Case getCase(int vertical,int horizontal){
        return this.CasesGrid[vertical][horizontal];
    }

//    public Player getPlayer(){
//        return this.player;
//    }

}
