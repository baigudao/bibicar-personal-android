package acplibrary.components;

/**
 * Created by jackie on 2017/9/27 17:20.
 * QQ : 971060378
 * Used as : xxx
 */
public class PetalCoordinate {

    private int startX, startY, endX, endY;

    public PetalCoordinate(int startX, int startY, int endX, int endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int getEndX() {
        return endX;
    }

    public int getEndY() {
        return endY;
    }
}
