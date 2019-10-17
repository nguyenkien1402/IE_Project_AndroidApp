package com.mobile.tiamo.activities;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mobile.tiamo.R;

import java.util.Calendar;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setDescription("64% employees in the workplace indicated a poor work-life balance, decreased work efficiency and increased individual stress levels. \"Liberzy\" provides better balance between work and life by efficient time management.")
                .setImage(R.drawable.liberzy_logo4)
                .addItem(new Element().setTitle("Version 1.7"))
                .addGroup("Connect with us")
                .addEmail("nkad0002@student.monash.edu")
                .addWebsite("https://liberzy.bss.design/index.html")
                .addYoutube("UC2P7ZdPvmU2uE0vI0C6PsvA", "Watch our product video")
                .addGitHub("nguyenkien1402/IE_Project_AndroidApp")
                .create();

        setContentView(aboutPage);
    }

    Element getCopyRightsElement() {
        Element copyRightsElement = new Element();
        final String copyrights = String.format("Copyright", Calendar.getInstance().get(Calendar.YEAR));
        copyRightsElement.setTitle(copyrights);
        copyRightsElement.setIconDrawable(R.drawable.about_icon_email);
        copyRightsElement.setIconTint(mehdi.sakout.aboutpage.R.color.about_item_icon_color);
        copyRightsElement.setIconNightTint(android.R.color.white);
        copyRightsElement.setGravity(Gravity.CENTER);
        copyRightsElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), copyrights, Toast.LENGTH_SHORT).show();
            }
        });
        return copyRightsElement;
    }
}
