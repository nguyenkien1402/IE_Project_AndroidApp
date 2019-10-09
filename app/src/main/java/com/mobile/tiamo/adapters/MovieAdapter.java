package com.mobile.tiamo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mobile.tiamo.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
  An adapter to populate the list of the movie
 **/
public class MovieAdapter extends ArrayAdapter<MovieItem> {

    private List<MovieItem> datasets;
    private Context context;

    public MovieAdapter(List<MovieItem> datasets, Context context){
        super(context, R.layout.item_movie, datasets);
        this.datasets = datasets;
        this.context = context;
    }
    public static class ViewHolder{
        TextView txtTile, txtYear;
        ImageView img;
    }


    /*
     Populate the view of the list of the item
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        MovieItem item = getItem(position);
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_movie, parent, false);
            viewHolder.txtTile = (TextView) convertView.findViewById(R.id.item_movie_title);
            viewHolder.txtYear = (TextView) convertView.findViewById(R.id.item_movie_year);
            viewHolder.img = (ImageView) convertView.findViewById(R.id.item_movie_image);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txtTile.setText(item.getTitle());
        viewHolder.txtYear.setText("Year: "+item.getYear());
        Picasso.get().load(item.getPoster()).resize(90,90).into(viewHolder.img);
        // Return the completed view to render on screen
        return convertView;
    }
}
