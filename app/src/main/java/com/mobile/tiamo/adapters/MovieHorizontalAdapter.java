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

public class MovieHorizontalAdapter extends ArrayAdapter<MovieItem> {
    private List<MovieItem> datasets;
    private Context context;

    public MovieHorizontalAdapter(List<MovieItem> datasets, Context context){
        super(context, R.layout.item_horizontal_movie, datasets);
        this.datasets = datasets;
        this.context = context;
    }
    public static class ViewHolder{
        TextView txtTile;
        ImageView img;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        MovieItem item = getItem(position);
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_horizontal_movie, null, true);

            holder.txtTile = (TextView) convertView.findViewById(R.id.item_horizontal_movie_title);
            holder.img = (ImageView) convertView.findViewById(R.id.item_horizontal_movie_img);

            convertView.setTag(holder);
        }else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (ViewHolder)convertView.getTag();
        }

        holder.txtTile.setText(item.getTitle());
        Picasso.get().load(item.getPoster()).into(holder.img);

        return convertView;
    }
}
