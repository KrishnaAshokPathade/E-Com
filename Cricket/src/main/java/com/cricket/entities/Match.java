package com.cricket.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
@Data
@Entity
@Table(name = "cricket_match")
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int matchId;

    private String teamHeading;

    private String matchNumberVersion;

    private String battingTeam;

    private String battingTeamScore;

    private String bowlTeam;
    private String liveText;

    private String matchLink;

    private String textComplete;
    @Enumerated
    private MatchStatus matchStatus;
    private Date date = new Date();

    public Match(int matchId, String teamHeading, String matchNumberVersion, String battingTeam, String battingTeamScore, String bowlTeam, String liveText, String matchLink, String textComplete, MatchStatus matchStatus, Date date) {
        this.matchId = matchId;
        this.teamHeading = teamHeading;
        this.matchNumberVersion = matchNumberVersion;
        this.battingTeam = battingTeam;
        this.battingTeamScore = battingTeamScore;
        this.bowlTeam = bowlTeam;
        this.liveText = liveText;
        this.matchLink = matchLink;
        this.textComplete = textComplete;
        this.matchStatus = matchStatus;
        this.date = date;
    }

    public  Match(){
        
    }


    @Override
    public String toString() {
        return "Match{" +
                "matchId=" + matchId +
                ", teamHeading='" + teamHeading + '\'' +
                ", matchNumberVersion='" + matchNumberVersion + '\'' +
                ", battingTeam='" + battingTeam + '\'' +
                ", battingTeamScore='" + battingTeamScore + '\'' +
                ", bowlTeam='" + bowlTeam + '\'' +
                ", liveText='" + liveText + '\'' +
                ", matchLink='" + matchLink + '\'' +
                ", textComplete='" + textComplete + '\'' +
                ", status=" + matchStatus +
                ", date=" + date +
                '}';

    }
}


