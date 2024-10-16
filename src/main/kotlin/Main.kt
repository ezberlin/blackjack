package org.example

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.main

fun clearConsole() {
    System.out.flush()
}
class BlackjackCommand: CliktCommand() {
    override fun run() {
        val blackjack = Blackjack()
        var input: String
        while (true) {
            input = readln()
            try {
                blackjack.availableOptionsList.forEach { if ( it[0] == input[0] ) input = it }
            } catch (_: StringIndexOutOfBoundsException) { }


            print("\u001b[H\u001b[2J")

            clearConsole()

            if (input in blackjack.availableOptionsList) {
                blackjack.optionMap[input]?.invoke()
            } else {
                echo("Please enter a valid command")
            }

            // Function to print cards
            fun printCards(cards: List<Card>, font: CardFont) {
                val cardLines = cards.map { font.visualize(it).lines() }
                val maxLines = cardLines.firstOrNull()?.size ?: 0
                for (i in 0 until maxLines) {
                    var printedLine = ""
                    for (card in cardLines) {
                        printedLine += "${card[i]} "
                    }
                    echo(printedLine)
                }
            }

            // Print dealer cards
            echo("Dealer's cards:")
            printCards(blackjack.dealerCards, trueCardFont)

            echo()

            echo("Player's cards:")
            printCards(blackjack.playerCards, trueCardFont)

            echo(when (blackjack.gameState) {
                "initialized" -> "Start a round!"
                "running" -> "Choose an option!"
                "win" -> "You won!"
                "lose" -> "You won!"
                "tie" -> "You tied!"
                "overflow" -> "You went over 21 and lost!"
                "blackjack" -> "You got a blackjack and won!"
                "five card charlie" -> "You got five cards and won!"
                else -> "An error occurred!"
            })

            var optionsOutput = "Options: "
            blackjack.availableOptionsList
                .forEach { optionsOutput += "[${it[0]}]${it.slice(1..it.lastIndex)} " }
            echo(optionsOutput)
        }
    }
}

fun main(args: Array<String>) = BlackjackCommand().main(args)