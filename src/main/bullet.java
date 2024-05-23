package main;

public class bullet {
    boolean exists = false;
    int speed;
    int x;
    int y;
    public bullet(int x, int y, int speed, boolean exists) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.exists = exists;
    }
}
