package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentTransactionDAO;


/**
 * Created by User on 11/20/2016.
 */

public class PersistentExpenseManager extends ExpenseManager {
    private Context context;

    public PersistentExpenseManager(Context context){
        this.context = context;
        setup();
    }

    @Override
    public void setup() {

        // Database Name
        final String DATABASE_NAME = "140010T";

        //Open database (If database not existing create a new one)
        SQLiteDatabase db = context.openOrCreateDatabase(DATABASE_NAME, context.MODE_PRIVATE, null);

        // Account table name
        final String TABLE_ACCOUNT = "Account";

        // Account Table Columns names
        final String KEY_AccountNo = "Account_No";
        final String BankNAME = "Bank_Name";
        final String AccountHolderName = "Account_Holder_Name";
        final String Balance = "Balance";

        String CREATE_ACCOUNT_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_ACCOUNT + "("
                + KEY_AccountNo + " VARCHAR PRIMARY KEY," + BankNAME + " VARCHAR,"
                + AccountHolderName + " VARCHAR," + Balance + " REAL" + " ); ";

        //create Account table if not exists
        db.execSQL(CREATE_ACCOUNT_TABLE);

        // Transaction table name
        final String TABLE_TRANSACTION = "TransactionDetails";

        // Transaction table columns names
        final String KEY_TransactionID = "Transaction_ID";
        final String AccountNo = "Account_No";
        final String Type = "Type";
        final String Amount = "Amount";
        final String Date = "Date";

        String CREATE_TRANSACTION_TABLE = "CREATE TABLE IF NOT EXISTS "+TABLE_TRANSACTION + "("
                + KEY_TransactionID + "INTEGER PRIMARY KEY, "+ AccountNo +" VARCHAR, "
                + Type + " INT, "+Amount+" REAL, "+ Date + "DATE, "
                + "FOREIGN KEY ("+AccountNo+") REFERENCES "+TABLE_ACCOUNT+"("+AccountNo+")"
                + ");" ;

        //create Transaction table
        db.execSQL(CREATE_TRANSACTION_TABLE);

        PersistentAccountDAO accountDAO = new PersistentAccountDAO(db);

        setAccountsDAO(accountDAO);

        setTransactionsDAO(new PersistentTransactionDAO(db));

    }
}
