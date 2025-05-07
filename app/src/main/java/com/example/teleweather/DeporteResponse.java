package com.example.teleweather;

import java.util.ArrayList;
import java.util.List;
public class DeporteResponse {
    private List<FootballMatch> football;
    private List<CricketMatch> cricket;
    private List<GolfMatch> golf;

    public List<FootballMatch> getFootball() {
        return football != null ? football : new ArrayList<>();
    }

    public void setFootball(List<FootballMatch> football) {
        this.football = football;
    }

    public List<CricketMatch> getCricket() {
        return cricket != null ? cricket : new ArrayList<>();
    }

    public void setCricket(List<CricketMatch> cricket) {
        this.cricket = cricket;
    }

    public List<GolfMatch> getGolf() {
        return golf != null ? golf : new ArrayList<>();
    }

    public void setGolf(List<GolfMatch> golf) {
        this.golf = golf;
    }

    public static class FootballMatch {
        private String stadium;
        private String country;
        private String region;
        private String tournament;
        private String start;
        private String match;

        public String getStadium() {
            return stadium != null ? stadium : "";
        }

        public void setStadium(String stadium) {
            this.stadium = stadium;
        }

        public String getCountry() {
            return country != null ? country : "";
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getRegion() {
            return region != null ? region : "";
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getTournament() {
            return tournament != null ? tournament : "";
        }

        public void setTournament(String tournament) {
            this.tournament = tournament;
        }

        public String getStart() {
            return start != null ? start : "";
        }

        public void setStart(String start) {
            this.start = start;
        }

        public String getMatch() {
            return match != null ? match : "";
        }

        public void setMatch(String match) {
            this.match = match;
        }
    }

    // Cricket y Golf tienen la misma estructura que Football
    public static class CricketMatch extends FootballMatch {
        // Utilizamos la misma estructura que FootballMatch
    }

    public static class GolfMatch extends FootballMatch {
        // Utilizamos la misma estructura que FootballMatch
    }
}
