package com.mobile.tiamo;

import androidx.fragment.app.Fragment;

import com.mobile.tiamo.fragments.DoneFragment;
import com.stephentuso.welcome.FragmentWelcomePage;
import com.stephentuso.welcome.WelcomeActivity;
import com.stephentuso.welcome.WelcomeConfiguration;

public class WelcomeScreen extends WelcomeActivity {


    @Override
    protected WelcomeConfiguration configuration() {
        return new WelcomeConfiguration.Builder(this)
                .defaultTitleTypefacePath("Montserrat-Bold.ttf")
                .defaultHeaderTypefacePath("Montserrat-Bold.ttf")
                .defaultBackgroundColor(R.color.orange_background)
                /*.page(new BasicPage(R.drawable.ic_front_desk_white,
                        "Found stress with work-life",
                        "Let's Liberzy help you to manage your work-life balance")
                        .background(R.color.orange_background))*/

//

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
