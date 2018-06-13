package com.ting.anchor;

import com.ting.bean.anchor.AnchorVO;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

/**
 * Created by liu on 2017/11/15.
 */

public class Comparents implements Comparator<AnchorVO>{

    @Override
    public int compare(AnchorVO o1, AnchorVO o2) {
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
//        Collator collator = Collator.getInstance();
//        return collator.getCollationKey(o1.getName()).compareTo(
//                collator.getCollationKey(o2.getName()));
    }
}
