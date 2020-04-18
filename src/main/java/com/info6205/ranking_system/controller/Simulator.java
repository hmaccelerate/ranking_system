package com.info6205.ranking_system.controller;

import com.info6205.ranking_system.model.Match;
import com.info6205.ranking_system.model.Team;
import com.info6205.ranking_system.util.ELOUtil;
import com.info6205.ranking_system.util.ReadUtil;

import java.io.File;
import java.util.*;

public class Simulator {
//    private Map<String, Team> teamsMap = new HashMap<>();

    public  int simulateEPL(List<Match> matches,Map<String, Team> teamsMap,double startPoint, String probability_function,int k){
        int correctPrediction=0;
        ELOUtil.setK(k);
        for(Match match:matches){
            Team homeTeam= teamsMap.getOrDefault(match.getHome(),new Team(startPoint,match.getHome()));
            Team awayTeam= teamsMap.getOrDefault(match.getAway(),new Team(startPoint,match.getAway()));

            int goalDifference=match.getHomeGoals()-match.getAwayGoals();
            int absGoalDifference=Math.abs(goalDifference);
            if(goalDifference>0)
                ELOUtil.EloUpdate(homeTeam, awayTeam, 1, absGoalDifference,probability_function);
            else if(goalDifference==0)
                ELOUtil.EloUpdate(homeTeam, awayTeam, 0.5, absGoalDifference,probability_function);
            else
                ELOUtil.EloUpdate(homeTeam, awayTeam, -1, absGoalDifference,probability_function);
            teamsMap.put(match.getHome(),homeTeam);
            teamsMap.put(match.getAway(),awayTeam);
            double probability = 0;
            if (probability_function.equals("normal"))
                probability=ELOUtil.normalDistProbability(homeTeam.getElo(),awayTeam.getElo());
            else
                probability=ELOUtil.logisticDistProbability(homeTeam.getElo(),awayTeam.getElo());
            if(goalDifference>0&&probability>0.5)
                correctPrediction++;
            else if (goalDifference<0&&probability<0.5)
                correctPrediction++;
            else if (goalDifference==00&&probability==0.5)
                correctPrediction++;
            String matchInfo=String.format("Match Info: Date:%tD,  Home team:%s ; AwayTeam:%s; Home goal:%s, Away Goal:%s ", match.getDate(),
                    match.getHome(),match.getAway(),match.getHomeGoals(),match.getAwayGoals());
            String eloInfo=String.format("Home team ELO:%s ; AwayTeam ELO :%s; P(Team1 beats Team2): %f",
                    homeTeam.getElo(),awayTeam.getElo(),probability);
//            System.out.println("-----------------------------------------------");
//            System.out.println(matchInfo);
//            System.out.println(eloInfo);

        }

        return correctPrediction;


    }

    public  List<Team> rankingTeams(Map<String, Team> teamsMap){
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
        return teams;
    }

    public static double precision(int correctPrediction,int totalNumber){
        double precision=  (double)correctPrediction/totalNumber;
        String precisionInfo= String.format("Precision:%f",precision);
        System.out.println(precisionInfo);
        return precision;
    }

    public static Map<String,List<Match>> trainTestSplit(List<Match> dataset, double testSize){
        int matchesSize=dataset.size();
        int trainingIndex= (int) (matchesSize*testSize);
        List<Match> trainingMatchs=dataset.subList(0, trainingIndex);
        List<Match> testingMatchs=dataset.subList(trainingIndex, matchesSize);
        Map<String,List<Match>> map=new HashMap<>();
        map.put("train",trainingMatchs);
        map.put("test",testingMatchs);
        return map;
    }

    public static void main(String[] args) {
        //1. create training dataset and testing dataset
        System.out.println("--------------------create training dataset and testing dataset---------------------");
        List<Match> matches = new ArrayList<>();
        String path = "src/main/resources/data";
        String newestSeasonPath="src/main/resources/data/2019-2020.csv";
        File file = new File(path);
        File[] fs = file.listFiles();
        if (fs != null) {
            for(File f:fs) {
                if (!f.isDirectory()&& !f.toString().equals(newestSeasonPath))
                    ReadUtil.readFromCSV(matches,f.toString());
                System.out.println(f.toString());
            }
            System.out.println(matches.size());
        }

        System.out.println("--------------------Tuning parameters to find the best parameter---------------------");
        Map<String,List<Match>> splitData=trainTestSplit(matches, 0.8);
        Simulator simulator=new Simulator();
        int[] startPoints=new int[]{1200,1700,2000,2500};
        int[] kValues=new int[]{30,50,70,90};
        String[] probFunctions=new String[]{"logistic","normal"};
        double bestPrecision=0; int bestStartPoint=0,bestK=0;  String bestProbFunction="";HashMap<String, Team> bestTeamsMap  = new HashMap<>();

        for (int startPoint: startPoints){
            for (int k:kValues){
                for (String probFunction:probFunctions){
                    HashMap<String, Team> teamsMap = new HashMap<>();
                    simulator.simulateEPL(splitData.get("train"),teamsMap,startPoint,probFunction,k);
                    int correctPrediction=simulator.simulateEPL(splitData.get("test"),teamsMap,startPoint,probFunction,k);
                    double newPrecision=precision(correctPrediction,splitData.get("test").size());
                    simulator.rankingTeams(teamsMap);
                    if (newPrecision>bestPrecision){
                        bestPrecision=newPrecision;
                        bestStartPoint= startPoint;
                        bestK=k;
                        bestProbFunction=probFunction;
                        bestTeamsMap.putAll(teamsMap);
                    }
                }
            }
        }

        String bestParametersInfo=String.format("Best precision:%f, Best k:%d,Best startppint: %d ,Best probfuncion: %s, ",
                bestPrecision,bestK,bestStartPoint,bestProbFunction);
        System.out.println(bestParametersInfo);

        System.out.println("--------------Make prediction on the newest season with best parameters and rank teams");
        List<Match> newestMatches= new ArrayList<>();
        ReadUtil.readFromCSV(newestMatches,newestSeasonPath);
        int correctPrediction=simulator.simulateEPL(newestMatches,bestTeamsMap,bestStartPoint,bestProbFunction,bestK);
        System.out.println("-----------------Rank Team-----------------");
        simulator.rankingTeams(bestTeamsMap);
        System.out.println("-----------------Precision-----------------");
        precision(correctPrediction,splitData.get("test").size());
    }
}
