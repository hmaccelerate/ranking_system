# INFO6205 Algorithms and Data Structures 
# Final Project: Ranking System
## Team Member
- Haimin Zhang
- Beiyi Sheng
## Project
This is an Elo Rating calculator specifically tweaked for soccer matches. 

It can calculate a given team rating based on a history of matches spread out over different seasons/leagues, give you the likelihood of Team A beating Team B, import and export data do JSON files, personalize the algorithm, and many more features.

## Elo
From Wikipedia:

> The Elo rating system is a method for calculating the relative skill levels of players in competitor-versus-competitor games such as chess. It is named after its creator Arpad Elo, a Hungarian-born American physics professor.
### Elo for Soccer
This implementation has a few tweaks to adjust to soccer matches, more notably, the addition of a variable that changes the points spread in a given match according to the goal difference.

For detailed information about the formulas and a bit more of theory, please visit the [Wikipedia Page](https://en.wikipedia.org/wiki/World_Football_Elo_Ratings)

## Usage
### Build an instance.

```Java
EloCalculator calculator = new EloCalculator();
try {
  calculator = new EloCalculator.Builder()
                .setK(20)
                .setLeagues("C:\\Soccer\\Data.json")
                .setRegressTowardMean(true)
                .build();
} catch (IOException ex) {
  Logger.getLogger(MainClass.class.getName()).log(Level.SEVERE, null, ex);
}
```        

### Calculate the ratings

```Java
calculator.calculateRatings();
```    

### Use your data
You can save it (and load it later, so you don't need to recalculate).

```Java
try {
  calculator.saveTeamsJSONFile("C:\\Soccer\\Ratings_Jan_2017.json");
} catch (IOException ex) {
  Logger.getLogger(MainClass.class.getName()).log(Level.SEVERE, null, ex);
}
```

Or you could get your data directly. In this example, we only print the rating in descending order.

```Java
System.out.println("Ranking: ");
List<Team> teams = calculator.getTeams(true);
for(Team t : teams)
  System.out.println(((int) t.getLastRating().getRating()) + " " + t.getName());         
```

### Leagues Input Scheme
```JSON
[
  {
    "champion": "Palmeiras",
    "name": "Campeonato Brasileiro 2016",
    "matches": [
      {
        "home": "Vitória",
        "away": "Palmeiras",
        "homeGoals": 1,
        "awayGoals": 2,
        "date": "Dec 11, 2016 12:00:00 AM"
      },
      {
        "home": "Palmeiras",
        "away": "Atlético-PR",
        "homeGoals": 4,
        "awayGoals": 0,
        "date": "May 14, 2016 12:00:00 AM"
      }
    ],
    "year": 2016
  }
]
```

## Data source
- [Data Files: England](http://www.football-data.co.uk/englandm.php)
