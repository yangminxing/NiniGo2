package com.ninigo2.ai;

import com.ninigo.aixml.XMLPhrase;

import java.util.List;

/**
 * Created by Yang on 2016/4/11.
 */
public class Evc {

    public Evc(int rank,String compare)
    {
        this.rank=rank;
        this.compare=compare;
    }

    private int rank;
    private String compare;

    public int getRank() {
        return rank;
    }

    public String getCompare() {
        return compare;
    }
}
