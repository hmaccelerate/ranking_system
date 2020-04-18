# Final Project: Ranking System
## Course: INFO6205 Algorithms and Data Structures
## Professor: Robin Hillyard
## Team Member
- Haimin Zhang
- Beiyi Sheng

## Project
This is an Elo Ranking system specifically  for English Premier League. 

It has two parts:
- Simulator: Simulating EPL to calculate teams' rating  based on a history of matches spread out over different seasons and give you the likelihood of Team A beating Team B
   
 - Website: A simple website based on the simulator that can upload csv files and then analyze csv files to rank teams

## Elo
From Wikipedia:

> The Elo rating system is a method for calculating the relative skill levels of players in competitor-versus-competitor games such as chess. It is named after its creator Arpad Elo, a Hungarian-born American physics professor.
### Elo for Soccer
This implementation has a few tweaks to adjust to soccer matches, more notably, the addition of a variable that changes the points spread in a given match according to the goal difference.

For detailed information about the formulas and a bit more of theory, please visit the [Wikipedia Page](https://en.wikipedia.org/wiki/World_Football_Elo_Ratings)

## Usage
This is a maven project, you can have to build the maven environment and import this project into Eclipse or Intellij idea.

## Run the simple website
The website entrance  is on the RankingSystemApplication.java. You can ran main method of this file and it will start to run.

## Run the simulator
The program entrance of the simulator is on the simulator.java. You can ran main method of this file and it will start to simulate EPL based on the history data.


### Load the data
```Java

List<Match> newestMatches= new ArrayList<>();
ReadUtil.readFromCSV(newestMatches,newestSeasonPath);
```

### Build the simulator 
```Java
int bestStartPoint=1200,bestK=50;  String bestProbFunction="normal";HashMap<String, Team> bestTeamsMap  = new HashMap<>();
int correctPrediction=simulator.simulateEPL(newestMatches,bestTeamsMap,bestStartPoint,bestProbFunction,bestK);

```        

### Rank teams and calculate the precision

```Java
simulator.rankingTeams(bestTeamsMap);
precision(correctPrediction,splitData.get("test").size());
```    

### CSV Input Scheme
```
Div,Date,HomeTeam,AwayTeam,FTHG,FTAG
E0,19/08/00,Charlton,Man City,4,0
```

## Data source
- [Data Files: England](http://www.football-data.co.uk/englandm.php)
