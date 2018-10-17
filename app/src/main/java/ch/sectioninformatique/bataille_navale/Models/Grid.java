package ch.sectioninformatique.bataille_navale.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ToRe on 01.09.2017.
 */

public class Grid implements Serializable {
    // Attributes
    /**
     * That Grid's 10x10 Array of Cases
     */
    private Case[][] CasesGrid;

    /**
     * The ships of the player
     */
    private List<Ship> ships = new ArrayList<Ship>();

    /**
     * The max length of the ArrayList
     */
    private int shipsMaxLength = 5;

    /**
     * That Gird's vertical and horizontal dimensions
     */
    private final int gridLength = 10;

    // Constructors
    public Grid(){
        this.CasesGrid = new Case[this.gridLength][this.gridLength];

        clear();
    }

    // Getters

    /**
     * Return the chosen Case
     * @param vertical Indicate the Vertical position of the chosen Case
     * @param horizontal Indicate the Horizontal position of the chosen Case
     * @return The chosen case
     */
    public Case getCase(int vertical,int horizontal){
        return this.CasesGrid[vertical][horizontal];
    }

    public List<Ship> getShips() {
        return ships;
    }

    // Methods

    /**
     * Try to see if it can add a Ship on the Grid
     * At the starting position indicated on the indicated orientation
     * If it can then add the Ship on the Grid according to the indicated position and orientation
     * Else throw a Exception
     *
     * @param ship The Ship who need to be placed
     * @param startCol The Column from where the Ship start to be placed
     * @param startRow The Row from where the Ship start to be placed
     * @throws Exception If the Ship went out of the Grid Or if the Orientation if impossible
     */
    public void addShip(Ship ship, int startCol, int startRow) /*throws Exception*/{
        char orientation = ship.getDefaultOrientation();
        int nbCases = ship.getNbCases();
        int testRow = startRow;
        int testCol = startCol;

        for(int i = 0; i < nbCases; i++){
            switch(orientation){
                case 'U':
                    testCol++;
                    break;
                case 'D':
                    testCol--;
                    break;
                case 'R':
                    testRow++;
                    break;
                case 'L':
                    testRow--;
                    break;
                default:
                    //throw new Exception("Orientation Incorrect 1");
                    throw new IllegalArgumentException("Orientation Incorrect 1");
            }
        }

        if(testRow < this.gridLength || testCol < this.gridLength){
            for(int i = 0; i < nbCases; i++) {
                switch (orientation) {
                    case 'U':
                        this.getCase(startCol, startRow).setShip(ship);
                        startCol++;
                        break;
                    case 'D':
                        this.getCase(startCol, startRow).setShip(ship);
                        startCol--;
                        break;
                    case 'R':
                        this.getCase(startCol, startRow).setShip(ship);
                        startRow++;
                        break;
                    case 'L':
                        this.getCase(startCol, startRow).setShip(ship);
                        startRow--;
                        break;
                    default:
                        //throw new Exception("Orientation Incorrect 2");
                        throw new IllegalArgumentException("Orientation Incorrect 2");
                }
            }
        }else{
            //throw new Exception("Out of grid");
            throw new ArrayIndexOutOfBoundsException("Out of grid");
        }
    }

    /**
     * Hit the chosen Case and return the Case's Ship
     * @param col The Column of the touched Case
     * @param row The Row of the touched Case
     * @return The Case's Ship, or null if the Case is empty
     */
    public Ship hitCase(int col,int row){
        return this.getCase(col, row).touchedCase();
    }

    /**
     * Remove the chosen Ship of the Grid
     * @param ship The chosen Ship to be removed of the Grid
     */
    public void removeShip(Ship ship){
        for(int i = 0; i < this.gridLength; i++){
            for(int j = 0; j < this.gridLength; j++){
                if(this.getCase(i, j).getShip().equals(ship)){
                    this.getCase(i, j).setShip(null);
                }
            }
        }
    }

    /**
     * Clear the entire content of the Grid
     * (Or just create if called from the Constructor)
     */
    public void clear(){
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

    /**
     * The max length of the ship
     */
    public int getShipsMaxLength() {
        return shipsMaxLength;
    }

    public void setShipsMaxLength(int shipsMaxLength) {
        this.shipsMaxLength = shipsMaxLength;
    }
}
