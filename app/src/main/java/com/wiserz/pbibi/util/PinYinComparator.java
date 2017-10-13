package com.wiserz.pbibi.util;

import com.wiserz.pbibi.bean.ContactsBean;

import java.util.Comparator;

/**
 * Created by jackie on 2017/10/13 14:13.
 * QQ : 971060378
 * Used as : xxx
 */
public class PinYinComparator implements Comparator<ContactsBean> {

    @Override
    public int compare(ContactsBean o1, ContactsBean o2) {
        return o1.getLetter().compareTo(o2.getLetter());
    }
}
