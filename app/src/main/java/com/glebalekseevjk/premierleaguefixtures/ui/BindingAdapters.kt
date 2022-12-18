package com.glebalekseevjk.premierleaguefixtures.ui

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.glebalekseevjk.premierleaguefixtures.R
import com.glebalekseevjk.premierleaguefixtures.utils.parseAndGetDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@BindingAdapter(value = ["homeTeamScore","awayTeamScore","isHome"], requireAll = true)
fun TextView.teamScore(homeTeamScore: Int, awayTeamScore: Int, isHome: Boolean){
    if (isHome){
        this.text = homeTeamScore.toString()
        if (homeTeamScore > awayTeamScore){
            this.setBackgroundColor(resources.getColor(R.color.green))
        }else if(homeTeamScore == awayTeamScore){
            this.setBackgroundColor(resources.getColor(R.color.yellow))
        }else {
            this.setBackgroundColor(resources.getColor(R.color.red))
        }
    }else{
        this.text = awayTeamScore.toString()
        if (awayTeamScore > homeTeamScore){
            this.setBackgroundColor(resources.getColor(R.color.green))
        }else if(awayTeamScore == homeTeamScore){
            this.setBackgroundColor(resources.getColor(R.color.yellow))
        }else {
            this.setBackgroundColor(resources.getColor(R.color.red))
        }
    }
}

@BindingAdapter(value = ["homeTeamScore","awayTeamScore"], requireAll = true)
fun TextView.teamScoreInline(homeTeamScore: Int, awayTeamScore: Int){
    this.text = "$homeTeamScore : $awayTeamScore\nEnd"
}

@BindingAdapter("hourMinuteAsText")
fun TextView.hourMinuteAsText(date: String){
    val resultText = parseAndGetDate(
        date = date.substring(0,date.length-1),
        inputPatter = "yyyy-MM-dd HH:mm:ss",
        outputPatter = "HH:mm"
    )
    this.text = resultText
}

@BindingAdapter("dayMonthYearAsText")
fun TextView.dayMonthYearAsText(date: String){
    val resultText = parseAndGetDate(
        date = date.substring(0,date.length-1),
        inputPatter = "yyyy-MM-dd HH:mm:ss",
        outputPatter = "EEEE, dd MMMM uuuu"
    )
    this.text = resultText.capitalize()
}

@BindingAdapter(value = ["matchNumber","roundNumber"], requireAll = true)
fun TextView.matchAndRoundNumber(matchNumber: Int, roundNumber: Int){
    this.text = "Match $matchNumber Round $roundNumber"
}