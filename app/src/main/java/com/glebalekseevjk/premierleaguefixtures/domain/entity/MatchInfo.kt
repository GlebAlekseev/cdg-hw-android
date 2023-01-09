package com.glebalekseevjk.premierleaguefixtures.domain.entity

import com.google.gson.annotations.SerializedName

data class MatchInfo(
    @SerializedName("MatchNumber") val matchNumber: Int,
    @SerializedName("RoundNumber") val roundNumber: Int,
    @SerializedName("DateUtc") val dateUtc: String,
    @SerializedName("Location") val location: String,
    @SerializedName("HomeTeam") val homeTeam: String,
    @SerializedName("AwayTeam") val awayTeam: String,
    @SerializedName("Group") val group: Int?,
    @SerializedName("HomeTeamScore") val homeTeamScore: Int,
    @SerializedName("AwayTeamScore") val awayTeamScore: Int
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