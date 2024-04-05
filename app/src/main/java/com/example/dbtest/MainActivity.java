package com.example.dbtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rv_todo;
    private FloatingActionButton btn_write;
    private ArrayList<TestTodo> testTodo;
    private DBHelper mDBHelper;
    private CustomAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setInit();
    }

    private void setInit() {
        mDBHelper = new DBHelper(this);

        rv_todo = findViewById(R.id.rv_todo);
        btn_write = findViewById(R.id.btn_write);
        testTodo = new ArrayList<>();

        loadRecentDB();

        btn_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(MainActivity.this, android.R.style.Theme_Material_Light_Dialog);
                dialog.setContentView(R.layout.dialog_edit);
                EditText et_title = dialog.findViewById(R.id.et_title);
                EditText et_content = dialog.findViewById(R.id.et_content);
                Button btn_ok = dialog.findViewById(R.id.btn_ok);

                btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Insert Database
                        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                        mDBHelper.InsertTodo(et_title.getText().toString(), et_content.getText().toString(), currentTime);

                        // Insert UI
                        TestTodo item = new TestTodo();
                        item.setTitle(et_title.getText().toString());
                        item.setContent(et_content.getText().toString());
                        item.setWriteDate(currentTime);

                        mAdapter.addItem(item);

                        rv_todo.smoothScrollToPosition(0);
                        dialog.dismiss();
                        Toast.makeText(MainActivity.this, "목록 추가", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show();
            }
        });
    }

    private void loadRecentDB() {
        // 저장되어있던 DB를 가져옴
        testTodo = mDBHelper.getTestTodo();
        if (mAdapter == null) {
            mAdapter = new CustomAdapter(testTodo, this);
            rv_todo.setHasFixedSize(true);
            rv_todo.setAdapter(mAdapter);
        }
    }
}