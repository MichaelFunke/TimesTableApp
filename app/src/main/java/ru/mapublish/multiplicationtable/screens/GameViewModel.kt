package ru.mapublish.multiplicationtable.screens

import android.os.CountDownTimer
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.mapublish.multiplicationtable.utils.OnKeyClick
import ru.mapublish.multiplicationtable.utils.Actions
import ru.mapublish.multiplicationtable.utils.Actions.GO_NEXT_STAGE
import ru.mapublish.multiplicationtable.utils.Actions.SPEEDx1
import ru.mapublish.multiplicationtable.utils.Actions.SPEEDx2
import ru.mapublish.multiplicationtable.utils.Actions.SPEEDx4
import ru.mapublish.multiplicationtable.utils.Actions.STANDARD_MODE
import ru.mapublish.multiplicationtable.utils.Actions.TRUEFALSE_MODE
import ru.mapublish.multiplicationtable.utils.Products
import ru.mapublish.multiplicationtable.utils.randomize
import java.util.concurrent.TimeUnit


class GameViewModel(level: Int, speed: Int, private val mode: Int) : ViewModel() {

    // sent to the GoNextLevelFragment to compute the average success
    private var _numberOfIncorrectAnswers = MutableLiveData<Int>()
    val numberOfIncorrectAnswers: LiveData<Int>
        get() = _numberOfIncorrectAnswers

    // user's keyboard input
    val textObservable = ObservableField<String?>()

    // keeps all the TextView's Ids and products that are shown in the table
    private val listOfBoxIdsAndProducts: MutableList<Pair<String, Int>> = mutableListOf()

    private val _goNextStageFlag = MutableLiveData<String>()
    val goNextStageFlag: LiveData<String>
        get() = _goNextStageFlag

    //id of the actual TextView
    private val _tvId = MutableLiveData<String>()
    val tvId: LiveData<String>
        get() = _tvId

    //product to show in a TextView with a matching Id
    private val _product = MutableLiveData<Int>()
    val product: LiveData<Int>
        get() = _product

    private val _isAnswerCorrect = MutableLiveData<Boolean>()
    val isAnswerCorrect: LiveData<Boolean>
        get() = _isAnswerCorrect

    //amount of cheats available to the player
    private val _cheats = MutableLiveData<Int>()
    val cheats: LiveData<Int>
        get() = _cheats

    // Countdown time
    private var _countDownTime = TimeUnit.SECONDS.toMillis(10)
    val countDownTime: Long
        get() = _countDownTime

    private val timer: CountDownTimer

    //keeps the true value of the product in True/False mode
    private var tempProductValue: Int? = 0

    init {
        _tvId.value = ""
        _product.value = 0
        _goNextStageFlag.value = ""
        _numberOfIncorrectAnswers.value = 0

        _countDownTime = TimeUnit.SECONDS.toMillis(
            when (speed) {
                SPEEDx1 -> 10
                SPEEDx2 -> 6
                SPEEDx4 -> 3
                else -> 10
            }
        )

        // defines the amount of cheats depending on the lvl
        _cheats.value = when (level) {
            1, 2, 3, 9, 10, 11, 17, 18, 19 -> 2
            4, 5, 6, 12, 13, 14, 20, 21, 22, 24 -> 3
            7, 15, 23 -> 4
            else -> 5
        }

        timer = object : CountDownTimer(
            countDownTime,
            ONE_SECOND
        ) {
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                onTimeRanOut()
            }
        }
        timer.start()
    }

    override fun onCleared() {
        super.onCleared()
        timer.cancel()
    }

    fun onCheatBtn() {
        if (_cheats.value != 0) {
            _cheats.value = _cheats.value?.minus(1)
            textObservable.set(_product.value.toString())
        }
    }

    companion object {
        // Countdown time interval
        private const val ONE_SECOND = 1000L
    }

    private fun nextQuestion() {
        if (listOfBoxIdsAndProducts.isEmpty()) {
            _goNextStageFlag.value = GO_NEXT_STAGE
        } else {
            // if game is in the standard mode value goes in the table without changing...
            _product.value = listOfBoxIdsAndProducts[0].second
            //...otherwise it is "randomized"
            if (mode == TRUEFALSE_MODE) {
                tempProductValue = _product.value
                _product.value = randomize(listOfBoxIdsAndProducts[0].second)
                Log.i(
                    "truefalse else",
                    "_product.value is ${_product.value}, tempProductValue is $tempProductValue"
                )
            }

            //this field should stay after the _product because it's an observable and causes UI changes
            _tvId.value = listOfBoxIdsAndProducts[0].first

            listOfBoxIdsAndProducts.removeAt(0)
        }
    }

    /**
     * adds next products to the productList at the beginning of a Stage and shuffles them
     */
    fun resetList(stage: Int) {
        listOfBoxIdsAndProducts.addAll(
            when (stage) {
                1, 9, 17 -> Products.LVL_ONE
                2, 10, 18 -> Products.LVL_TWO
                3, 11, 19 -> Products.LVL_THREE
                4, 12, 20 -> Products.LVL_FOUR
                5, 13, 21 -> Products.LVL_FIVE
                6, 14, 22 -> Products.LVL_SIX
                7, 15, 23 -> Products.LVL_SEVEN
                8, 16, 24 -> Products.LVL_EIGHT
                else -> Products.LVL_EIGHT
            }
        )
        listOfBoxIdsAndProducts.shuffle()
        nextQuestion()
    }

    /**
     * attached to the sendAnswer ImageView in XML. Checks if the player's answer was isAnswerCorrect and assigns true or false to the _isAnswerCorrect field
     */
    fun validateAnswer(message: Char) {

        when (message) {
            //when in Standard mode - handles Send button
            'S' -> {
                if (textObservable.get().isNullOrEmpty()) return
                _isAnswerCorrect.value = textObservable.get() == _product.value.toString()
                // increments numberOfIncorrectAnswers if the answer was wrong
                if (!_isAnswerCorrect.value!!) _numberOfIncorrectAnswers.value = _numberOfIncorrectAnswers.value!! + 1
            }
            //when in TrueFalse mode - handles button True
            'T' -> {
                // ==
                if (tempProductValue == _product.value) {
                    _product.value = tempProductValue
                    _isAnswerCorrect.value = true
                } else {
                    _isAnswerCorrect.value = false
                    _numberOfIncorrectAnswers.value = _numberOfIncorrectAnswers.value!! + 1
                }
            }
            //when in TrueFalse mode - handles button False
            'F' -> {
                // !=
                if (tempProductValue != _product.value) {
                    _product.value = tempProductValue
                    _isAnswerCorrect.value = true
                } else {
                    _numberOfIncorrectAnswers.value = _numberOfIncorrectAnswers.value!! + 1
                    _isAnswerCorrect.value = false
                }
            }
        }

        timer.cancel()
        timer.start()

        nextQuestion()
        textObservable.set("")
    }

    fun onTimeRanOut() {
        // change values to call onWrongAnswer() and showQuestionMark() in GameFragment
        _isAnswerCorrect.value = false
        _tvId.value = "zero"

        nextQuestion()
        textObservable.set("")

        timer.start()
    }

    /**
     * key listener for the custom keyboard
     */
    val onKeyClick: OnKeyClick = object :
        OnKeyClick {
        override fun onKeyClick(key: Char) {
            receiveText(key.toString())
        }
    }

    /**
     * puts and removes text in EditText field through textObservable
     */
    fun receiveText(text: String) {
        if (text.toCharArray().first() == Actions.ERASE_TEXT) {
            if (textObservable.get().isNullOrEmpty()) textObservable.set("")
            //removes by one symbol
            else textObservable.set(
                textObservable.get()?.substring(
                    0,
                    textObservable.get()?.length!!.minus(1)
                )
            )
        } else {
            if (textObservable.get().isNullOrEmpty()) textObservable.set(text)
            else textObservable.set(textObservable.get().plus(text))
        }
    }
}
