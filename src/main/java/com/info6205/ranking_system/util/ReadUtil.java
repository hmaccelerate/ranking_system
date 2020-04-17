package com.info6205.ranking_system.util;

import com.info6205.ranking_system.model.Match;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.*;

public class ReadUtil {
    public static void readFromCSV(List<Match> matchs,String fileName){
//        List<Match> matchs = new ArrayList<>();
        Path pathToFile = Paths.get(fileName);
        // create an instance of BufferedReader // using try with resource, Java 7 feature to close resources
        try (BufferedReader br = Files.newBufferedReader(pathToFile, StandardCharsets.US_ASCII)) {

            // read the first line from the text file
             String firstLine = br.readLine();
             String[] header = firstLine.split(",");
            Map<String,Integer> columnIndex =null;
             if(header.length!=0) {
                 columnIndex=findColumnIndex(header);
             }
            // loop until all lines are read
            String line = br.readLine();
            while (line != null) {
                 String[] attributes = line.split(",");
//                 System.out.println(attributes.length);
                 if(attributes.length!=0){
                     Match match = createMatch(attributes,columnIndex);
                     // adding match into ArrayList
                     matchs.add(match);
                 }
                 // read next line before looping // if end of file reached, line would be null
                 line = br.readLine();
             }}
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    private static Map<String,Integer> findColumnIndex(String[] header){
        Map<String,Integer> columnIndex=new HashMap<>();
        for(int i=0;i<header.length;i++){
            if(header[i].equals("Date"))
                columnIndex.put(header[i],i);
            if(header[i].equals("HomeTeam"))
                columnIndex.put(header[i],i);
            if(header[i].equals("AwayTeam"))
                columnIndex.put(header[i],i);
            if(header[i].equals("FTHG"))
                columnIndex.put(header[i],i);
            if(header[i].equals("FTAG"))
                columnIndex.put(header[i],i);
        }
        return  columnIndex;
    }

    private static Match createMatch(String[] metadata,Map<String,Integer> columnIndex) {
        String date = metadata[columnIndex.get("Date")];
        String home = metadata[columnIndex.get("HomeTeam")];
        String away = metadata[columnIndex.get("AwayTeam")];
        int homeGoals = Integer.parseInt(metadata[columnIndex.get("FTHG")]);
        int awayGoals = Integer.parseInt(metadata[columnIndex.get("FTAG")]);
        Match match = null;
        try {
            // create and return match of this metadata
            match = new Match(home, away, homeGoals, awayGoals);
            match.setDate(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return match;
    }

    public static void main(String[] args) {
        List<Match> matchs = new ArrayList<>();
        ReadUtil.readFromCSV(matchs,"src/main/resources/data/2000-2001.csv");
        System.out.println(matchs.get(100).getHome());
    }
}



