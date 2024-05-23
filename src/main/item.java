package main;

public class item{
    public boolean valid;
    public int itemcounter;
    public String name;
    public int type;
    public int x;
    public int y;
    public int index;

    public item(int index, String name,int itemcounter, int type, boolean valid, int x, int y) {
        this.index = index;
        this.name = name;
        this.itemcounter = itemcounter;
        this.type = type;
        this.valid = valid;
        this.x = x;
        this.y = y;
    }
}
