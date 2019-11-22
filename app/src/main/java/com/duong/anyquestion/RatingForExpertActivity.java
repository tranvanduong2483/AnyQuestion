package com.duong.anyquestion;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.duong.anyquestion.classes.ToastNew;

public class RatingForExpertActivity extends AppCompatActivity {
    RatingBar mRatingBar;
    TextView mRatingScale;
    Button mSendFeedback;
    EditText edt_feedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_for_expert);


        mRatingScale = findViewById(R.id.tvRatingScale);
        mRatingBar = findViewById(R.id.rb_danhgia);
        mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                mRatingScale.setText(String.valueOf(v));
                switch ((int) ratingBar.getRating()) {
                    case 1:
                        mRatingScale.setText("Quá tệ");
                        break;
                    case 2:
                        mRatingScale.setText("Cần được cải thiện hơn");
                        break;
                    case 3:
                        mRatingScale.setText("Bình thường");
                        break;
                    case 4:
                        mRatingScale.setText("Khá tốt");
                        break;
                    case 5:
                        mRatingScale.setText("Quá tuyệt vời");
                        break;
                    default:
                        mRatingScale.setText("");
                }
            }
        });


        edt_feedback = findViewById(R.id.edt_feedback);

        mSendFeedback = findViewById(R.id.btnSubmit);
        mSendFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edt_feedback.getText().toString().isEmpty())
                    ToastNew.showToast(RatingForExpertActivity.this, "Vui lòng điền vào cảm nhận của bạn để phản hồi!", Toast.LENGTH_SHORT);
                else
                    ToastNew.showToast(RatingForExpertActivity.this, "Cảm ơn bạn đã phản hồi!", Toast.LENGTH_LONG);
                finish();
            }
        });


    }


}
