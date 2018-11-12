package com.example.max.retrofittmdb.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.max.retrofittmdb.R;
import com.example.max.retrofittmdb.model.Movie;
import com.example.max.retrofittmdb.view.MainActivity;
import com.example.max.retrofittmdb.view.MovieDetailFragment;
import com.example.max.retrofittmdb.view.MovieFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private Context mContext;
    private ArrayList<Movie> movieArrayList;

    public MovieAdapter(Context mContext, ArrayList<Movie> movieArrayList) {
        this.mContext = mContext;
        this.movieArrayList = movieArrayList;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_list_item, viewGroup, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder movieViewHolder, int i) {
        Movie movie = movieArrayList.get(i);
        movieViewHolder.movieTitle.setText(movie.getOriginalTitle());
        movieViewHolder.movieRating.setText(movie.getVoteAverage().toString());
        String imagePath = "https://image.tmdb.org/t/p/w500"+movie.getPosterPath();
        Picasso.get()
                .load(imagePath)
                .placeholder(R.drawable.loading)
                .into(movieViewHolder.movieImage);
    }

    @Override
    public int getItemCount() {
        return movieArrayList.size();
    }



    public class MovieViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_title)
        TextView movieTitle;
        @BindView(R.id.tv_rating)
        TextView movieRating;
        @BindView(R.id.iv_movie)
        ImageView movieImage;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        @OnClick(R.id.iv_movie)
        public void changeFragment(){
            int position = getAdapterPosition();
            Movie selectedMoview = movieArrayList.get(position);
            Log.v("********Clicked ","*****Movie " + selectedMoview);

            Bundle bundle = new Bundle();
            if (movieArrayList != null){
                bundle.putParcelable(MovieDetailFragment.KEY_MOVIEDETAIL,selectedMoview);
            }

            MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
            MainActivity mainActivity = (MainActivity) mContext;
            FragmentManager manager = mainActivity.getSupportFragmentManager();
            List<Fragment> fragments = manager.getFragments();
            Log.v("*****FRAGMENTS","" + fragments.toString());
            FragmentTransaction transaction = manager.beginTransaction();
            movieDetailFragment.setArguments(bundle);
            transaction
                    .replace(R.id.container,movieDetailFragment)
                    .addToBackStack(null)
                    .commit()
            ;


        }

    }
}
