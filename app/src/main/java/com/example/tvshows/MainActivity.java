package com.example.tvshows;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.tvshows.adapters.ShowsAdapter;
import com.example.tvshows.model.TVShow;
import com.example.tvshows.service.ShowsService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private String TAG = "MainActivity";
    private ListView list;
    private ArrayList<TVShow> shows = new ArrayList<>();
    private ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        arrayAdapter = new ShowsAdapter(getApplicationContext(), shows);
        list = findViewById(R.id.list);
        list.setAdapter(arrayAdapter);
        list.setOnItemClickListener(this);
        getPosts();
    }

    private void getPosts() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.tvmaze.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ShowsService service = retrofit.create(ShowsService.class);
        Call<List<TVShow>> call = service.getShows();

        call.enqueue(new Callback<List<TVShow>>() {
            @Override
            public void onResponse(Call<List<TVShow>> call, Response<List<TVShow>> response) {
                Log.d (TAG, response.body().toString());
                for(TVShow show : response.body()) {
                    shows.add(show);
                    Log.d (TAG, show.toString());
                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<TVShow>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("show", shows.get(i));
        startActivity(intent);
    }
}
