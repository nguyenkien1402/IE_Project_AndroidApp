package com.mobile.tiamo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.tiamo.R;
import com.mobile.tiamo.adapters.MovieHorizontalAdapter;
import com.mobile.tiamo.adapters.MovieItem;
import com.mobile.tiamo.rest.services.MovieService;
import com.mobile.tiamo.utilities.HorizontalListView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
  This activity show the movie recommendation getting
  from the MoodAndMovieTypeActivity
 **/
public class MovieRecommendationActivity extends AppCompatActivity {

    private String TAG = "MovieRecommendationActivity";
    List<MovieItem> movieItems;
    private HorizontalListView mImageTop, mTopOne,mTopTwo,mTopThree;
    private MovieHorizontalAdapter topAdapter, topOneAdapter, topTwoAdapter, topThreeAdapter;
    private List<MovieItem> listTop, listOne, listTwo, listThree;
    private TextView txtTopOne, txtTopTwo, txtTopThree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_recommendation);

        mImageTop = (HorizontalListView) findViewById(R.id.recommendation_movie_top);
        mTopOne = (HorizontalListView) findViewById(R.id.recommendation_movie_top_one);
        mTopTwo = (HorizontalListView) findViewById(R.id.recommendation_movie_top_two);
        mTopThree = (HorizontalListView) findViewById(R.id.recommendation_movie_top_three);

        txtTopOne = findViewById(R.id.recommendation_movie_top_one_txt);
        txtTopTwo = findViewById(R.id.recommendation_movie_top_two_txt);
        txtTopThree = findViewById(R.id.recommendation_movie_top_three_txt);

        mTopOne.setVisibility(View.GONE);
        txtTopOne.setVisibility(View.GONE);
        mTopTwo.setVisibility(View.GONE);
        txtTopTwo.setVisibility(View.GONE);
        mTopThree.setVisibility(View.GONE);
        txtTopThree.setVisibility(View.GONE);

        fillData();

        clickable();
    }

    /*
     Clickable of the movie, basically
     When use select the movie, it going to the detail of that movie
     */
    private void clickable(){
        mImageTop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                MovieItem movieItem = listTop.get(position);
                Toast.makeText(getApplicationContext(),listTop.get(position).getImdbID(),Toast.LENGTH_LONG).show();
                GetMovieDetailAsync getMovieDetailAsync = new GetMovieDetailAsync();
                getMovieDetailAsync.execute(movieItem);
            }
        });

        mTopOne.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MovieItem movieItem = listOne.get(position);
                Toast.makeText(getApplicationContext(),listOne.get(position).getImdbID(),Toast.LENGTH_LONG).show();
                GetMovieDetailAsync getMovieDetailAsync = new GetMovieDetailAsync();
                getMovieDetailAsync.execute(movieItem);
            }
        });
        mTopTwo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MovieItem movieItem = listTwo.get(position);
                Toast.makeText(getApplicationContext(),listTwo.get(position).getImdbID(),Toast.LENGTH_LONG).show();
                GetMovieDetailAsync getMovieDetailAsync = new GetMovieDetailAsync();
                getMovieDetailAsync.execute(movieItem);
            }
        });
        mTopThree.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MovieItem movieItem = listThree.get(position);
                Toast.makeText(getApplicationContext(),listThree.get(position).getImdbID(),Toast.LENGTH_LONG).show();
                GetMovieDetailAsync getMovieDetailAsync = new GetMovieDetailAsync();
                getMovieDetailAsync.execute(movieItem);
            }
        });
    }

    private class GetMovieDetailAsync extends AsyncTask<MovieItem, Void, JSONObject>{

        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MovieRecommendationActivity.this);
            progressDialog.setTitle("Loading");
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(MovieItem... movieItems) {
            String imdbId = getImdbId(movieItems[0].getImdbID());
            return MovieService.getMovieById(imdbId);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            Intent intent = new Intent(getApplicationContext(),MovieDetailActivity.class);
            intent.putExtra("data",jsonObject.toString());
            startActivity(intent);
            progressDialog.dismiss();
        }
    }

    /*
     Fill down the data from top recommendation
     */
    private void fillData(){
        try {
            JSONArray topAll = new JSONArray(getIntent().getStringExtra("top_all"));
            Log.d(TAG,topAll.toString());
            fillDataToTop(topAll);

            String typeName = getIntent().getStringExtra("typeName");
            Log.d(TAG,typeName);
            String[] arrTypeName = typeName.split(",");
            if(arrTypeName.length == 1){
                mTopOne.setVisibility(View.VISIBLE);
                txtTopOne.setVisibility(View.VISIBLE);
                JSONArray data = new JSONArray(getIntent().getStringExtra(arrTypeName[0]));
                fillTopOne(data,arrTypeName[0]);
            }
            if(arrTypeName.length == 2){
                mTopOne.setVisibility(View.VISIBLE);
                txtTopOne.setVisibility(View.VISIBLE);
                mTopTwo.setVisibility(View.VISIBLE);
                txtTopTwo.setVisibility(View.VISIBLE);
                JSONArray data1 = new JSONArray(getIntent().getStringExtra(arrTypeName[0]));
                JSONArray data2 = new JSONArray(getIntent().getStringExtra(arrTypeName[1]));
                fillTopOne(data1,arrTypeName[0]);
                fillTopTwo(data2,arrTypeName[1]);
            }
            if(arrTypeName.length == 3){
                mTopOne.setVisibility(View.VISIBLE);
                txtTopOne.setVisibility(View.VISIBLE);
                mTopTwo.setVisibility(View.VISIBLE);
                txtTopTwo.setVisibility(View.VISIBLE);
                mTopThree.setVisibility(View.VISIBLE);
                txtTopThree.setVisibility(View.VISIBLE);
                JSONArray data1 = new JSONArray(getIntent().getStringExtra(arrTypeName[0]));
                JSONArray data2 = new JSONArray(getIntent().getStringExtra(arrTypeName[1]));
                JSONArray data3 = new JSONArray(getIntent().getStringExtra(arrTypeName[2]));
                fillTopOne(data1,arrTypeName[0]);
                fillTopTwo(data2,arrTypeName[1]);
                fillTopThree(data3,arrTypeName[2]);
            }

        }catch (Exception e){
            Log.d(TAG,e.toString());

        }
    }

    /*
     Fill the data for the top one
     Based on the type user select
     */
    private void fillDataToTop(JSONArray data) {
        listTop = new ArrayList<MovieItem>();
        try {
            for (int i = 0; i < data.length(); i++) {
                JSONObject result = data.getJSONObject(i);
                MovieItem item = new MovieItem();
                item.setImdbID(result.getString("imdbId"));
                item.setPoster(result.getString("poster"));
                item.setTitle(result.getString("title"));
                listTop.add(item);
            }
            topAdapter = new MovieHorizontalAdapter(listTop,getApplicationContext());
            mImageTop.setAdapter(topAdapter);
            topAdapter.notifyDataSetChanged();
        }catch (Exception e){
            Log.d(TAG,e.toString());
        }
    }

    /*
    Fill the data for the top one
    Based on the type user select
    */
    private void fillTopOne(JSONArray data, String title){
        listOne = new ArrayList<MovieItem>();
        try {
            txtTopOne.setText("Top " + title + " Recommendations");
            for (int i = 0; i < data.length(); i++) {
                JSONObject result = data.getJSONObject(i);
                MovieItem item = new MovieItem();
                item.setImdbID(result.getString("imdbId"));
                item.setPoster(result.getString("poster"));
                item.setTitle(result.getString("title"));
                listOne.add(item);
            }
            topOneAdapter = new MovieHorizontalAdapter(listOne,getApplicationContext());
            mTopOne.setAdapter(topOneAdapter);
            topOneAdapter.notifyDataSetChanged();
        }catch (Exception e){
            Log.d(TAG,e.toString());
        }
    }

    /*
    Fill the data for the top two
    Based on the type user select
    */
    private void fillTopTwo(JSONArray data, String title){
        listTwo = new ArrayList<MovieItem>();
        try {
            txtTopTwo.setText("Top " + title + " Recommendations");
            for (int i = 0; i < data.length(); i++) {
                JSONObject result = data.getJSONObject(i);
                MovieItem item = new MovieItem();
                item.setImdbID(result.getString("imdbId"));
                item.setPoster(result.getString("poster"));
                item.setTitle(result.getString("title"));
                listTwo.add(item);
            }
            topTwoAdapter = new MovieHorizontalAdapter(listTwo,getApplicationContext());
            mTopTwo.setAdapter(topTwoAdapter);
            topTwoAdapter.notifyDataSetChanged();
        }catch (Exception e){
            Log.d(TAG,e.toString());
        }
    }

    /*
    Fill the data for the top three
    Based on the type user select
    */
    private void fillTopThree(JSONArray data,String title){
        listThree = new ArrayList<MovieItem>();
        try {
            txtTopThree.setText("Top " + title + " Recommendations");
            for (int i = 0; i < data.length(); i++) {
                JSONObject result = data.getJSONObject(i);
                MovieItem item = new MovieItem();
                item.setImdbID(result.getString("imdbId"));
                item.setPoster(result.getString("poster"));
                item.setTitle(result.getString("title"));
                listThree.add(item);
            }
            topThreeAdapter = new MovieHorizontalAdapter(listThree,getApplicationContext());
            mTopThree.setAdapter(topThreeAdapter);
            topThreeAdapter.notifyDataSetChanged();
        }catch (Exception e){
            Log.d(TAG,e.toString());
        }
    }

    private String getImdbId(String imdbId){
        while (imdbId.length() < 7){
            imdbId = "0" + imdbId;
        }
        imdbId = "tt"+imdbId;
        return imdbId;
    }
}

