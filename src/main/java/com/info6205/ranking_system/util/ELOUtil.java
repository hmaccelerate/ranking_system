package com.info6205.ranking_system.util;

import com.info6205.ranking_system.model.Team;

public class ELOUtil {

	private static int K = 10;
	private static int beta = 4;
	
	
	public static double probability(double t1, double t2) {
//		System.out.println("t1 " + t1 + "t2 " + t2);
		double x = (t1-t2) / (Math.sqrt(2)*beta);
		return normCDF(x);
	}
	
	public static double get_G(int goal_difference) {
		double G;
		switch(goal_difference) {
			case 1: G = 1; break;
			case 2: G = 3/2; break;
			default: G = (11+goal_difference)/8; break;
		}
		return G;
	}
	
	private static double normCDF(double z){
		double LeftEndpoint = -100;
		int nRectangles = 100000;
	    double runningSum = 0;
	    double x;
	    for(int n = 0; n < nRectangles; n++){
	    x = LeftEndpoint + n*(z-LeftEndpoint)/nRectangles;
	        runningSum += Math.pow(Math.sqrt(2*Math.PI),-1)*Math.exp(-Math.pow(x,2)/2)*(z-LeftEndpoint)/nRectangles;
	    }
//	    System.out.println("Probability " + runningSum);
	    return runningSum;
	}
	
	
	public static void EloUpdate(Team t1, Team t2, double score, int goal_difference) {
		double temp1 = t1.getElo();
		double temp2 = t2.getElo();
		double G = get_G(goal_difference);
		t1.setElo(temp1+K*G*(score-probability(temp1, temp2)));
		if(score == 1/2) {
			t2.setElo(temp2+K*G*(score-probability(temp2, temp1)));
		}
		else {
			t2.setElo(temp2+K*G*((-1)*score-probability(temp2, temp1)));
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Team t1 = new Team(120, "A");
		Team t2 = new Team(120, "B");
		System.out.println(t1.getElo());
		System.out.println(t2.getElo());
		EloUpdate(t1,t2,1,5);
		EloUpdate(t1,t2,1,5);
		EloUpdate(t1,t2,-1,5);
		//EloUpdate(t1,t2,-1);
		System.out.println(t1.getElo());
		System.out.println(t2.getElo());
		System.out.println(probability(t1.getElo(), t2.getElo()));
		
	}

}
