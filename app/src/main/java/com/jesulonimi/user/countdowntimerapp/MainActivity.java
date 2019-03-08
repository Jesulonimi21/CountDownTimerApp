package com.jesulonimi.user.countdowntimerapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
        public long time_milis;
        Button startPauseButton;
        Button resetButton;
        Button setButton;
        EditText timeEditText;
        TextView countDownTimerText;
        public  long time_left_in_millis;
        Boolean countDownTimerRunnng=false;
        CountDownTimer countDownTimer;
        private long endTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            setButton=findViewById(R.id.button_set);
            timeEditText=findViewById(R.id.set_edit_text);
        TelephonyManager telephony=(TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String countryCode=telephony.getSimCountryIso();

           String netWorkCode=  telephony.getNetworkCountryIso();
        Toast.makeText(this, countryCode, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, netWorkCode, Toast.LENGTH_SHORT).show();


//        String locale=MainActivity.this.getResources().getConfiguration().locale.getCountry();
//        Toast.makeText(this, locale, Toast.LENGTH_SHORT).show();
//        String localIso3=MainActivity.this.getResources().getConfiguration().locale.getISO3Country();
//        Toast.makeText(this, localIso3, Toast.LENGTH_SHORT).show();
//        String localeDisplayCountry=MainActivity.this.getResources().getConfiguration().locale.getDisplayCountry();
//        Toast.makeText(this, localeDisplayCountry, Toast.LENGTH_SHORT).show();

        startPauseButton=findViewById(R.id.start_timer);
        countDownTimerText=findViewById(R.id.text_timer);
        startPauseButton=findViewById(R.id.start_timer);
        resetButton=findViewById(R.id.reset_btn);

        startPauseButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(countDownTimerRunnng==true){
                            pauseTimer();
                        }else{
                            startTimer();
                        }
                    }
                }
        );

        resetButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       resetTimer();
                    }
                }
        );
        setButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
            String input=timeEditText.getText().toString();
            if(input.length()==0){
                Toast.makeText(MainActivity.this, "Field cannot be empty", Toast.LENGTH_SHORT).show();
            return;
            }

            long milisInput=Long.parseLong(input)*60000;

            if(milisInput==0){
                Toast.makeText(MainActivity.this, "Please enter a positive number", Toast.LENGTH_SHORT).show();
            return;
            }

            setTime(milisInput);
            timeEditText.setText("");
                    }
                }
        );

    }

    public void startTimer(){
        endTime=System.currentTimeMillis()+time_left_in_millis;
        countDownTimer=new CountDownTimer(time_left_in_millis,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                time_left_in_millis=millisUntilFinished;
                updateTextView();
            }

            @Override
            public void onFinish() {
            countDownTimerRunnng=false;
          upDateButtons();
            }
        }.start();
        countDownTimerRunnng=true;
      upDateButtons();
    }

    private void updateTextView(){
        int hour=(int)((time_left_in_millis)/1000)/3600;
        int minutes=(int)((time_left_in_millis/1000)%3600)/60;
        int seconds=(int)(time_left_in_millis/1000)%60;

        String timeLeftFormatted;
        if(hour>0){
            timeLeftFormatted=String.format(Locale.getDefault(),"%d:%02d:%02d",hour,minutes,seconds);
        }else{
            timeLeftFormatted=String.format(Locale.getDefault(),"%02d:%02d",minutes,seconds);
        }


        countDownTimerText.setText(timeLeftFormatted);
    }
    public void pauseTimer(){
        countDownTimer.cancel();
        countDownTimerRunnng=false;
        upDateButtons();
    }
    public void resetTimer(){
        time_left_in_millis=time_milis;
        updateTextView();
        countDownTimerRunnng=false;
       upDateButtons();
    }

    public void upDateButtons(){
        if(countDownTimerRunnng==true){
            timeEditText.setVisibility(View.INVISIBLE);
            setButton.setVisibility(View.INVISIBLE);
            startPauseButton.setText("pause");
            resetButton.setVisibility(View.INVISIBLE);
        }else{
            timeEditText.setVisibility(View.VISIBLE);
            setButton.setVisibility(View.VISIBLE);
            if(time_left_in_millis<1000){
                startPauseButton.setVisibility(View.INVISIBLE);
            }else{
                startPauseButton.setVisibility(View.VISIBLE);
                startPauseButton.setText("start");
            }

            if(time_left_in_millis<time_milis){
                resetButton.setVisibility(View.VISIBLE);
            }else{
                resetButton.setVisibility(View.INVISIBLE);
            }
        }
    }




    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences sharedPreferences=getSharedPreferences("prefs",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putLong("startTime",time_milis);
        editor.putLong("timeLeft",time_left_in_millis);
        editor.putBoolean("timerstatus",countDownTimerRunnng);
        editor.putLong("endTime",endTime);
        editor.apply();
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences=getSharedPreferences("prefs",MODE_PRIVATE);
        time_milis=sharedPreferences.getLong("startTime",60000);
        time_left_in_millis=sharedPreferences.getLong("timeLeft",time_milis);
        countDownTimerRunnng=sharedPreferences.getBoolean("timerstatus",false);
        updateTextView();
        upDateButtons();
        if(countDownTimerRunnng==true){
            endTime=sharedPreferences.getLong("endTime",time_milis);
            time_left_in_millis=endTime-System.currentTimeMillis();

            if(endTime<0){
                time_left_in_millis=0;
                countDownTimerRunnng=false;
                updateTextView();
                upDateButtons();
            }else{
                startTimer();
            }
        }
    }
    public void setTime(long timeInMiLLIS){
        time_milis=timeInMiLLIS;
        resetTimer();
        closeKeyboard();
    }
    private void closeKeyboard(){
        View view=this.getCurrentFocus();
        if(view!=null){
            InputMethodManager inputMethodManager=(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
        }

    }
        public void navigateToNext(View v){
        Intent i=new Intent(this,TestingActivity.class);
        startActivity(i);
        }
}
