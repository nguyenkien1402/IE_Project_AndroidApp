package com.mobile.tiamo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.mobile.tiamo.R;
import com.mobile.tiamo.adapters.MovieAdapter;
import com.mobile.tiamo.adapters.MovieItem;
import com.mobile.tiamo.rest.services.MovieService;

import java.util.ArrayList;
import java.util.List;

/**
  Title say the function
  Just enter the name of the movie user wanna search
  Get the result back
 **/
public class SearchMoviesActivity extends AppCompatActivity {

    private ListView listMovie;
    private EditText edMovieTitle;
    private ImageView btnSearch;
    private List<MovieItem> movieItems;
    private MovieAdapter movieAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_movies);
        initComponent();

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edMovieTitle.getText() != null){
                    String title = edMovieTitle.getText().toString();
                    SearchMovieAsync searchMovieAsync = new SearchMovieAsync();
                    searchMovieAsync.execute(title);
                }else{
                    edMovieTitle.setError("Please Enter Title");
                }
            }
        });

        listMovie.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MovieItem item = movieItems.get(position);
                String imdbId = item.getImdbID();
                Log.d("Search",imdbId);
                Intent intent = new Intent(getApplicationContext(),MoodAndMovieTypeActivity.class);
                intent.putExtra("imdbId",imdbId);
                startActivity(intent);
            }
        });
    }


    /*
     Init main component of the activity
     */
    private void initComponent(){
        listMovie = findViewById(R.id.lv_movies);
        edMovieTitle = findViewById(R.id.search_input_movie);
        btnSearch = findViewById(R.id.btnSearchMovie);

    }

    /*
     Getting the movie based on the searching title
     */
    private class SearchMovieAsync extends AsyncTask<String, Void, List<MovieItem>>{
        @Override
        protected List<MovieItem> doInBackground(String... strings) {
            movieItems = MovieService.searchMovieByTitle(strings[0],1);
            return movieItems;
        }

        @Override
        protected void onPostExecute(List<MovieItem> movieItems) {
            super.onPostExecute(movieItems);
            movieAdapter = new MovieAdapter(movieItems,getApplicationContext());
            listMovie.setAdapter(movieAdapter);
            movieAdapter.notifyDataSetChanged();
        }
    }
}
