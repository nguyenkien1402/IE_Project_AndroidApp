package com.mobile.tiamo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.mobile.tiamo.R;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

public class MovieDetailActivity extends AppCompatActivity {

    private KenBurnsView imgView;
    private TextView txtTitle, txtStory, txtDescription;
    private String content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        imgView = findViewById(R.id.movie_detail_image);
        txtTitle = findViewById(R.id.movie_detail_title);
        txtStory = findViewById(R.id.movie_detail_content);
        txtDescription = findViewById(R.id.movie_detail_other);
        String data = getIntent().getStringExtra("data");
        try{
            JSONObject movie = new JSONObject(data);
            Picasso.get().load(movie.getString("Poster")).into(imgView);
            txtTitle.setText(movie.getString("Title"));
            txtStory.setText(movie.getString("Plot"));

            content = movie.getString("Awards");
            content = content +"\n" + "Actors: "+movie.getString("Actors");
            content = content +"\n" + "Rating: "+movie.getString("imdbRating");
            content = content +"\n" + "Director: "+movie.getString("Director");

            txtDescription.setText(content);
        }catch (Exception e){

        }
    }
}
