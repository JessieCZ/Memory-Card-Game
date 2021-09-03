package com.example.mymemory.models

import com.example.mymemory.utils.DEFAULT_ICONS
import com.rkpandey.mymemory.models.BoardSize

class MemoryGame(private val boardSize: BoardSize) {

    val cards: List<MemoryCard>
    var numPairsFound = 0
    private var numCardFlips = 0
    private var indexOfSingleSelectedCard: Int ?= null

    init {
        val chosenImages = DEFAULT_ICONS.shuffled().take(boardSize.getNumPairs())
        val randomizedImaged = (chosenImages + chosenImages).shuffled()
        cards = randomizedImaged.map { MemoryCard(it) }

    }

    fun flipCard(position: Int): Boolean {
        numCardFlips++
        val card = cards[position]
        // Three cases
        // 0 cards previously flipped over => flip over selected cards
        // 1 cards previously flipped over => flip over the selected cards + check if the images match
        // 2 cards previously flipped over => restore cards + flip over the selected card
        var foundMatch = false
        if (indexOfSingleSelectedCard == null) {
            // 0 card flipped over OR 2 cards flipped over
            restoreCards()
            indexOfSingleSelectedCard = position
        } else {
            foundMatch = checkedForMatch(indexOfSingleSelectedCard!!, position)
            indexOfSingleSelectedCard = null
        }
        card.isFaceUp = !card.isFaceUp
        return foundMatch

    }

    private fun checkedForMatch(position1: Int, position2: Int): Boolean {
        if (cards[position1].identifier != cards[position2].identifier) {
            return false
        }
        cards[position1].isMatched = true
        cards[position2].isMatched = true
        numPairsFound ++
        return true

    }

    private fun restoreCards() {
        for (card in cards) {
            if (!card.isMatched) {
                card.isFaceUp = false
            }
        }
    }

    fun haveWonGame(): Boolean {
        return numPairsFound == boardSize.getNumPairs()

    }

    fun isCardFaceUp(position: Int): Boolean {
        return cards[position].isFaceUp
    }

    fun getNumMoves(): Int {
        return numCardFlips / 2

    }
}