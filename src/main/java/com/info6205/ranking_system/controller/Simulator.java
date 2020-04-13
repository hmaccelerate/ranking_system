package com.info6205.ranking_system.controller;

import com.info6205.ranking_system.model.Match;
import com.info6205.ranking_system.model.Team;
import com.info6205.ranking_system.util.ELOUtil;
import com.info6205.ranking_system.util.ReadUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Simulator {
    private Map<String, Team> teams= new HashMap<>();
    private double startPoint=1200;

    public  void simulateEPL(List<Match> matches){
        for(Match match:matches){
            Team homeTeam=teams.getOrDefault(match.getHome(),new Team(startPoint,match.getHome()));
            Team awayTeam=teams.getOrDefault(match.getAway(),new Team(startPoint,match.getAway()));
            int goalDifference=match.getHomeGoals()-match.getAwayGoals();

            if(goalDifference>0)
                ELOUtil.EloUpdate(homeTeam, awayTeam, 1, goalDifference);
            else if(goalDifference==0)
                ELOUtil.EloUpdate(homeTeam, awayTeam, 0.5, goalDifference);
            else
                ELOUtil.EloUpdate(homeTeam, awayTeam, 0, goalDifference);
            System.out.println();
        }

    }

    public static void main(String[] args) {
        String filePath="src/main/resources/data/2000-2001.csv";
        List<Match> matches= ReadUtil.readFromCSV(filePath);
        Simulator simulator=new Simulator();
        simulator.simulateEPL(matches);


    }
}
