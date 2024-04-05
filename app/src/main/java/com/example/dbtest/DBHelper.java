package com.example.dbtest;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "todo.db";

    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 데이터 베이스가 생성이 될 때 호출
        db.execSQL("CREATE TABLE IF NOT EXISTS TODOLIST_TEXT_SEQ (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT NOT NULL, content TEXT NOT NULL, writeDate TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    // SELECT (할일 목록을 조회)
    public ArrayList<TestTodo> getTestTodo() {
        ArrayList<TestTodo> testTodo = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM TODOLIST_TEXT_SEQ ORDER BY writeDate DESC", null);
        if (cursor.getCount() != 0) {
            // 조회한 데이터가 있을때 내부 수행
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String content = cursor.getString(cursor.getColumnIndexOrThrow("content"));
                String writeDate = cursor.getString(cursor.getColumnIndexOrThrow("writeDate"));

                TestTodo todo = new TestTodo();
                todo.setId(id);
                todo.setTitle(title);
                todo.setContent(content);
                todo.setWriteDate(writeDate);
                testTodo.add(todo);
            }
        }
        cursor.close();

        return testTodo;
    }

    // INSERT (할일 목록을 추가)
    public void InsertTodo(String _title, String _content, String _writeDate) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO TODOLIST_TEXT_SEQ (title, content, writeDate) VALUES('" + _title + "', '" + _content + "', '" + _writeDate + "');");
    }

    // UPDATE (할일 목록을 수정)
    public void UpdateTodo(String _title, String _content, String _writeDate, String _beforeDate) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE TODOLIST_TEXT_SEQ SET title = '" + _title + "', content = '" + _content + "', writeDate = '" + _writeDate + "' WHERE writeDate = '" + _beforeDate + "'");   // 기준값 id
    }

    // DELETE (할일 목록을 제거)
    public void DeleteTodo(String _beforeDate) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM TODOLIST_TEXT_SEQ WHERE writeDate = '" + _beforeDate  + "'");
    }

}