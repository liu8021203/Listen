package com.ting.anchor;

import com.ting.bean.anchor.AnchorVO;
import com.ting.bean.vo.HostVO;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

/**
 * Created by liu on 2017/11/15.
 */

public class Comparents implements Comparator<HostVO>{

    @Override
    public int compare(HostVO o1, HostVO o2) {
        Collator collator = Collator.getInstance();
        collator.setStrength(Collator.PRIMARY);
        int flags = 0;
        if(collator.compare(o1.getFirstStr().trim(), o2.getFirstStr().trim()) < 0){
            flags = -1;
        }else if(collator.compare(o1.getFirstStr().trim(), o2.getFirstStr().trim()) > 0){
            flags = 1;
        }else{
            flags = 0;
        }
        return flags;
    }
}
