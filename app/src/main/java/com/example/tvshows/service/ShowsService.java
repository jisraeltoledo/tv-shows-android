package com.example.tvshows.service;

import com.example.tvshows.model.TVShow;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ShowsService {
    String API_ROUTE = "/shows";

    @GET(API_ROUTE)
    Call< List<TVShow> > getShows();
}
