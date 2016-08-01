package com.thomaskioko.moviemaniac.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.thomaskioko.moviemaniac.MovieManiacApplication;
import com.thomaskioko.moviemaniac.R;
import com.thomaskioko.moviemaniac.api.TmdbApiClient;
import com.thomaskioko.moviemaniac.model.Movie;
import com.thomaskioko.moviemaniac.model.Result;
import com.thomaskioko.moviemaniac.ui.adapters.MoviesRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class PopularMoviesFragment extends Fragment {

    @Bind(R.id.movie_list)
    RecyclerView mRecyclerView;
    @Bind(R.id.progressBar)
    ProgressBar mProgressBar;
    private TmdbApiClient mTmdbApiClient;
    private List<Result> mResultList = new ArrayList<>();
    private static final String LOG_TAG = PopularMoviesFragment.class.getSimpleName();

    public PopularMoviesFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTmdbApiClient = MovieManiacApplication.getTmdbApiClient();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_popular_movies, container, false);
        ButterKnife.bind(this, rootView);

        int NUMBER_OF_GRID_ITEMS = 3;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), NUMBER_OF_GRID_ITEMS);
        assert mRecyclerView != null;
        mRecyclerView.setLayoutManager(gridLayoutManager);


        getPopularMovies();

        return rootView;
    }

    /**
     * Method to get Popular movies
     */
    private void getPopularMovies() {
        mResultList.clear();
        mRecyclerView.setAdapter(null);
        mProgressBar.setVisibility(View.VISIBLE);
        Call<Movie> topRatedList = mTmdbApiClient.movieInterface().getPopularMovies();
        topRatedList.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                mProgressBar.setVisibility(View.GONE);
                for (Result result : response.body().getResults()) {
                    mResultList.add(result);
                    mRecyclerView.setAdapter(new MoviesRecyclerViewAdapter(
                            getActivity(),
                            getFragmentManager(),
                            MovieManiacApplication.isTwoPane,
                            mResultList));
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                mProgressBar.setVisibility(View.GONE);
                Log.e(LOG_TAG, "@getTopRatedMovies Error Message:: " + t.getLocalizedMessage());
            }
        });
    }

}