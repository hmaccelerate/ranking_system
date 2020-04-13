package com.info6205.ranking_system.model;

public class Team {
	private double elo;
	private String teamName;
	
	public double getElo() {
		return elo;
	}
	public void setElo(double elo) {
		this.elo = elo;
	}
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	
	public Team(double elo, String teamName) {
		super();
		this.elo = elo;
		this.teamName = teamName;
	}
}