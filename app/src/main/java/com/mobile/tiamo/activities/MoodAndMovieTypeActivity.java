package com.mobile.tiamo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;
import com.mobile.tiamo.R;
import com.mobile.tiamo.adapters.MovieItem;
import com.mobile.tiamo.rest.services.MovieService;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MoodAndMovieTypeActivity extends AppCompatActivity {

    private String TAG = "MoodAndMovieTypeActivity";
    private ImageView imgMovie;
    private TextView movieTitle, movieYear, movieImdb, moviePlot;
    private ImageView imgHappy, imgSad, imgNeutral;
    private ChipGroup chipGroup;
    private Button btn;
    private String imdbId;
    private String[] movieTypes = {"Drama","Comedy","Musical","Action","Adventure","Fantasy","Romance","Horror","War","Animation",
                    "Sci-Fi","Mystery","Thriller","Crime",};
    private String mood = "";
    private int numberOfType = 0;
    private int maximumType = 3;
    private View parentLayout;
    private List<String> types= new ArrayList<String>();
    private String movieType = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_and_movie_type);
        imdbId = getIntent().getStringExtra("imdbId");

        initComponent();

        GetRequestMovieInformation getRequestInformationAsync = new GetRequestMovieInformation();
        getRequestInformationAsync.execute();

        btnActionListener();


    }

    private void btnActionListener(){

        imgHappy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imgHappy.isSelected()){
                    mood = "Not Selected";
                    imgHappy.setSelected(false);
                    imgHappy.setColorFilter(null);
                }else{
                    imgHappy.setSelected(true);
                    mood = "Happy";
                    imgHappy.setColorFilter(R.color.background);
                }
                imgNeutral.setSelected(false);
                imgNeutral.setColorFilter(null);
                imgSad.setSelected(false);
                imgSad.setColorFilter(null);
            }
        });

        imgSad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imgSad.isSelected()){
                    mood = "Not Selected";
                    imgSad.setSelected(false);
                    imgSad.setColorFilter(null);
                }else{
                    imgSad.setSelected(true);
                    mood = "Sad";
                    imgSad.setColorFilter(R.color.background);
                }
                imgHappy.setSelected(false);
                imgHappy.setColorFilter(null);
                imgNeutral.setSelected(false);
                imgNeutral.setColorFilter(null);

            }
        });

        imgNeutral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imgNeutral.isSelected()){
                    mood = "Not Selected";
                    imgNeutral.setSelected(false);
                    imgNeutral.setColorFilter(null);
                }else{
                    imgNeutral.setSelected(true);
                    mood = "Neutral";
                    imgNeutral.setColorFilter(R.color.background);
                }
                imgHappy.setSelected(false);
                imgHappy.setColorFilter(null);
                imgSad.setSelected(false);
                imgSad.setColorFilter(null);
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetMovieRecommendationAsync getMovieRecommendationAsync = new GetMovieRecommendationAsync();
                getMovieRecommendationAsync.execute();
            }
        });
    }

    private class GetMovieRecommendationAsync extends AsyncTask<Void, Void, JSONObject>{

        ProgressDialog pm;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pm = new ProgressDialog(MoodAndMovieTypeActivity.this);
            pm.setTitle("Getting Recommendation..");
            pm.show();
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);
            Intent intent = new Intent(getApplication(),MovieRecommendationActivity.class);
            try {
                for (String type : types) {
                    intent.putExtra(type, result.getString(type));
                }
                intent.putExtra("top_all",result.getString("top_all"));
                intent.putExtra("typeName",movieType);
                pm.dismiss();
                startActivity(intent);
//
            }catch (Exception e){
                Log.d(TAG,e.toString());
                pm.dismiss();

            }
        }

        @Override
        protected JSONObject doInBackground(Void... voids) {
            for(String k : types){
                movieType = movieType + k+",";
            }
            movieType = movieType.substring(0,movieType.length()-1);
            JSONObject result = MovieService.getRecommendationMovie(1,
                    movieTitle.getText()+" ("+movieYear.getText().toString().split(":")[1].trim()+")",
                    movieType);
//            JSONObject result = MovieService.getRecommendationMovie(1,"Toy Story (1995)","Adventure,Fantasy");
            return result;
        }
    }

    private void initComponent(){
        parentLayout = findViewById(android.R.id.content);
        imgMovie = findViewById(R.id.mood_type_movie_image);
        movieTitle = findViewById(R.id.mood_type_movie_title);
        movieYear = findViewById(R.id.mood_type_movie_year);
        moviePlot = findViewById(R.id.mood_type_movie_plot);
        movieImdb = findViewById(R.id.mood_type_movie_imdb);
        imgHappy = findViewById(R.id.mood_type_happy);
        imgNeutral = findViewById(R.id.mood_type_neutral);
        imgSad = findViewById(R.id.mood_type_sad);
        chipGroup = findViewById(R.id.chip_movies);
        btn = findViewById(R.id.mood_type_btn);

        LayoutInflater inflater = LayoutInflater.from(this);
        for(int i = 0 ; i < movieTypes.length ; i++){
            Chip chip = (Chip) inflater.inflate(R.layout.item_chip,chipGroup,false);
            chip.setText(movieTypes[i]);
            chipGroup.addView(chip);
        }

        for(int i = 0 ; i < chipGroup.getChildCount() ; i++){
            final Chip c = (Chip) chipGroup.getChildAt(i);
            c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        if(numberOfType >= maximumType){
                            Snackbar snackbar = Snackbar.make(parentLayout, "Select 3 movie types only", Snackbar.LENGTH_LONG);
                            snackbar.show();
                            c.setChecked(false);
                        }else{
                            numberOfType += 1;
                            types.add(c.getText().toString());
                        }
                    }else{
                        numberOfType -=1;
                        types.remove(c.getText().toString());
                    }
                }
            });
        }
    }

    private class GetRequestMovieInformation extends AsyncTask<Void, Void, JSONObject>{

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MoodAndMovieTypeActivity.this);
            progressDialog.setTitle("Loading");
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(Void... voids) {
            return MovieService.getMovieById(imdbId);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            try {
                movieTitle.setText(jsonObject.getString("Title"));
                movieYear.setText("Year:" +jsonObject.getString("Year"));
                moviePlot.setText(jsonObject.getString("Plot"));
                movieImdb.setText("IMDB Rating: "+jsonObject.getString("imdbRating"));
                Picasso.get().load(jsonObject.getString("Poster")).into(imgMovie);
            }catch (Exception e){
                Log.d("MoodAndType",e.getMessage());
            }
            progressDialog.dismiss();
        }
    }
}