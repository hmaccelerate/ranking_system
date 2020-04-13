package com.info6205.ranking_system.controller;

import com.info6205.ranking_system.model.Match;
import com.info6205.ranking_system.model.Team;
import com.info6205.ranking_system.util.ELOUtil;
import com.info6205.ranking_system.util.ReadUtil;

import java.io.File;
import java.util.*;

public class Simulator {
    private Map<String, Team> teamsMap = new HashMap<>();

    public  void simulateEPL(List<Match> matches,double startPoint){
        for(Match match:matches){
            Team homeTeam= teamsMap.getOrDefault(match.getHome(),new Team(startPoint,match.getHome()));
            Team awayTeam= teamsMap.getOrDefault(match.getAway(),new Team(startPoint,match.getAway()));
            int goalDifference=match.getHomeGoals()-match.getAwayGoals();

            if(goalDifference>0)
                ELOUtil.EloUpdate(homeTeam, awayTeam, 1, goalDifference);
            else if(goalDifference==0)
                ELOUtil.EloUpdate(homeTeam, awayTeam, 0.5, goalDifference);
            else
                ELOUtil.EloUpdate(homeTeam, awayTeam, 0, goalDifference);
            teamsMap.put(match.getHome(),homeTeam);
            teamsMap.put(match.getAway(),awayTeam);

            String matchInfo=String.format("Match Info: Date:%tD,  Home team:%s ; AwayTeam:%s; Home goal:%s, Away Goal:%s ", match.getDate(),
                    match.getHome(),match.getAway(),match.getHomeGoals(),match.getAwayGoals());
            String eloInfo=String.format("Home team ELO:%s ; AwayTeam ELO :%s; Winning Probability of Home Team:%f",
                    homeTeam.getElo(),awayTeam.getElo(),ELOUtil.probability(homeTeam.getElo(),awayTeam.getElo()));
            System.out.println("-----------------------------------------------");
            System.out.println(matchInfo);
            System.out.println(eloInfo);

        }

    }

    public void rankingTeams(){
        List<Team> teams= new ArrayList<>();
        for (Map.Entry<String, Team> entry : teamsMap.entrySet()) {
            teams.add(entry.getValue());
        }
        Collections.sort(teams);
        Collections.reverse(teams);
        for(int i=0;i<teams.size();i++){
            String rankingInfo=String.format("NO.%d ; Team Name:%s; Team ELO:%f",i+1,teams.get(i).getTeamName(),
                    teams.get(i).getElo());
            System.out.println("-----------------------------------------------");
            System.out.println(rankingInfo);
        }
    }

    public static void main(String[] args) {

        List<Match> matchs = new ArrayList<>();
        String path = "src/main/resources/data";
        File file = new File(path);
        File[] fs = file.listFiles();
        if (fs != null) {
            for(File f:fs) {
                if (!f.isDirectory())
                    System.out.println(f.toString());
                    ReadUtil.readFromCSV(matchs,f.toString());
            }
            System.out.println(matchs.size());
        }

        System.out.println("-----------------Start Match-----------------");
        Simulator simulator=new Simulator();
        simulator.simulateEPL(matchs,1200);

        System.out.println("-----------------Rank Team-----------------");
        simulator.rankingTeams();
    }
}
