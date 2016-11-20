package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * Created by User on 11/20/2016.
 */

public class PersistentTransactionDAO implements TransactionDAO{
    private SQLiteDatabase db;

    public PersistentTransactionDAO(SQLiteDatabase db) {
        this.db = db;
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        final String TABLE_TRANSACTION = "TransactionDetails";

        final String AccountNo = "Account_No";
        final String Type = "Type";
        final String Amount = "Amount";
        final String Date = "Date";

        String INSERT_TRASACTION = "INSERT INTO "+ TABLE_TRANSACTION + "( "
                +AccountNo+","+Type+","+Amount+","+Date
                +") VALUES (?,?,?,?)";

        SQLiteStatement sqLiteStatement = db.compileStatement(INSERT_TRASACTION);

        //Bind the values
        sqLiteStatement.bindString(1, accountNo);
        sqLiteStatement.bindLong(2, (expenseType == ExpenseType.EXPENSE) ? 0 : 1 );
        sqLiteStatement.bindDouble(3, amount);
        sqLiteStatement.bindLong(4, date.getTime());

        //Execute it
        sqLiteStatement.executeInsert();
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        final String TABLE_TRANSACTION = "TransactionDetails";

        int AccountNoIndex;
        int TypeIndex;
        int AmountIndex;
        int DateIndex;

        final String AccountNo = "Account_No";
        final String Type = "Type";
        final String Amount = "Amount";
        final String date = "Date";

        String AccountNoValue;
        int TypeValue;
        Double AmountValue;
        Date dateValue;

        String query = "SELECT * FROM "+TABLE_TRANSACTION;
        Cursor result = db.rawQuery(query, null);

        List<Transaction> TransactionList = new ArrayList<>();

        boolean isFirst = result.moveToFirst();
        if(isFirst){
            do{
                AccountNoIndex = result.getColumnIndex(AccountNo);
                TypeIndex = result.getColumnIndex(Type);
                AmountIndex = result.getColumnIndex(Amount);
                DateIndex = result.getColumnIndex(date);

                AccountNoValue = result.getString(AccountNoIndex);
                TypeValue = result.getInt(TypeIndex);
                AmountValue = result.getDouble(AmountIndex);
                dateValue = new Date(result.getLong(DateIndex));

                //create Transaction instance with relevant values
                Transaction transaction = new Transaction(dateValue, AccountNoValue,(TypeValue==0 ? ExpenseType.EXPENSE : ExpenseType.INCOME), AmountValue);
                TransactionList.add(transaction);
            }while (result.moveToNext());
        }

        return TransactionList;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        final String TABLE_TRANSACTION = "TransactionDetails";

        int AccountNoIndex;
        int TypeIndex;
        int AmountIndex;
        int DateIndex;

        final String AccountNo = "Account_No";
        final String Type = "Type";
        final String Amount = "Amount";
        final String date = "Date";

        String AccountNoValue;
        int TypeValue;
        Double AmountValue;
        Date dateValue;

        String query = "SELECT * FROM "+TABLE_TRANSACTION+" LIMIT "+ limit;
        Cursor result = db.rawQuery(query, null);

        List<Transaction> TransactionList = new ArrayList<>();

        boolean isFirst = result.moveToFirst();
        if(isFirst){
            do{
                AccountNoIndex = result.getColumnIndex(AccountNo);
                TypeIndex = result.getColumnIndex(Type);
                AmountIndex = result.getColumnIndex(Amount);
                DateIndex = result.getColumnIndex(date);

                AccountNoValue = result.getString(AccountNoIndex);
                TypeValue = result.getInt(TypeIndex);
                AmountValue = result.getDouble(AmountIndex);
                dateValue = new Date(result.getLong(DateIndex));

                //create Transaction instance with relevant values
                Transaction transaction = new Transaction(dateValue, AccountNoValue,(TypeValue==0 ? ExpenseType.EXPENSE : ExpenseType.INCOME), AmountValue);
                TransactionList.add(transaction);
            }while (result.moveToNext());
        }

        return TransactionList;
    }
}
