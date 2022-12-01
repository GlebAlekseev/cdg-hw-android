package example1

import kotlin.math.abs

// 1. Описал класс, хранящий информацию о футбольном матче
// 2. Функция? - конструктор дата класса
data class FootballMatchInfo(
    val firstTeamScore: Int,
    val secondTeamScore: Int
){
    companion object{
        val SCORE_RANGE = 0..5
    }
}

fun Collection<FootballMatchInfo>.getMaxGap(): Int {
    return this.fold(0){ acc, footballMatchInfo ->
        if (acc == FootballMatchInfo.SCORE_RANGE.last) return acc
        val difference = abs(footballMatchInfo.firstTeamScore - footballMatchInfo.secondTeamScore)
        return@fold if (acc < difference) difference else acc
    }
}

fun main() {
    // 3. Массив из 10ти матчей
    val list = List(10) {
        FootballMatchInfo(
            firstTeamScore = FootballMatchInfo.SCORE_RANGE.random(),
            secondTeamScore = FootballMatchInfo.SCORE_RANGE.random()
        )
    }
    println("Массив из 10ти матчей: \n$list")
    // 4. Удалить матчи с одинаковыми очками
    // Преобразования к Set (Множеству) достаточно, т.к. equals в data class переопределяется
    // и идет сравнение не только по ссылке, но и по содержимому
    val set = list.toSet()
    println("Коллекция без совпадений очков команд: \n$set")
    // 5. Создать множество с максимальным разрывом в очках
    val maxGap = set.getMaxGap()
    println("Максимальный разрыв в очках: $maxGap")
    val result = set.filter { abs(it.firstTeamScore-it.secondTeamScore) == maxGap }
    println("Множество с максимальным разрывом в очках: \n$result")
}

