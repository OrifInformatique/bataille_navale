package ch.sectioninformatique.bataille_navale.Models;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;

public class CustomButton extends AppCompatButton {

    private int gridX;
    private int gridY;

    public CustomButton(Context context) {
        super(context);
    }

    public int getGridX() {
        return gridX;
    }

    public void setGridX(int gridX) {
        this.gridX = gridX;
    }

    public int getGridY() {
        return gridY;
    }

    public void setGridY(int gridY) {
        this.gridY = gridY;
    }
}
