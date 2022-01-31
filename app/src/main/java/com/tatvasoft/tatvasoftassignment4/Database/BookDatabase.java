package com.tatvasoft.tatvasoftassignment4.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.tatvasoft.tatvasoftassignment4.Utils.Constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class BookDatabase extends SQLiteOpenHelper {


    Context context;
    private static final String DB_NAME = "BookDatabase";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "Book";
    private final static String BOOK_ID = "BookId";
    private final static String BOOK_NAME = "BookName";
    private final static String AUTHOR_NAME = "AuthorName";
    private final static String BOOK_TYPE = "BookType";
    private final static String BOOK_GENRE = "BookGenre";
    private final static String LAUNCH_DATE = "LaunchDate";
    private final static String AGE_GROUP = "AgeGroup";

    SQLiteDatabase sqLiteDatabase;
    public BookDatabase(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String createQuery = " CREATE TABLE " + TABLE_NAME+ " ( "
                + BOOK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + BOOK_NAME + " TEXT, "
                + AUTHOR_NAME + " TEXT, "
                + BOOK_TYPE + " TEXT, "
                + BOOK_GENRE + " TEXT, "
                + LAUNCH_DATE + " TEXT, "
                + AGE_GROUP + " TEXT )";

        sqLiteDatabase.execSQL(createQuery);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void addBook(String bookName,
                        String authorName,
                        String bookType,
                        String bookGenre,
                        String launchDate,
                        String ageGroup){

        sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(BOOK_NAME,bookName);
        contentValues.put(AUTHOR_NAME,authorName);
        contentValues.put(BOOK_TYPE,bookType);
        contentValues.put(BOOK_GENRE,bookGenre);
        contentValues.put(LAUNCH_DATE,launchDate);
        contentValues.put(AGE_GROUP,ageGroup);


        sqLiteDatabase.insert(TABLE_NAME,null,contentValues);

        sqLiteDatabase.close();
    }

    public ArrayList<HashMap<String,String>> getBookList() {
        ArrayList<HashMap<String,String>> bookList = new ArrayList<>();

        // select all query
        String select_query= "SELECT *FROM " + TABLE_NAME;

        sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(select_query, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                HashMap<String,String> bookHashMap = new HashMap<>();
                bookHashMap.put(Constant.BOOK_ID,cursor.getString(0));
                bookHashMap.put(Constant.BOOK_NAME,cursor.getString(1));
                bookHashMap.put(Constant.AUTHOR_NAME,cursor.getString(2));
                bookHashMap.put(Constant.BOOK_TYPE,cursor.getString(3));
                bookHashMap.put(Constant.BOOK_GENRE,cursor.getString(4));
                bookHashMap.put(Constant.LAUNCH_DATE,cursor.getString(5));
                bookHashMap.put(Constant.AGE_GROUP,cursor.getString(6));
                bookList.add(bookHashMap);
            }while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return bookList;
    }
    public ArrayList<HashMap<String,String>> getBookDetailById(int bookId){

        SQLiteDatabase db = getReadableDatabase();
        ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();
        String query =" SELECT * FROM "+ TABLE_NAME +" WHERE BookId = '"+bookId+"'";

        Cursor cursor = db.rawQuery(query,null);
        if (cursor.moveToFirst()) {
            do {

                HashMap<String,String> bookHashMap = new HashMap<>();
                bookHashMap.put(Constant.BOOK_ID,cursor.getString(0));
                bookHashMap.put(Constant.BOOK_NAME,cursor.getString(1));
                bookHashMap.put(Constant.AUTHOR_NAME,cursor.getString(2));
                bookHashMap.put(Constant.BOOK_TYPE,cursor.getString(3));
                bookHashMap.put(Constant.BOOK_GENRE,cursor.getString(4));
                bookHashMap.put(Constant.LAUNCH_DATE,cursor.getString(5));
                bookHashMap.put(Constant.AGE_GROUP,cursor.getString(6));
                arrayList.add(bookHashMap);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return arrayList;
    }

    public void updateBookById(int bookId,
                               String bookName,
                               String authorName,
                               String bookType,
                               String bookGenre,
                               String launchDate,
                               String ageGroup){

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BOOK_NAME,bookName);
        contentValues.put(AUTHOR_NAME,authorName);
        contentValues.put(BOOK_TYPE,bookType);
        contentValues.put(BOOK_GENRE,bookGenre);
        contentValues.put(LAUNCH_DATE,launchDate);
        contentValues.put(AGE_GROUP,ageGroup);

        db.update(TABLE_NAME,contentValues,BOOK_ID + " = ?",new String[]{String.valueOf(bookId)});
        db.close();
    }

    public ArrayList<HashMap<String,String>> getBookByBookType(String type)
    {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();
        String query_selectByType =" SELECT * FROM "+ TABLE_NAME +" WHERE BookType = '"+type+"'";

        Cursor cursor = sqLiteDatabase.rawQuery(query_selectByType,null);
        if (cursor.moveToFirst()) {
            do {

                HashMap<String,String> bookHashMap = new HashMap<>();
                bookHashMap.put(Constant.BOOK_ID,cursor.getString(0));
                bookHashMap.put(Constant.BOOK_NAME,cursor.getString(1));
                bookHashMap.put(Constant.AUTHOR_NAME,cursor.getString(2));
                bookHashMap.put(Constant.BOOK_TYPE,cursor.getString(3));
                bookHashMap.put(Constant.BOOK_GENRE,cursor.getString(4));
                bookHashMap.put(Constant.LAUNCH_DATE,cursor.getString(5));
                bookHashMap.put(Constant.AGE_GROUP,cursor.getString(6));
                arrayList.add(bookHashMap);
            }while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();

        return arrayList;
    }

    public String[] getAuthorArray()
    {
        SQLiteDatabase db = getReadableDatabase();

        String query_author = "SELECT DISTINCT AuthorName FROM "+ TABLE_NAME;
        Cursor cursor = db.rawQuery(query_author,null);

        String[] author = new String[]{};
        ArrayList<String> list = new ArrayList<>(Arrays.asList(author));
        Log.d("cursor","cursor");
        if (cursor.moveToFirst()) {
            do {
                list.add(0,cursor.getString(0));

            }while (cursor.moveToNext());
        }

        author = list.toArray(author);

        cursor.close();
        db.close();
        return author;

    }

    public ArrayList<HashMap<String,String>> getBookByAuthor(ArrayList<String> checkedAuthors)
    {
        for (int i=0; i< checkedAuthors.size(); i++)
            Log.d("checkedAuthors",checkedAuthors.get(i));
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();
        String query_selectByAuthor =" SELECT * FROM "+ TABLE_NAME;

        String whereCondition = "";
        for(String item : checkedAuthors)
        {
            whereCondition = String.format("%sAuthorName = '%s' OR ", whereCondition, item);
        }

        if(checkedAuthors.size() > 0)
            query_selectByAuthor += String.format(" WHERE %s", whereCondition.substring(0, whereCondition.length() - 4));

        Cursor cursor = sqLiteDatabase.rawQuery(query_selectByAuthor,null);
        if (cursor.moveToFirst()) {
            do {

                HashMap<String,String> bookHashMap = new HashMap<>();
                bookHashMap.put("BookId",cursor.getString(0));
                bookHashMap.put("BookName",cursor.getString(1));
                bookHashMap.put("AuthorName",cursor.getString(2));
                bookHashMap.put("BookType",cursor.getString(3));
                bookHashMap.put("BookGenre",cursor.getString(4));
                bookHashMap.put("LaunchDate",cursor.getString(5));
                bookHashMap.put("AgeGroup",cursor.getString(6));
                arrayList.add(bookHashMap);
            }while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();

        return arrayList;
    }

    public String[] getGenreArray()
    {
        SQLiteDatabase db = getReadableDatabase();

        String query_genre = "SELECT DISTINCT BookGenre FROM "+ TABLE_NAME;
        Cursor cursor = db.rawQuery(query_genre,null);

        String[] genre = new String[]{};
        ArrayList<String> list = new ArrayList<>(Arrays.asList(genre));
        if (cursor.moveToFirst()) {
            do {
                list.add(0,cursor.getString(0));

            }while (cursor.moveToNext());
        }
        genre = list.toArray(genre);
        cursor.close();
        db.close();
        return genre;

    }

    public ArrayList<HashMap<String,String>> getBookByGenre(ArrayList<String> checkedGenres)
    {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();
        String query_selectByGenre =" SELECT * FROM "+ TABLE_NAME;

        String whereCondition = "";
        for(String item : checkedGenres)
        {
            whereCondition = String.format("%sBookGenre = '%s' OR ", whereCondition, item);
        }

        if(checkedGenres.size() > 0)
            query_selectByGenre += String.format(" WHERE %s", whereCondition.substring(0, whereCondition.length() - 4));

        Cursor cursor = sqLiteDatabase.rawQuery(query_selectByGenre,null);
        if (cursor.moveToFirst()) {
            do {

                HashMap<String,String> bookHashMap = new HashMap<>();
                bookHashMap.put(Constant.BOOK_ID,cursor.getString(0));
                bookHashMap.put(Constant.BOOK_NAME,cursor.getString(1));
                bookHashMap.put(Constant.AUTHOR_NAME,cursor.getString(2));
                bookHashMap.put(Constant.BOOK_TYPE,cursor.getString(3));
                bookHashMap.put(Constant.BOOK_GENRE,cursor.getString(4));
                bookHashMap.put(Constant.LAUNCH_DATE,cursor.getString(5));
                bookHashMap.put(Constant.AGE_GROUP,cursor.getString(6));
                arrayList.add(bookHashMap);
            }while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();

        return arrayList;
    }
}
