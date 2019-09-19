package ru.mapublish.multiplicationtable.views

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import ru.mapublish.multiplicationtable.R
import ru.mapublish.multiplicationtable.utils.Actions
import ru.mapublish.multiplicationtable.utils.Actions.CURRENT_LEVEL
import ru.mapublish.multiplicationtable.utils.Actions.CURRENT_SPEED
import ru.mapublish.multiplicationtable.utils.Actions.TOTAL_LEVEL
import ru.mapublish.multiplicationtable.utils.Actions.TOTAL_SPEED
import ru.mapublish.multiplicationtable.utils.readFromShPrefs
import ru.mapublish.multiplicationtable.utils.writeToShPrefs

class LevelSelectorView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    private val levelButtons: List<TextView>
    private val speedButtons: List<TextView>

    private val lvl1Btn: TextView
    private val lvl2Btn: TextView
    private val lvl3Btn: TextView
    private val lvl4Btn: TextView
    private val lvl5Btn: TextView
    private val lvl6Btn: TextView
    private val lvl7Btn: TextView
    private val lvl8Btn: TextView

    private val modeStandardTv: TextView
    private val modeTrueFalseTv: TextView

    private val speedX1Btn: TextView
    private val speedX2Btn: TextView
    private val speedX4Btn: TextView

    var currentLevel = readFromShPrefs(context, CURRENT_LEVEL)
    var currentSpeed = readFromShPrefs(context, CURRENT_SPEED)

    var totalLevel = readFromShPrefs(context, TOTAL_LEVEL)
    var totalSpeed = readFromShPrefs(context, TOTAL_SPEED)


    init {
        inflate(context, R.layout.level_selector_view, this)

        lvl1Btn = findViewById(R.id.lvl1_btn)
        lvl2Btn = findViewById(R.id.lvl2_btn)
        lvl3Btn = findViewById(R.id.lvl3_btn)
        lvl4Btn = findViewById(R.id.lvl4_btn)
        lvl5Btn = findViewById(R.id.lvl5_btn)
        lvl6Btn = findViewById(R.id.lvl6_btn)
        lvl7Btn = findViewById(R.id.lvl7_btn)
        lvl8Btn = findViewById(R.id.lvl8_btn)

        modeStandardTv = findViewById(R.id.standard_mode_tv)
        modeTrueFalseTv = findViewById(R.id.trueFalse_mode_tv)

        speedX1Btn = findViewById(R.id.speedX1_btn)
        speedX2Btn = findViewById(R.id.speedX2_btn)
        speedX4Btn = findViewById(R.id.speedX4_btn)


        levelButtons = listOf(
            lvl1Btn,
            lvl2Btn,
            lvl3Btn,
            lvl4Btn,
            lvl5Btn,
            lvl6Btn,
            lvl7Btn,
            lvl8Btn
        )
        speedButtons = listOf(
            speedX1Btn,
            speedX2Btn,
            speedX4Btn
        )

        //TODO delete!!!
//       writeToShPrefs(context, CURRENT_LEVEL, 9)
//       writeToShPrefs(context, CURRENT_SPEED, 2)

        currentLevel = readFromShPrefs(context, CURRENT_LEVEL)
        currentSpeed = readFromShPrefs(context, CURRENT_SPEED)

        totalLevel = readFromShPrefs(context, TOTAL_LEVEL)
        totalSpeed = readFromShPrefs(context, TOTAL_SPEED)

        Log.i("levels", "currentLevel is $currentLevel, currentSpeed is $currentSpeed")
        Log.i("levels", "totalLevel is $totalLevel, totalSpeed is $totalSpeed")

        enableLevelButtons(totalLevel, totalSpeed)
        manageLevelButtons(totalLevel)

        enableSpeedButtons(totalSpeed)
        manageSpeedButtons(totalSpeed)


        //selects the last game mode
        if (readFromShPrefs(context, Actions.MODE) == Actions.TRUEFALSE_MODE) {
            modeTrueFalseTv.isSelected = true
        } else {
            modeStandardTv.isSelected = true
        }


        //when pressed game starts in standard mode
        modeStandardTv.setOnClickListener {
            writeToShPrefs(context, Actions.MODE, Actions.STANDARD_MODE)
            changeModeButtonsColors(it)
        }

        //when pressed  game starts in true/false mode
        modeTrueFalseTv.setOnClickListener {
            writeToShPrefs(context, Actions.MODE, Actions.TRUEFALSE_MODE)
            changeModeButtonsColors(it)
        }


        lvl1Btn.setOnClickListener {
            manageLevelButtons(
                when (currentSpeed) {
                    1 -> 1
                    2 -> 9
                    else -> 17
                }
            )
        }

        lvl2Btn.setOnClickListener {
            manageLevelButtons(
                when (currentSpeed) {
                    1 -> 2
                    2 -> 10
                    else -> 18
                }
            )
        }
        lvl3Btn.setOnClickListener {
            manageLevelButtons(
                when (currentSpeed) {
                    1 -> 3
                    2 -> 11
                    else -> 19
                }
            )
        }
        lvl4Btn.setOnClickListener {
            manageLevelButtons(
                when (currentSpeed) {
                    1 -> 4
                    2 -> 12
                    else -> 20
                }
            )
        }
        lvl5Btn.setOnClickListener {
            manageLevelButtons(
                when (currentSpeed) {
                    1 -> 5
                    2 -> 13
                    else -> 21
                }
            )
        }
        lvl6Btn.setOnClickListener {
            manageLevelButtons(
                when (currentSpeed) {
                    1 -> 6
                    2 -> 14
                    else -> 22
                }
            )
        }
        lvl7Btn.setOnClickListener {
            manageLevelButtons(
                when (currentSpeed) {
                    1 -> 7
                    2 -> 15
                    else -> 23
                }
            )
        }
        lvl8Btn.setOnClickListener {
            manageLevelButtons(
                when (currentSpeed) {
                    1 -> 8
                    2 -> 16
                    else -> 24
                }
            )
        }


        speedX1Btn.setOnClickListener {
            manageLevelButtons(1)
            manageSpeedButtons(1)
            enableLevelButtons(totalLevel, 1)
        }

        speedX2Btn.setOnClickListener {
            manageLevelButtons(9)
            manageSpeedButtons(2)
            enableLevelButtons(totalLevel, 2)
        }

        speedX4Btn.setOnClickListener {
            manageLevelButtons(17)
            manageSpeedButtons(4)
            enableLevelButtons(totalLevel, 4)
        }
    }

    private fun changeModeButtonsColors(view: View) {
        when (view.id) {
            R.id.standard_mode_tv -> {
                modeStandardTv.isSelected = true
                modeTrueFalseTv.isSelected = false
            }
            R.id.trueFalse_mode_tv -> {
                modeStandardTv.isSelected = false
                modeTrueFalseTv.isSelected = true
            }
        }

    }

    private fun enableLevelButtons(level: Int, speed: Int) {
        Log.i("levels", "enableLevelButtons currentLevel is $level, currentSpeed is $speed")


        levelButtons.forEach { it.isEnabled = false }
        when (speed) {
            1 -> {
                when (level) {
                    1 -> {
                        lvl1Btn.isEnabled = true
                    }
                    2 -> {
                        lvl1Btn.isEnabled = true
                        lvl2Btn.isEnabled = true
                    }
                    3 -> {
                        lvl1Btn.isEnabled = true
                        lvl2Btn.isEnabled = true
                        lvl3Btn.isEnabled = true
                    }
                    4 -> {
                        lvl1Btn.isEnabled = true
                        lvl2Btn.isEnabled = true
                        lvl3Btn.isEnabled = true
                        lvl4Btn.isEnabled = true
                    }
                    5 -> {
                        lvl1Btn.isEnabled = true
                        lvl2Btn.isEnabled = true
                        lvl3Btn.isEnabled = true
                        lvl4Btn.isEnabled = true
                        lvl5Btn.isEnabled = true
                    }
                    6 -> {
                        lvl1Btn.isEnabled = true
                        lvl2Btn.isEnabled = true
                        lvl3Btn.isEnabled = true
                        lvl4Btn.isEnabled = true
                        lvl5Btn.isEnabled = true
                        lvl6Btn.isEnabled = true
                    }
                    7 -> {
                        lvl1Btn.isEnabled = true
                        lvl2Btn.isEnabled = true
                        lvl3Btn.isEnabled = true
                        lvl4Btn.isEnabled = true
                        lvl5Btn.isEnabled = true
                        lvl6Btn.isEnabled = true
                        lvl7Btn.isEnabled = true
                    }
                    else -> {
                        levelButtons.forEach { it.isEnabled = true }
                    }
                }
            }
            2 -> {
                when (level) {
                    9 -> {
                        lvl1Btn.isEnabled = true
                    }
                    10 -> {
                        lvl1Btn.isEnabled = true
                        lvl2Btn.isEnabled = true
                    }
                    11 -> {
                        lvl1Btn.isEnabled = true
                        lvl2Btn.isEnabled = true
                        lvl3Btn.isEnabled = true
                    }
                    12 -> {
                        lvl1Btn.isEnabled = true
                        lvl2Btn.isEnabled = true
                        lvl3Btn.isEnabled = true
                        lvl4Btn.isEnabled = true
                    }
                    13 -> {
                        lvl1Btn.isEnabled = true
                        lvl2Btn.isEnabled = true
                        lvl3Btn.isEnabled = true
                        lvl4Btn.isEnabled = true
                        lvl5Btn.isEnabled = true
                    }
                    14 -> {
                        lvl1Btn.isEnabled = true
                        lvl2Btn.isEnabled = true
                        lvl3Btn.isEnabled = true
                        lvl4Btn.isEnabled = true
                        lvl5Btn.isEnabled = true
                        lvl6Btn.isEnabled = true
                    }
                    15 -> {
                        lvl1Btn.isEnabled = true
                        lvl2Btn.isEnabled = true
                        lvl3Btn.isEnabled = true
                        lvl4Btn.isEnabled = true
                        lvl5Btn.isEnabled = true
                        lvl6Btn.isEnabled = true
                        lvl7Btn.isEnabled = true
                    }
                    else -> {
                        levelButtons.forEach { it.isEnabled = true }
                    }
                }
            }
            4 -> {
                when (level) {
                    17 -> {
                        lvl1Btn.isEnabled = true
                    }
                    18 -> {
                        lvl1Btn.isEnabled = true
                        lvl2Btn.isEnabled = true
                    }
                    19 -> {
                        lvl1Btn.isEnabled = true
                        lvl2Btn.isEnabled = true
                        lvl3Btn.isEnabled = true
                    }
                    20 -> {
                        lvl1Btn.isEnabled = true
                        lvl2Btn.isEnabled = true
                        lvl3Btn.isEnabled = true
                        lvl4Btn.isEnabled = true
                    }
                    21 -> {
                        lvl1Btn.isEnabled = true
                        lvl2Btn.isEnabled = true
                        lvl3Btn.isEnabled = true
                        lvl4Btn.isEnabled = true
                        lvl5Btn.isEnabled = true
                    }
                    22 -> {
                        lvl1Btn.isEnabled = true
                        lvl2Btn.isEnabled = true
                        lvl3Btn.isEnabled = true
                        lvl4Btn.isEnabled = true
                        lvl5Btn.isEnabled = true
                        lvl6Btn.isEnabled = true
                    }
                    23 -> {
                        lvl1Btn.isEnabled = true
                        lvl2Btn.isEnabled = true
                        lvl3Btn.isEnabled = true
                        lvl4Btn.isEnabled = true
                        lvl5Btn.isEnabled = true
                        lvl6Btn.isEnabled = true
                        lvl7Btn.isEnabled = true
                    }
                    else -> {
                        levelButtons.forEach { it.isEnabled = true }
                    }
                }
            }
        }

    }

    private fun enableSpeedButtons(speed: Int) {

        speedButtons.forEach { it.isEnabled = false }
        when (speed) {
            1 -> {
                speedX1Btn.isEnabled = true
            }
            2 -> {
                speedX1Btn.isEnabled = true
                speedX2Btn.isEnabled = true
            }
            else -> {
                speedX1Btn.isEnabled = true
                speedX2Btn.isEnabled = true
                speedX4Btn.isEnabled = true
            }
        }
    }

    private fun manageLevelButtons(level: Int) {
        Log.i("levels", "manageLevelButtons currentLevel is $level, totalLevel is $totalLevel, totalSpeed is $totalSpeed")


        writeToShPrefs(context, CURRENT_LEVEL, level)

        levelButtons.forEach { it.isSelected = false }
        when (level) {
            1, 9, 17 -> lvl1Btn.isSelected = true
            2, 10, 18 -> lvl2Btn.isSelected = true
            3, 11, 19 -> lvl3Btn.isSelected = true
            4, 12, 20 -> lvl4Btn.isSelected = true
            5, 13, 21 -> lvl5Btn.isSelected = true
            6, 14, 22 -> lvl6Btn.isSelected = true
            7, 15, 23 -> lvl7Btn.isSelected = true
            8, 16, 24 -> lvl8Btn.isSelected = true
        }
    }

    private fun manageSpeedButtons(speed: Int) {
        Log.i(
            "levels",
            "manageSpeedButtons currentSpeed is $speed, totalLevel is $totalLevel, totalSpeed is $totalSpeed"
        )


        writeToShPrefs(context, CURRENT_SPEED, speed)

        speedButtons.forEach { it.isSelected = false }
        when (speed) {
            1 -> speedX1Btn.isSelected = true
            2 -> speedX2Btn.isSelected = true
            4 -> speedX4Btn.isSelected = true
        }
    }

}