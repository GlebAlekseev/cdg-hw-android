package com.glebalekseevjk.premierleaguefixtures.ui

import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.BindingAdapter
import com.glebalekseevjk.premierleaguefixtures.R
import com.glebalekseevjk.premierleaguefixtures.utils.parseAndGetDate
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.card.MaterialCardView

object BindingAdapters {
    @BindingAdapter(value = ["homeTeamScore", "awayTeamScore", "isHome"], requireAll = true)
    @JvmStatic
    fun TextView.setTeamScore(homeTeamScore: Int, awayTeamScore: Int, isHome: Boolean) {
        this.text = if (isHome) homeTeamScore.toString() else awayTeamScore.toString()
    }

    @BindingAdapter(value = ["homeTeamScore", "awayTeamScore", "isHome"], requireAll = true)
    @JvmStatic
    fun MaterialCardView.setTeamScoreBackground(
        homeTeamScore: Int,
        awayTeamScore: Int,
        isHome: Boolean
    ) {
        val score = if (isHome) homeTeamScore else awayTeamScore
        val opponentScore = if (isHome) awayTeamScore else homeTeamScore

        this.setCardBackgroundColor(
            when {
                score > opponentScore -> AppCompatResources.getColorStateList(
                    context,
                    R.color.green
                )
                score == opponentScore -> AppCompatResources.getColorStateList(
                    context,
                    R.color.yellow
                )
                else -> AppCompatResources.getColorStateList(context, R.color.red)
            }
        )
    }

    @BindingAdapter(value = ["homeTeamScore", "awayTeamScore"], requireAll = true)
    @JvmStatic
    fun TextView.setTeamScoreInline(homeTeamScore: Int, awayTeamScore: Int) {
        this.text = resources.getString(R.string.score_status_text, homeTeamScore, awayTeamScore)
    }

    @BindingAdapter("hourMinuteAsText")
    @JvmStatic
    fun TextView.setHourMinuteAsText(date: String) {
        this.text = parseAndGetDate(
            date = date.substring(0, date.length - 1),
            inputPattern = "yyyy-MM-dd HH:mm:ss",
            outputPattern = "HH:mm"
        )
    }

    @BindingAdapter("dayMonthYearAsText")
    @JvmStatic
    fun TextView.setDayMonthYearAsText(date: String) {
        this.text = parseAndGetDate(
            date = date.substring(0, date.length - 1),
            inputPattern = "yyyy-MM-dd HH:mm:ss",
            outputPattern = "EEEE, dd MMMM uu"
        ).replaceFirstChar { it.uppercaseChar() }
    }

    @BindingAdapter(value = ["matchNumber", "roundNumber"], requireAll = true)
    @JvmStatic
    fun TextView.setMatchAndRoundNumber(matchNumber: Int, roundNumber: Int) {
        this.text = resources.getString(R.string.match_round_text, matchNumber, roundNumber)
    }

    @BindingAdapter(value = ["matchNumber", "roundNumber"], requireAll = true)
    @JvmStatic
    fun MaterialToolbar.setMatchAndRoundNumber(matchNumber: Int, roundNumber: Int) {
        this.title = resources.getString(R.string.match_round_text, matchNumber, roundNumber)
    }
}