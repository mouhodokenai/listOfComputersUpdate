package com.example.listofcomputers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.SimpleCursorAdapter;
import android.database.Cursor;

import java.util.ArrayList;

//Класс для доступа к БД
public class DataBaseAccessor extends SQLiteOpenHelper
{
    // Основные данные базы
    private static final String DATABASE_NAME = "listOfComputers.db";
    private static final int DB_VERSION = 3;

    // таблицы
    private static final String TABLE_COMPUTERS = "Computers";


    // столбцы таблицы Computers
    private static final String COLUMN_ID = "_id";//Обязательно с подчеркиванием
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_STATUS = "status";
    private static final String COLUMN_LOCATION = "location";
    private static final String COLUMN_LAST_ONLINE = "last_online";




    public DataBaseAccessor(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Создать таблицу
        db.execSQL("CREATE TABLE " + TABLE_COMPUTERS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_STATUS + " TEXT,"
                + COLUMN_LOCATION + " TEXT,"
                + COLUMN_LAST_ONLINE + " TEXT);");

        db.execSQL("INSERT INTO " + TABLE_COMPUTERS + "(" + COLUMN_NAME + ", " + COLUMN_STATUS + ", " + COLUMN_LOCATION + ", " + COLUMN_LAST_ONLINE + ") values('comp1','вкл', 'Пенза', '04.11.2023 17:52')");
        db.execSQL("INSERT INTO " + TABLE_COMPUTERS + "(" + COLUMN_NAME + ", " + COLUMN_STATUS + ", " + COLUMN_LOCATION + ", " + COLUMN_LAST_ONLINE + ") values('comp2','выкл', 'Пенза', '04.11.2023 15:37')");


    }

    @SuppressLint("Range")
    public ArrayList<Computer> getAllData() {
        ArrayList<Computer> dataItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_COMPUTERS,null);

        if (cursor.moveToFirst()) {
            do {
                Computer item = new Computer();
                item.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                item.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
                item.setStatus(cursor.getString(cursor.getColumnIndex(COLUMN_STATUS)));
                item.setLocation(cursor.getString(cursor.getColumnIndex(COLUMN_LOCATION)));
                item.setLastOnline(cursor.getString(cursor.getColumnIndex(COLUMN_LAST_ONLINE)));
                dataItems.add(item);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return dataItems;
    }


/*
    public SimpleCursorAdapter getCursorAdapter(Context context, int layout, int[] viewIds)
    {
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_COMPUTERS,null);

        String[] columns = new  String[] {COLUMN_NAME,COLUMN_STATUS};

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(context,layout,cursor,columns,viewIds,0);
        return  adapter;
    }

 */




    public void updateNote(int id, String name, String status, String location, String online) {
        // выполнить запрос на обновление БД
        getReadableDatabase().execSQL("UPDATE "+ TABLE_COMPUTERS
                + " SET "
                + COLUMN_NAME + "='" + name + "', "
                + COLUMN_LOCATION + "='" + location + "', "
                + COLUMN_LAST_ONLINE + "='" + online + "', "
                + COLUMN_STATUS + "='" + status + "'"
                + " WHERE "
                + COLUMN_ID + "=" + id);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try
        {
            //удалить старую таблицу
            db.execSQL("DROP TABLE " + TABLE_COMPUTERS);
        }
        catch (Exception exception)
        {

        }
        finally {
            //создать новую и заполнить ее
            onCreate(db);
        }
    }
}
