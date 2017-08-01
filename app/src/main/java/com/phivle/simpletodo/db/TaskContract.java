package com.phivle.simpletodo.db;

import android.provider.BaseColumns;

/**
 * Created by Vsin on 7/25/17.
 */

public class TaskContract {
    public static final String DB_NAME = "com.phivle.simpletodo.db";
    public static final int DB_VERSION = 2;

    public class TaskEntry implements BaseColumns{
        public static final String TABLE = "tasks";

        public static final String COL_TASK_TEXT = "text";
    }
}
