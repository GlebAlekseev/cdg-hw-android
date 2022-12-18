package com.glebalekseevjk.premierleaguefixtures.ui

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.glebalekseevjk.premierleaguefixtures.R
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

@BindingAdapter("hourMinuteAsText")
fun TextView.hourMinuteAsText(date: String){
    val parseFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    val localDateTime = LocalDateTime.parse(date.substring(0,date.length-1),parseFormatter)
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    this.text = localDateTime.format(formatter)
}

@BindingAdapter("dayMonthYearAsText")
fun TextView.dayMonthYearAsTextAsText(date: String){
    val parseFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    val localDateTime = LocalDateTime.parse(date.substring(0,date.length-1), parseFormatter)
    val formatter = DateTimeFormatter.ofPattern("EEEE,\n dd MMMM uuuu")
    val resultText = localDateTime.format(formatter)
    this.text = resultText.capitalize()
}
