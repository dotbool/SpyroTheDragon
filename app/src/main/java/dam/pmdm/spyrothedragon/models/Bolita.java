package dam.pmdm.spyrothedragon.models;

public class Bolita {

    public Bolita(int cx, int cy, int limitX, int limitY) {
        this.cx = cx;
        this.cy = cy;
        this.limitX = limitX;
        this.limitY = limitY;
    }

    public int getCx() {
        return cx;
    }

    public void setCx(int cx) {
        if(cx > limitX || cx < 0){
            this.cx = (int) (Math.random() * limitX);
            this.cy = limitY;
        }
        else{
            this.cx = cx;
        }
    }

    public int getCy() {
        return cy;
    }

    public void setCy(int cy) {
        if(cy < 0 ){
            this.cy = limitY;
            this.cx = (int) (Math.random() * limitX);
        }
        else {
            this.cy = cy;
        }
    }

    public int getLimitX() {
        return limitX;
    }

    public void setLimitX(int limitX) {
        this.limitX = limitX;
    }

    public int getLimitY() {
        return limitY;
    }

    public void setLimitY(int limitY) {
        this.limitY = limitY;
    }

    int limitX;
    int limitY;
    int cx;
    int cy;
}
