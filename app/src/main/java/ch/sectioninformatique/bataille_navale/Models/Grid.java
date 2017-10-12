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
    }

    // Getters
    public Case getCase(int vertical,int horizontal){
        return this.CasesGrid[vertical][horizontal];
    }
}
