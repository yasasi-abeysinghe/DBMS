package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

/**
 * Created by User on 11/20/2016.
 */

public class PersistentAccountDAO implements AccountDAO {
    private SQLiteDatabase db;


    public PersistentAccountDAO(SQLiteDatabase db) {
        this.db = db;
    }

    @Override
    public List<String> getAccountNumbersList() {
        final String TABLE_ACCOUNT = "Account";
        final String AccountNo = "Account_No";

        String query = "SELECT "+AccountNo+" FROM "+TABLE_ACCOUNT+" ";

        Cursor result = db.rawQuery(query, null);

        //Initialize AccountNoList which holds all the Account Numbers from Account Table
        List<String> AccountNoList = new ArrayList<>();

        //Move to First Account No in the table Account
        boolean isFirst = result.moveToFirst();
        if(isFirst){
            //Add all account No one by one to the Account List
            do{
                int columnIndex = result.getColumnIndex(AccountNo);
                AccountNoList.add(result.getString(columnIndex));
            }while (result.moveToNext());
        }
        //Return the list
        return AccountNoList;
    }

    @Override
    public List<Account> getAccountsList() {
        final String TABLE_ACCOUNT = "Account";

        int AccountNoIndex;
        int BankNameIndex;
        int AccountHolderIndex;
        int BalanceIndex;

        final String AccountNo = "Account_No";
        final String BankName = "Bank_Name";
        final String AccountHolderName = "Account_Holder_Name";
        final String Balance = "Balance";

        String AccountNoValue;
        String BankNameValue;
        String AccountHolderNameValue;
        double BalanceValue;

        String query = "SELECT * FROM "+TABLE_ACCOUNT+" ";
        Cursor result = db.rawQuery(query, null);

        //Initialize AccountList which hold all accounts in the Account table
        List<Account> AccountList = new ArrayList<>();

        boolean isFirst = result.moveToFirst();
        if(isFirst){
            do{
                AccountNoIndex = result.getColumnIndex(AccountNo);
                BankNameIndex = result.getColumnIndex(BankName);
                AccountHolderIndex = result.getColumnIndex(AccountHolderName);
                BalanceIndex = result.getColumnIndex(Balance);

                AccountNoValue = result.getString(AccountNoIndex);
                BankNameValue = result.getString(BankNameIndex);
                AccountHolderNameValue = result.getString(AccountHolderIndex);
                BalanceValue = result.getDouble(BalanceIndex);

                //create Account instance with relavent values
                Account account = new Account(AccountNoValue, BankNameValue, AccountHolderNameValue, BalanceValue);

                AccountList.add(account);

            }while (result.moveToNext());
        }
        return AccountList;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        final String TABLE_ACCOUNT = "Account";

        int AccountNoIndex;
        int BankNameIndex;
        int AccountHolderIndex;
        int BalanceIndex;

        final String AccountNo = "Account_No";
        final String BankName = "Bank_Name";
        final String AccountHolderName = "Account_Holder_Name";
        final String Balance = "Balance";

        String AccountNoValue;
        String BankNameValue;
        String AccountHolderNameValue;
        double BalanceValue;

        String query = "SELECT * FROM "+TABLE_ACCOUNT+" WHERE "+AccountNo+" = "+accountNo;
        Cursor result = db.rawQuery(query, null);


        Account account = null;
        boolean isFirst = result.moveToFirst();
        if(isFirst){
            do{
                AccountNoIndex = result.getColumnIndex(AccountNo);
                BankNameIndex = result.getColumnIndex(BankName);
                AccountHolderIndex = result.getColumnIndex(AccountHolderName);
                BalanceIndex = result.getColumnIndex(Balance);

                AccountNoValue = result.getString(AccountNoIndex);
                BankNameValue = result.getString(BankNameIndex);
                AccountHolderNameValue = result.getString(AccountHolderIndex);
                BalanceValue = result.getDouble(BalanceIndex);

                //create Account instance with relevant values
                account = new Account(AccountNoValue, BankNameValue, AccountHolderNameValue, BalanceValue);
            }while (result.moveToNext());
        }
        return account;
    }

    @Override
    public void addAccount(Account account) {
        final String TABLE_ACCOUNT = "Account";

        final String AccountNo = "Account_No";
        final String BankName = "Bank_Name";
        final String AccountHolderName = "Account_Holder_Name";
        final String Balance = "Balance";

        String INSERT_ACCOUNT = "INSERT INTO "+ TABLE_ACCOUNT + "( "
                +AccountNo+","+BankName+","+AccountHolderName+","+Balance
                +") VALUES (?,?,?,?)";

        SQLiteStatement sqLiteStatement = db.compileStatement(INSERT_ACCOUNT);

        //Bind the values
        sqLiteStatement.bindString(1, account.getAccountNo());
        sqLiteStatement.bindString(2, account.getBankName());
        sqLiteStatement.bindString(3, account.getAccountHolderName());
        sqLiteStatement.bindString(4, account.getAccountHolderName());

        //Execute it
        sqLiteStatement.executeInsert();
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        final String TABLE_ACCOUNT = "Account";
        final String AccountNo = "Account_No";

        String DELETE_ACCOUNT = "DELETE FROM "+TABLE_ACCOUNT+" WHERE "
                +AccountNo+" = ?";
        SQLiteStatement sqLiteStatement = db.compileStatement(DELETE_ACCOUNT);

        sqLiteStatement.bindString(1, accountNo);

        sqLiteStatement.executeUpdateDelete();

    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        final String TABLE_ACCOUNT = "Account";
        final String Balance = "Balance";

        String UPDATE_ACCOUNT = "UPDATE "+TABLE_ACCOUNT+" SET "
                +Balance+" = "+Balance+" + ?";
        SQLiteStatement sqLiteStatement = db.compileStatement(UPDATE_ACCOUNT);

        if(expenseType == ExpenseType.EXPENSE){
            sqLiteStatement.bindDouble(1,-amount);
        }
        else{
            sqLiteStatement.bindDouble(1, amount);
        }

        sqLiteStatement.executeUpdateDelete();


    }
}
