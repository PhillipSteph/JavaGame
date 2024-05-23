package main;

public class enemy {
    double size;
    double x;
    double y;
    int life;
    double speed;
    boolean shown;
    public enemy(int size, int x, int y, int life, double speed, boolean shown){
        this.size = size;
        this.x = x;
        this.y = y;
        this.life = life;
        this.speed = speed;
        this.shown = shown;
    }
}
