package com.cricket.service;

import com.cricket.entities.Match;

import java.util.List;
import java.util.Map;

public interface MatchService {

    //get All mathces
    List<Match> getAllMatches();
    // get live Matches

    List<Match> getLiveMatches();
    //get CWC 2023 time table

    List<Map<String,String>> getMatchesTimeTable();


}
