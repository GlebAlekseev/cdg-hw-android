package com.glebalekseevjk.premierleaguefixtures.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MatchInfoDbModel(
    @PrimaryKey
    @ColumnInfo(name = "MatchNumber") val matchNumber: Int,
    @ColumnInfo(name = "RoundNumber") val roundNumber: Int,
    @ColumnInfo(name = "DateUtc") val dateUtc: String,
    @ColumnInfo(name = "Location") val location: String,
    @ColumnInfo(name = "HomeTeam") val homeTeam: String,
    @ColumnInfo(name = "AwayTeam") val awayTeam: String,
    @ColumnInfo(name = "Group") val group: Int?,
    @ColumnInfo(name = "HomeTeamScore") val homeTeamScore: Int,
    @ColumnInfo(name = "AwayTeamScore") val awayTeamScore: Int
)