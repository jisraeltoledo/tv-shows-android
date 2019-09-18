package com.example.tvshows;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.gson.Gson;
import com.wdullaer.swipeactionadapter.SwipeActionAdapter;
import com.wdullaer.swipeactionadapter.SwipeDirection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

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
        SwipeActionAdapter swipeAdapter = new SwipeActionAdapter(arrayAdapter);
        swipeAdapter.setListView(list);
        list.setAdapter(swipeAdapter);
        list.setOnItemClickListener(this);
        swipeAdapter.setSwipeActionListener(new MySwipeListener(this, swipeAdapter));
        BottomNavigationItemView favoritesMenu = findViewById(R.id.action_favorites);
        favoritesMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFromLocalStorage();
            }
        });
        BottomNavigationItemView showsMenu = findViewById(R.id.action_shows);
        showsMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFromAPI();
            }
        });

        getFromAPI();
    }

    private void getFromLocalStorage () {
        SharedPreferences sharedPref = getSharedPreferences("shows", MODE_PRIVATE);
        Gson gson = new Gson();
        Map<String, ?> favorites = sharedPref.getAll();
        shows.clear();
        for (String key : favorites.keySet()){
            String json = (String) favorites.get(key);
            TVShow show = gson.fromJson(json, TVShow.class);
            shows.add (show);
        }
        arrayAdapter.notifyDataSetChanged();
    }

    private void getFromAPI() {
        shows.clear();
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

    @Override
    public void onClick(View view) {

    }
}

class MySwipeListener implements SwipeActionAdapter.SwipeActionListener {

    private static final String TAG = "MySwipeListener";
    Context context;
    SwipeActionAdapter adapter;

    public MySwipeListener (Context context, SwipeActionAdapter adapter){
        this.context = context;
        this.adapter = adapter;
    }
    @Override
    public boolean hasActions(int position, SwipeDirection direction){
        if(direction.isLeft()) return true; // Change this to false to disable left swipes
        if(direction.isRight()) return false;
        return false;
    }

    @Override
    public boolean shouldDismiss(int position, SwipeDirection direction){

        return false;
    }

    @Override
    public void onSwipe(int[] positionList, SwipeDirection[] directionList){

        for(int i=0;i<positionList.length;i++) {
            SwipeDirection direction = directionList[i];
            int position = positionList[i];
            String dir = "";
            TVShow show = (TVShow) adapter.getItem(position);
            addOrRemove (show);
            adapter.notifyDataSetChanged();
        }
    }

    void addOrRemove (TVShow show){
        SharedPreferences sharedPref = context.getSharedPreferences("shows", MODE_PRIVATE);
        String savedShow = sharedPref.getString("ID" + show.getId(), "");
        SharedPreferences.Editor editor = sharedPref.edit();
        if (savedShow.isEmpty()) {
            add (show, editor);
        }else {
            editor.remove("ID" + show.getId()).commit();
            Toast.makeText(context,show + " removed from favorites", Toast.LENGTH_SHORT).show();
        }
    }

    void add (TVShow show, SharedPreferences.Editor editor){

        Gson gson = new Gson();
        String json = gson.toJson(show);
        editor.putString("ID" + show.getId(), json).commit();
        Toast.makeText(
                context,
                show + " saved in favorites",
                Toast.LENGTH_SHORT
        ).show();
    }

}