package ru.mapublish.multiplicationtable.screens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.mapublish.multiplicationtable.utils.Actions.GO_NEXT_LVL
import ru.mapublish.multiplicationtable.utils.Actions.REPEAT_LVL
import ru.mapublish.multiplicationtable.utils.Quotes

class GoNextLevelViewModel : ViewModel() {


    var quotes: MutableList<Pair<String, String>> = mutableListOf()



    private val _quote = MutableLiveData<String>()
    val quote: LiveData<String>
        get() = _quote

    private val _author = MutableLiveData<String>()
    val author: LiveData<String>
        get() = _author

    private val _goNextLevel = MutableLiveData<Char>()
    val goNextLevel: LiveData<Char>
        get() = _goNextLevel

    init {
        _quote.value = ""
        _author.value = ""
        _goNextLevel.value = '0'

        getNextQuote()
    }


    /**
     * gets new quote
     */
    private fun getNextQuote() {
        quotes.addAll(Quotes.quotes)
        quotes.shuffle()
        _quote.value = quotes[0].first
        _author.value = quotes[0].second
    }


    /**
     * runs the same level again
     */
    fun repeatLevel() {
        _goNextLevel.value = REPEAT_LVL
    }


    /**
     * stars new level
     */
    fun goNextLevel() {
        _goNextLevel.value = GO_NEXT_LVL
    }


}