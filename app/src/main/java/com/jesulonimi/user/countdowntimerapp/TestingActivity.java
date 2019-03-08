package com.jesulonimi.user.countdowntimerapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class TestingActivity extends AppCompatActivity {
Button button;
    EditText editText;
    CardView textView;
    boolean isVisible=false;
    TextView quotedMessage;
    ImageView camera;
    ImageView attachment;
    ImageView mikeImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);
        button=findViewById(R.id.show_btn);
        editText=findViewById(R.id.et_text);
        textView=findViewById(R.id.quoted_text);
        camera=findViewById(R.id.camera);
        attachment=findViewById(R.id.attachment);
        mikeImage=findViewById(R.id.img_mic);
        quotedMessage=findViewById(R.id.quoted_messsage);
        button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(isVisible){
                            textView.setVisibility(View.GONE);
                            quotedMessage.setVisibility(View.GONE);
                            isVisible=false;
                        }else{
                        textView.setVisibility(View.VISIBLE);
                        quotedMessage.setVisibility(View.VISIBLE);
                        quotedMessage.setText("I will do it");
                        isVisible=true;
                        }
                    }
                }
        );
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if(s.length()>0){
                            mikeImage.setImageResource(R.drawable.ic_send_black_24dp);
                            camera.setVisibility(View.GONE);
                            attachment.setVisibility(View.GONE);
                        }else{
                            mikeImage.setImageResource(R.drawable.ic_mic_black_24dp);
                            camera.setVisibility(View.VISIBLE);
                            attachment.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

    }
}
