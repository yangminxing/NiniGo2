package com.ninigo2.ai;

import com.ninigo2.Cross;
import com.ninigo2.Pan;

/**
 * Created by Yang on 2016/4/12.
 */
public interface IPlayer {
    Cross go(Pan p);

    public int CompareEvc(Cross cross,Evc evc);
}
