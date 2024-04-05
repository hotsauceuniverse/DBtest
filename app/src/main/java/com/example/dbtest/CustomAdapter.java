package com.example.dbtest;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private ArrayList<TestTodo> mTestTodo;
    private Context mContext;
    private DBHelper mDBHelper;

    // 생성자 만들기
    public CustomAdapter(ArrayList<TestTodo> mTestTodo, Context mContext) {
        this.mTestTodo = mTestTodo;
        this.mContext = mContext;
        mDBHelper = new DBHelper(mContext);
    }

    @NonNull
    @Override
    public CustomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View holder = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);

        return new ViewHolder(holder);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.ViewHolder holder, int position) {
        holder.tv_title.setText(mTestTodo.get(position).getTitle());
        holder.tv_content.setText(mTestTodo.get(position).getContent());
        holder.tv_writeDate.setText(mTestTodo.get(position).getWriteDate());
    }

    @Override
    public int getItemCount() {
        return mTestTodo.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_title;
        private TextView tv_content;
        private TextView tv_writeDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_title = itemView.findViewById(R.id.tv_title);
            tv_content = itemView.findViewById(R.id.tv_content);
            tv_writeDate = itemView.findViewById(R.id.tv_date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int curPos = getAdapterPosition();  // 현재 리스트 클릭한 아이템 위치
                    TestTodo testTodo = mTestTodo.get(curPos);

                    String[] strChoiceItem = {"수정하기", "삭제하기"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("원하는 작업을 선택하세요");
                    builder.setItems(strChoiceItem, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int position) {
                            if (position == 0) {
                                // 수정하기
                                Dialog dialog = new Dialog(mContext, android.R.style.Theme_Material_Light_Dialog);
                                dialog.setContentView(R.layout.dialog_edit);
                                EditText et_title = dialog.findViewById(R.id.et_title);
                                EditText et_content = dialog.findViewById(R.id.et_content);
                                Button btn_ok = dialog.findViewById(R.id.btn_ok);

                                // 기존내용 가져오기
                                et_title.setText(testTodo.getTitle());
                                et_content.setText(testTodo.getContent());
                                et_title.setSelection(et_title.getText().length());  // 커서 마지막 이동

                                btn_ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        // update database
                                        String title = et_title.getText().toString();
                                        String content = et_content.getText().toString();
                                        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                                        String beforeTime = testTodo.getWriteDate();

                                        mDBHelper.UpdateTodo(title, content, currentTime, beforeTime);

                                        // update UI
                                        testTodo.setTitle(title);
                                        testTodo.setContent(content);
                                        testTodo.setWriteDate(currentTime);

                                        notifyItemChanged(curPos, testTodo);
                                        dialog.dismiss();
                                        Toast.makeText(mContext, "목록 수정 완료", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                dialog.show();

                            } else if (position == 1) {
                                // delete table
                                String beforeTime = testTodo.getWriteDate();
                                mDBHelper.DeleteTodo(beforeTime);

                                // delete UI
                                mTestTodo.remove(curPos);
                                notifyItemRemoved(curPos);
                                Toast.makeText(mContext, "목록 제거 완료", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    builder.show();
                }
            });
        }
    }

    // 액티비티에서 호출되는 함수, 현재 어댑터에 새로운 게시글 아이템을 전달받아 추가하는 목적
    public void addItem(TestTodo _testItem) {
        mTestTodo.add(0, _testItem);
        notifyItemInserted(0);
    }
}
