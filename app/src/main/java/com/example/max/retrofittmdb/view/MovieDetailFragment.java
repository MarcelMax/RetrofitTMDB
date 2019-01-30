package com.example.max.retrofittmdb.view;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.max.retrofittmdb.R;
import com.example.max.retrofittmdb.model.Movie;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class MovieDetailFragment extends Fragment {
   @BindView(R.id.tv_movieTitle)
    TextView tvMovieTitle;
   @BindView(R.id.tv_movieRating)
    TextView tvMovieRating;
   @BindView(R.id.tv_releaseDate)
    TextView tvMovieReleaseDate;
   @BindView(R.id.tv_plotsynopsis)
    TextView tvMoviePlot;
   @BindView(R.id.iv_MovieLarge)
    ImageView ivMovieLarge;
   @BindView(R.id.toolbar)
   Toolbar toolbar;


    public static String KEY_MOVIEDETAIL = "movieDetail";
    private Unbinder unbinder;
    private Movie movie;

    public MovieDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        Log.v("MOVIEDETAILFRAGMENT","");
        unbinder = ButterKnife.bind(this,rootview);

        showBackButton();
        fillWithContent();
        return rootview;
    }


    public void showBackButton() {
        if (getActivity() instanceof MainActivity) {

            ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    // gets the movies from the request
    public Movie getMovie() {
        if(movie == null){
            movie = new Movie();
            Bundle bundle = this.getArguments();

            if (bundle != null) {

                movie = bundle.getParcelable(KEY_MOVIEDETAIL);
                Log.v("MOVIEDETAILFRAG ", "MOVIE: " + movie);

            }

        }
        return movie;
    }

    // fills the view with content
    private void fillWithContent(){
        Movie movie = getMovie();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(movie.getTitle());

        String imagePath = "https://image.tmdb.org/t/p/w500"+movie.getPosterPath();
        Log.v("MOVIEDETAILFRAG ", "MOVIE: " + movie);
        Picasso.get()
                .load(imagePath)
                .into(ivMovieLarge);
        tvMoviePlot.setText(movie.getOverview());
        tvMovieRating.setText(movie.getVoteAverage().toString());
        tvMovieReleaseDate.setText(movie.getReleaseDate());
        tvMovieTitle.setText(movie.getTitle());

    }



}
