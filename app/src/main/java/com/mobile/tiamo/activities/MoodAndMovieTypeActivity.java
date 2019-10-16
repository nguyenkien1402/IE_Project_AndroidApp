package com.mobile.tiamo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 This activity to get the movie recommendation
 User selected type of the movie, choose their mood, mainly is already predictable
 And the get some recommendation movie
 **/
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
    private AutoCompleteTextView txtSearch;
    private int randomNumber = 0;
    private Random randomGenerator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_and_movie_type);
        randomGenerator = new Random();
        initComponent();

        GetAllMoviesAsync getAllMoviesAsync = new GetAllMoviesAsync();
        getAllMoviesAsync.execute();

        btnActionListener();
    }

    private class GetAllMoviesAsync extends AsyncTask<Void, Void, List<String>>{
        ProgressDialog pm;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pm = new ProgressDialog(MoodAndMovieTypeActivity.this);
            pm.setTitle("Loading");
            pm.show();
        }

        @Override
        protected List<String> doInBackground(Void... voids) {
            List<String> moviesTitle = new ArrayList<String>();
            try {
                JSONArray array = MovieService.getAllMovies();
                for (int i = 0; i < array.length(); i++) {
                    moviesTitle.add(array.getJSONObject(i).getString("title"));
                }
            }catch (Exception e){
                Log.d(TAG,"GetAllMoviesAsync JSON Parser");
            }
            return moviesTitle;
        }

        @Override
        protected void onPostExecute(List<String> result) {
            super.onPostExecute(result);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MoodAndMovieTypeActivity.this,android.R.layout.select_dialog_item, result);
            txtSearch.setThreshold(2);
            txtSearch.setAdapter(adapter);
            pm.dismiss();
        }
    }

    /**
     This is using to choose the mood baby
     **/
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
                    randomNumber = randomGenerator.nextInt(400)+200;
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
                    randomNumber = randomGenerator.nextInt(200)+1;
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
                    randomNumber = randomGenerator.nextInt(600)+400;
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
                if(txtSearch.getText().toString().trim().equals("")){
                    GetMovieWithoutMovieAsync getMovieWithoutMovieAsync = new GetMovieWithoutMovieAsync();
                    getMovieWithoutMovieAsync.execute();
                }else{
                    GetMovieRecommendationAsync getMovieRecommendationAsync = new GetMovieRecommendationAsync();
                    getMovieRecommendationAsync.execute();
                }

            }
        });
    }

    /**
     * This method is using to get the movie without movie similarity
     */
    private class GetMovieWithoutMovieAsync extends AsyncTask<Void, Void, JSONObject>{

        ProgressDialog pm;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pm = new ProgressDialog(MoodAndMovieTypeActivity.this);
            pm.setTitle("Getting Recommendation...");
            pm.show();
        }

        @Override
        protected JSONObject doInBackground(Void... voids) {
            movieType = "";
            for(String k : types){
                movieType = movieType + k+",";
            }
            JSONObject result = MovieService.getRecommendationMovieWithoutTitle(randomNumber, movieType);
            return result;
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
            }catch (Exception e){
                Log.d(TAG,e.toString());
                pm.dismiss();
            }
        }
    }
    /**
      This AsyncTask is using to get the movie from server
      The movie is suggest based on the selected
     */
    private class GetMovieRecommendationAsync extends AsyncTask<Void, Void, JSONObject>{

        ProgressDialog pm;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pm = new ProgressDialog(MoodAndMovieTypeActivity.this);
            pm.setTitle("Getting Recommendation...");
            pm.show();
        }


        @Override
        protected JSONObject doInBackground(Void... voids) {
            movieType = "";
            for(String k : types){
                movieType = movieType + k+",";
            }
            movieType = movieType.substring(0,movieType.length()-1);
            JSONObject result = MovieService.getRecommendationMovie(randomNumber,
                    txtSearch.getText().toString(),
//                            +" ("+movieYear.getText().toString().split(":")[1].trim()+")",
                    movieType);
            return result;
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
            }catch (Exception e){
//                Log.d(TAG,e.toString());
                pm.dismiss();

            }
        }
    }

    /**
       Initialize the component of the activity dump ass
     **/
    private void initComponent(){
        parentLayout = findViewById(android.R.id.content);
        imgHappy = findViewById(R.id.mood_type_happy);
        imgNeutral = findViewById(R.id.mood_type_neutral);
        imgSad = findViewById(R.id.mood_type_sad);
        chipGroup = findViewById(R.id.chip_movies);
        btn = findViewById(R.id.mood_type_btn);
        txtSearch = (AutoCompleteTextView) findViewById(R.id.search_input_movie);

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
