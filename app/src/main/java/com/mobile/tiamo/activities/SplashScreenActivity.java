package com.mobile.tiamo.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mobile.tiamo.MainActivity;
import com.mobile.tiamo.R;
import com.mobile.tiamo.questionaires.QuestionnairesFirststart;
import com.mobile.tiamo.utilities.Messages;
import com.mobile.tiamo.utilities.SavingDataSharePreference;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        final Handler myHandler = new Handler();
        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                goToMainActivity();
            }
        },1000);

    }




    private void goToMainActivity() {
        if(SavingDataSharePreference.getDataInt(getApplicationContext(), Messages.LOCAL_DATA,Messages.FLAG_IS_ANSWER) == 1){
            Log.d("TAG","Go to the main: " + SavingDataSharePreference.getDataInt(getApplicationContext(), Messages.LOCAL_DATA,Messages.FLAG_IS_ANSWER));
            Intent i = new Intent(this,MainActivity.class);
            startActivity(i);
            finish();
        }else{
            Intent i = new Intent(this, QuestionnairesFirststart.class);
            startActivity(i);
            finish();
        }
    }
}
