package com.mobile.tiamo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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

public class MovieRecommendationActivity extends AppCompatActivity {

    private String TAG = "MovieRecommendationActivity";
    List<MovieItem> movieItems;
//    private ViewGroup mImageTop;
//    private ViewGroup mTopOne;
//    private ViewGroup mTopTwo;
//    private ViewGroup mTopThree;
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

//        mImageTop.removeAllViews();
//        mTopOne.removeAllViews();
//        mTopTwo.removeAllViews();
//        mTopThree.removeAllViews();

        mTopOne.setVisibility(View.GONE);
        txtTopOne.setVisibility(View.GONE);
        mTopTwo.setVisibility(View.GONE);
        txtTopTwo.setVisibility(View.GONE);
        mTopThree.setVisibility(View.GONE);
        txtTopThree.setVisibility(View.GONE);

        fillData();

        clickable();
    }

    private void clickable(){
        mImageTop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),listTop.get(position).getTitle(),Toast.LENGTH_LONG).show();
            }
        });

        mTopOne.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),listOne.get(position).getTitle(),Toast.LENGTH_LONG).show();
            }
        });
        mTopTwo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),listTwo.get(position).getTitle(),Toast.LENGTH_LONG).show();
            }
        });
        mTopThree.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),listThree.get(position).getTitle(),Toast.LENGTH_LONG).show();
            }
        });
    }

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
//                RelativeLayout relativeLayout = (RelativeLayout) LayoutInflater.from(getApplication()).inflate(R.layout.item_horizontal_movie,mImageTop,false);
//                ImageView iv = (ImageView) relativeLayout.findViewById(R.id.item_horizontal_movie_img);
//                TextView txt = (TextView) relativeLayout.findViewById(R.id.item_horizontal_movie_title);
//                txt.setText(result.getString("title"));
//                Picasso.get().load(result.getString("poster")).into(iv);
//                mImageTop.addView(relativeLayout);
            }
            topAdapter = new MovieHorizontalAdapter(listTop,getApplicationContext());
            mImageTop.setAdapter(topAdapter);
            topAdapter.notifyDataSetChanged();
        }catch (Exception e){
            Log.d(TAG,e.toString());
        }
    }

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
//                RelativeLayout relativeLayout = (RelativeLayout) LayoutInflater.from(getApplication()).inflate(R.layout.item_horizontal_movie,mImageTop,false);
//                ImageView iv = (ImageView) relativeLayout.findViewById(R.id.item_horizontal_movie_img);
//                TextView txt = (TextView) relativeLayout.findViewById(R.id.item_horizontal_movie_title);
//                txt.setText(result.getString("title"));
//                Picasso.get().load(result.getString("poster")).into(iv);
//                mTopOne.addView(relativeLayout);
            }
            topOneAdapter = new MovieHorizontalAdapter(listOne,getApplicationContext());
            mTopOne.setAdapter(topOneAdapter);
            topOneAdapter.notifyDataSetChanged();
        }catch (Exception e){
            Log.d(TAG,e.toString());
        }
    }
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
//                RelativeLayout relativeLayout = (RelativeLayout) LayoutInflater.from(getApplication()).inflate(R.layout.item_horizontal_movie,mImageTop,false);
//                ImageView iv = (ImageView) relativeLayout.findViewById(R.id.item_horizontal_movie_img);
//                TextView txt = (TextView) relativeLayout.findViewById(R.id.item_horizontal_movie_title);
//                txt.setText(result.getString("title"));
//                Picasso.get().load(result.getString("poster")).into(iv);
//                mTopTwo.addView(relativeLayout);
            }
            topTwoAdapter = new MovieHorizontalAdapter(listTwo,getApplicationContext());
            mTopTwo.setAdapter(topTwoAdapter);
            topTwoAdapter.notifyDataSetChanged();
        }catch (Exception e){
            Log.d(TAG,e.toString());
        }
    }

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
//                RelativeLayout relativeLayout = (RelativeLayout) LayoutInflater.from(getApplication()).inflate(R.layout.item_horizontal_movie,mImageTop,false);
//                ImageView iv = (ImageView) relativeLayout.findViewById(R.id.item_horizontal_movie_img);
//                TextView txt = (TextView) relativeLayout.findViewById(R.id.item_horizontal_movie_title);
//                txt.setText(result.getString("title"));
//                Picasso.get().load(result.getString("poster")).into(iv);
//                mTopThree.addView(relativeLayout);
            }
            topThreeAdapter = new MovieHorizontalAdapter(listThree,getApplicationContext());
            mTopThree.setAdapter(topThreeAdapter);
            topThreeAdapter.notifyDataSetChanged();
        }catch (Exception e){
            Log.d(TAG,e.toString());
        }
    }

    private class GetMovieOnTopAsync extends AsyncTask<Void, Void,List<MovieItem>>{
        @Override
        protected List<MovieItem> doInBackground(Void... voids) {
            movieItems = MovieService.searchMovieByTitle("The Avenger",1);
            return movieItems;
        }

        @Override
        protected void onPostExecute(List<MovieItem> movieItems) {
            super.onPostExecute(movieItems);
            for (int i = 0 ; i < movieItems.size() ; i++){
                MovieItem item = movieItems.get(i);
                RelativeLayout relativeLayout = (RelativeLayout) LayoutInflater.from(getApplication()).inflate(R.layout.item_horizontal_movie,mTopOne,false);
                ImageView iv = (ImageView) relativeLayout.findViewById(R.id.item_horizontal_movie_img);
                TextView txt = (TextView) relativeLayout.findViewById(R.id.item_horizontal_movie_title);
                txt.setText(item.getTitle());
                Picasso.get().load(item.getPoster()).into(iv);
                mTopOne.addView(relativeLayout);
            }
        }
    }

    private class GetMovieRecommendationAsync extends AsyncTask<Void, Void, List<MovieItem>>{

        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(MovieRecommendationActivity.this);
            pd.setTitle("Loading");
            pd.show();
        }

        @Override
        protected List<MovieItem> doInBackground(Void... voids) {
            movieItems = MovieService.searchMovieByTitle("Thor",1);
            return movieItems;
        }

        @Override
        protected void onPostExecute(List<MovieItem> result) {
            super.onPostExecute(result);
            for (int i = 0 ; i < result.size() ; i++){
                MovieItem item = result.get(i);
                RelativeLayout relativeLayout = (RelativeLayout) LayoutInflater.from(getApplication()).inflate(R.layout.item_horizontal_movie,mImageTop,false);
                ImageView iv = (ImageView) relativeLayout.findViewById(R.id.item_horizontal_movie_img);
                TextView txt = (TextView) relativeLayout.findViewById(R.id.item_horizontal_movie_title);
                txt.setText(item.getTitle());
                Picasso.get().load(item.getPoster()).into(iv);
                mImageTop.addView(relativeLayout);
            }
            pd.dismiss();

        }
    }
}

