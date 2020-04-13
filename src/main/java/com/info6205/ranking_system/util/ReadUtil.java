package com.info6205.ranking_system.util;

import com.info6205.ranking_system.model.Match;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReadUtil {
    public static List<Match> readFromCSV(String fileName){
        List<Match> matchs = new ArrayList<>();
        Path pathToFile = Paths.get(fileName);
        // create an instance of BufferedReader // using try with resource, Java 7 feature to close resources
        try (BufferedReader br = Files.newBufferedReader(pathToFile, StandardCharsets.US_ASCII)) {
            // read the first line from the text file
             String line = br.readLine();
            // loop until all lines are read
             while (line != null) {
                 String[] attributes = line.split(",");
                 System.out.println(attributes[1]);
                 Match match = createMatch(attributes);
                 // adding book into ArrayList
                 matchs.add(match);
                 // read next line before looping // if end of file reached, line would be null
                 line = br.readLine();
             }}
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return matchs;
    }

    private static Match createMatch(String[] metadata) {
         String date= metadata[1];
         String home= metadata[2];
         String away= metadata[3];
        int homeGoals=Integer.parseInt(metadata[4]) ;
        int awayGoals= Integer.parseInt(metadata[5]);
        // create and return book of this metadata
        Match match=new Match(home,away,homeGoals,awayGoals);
        try {
            match.setDate(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return match;
    }

    public static void main(String[] args) {

        ReadUtil.readFromCSV("src/main/resources/data/2000-2001.csv");
    }
}



