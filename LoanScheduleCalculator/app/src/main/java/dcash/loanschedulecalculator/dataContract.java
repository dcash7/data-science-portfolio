package dcash.loanschedulecalculator;

import android.provider.BaseColumns;

/**
 * Created by student on 1/30/2018.
 */

public final class dataContract {

    private dataContract() {}

    //Contract defines the column names and table name
    public static class DataEntry implements BaseColumns {

        public static final String TABLE_NAME = "data";

        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_Pay = "payment";
        public static final String COLUMN_Int = "interest";
        public static final String COLUMN_Prin = "principal";
        public static final String COLUMN_Bal = "balance";

    }
}
