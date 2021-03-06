package com.duong.anyquestion;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.duong.anyquestion.classes.ConnectThread;
import com.duong.anyquestion.classes.Question;
import com.duong.anyquestion.classes.ToastNew;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

public class LoadingSearchExpertActivity extends AppCompatActivity {

    final Handler handler = new Handler();
    TextView tv_tittle, tv_money, tv_note;
    private Socket mSocket = ConnectThread.getInstance().getSocket();
    private Question question;

    private int count = 0;
    private Runnable finnish_time;
    private Emitter.Listener timkiemthabai = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    cancel_all();
                    ToastNew.showToast(LoadingSearchExpertActivity.this, args[0] + "", Toast.LENGTH_SHORT);
                    mSocket.emit("cancel-search-expert", "Chuyen gia tu choi");
                    finish();
                }

            });
        }
    };
    private Emitter.Listener callback_kqtkcg = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        ToastNew.showToast(LoadingSearchExpertActivity.this, "Đây nè", Toast.LENGTH_SHORT);

                        int conversation_id = (int) args[0];
                        Intent intent_nhantin = new Intent(LoadingSearchExpertActivity.this, MessageListActivity.class);

                        Bundle bundle = new Bundle();
                        bundle.putSerializable("question", question);
                        bundle.putInt("conversation_id", conversation_id);
                        intent_nhantin.putExtras(bundle);

                        cancel_all();
                        intent_nhantin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent_nhantin);
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastNew.showToast(LoadingSearchExpertActivity.this, "Lỗi gì đó", Toast.LENGTH_SHORT);
                    }
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_search_expert);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tv_tittle = findViewById(R.id.tv_tittle);
        tv_money = findViewById(R.id.tv_money);
        tv_note = findViewById(R.id.tv_note);


        if (getIntent().getExtras() != null) {
            question = (Question) getIntent().getExtras().getSerializable("question");
            tv_tittle.setText(question.getTittle());
            tv_money.setText(question.getMoney() + "");
            tv_note.setText(question.getDetailed_description().isEmpty() ? "Không có" : question.getDetailed_description());
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSocket.emit("user-search-expert", question.toJSON());
            }
        }, 5000);


        finnish_time = new Runnable() {
            @Override
            public void run() {
                ToastNew.showToast(LoadingSearchExpertActivity.this, "Hủy do quá lâu!", Toast.LENGTH_LONG);
                mSocket.emit("cancel-search-expert", "Huy tim kiem chuyen gia do qua thoi gian");
                cancel_all();
                finish();
            }
        };

        handler.postDelayed(finnish_time, 30000);

        mSocket.once("tim kiem chuyen gia that bai", timkiemthabai);
        mSocket.once("bat dau cuoc thao luan", callback_kqtkcg);

        mSocket.on("disconnect", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastNew.showToast(getApplication(), "Máy chủ ngắt kết nối!", Toast.LENGTH_LONG);
                        cancel_all();
                        finish();
                    }
                });
            }
        });

    }

    public void btn_cancel(View view) {
        mSocket.emit("cancel-search-expert", "Tu tay huy tim kiem chuyen gia");
        cancel_all();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mSocket.emit("cancel-search-expert", "Tu tay huy tim kiem chuyen gia");
                cancel_all();
                this.finish();
                return true;
            default:
                mSocket.emit("cancel-search-expert", "Tu tay huy tim kiem chuyen gia");
                cancel_all();
                this.finish();
                return super.onOptionsItemSelected(item);
        }
    }


    private void cancel_all() {
        mSocket.off("tim kiem chuyen gia that bai");
        mSocket.off("bat dau cuoc thao luan");
        handler.removeCallbacks(finnish_time);
    }
}
