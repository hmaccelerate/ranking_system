package com.info6205.ranking_system.controller;

import com.info6205.ranking_system.model.Match;
import com.info6205.ranking_system.model.Team;
import com.info6205.ranking_system.util.ELOUtil;
import com.info6205.ranking_system.util.ReadUtil;

import java.io.File;
import java.util.*;

public class Simulator {
    private Map<String, Team> teamsMap = new HashMap<>();

    public  int simulateEPL(List<Match> matches,double startPoint, String probability_function,int k){
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

            double probability=ELOUtil.normalDistProbability(homeTeam.getElo(),awayTeam.getElo());
            if(goalDifference>0&&probability>0.5)
                correctPrediction++;
            else if (goalDifference<0&&probability<0.5)
                correctPrediction++;

            String matchInfo=String.format("Match Info: Date:%tD,  Home team:%s ; AwayTeam:%s; Home goal:%s, Away Goal:%s ", match.getDate(),
                    match.getHome(),match.getAway(),match.getHomeGoals(),match.getAwayGoals());
            String eloInfo=String.format("Home team ELO:%s ; AwayTeam ELO :%s; P(Team1 beats Teams): %f",
                    homeTeam.getElo(),awayTeam.getElo(),probability);
            System.out.println("-----------------------------------------------");
            System.out.println(matchInfo);
            System.out.println(eloInfo);

        }
        return correctPrediction;


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

    public static double precision(int correctPrediction,int totalNumber){
        System.out.println(correctPrediction);
        System.out.println(totalNumber);
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
        //logic distribution probability
        //tuning parameter
        List<Match> matches = new ArrayList<>();
        String path = "src/main/resources/data";
        File file = new File(path);
        File[] fs = file.listFiles();
        if (fs != null) {
            for(File f:fs) {
                if (!f.isDirectory())
                    ReadUtil.readFromCSV(matches,f.toString());
            }
            System.out.println(matches.size());
        }
        Map<String,List<Match>> splitData=trainTestSplit(matches, 0.8);

        Simulator simulator=new Simulator();
        System.out.println("-----------------Start Match(Training stage)-----------------");
        simulator.simulateEPL(splitData.get("train"),1200,"logistic",50);

        System.out.println("-----------------Start Match(Testing stage)-----------------");
        int correctPrediction=simulator.simulateEPL(splitData.get("test"),1200,"logistic",50);
        precision(correctPrediction,splitData.get("test").size());

        System.out.println("-----------------Rank Team-----------------");
        simulator.rankingTeams();

        System.out.println("--------------------Tuning parameters---------------------");
        int[] startPoints=new int[]{1200,1700,2000,2500};
        int[] kValues=new int[]{30,50,70,90};
        String[] probFunctions=new String[]{"logistic","normal"};
        double bestPrecision=0; int bestStartPoint=0,bestK=0;  String bestProbFunction="";

        for (int startPoint: startPoints){
            for (int k:kValues){
                for (String probFunction:probFunctions){
                    simulator.simulateEPL(splitData.get("train"),startPoint,probFunction,k);
                    double newPrecision=simulator.simulateEPL(splitData.get("test"),startPoint,probFunction,k);
                    simulator.rankingTeams();
                    if (newPrecision>bestPrecision){
                        bestPrecision=newPrecision;
                        bestStartPoint= startPoint;
                        bestK=k;
                        bestProbFunction=probFunction;
                    }
                    bestPrecision=Math.max(bestPrecision, newPrecision);
                }
            }
        }
        String bestParametersInfo=String.format("Best precision:%f, Best k:%d,Best startppint: %d ,Best probfuncion: %s, ",
                bestPrecision,bestK,bestStartPoint,bestProbFunction);
        System.out.println(bestParametersInfo);

    }
}
