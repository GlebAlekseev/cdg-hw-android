package com.glebalekseevjk.premierleaguefixtures.data.mapper

import com.glebalekseevjk.premierleaguefixtures.data.local.model.MatchInfoDbModel
import com.glebalekseevjk.premierleaguefixtures.domain.entity.MatchInfo
import com.glebalekseevjk.premierleaguefixtures.domain.mapper.Mapper
import javax.inject.Inject

class MatchInfoMapperImpl @Inject constructor() : Mapper<MatchInfo, MatchInfoDbModel> {
    override fun mapItemToDbModel(item: MatchInfo): MatchInfoDbModel {
        with(item) {
            return MatchInfoDbModel(
                matchNumber,
                roundNumber,
                dateUtc,
                location,
                homeTeam,
                awayTeam,
                group,
                homeTeamScore,
                awayTeamScore
            )
        }
    }

    override fun mapDbModelToItem(dbModel: MatchInfoDbModel): MatchInfo {
        with(dbModel) {
            return MatchInfo(
                matchNumber,
                roundNumber,
                dateUtc,
                location,
                homeTeam,
                awayTeam,
                group,
                homeTeamScore,
                awayTeamScore
            )
        }
    }
}