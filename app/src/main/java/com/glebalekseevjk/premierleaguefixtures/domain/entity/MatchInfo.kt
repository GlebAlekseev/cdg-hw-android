package com.glebalekseevjk.premierleaguefixtures.domain.entity

data class MatchInfo(
    val matchNumber: Int,
    val roundNumber: Int,
    val dateUtc: String,
    val location: String,
    val homeTeam: String,
    val awayTeam: String,
    val group: Int?,
    val homeTeamScore: Int,
    val awayTeamScore: Int
){
    companion object{
         val MOCK = MatchInfo(
            0,
            0,
            "2021-08-14 14:00:00Z",
            "",
            "",
            "",
            null,
            0,
            0
        )
    }
}