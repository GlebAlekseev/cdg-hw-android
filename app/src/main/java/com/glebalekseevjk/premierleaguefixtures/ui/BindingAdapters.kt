package com.glebalekseevjk.premierleaguefixtures.ui

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.glebalekseevjk.premierleaguefixtures.R
import com.glebalekseevjk.premierleaguefixtures.utils.parseAndGetDate
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.card.MaterialCardView

@BindingAdapter(value = ["homeTeamScore", "awayTeamScore", "isHome"], requireAll = true)
fun TextView.teamScore(homeTeamScore: Int, awayTeamScore: Int, isHome: Boolean) {
    if (isHome) {
        this.text = homeTeamScore.toString()
    } else {
        this.text = awayTeamScore.toString()
    }
}

@BindingAdapter(value = ["homeTeamScore", "awayTeamScore", "isHome"], requireAll = true)
fun MaterialCardView.teamScore(homeTeamScore: Int, awayTeamScore: Int, isHome: Boolean) {
    if (isHome) {
        if (homeTeamScore > awayTeamScore) {
            this.setCardBackgroundColor(resources.getColor(R.color.green))
        } else if (homeTeamScore == awayTeamScore) {
            this.setCardBackgroundColor(resources.getColor(R.color.yellow))
        } else {
            this.setCardBackgroundColor(resources.getColor(R.color.red))
        }
    } else {
        if (awayTeamScore > homeTeamScore) {
            this.setCardBackgroundColor(resources.getColor(R.color.green))
        } else if (awayTeamScore == homeTeamScore) {
            this.setCardBackgroundColor(resources.getColor(R.color.yellow))
        } else {
            this.setCardBackgroundColor(resources.getColor(R.color.red))
        }
    }
}

@BindingAdapter(value = ["homeTeamScore", "awayTeamScore"], requireAll = true)
fun TextView.teamScoreInline(homeTeamScore: Int, awayTeamScore: Int) {
    val text = "$homeTeamScore : $awayTeamScore\nЗавершено"
    this.text = text
}

@BindingAdapter("hourMinuteAsText")
fun TextView.hourMinuteAsText(date: String) {
    val resultText = parseAndGetDate(
        date = date.substring(0, date.length - 1),
        inputPatter = "yyyy-MM-dd HH:mm:ss",
        outputPatter = "HH:mm"
    )
    this.text = resultText
}

@BindingAdapter("dayMonthYearAsText")
fun TextView.dayMonthYearAsText(date: String) {
    val resultText = parseAndGetDate(
        date = date.substring(0, date.length - 1),
        inputPatter = "yyyy-MM-dd HH:mm:ss",
        outputPatter = "EEEE, dd MMMM uuuu"
    )
    this.text = resultText.capitalize()
}

@BindingAdapter(value = ["matchNumber", "roundNumber"], requireAll = true)
fun TextView.matchAndRoundNumber(matchNumber: Int, roundNumber: Int) {
    val text = "Матч $matchNumber Раунд $roundNumber"
    this.text = text
}

@BindingAdapter(value = ["matchNumber", "roundNumber"], requireAll = true)
fun MaterialToolbar.matchAndRoundNumber(matchNumber: Int, roundNumber: Int) {
    this.title = "Матч $matchNumber Раунд $roundNumber"
}