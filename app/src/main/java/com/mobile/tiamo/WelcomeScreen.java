package com.mobile.tiamo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.mobile.tiamo.fragments.DoneFragment;
import com.stephentuso.welcome.BasicPage;
import com.stephentuso.welcome.FragmentWelcomePage;
import com.stephentuso.welcome.ParallaxPage;
import com.stephentuso.welcome.WelcomeActivity;
import com.stephentuso.welcome.WelcomeConfiguration;

public class WelcomeScreen extends WelcomeActivity {


    @Override
    protected WelcomeConfiguration configuration() {
        return new WelcomeConfiguration.Builder(this)
                .defaultTitleTypefacePath("Montserrat-Bold.ttf")
                .defaultHeaderTypefacePath("Montserrat-Bold.ttf")
                .defaultBackgroundColor(R.color.red_background)
                .page(new BasicPage(R.drawable.ic_front_desk_white,
                        "Found stress with work-life",
                        "Let's Liberzy help you to manage your work-life balance")
                        .background(R.color.orange_background))

//                .page(new BasicPage(R.drawable.ic_thumb_up_white,
//                        "Simple to use",
//                        "Add a welcome screen to your app with only a few lines of code.")
//                        .background(R.color.red_background))
                .page(new ParallaxPage(R.layout.parallax_example,
                        "Easy parallax",
                        "Supply a layout and parallax effects will automatically be applied")
                        .lastParallaxFactor(2f)
                        .background(R.color.purple_background)
                )

//                .page(new BasicPage(R.drawable.ic_edit_white,
//                        "Customizable",
//                        "All elements of the welcome screen can be customized easily.")
//                        .background(R.color.blue_background)
//                )

                .page(new FragmentWelcomePage() {
                    @Override
                    protected Fragment fragment() {
                        return new DoneFragment();
                    }
                })
                .useCustomDoneButton(true)
                .canSkip(false)
                .swipeToDismiss(false)
                .exitAnimation(android.R.anim.fade_out)
                .build();
    }

    public static String welcomeKey() {
        return "WelcomeScreen";
    }
}
