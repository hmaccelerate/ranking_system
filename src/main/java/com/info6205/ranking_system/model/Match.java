package com.info6205.ranking_system.model;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.info6205.ranking_system.model.Team;
public class Match {
    private String home;
    private String away;
    private int homeGoals;
    private int awayGoals;
    private Date date;

    public Match() {
    }

    public Match(String home, String away, int homeGoals, int awayGoals) {
        this.home = home;
        this.away = away;
        this.homeGoals = homeGoals;
        this.awayGoals = awayGoals;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getAway() {
        return away;
    }

    public void setAway(String away) {
        this.away = away;
    }

    public int getHomeGoals() {
        return homeGoals;
    }

    public void setHomeGoals(int homeGoals) {
        this.homeGoals = homeGoals;
    }

    public int getAwayGoals() {
        return awayGoals;
    }

    public void setAwayGoals(int awayGoals) {
        this.awayGoals = awayGoals;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(String date) throws ParseException {
        DateFormat df = new SimpleDateFormat("DD/MM/YY");
        this.date = df.parse(date);
    }

}
