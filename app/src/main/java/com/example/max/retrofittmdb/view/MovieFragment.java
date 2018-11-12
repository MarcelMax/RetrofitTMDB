package com.example.max.retrofittmdb.view;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.example.max.retrofittmdb.R;
import com.example.max.retrofittmdb.adapter.MovieAdapter;
import com.example.max.retrofittmdb.model.Movie;
import com.example.max.retrofittmdb.model.MovieDBResponse;
import com.example.max.retrofittmdb.service.MovieDataService;
import com.example.max.retrofittmdb.service.RetrofitInstance;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieFragment extends Fragment {
    @BindView(R.id.rv_movies)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.et_movie)
    EditText editText;


    public static String KEY_MOVIE = "movie";

    private Unbinder unbinder;
    private ArrayList<Movie> movies;
    private MovieAdapter movieAdapter;


    //todo wenn man von der detailansicht zurückkommt, sollte der view bei dem gesuchten bleiben
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie,container,false);
        unbinder = ButterKnife.bind(this,rootView);

        showBackButton();
        getPopularMovies();
        searchMovie();


        swipeLayoutSetup();
        return rootView;
    }

    private void getPopularMovies() {
        // 1 use Retrofit singleton through API Interface
        MovieDataService movieDataService = RetrofitInstance.getService();
        // 2 access the interface method with the needed apikey and assign it to a call
        Call<MovieDBResponse> call = movieDataService.getPopularMovies(this.getString(R.string.api_key));
        Log.v("***********","Call: " + call.toString());
        // 3 for the response enque the call
        call.enqueue(new Callback<MovieDBResponse>() {
            @Override
            public void onResponse(Call<MovieDBResponse> call, Response<MovieDBResponse> response) {
                // 4 the body of the response should be a call instance of type of MovieDBRespsone
                MovieDBResponse movieDBResponse = response.body();

                if (movieDBResponse != null && movieDBResponse.getMovies() != null ){

                    movies = (ArrayList<Movie>) movieDBResponse.getMovies();

                    showOnRecyclerView();
                    swipeRefreshLayout.setRefreshing(false);

                }
            }

            @Override
            public void onFailure(Call<MovieDBResponse> call, Throwable t) {

            }
        });

    }

    private void searchMovie(){
        editText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    getPopularMoviesSearch();
                    Log.v("***********","DONE ");

                    //wenn die suche leer ist und auf suche geklickt wird, werden die popular movies angezeigt
                    if (isEmpty(editText)){
                        getPopularMovies();
                    }
                }
                return false;
            }
        });
    }

    private boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0){
            return false;
        }

        return true;
    }

    private void getPopularMoviesSearch(){
        // 1 use Retrofit singleton through API Interface
        MovieDataService movieDataService = RetrofitInstance.getService();
        // 2 access the interface method with the needed apikey and assign it to a call
        Call<MovieDBResponse> call = movieDataService.getPopularMoviesSearch(this.getString(R.string.api_key),editText.getText().toString());
        Log.v("***********","REQUEST SEARCH URL " + call.request().url().queryParameterNames());
        // 3 for the response enque the call
        call.enqueue(new Callback<MovieDBResponse>() {
            @Override
            public void onResponse(Call<MovieDBResponse> call, Response<MovieDBResponse> response) {
                // 4 the body of the response should be a call instance of type of MovieDBRespsone
                MovieDBResponse movieDBResponse = response.body();

                if (movieDBResponse != null && movieDBResponse.getMovies() != null ){

                    movies = (ArrayList<Movie>) movieDBResponse.getMovies();

                    showOnRecyclerView();
                    swipeRefreshLayout.setRefreshing(false);
                    editText.getText().clear();
                }
            }

            @Override
            public void onFailure(Call<MovieDBResponse> call, Throwable t) {

            }
        });

    }

    private void showOnRecyclerView() {
        movieAdapter = new MovieAdapter(getContext(),movies);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(movieAdapter);
        movieAdapter.notifyDataSetChanged();
    }

    private void swipeLayoutSetup(){
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPopularMovies();
            }
        });
    }

    public void showBackButton() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity)getActivity()).getSupportActionBar().setTitle("TMDB Popular Movies Today");
            ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            ((MainActivity)getActivity()).getSupportActionBar().show();
        }
    }

}
