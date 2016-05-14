package com.ninigo2;

/**
 * Created by Yang on 2016/4/12.
 */
public class Cross {
    private int x;
    private int y;
    private Character value;

    public Cross(int x,int y,Character c)
    {
        this.x=x;
        this.y=y;
        this.value=c;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public Character getValue() { return value;}
}
