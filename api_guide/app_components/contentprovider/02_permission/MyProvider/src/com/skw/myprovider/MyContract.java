package com.skw.myprovider;

import android.provider.BaseColumns;

public final class MyContract {
    public MyContract() {}

    /**
     * BaseColumns类中有两个属性：_ID 和 _COUNT
     */
    public static abstract class Entry implements BaseColumns {
        public static final String TABLE_NAME = "mytable01";
        public static final String NAME       = "name";
        public static final String BIRTH_DAY  = "birthday";
        public static final String EMAIL      = "email";
        public static final String GENDER     = "gender";
    }
}
