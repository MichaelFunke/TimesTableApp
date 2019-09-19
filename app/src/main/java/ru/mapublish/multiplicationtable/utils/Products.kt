package ru.mapublish.multiplicationtable.utils

class Products {
    companion object {


        //STAGE ONE
        private val rI1 = Pair("rI_1", 1)
        private val rI2 = Pair("rI_2", 2)
        private val rII2 = Pair("rII_2", 2)
        private val rII4 = Pair("rII_4", 4)

        val LVL_ONE = listOf(
            rI1,
            rI2,
            rII2,
            rII4
        )

        // + STAGE TWO
        private val rI3 = Pair("rI_3", 3)
        private val rII6 = Pair("rII_6", 6)
        private val rIII3 = Pair("rIII_3", 3)
        private val rIII6 = Pair("rIII_6", 6)
        private val rIII9 = Pair("rIII_9", 9)

        val LVL_TWO = listOf(
            rI1,
            rI2,
            rII2,
            rII4,
            rI3,
            rII6,
            rIII3,
            rIII6,
            rIII9
        )

        // + STAGE THREE
        private val rI4 = Pair("rI_4", 4)
        private val rII8 = Pair("rII_8", 8)
        private val rIII12 = Pair("rIII_12", 12)
        private val rIV4 = Pair("rIV_4", 4)
        private val rIV8 = Pair("rIV_8", 8)
        private val rIV12 = Pair("rIV_12", 12)
        private val rIV16 = Pair("rIV_16", 16)

        val LVL_THREE = listOf(
            rI1,
            rI2,
            rII2,
            rII4,
            rI3,
            rII6,
            rIII3,
            rIII6,
            rIII9,
            rI4,
            rII8,
            rIII12,
            rIV4,
            rIV8,
            rIV12,
            rIV16
        )

        // + STAGE FOUR
        private val rI5 = Pair("rI_5", 5)
        private val rII10 = Pair("rII_10", 10)
        private val rIII15 = Pair("rIII_15", 15)
        private val rIV20 = Pair("rIV_20", 20)
        private val rV5 = Pair("rV_5", 5)
        private val rV10 = Pair("rV_10", 10)
        private val rV15 = Pair("rV_15", 15)
        private val rV20 = Pair("rV_20", 20)
        private val rV25 = Pair("rV_25", 25)

        val LVL_FOUR = listOf(
            rI1,
            rI2,
            rII2,
            rII4,
            rI3,
            rII6,
            rIII3,
            rIII6,
            rIII9,
            rI4,
            rII8,
            rIII12,
            rIV4,
            rIV8,
            rIV12,
            rIV16,
            rI5,
            rII10,
            rIII15,
            rIV20,
            rV5,
            rV10,
            rV15,
            rV20,
            rV25
        )

        // + STAGE FIVE
        private val rI6 = Pair("rI_6", 6)
        private val rII12 = Pair("rII_12", 12)
        private val rIII18 = Pair("rIII_18", 18)
        private val rIV24 = Pair("rIV_24", 24)
        private val rV30 = Pair("rV_30", 30)
        private val rVI6 = Pair("rVI_6", 6)
        private val rVI12 = Pair("rVI_12", 12)
        private val rVI18 = Pair("rVI_18", 18)
        private val rVI24 = Pair("rVI_24", 24)
        private val rVI30 = Pair("rVI_30", 30)
        private val rVI36 = Pair("rVI_36", 36)


        val LVL_FIVE = listOf(
            rI1,
            rI2,
            rII2,
            rII4,
            rI3,
            rII6,
            rIII3,
            rIII6,
            rIII9,
            rI4,
            rII8,
            rIII12,
            rIV4,
            rIV8,
            rIV12,
            rIV16,
            rI5,
            rII10,
            rIII15,
            rIV20,
            rV5,
            rV10,
            rV15,
            rV20,
            rV25,
            rI6,
            rII12,
            rIII18,
            rIV24,
            rV30,
            rVI6,
            rVI12,
            rVI18,
            rVI24,
            rVI30,
            rVI36
        )

        // + STAGE SIX
        private val rI7 = Pair("rI_7", 7)
        private val rII14 = Pair("rII_14", 14)
        private val rIII21 = Pair("rIII_21", 21)
        private val rIV28 = Pair("rIV_28", 28)
        private val rV35 = Pair("rV_35", 35)
        private val rVI42 = Pair("rVI_42", 42)
        private val rVII7 = Pair("rVII_7", 7)
        private val rVII14 = Pair("rVII_14", 14)
        private val rVII21 = Pair("rVII_21", 21)
        private val rVII28 = Pair("rVII_28", 28)
        private val rVII35 = Pair("rVII_35", 35)
        private val rVII42 = Pair("rVII_42", 42)
        private val rVII49 = Pair("rVII_49", 49)


        val LVL_SIX = listOf(
            rI1,
            rI2,
            rII2,
            rII4,
            rI3,
            rII6,
            rIII3,
            rIII6,
            rIII9,
            rI4,
            rII8,
            rIII12,
            rIV4,
            rIV8,
            rIV12,
            rIV16,
            rI5,
            rII10,
            rIII15,
            rIV20,
            rV5,
            rV10,
            rV15,
            rV20,
            rV25,
            rI6,
            rII12,
            rIII18,
            rIV24,
            rV30,
            rVI6,
            rVI12,
            rVI18,
            rVI24,
            rVI30,
            rVI36,
            rI7,
            rII14,
            rIII21,
            rIV28,
            rV35,
            rVI42,
            rVII7,
            rVII14,
            rVII21,
            rVII28,
            rVII35,
            rVII42,
            rVII49
        )

        // + STAGE SEVEN
        private val rI8 = Pair("rI_8", 8)
        private val rII16 = Pair("rII_16", 16)
        private val rIII24 = Pair("rIII_24", 24)
        private val rIV32 = Pair("rIV_32", 32)
        private val rV40 = Pair("rV_40", 40)
        private val rVI48 = Pair("rVI_48", 48)
        private val rVII56 = Pair("rVII_56", 56)
        private val rVIII8 = Pair("rVIII_8", 8)
        private val rVIII16 = Pair("rVIII_16", 16)
        private val rVIII24 = Pair("rVIII_24", 24)
        private val rVIII32 = Pair("rVIII_32", 32)
        private val rVIII40 = Pair("rVIII_40", 40)
        private val rVIII48 = Pair("rVIII_48", 48)
        private val rVIII56 = Pair("rVIII_56", 56)
        private val rVIII64 = Pair("rVIII_64", 64)


        val LVL_SEVEN = listOf(
            rI1,
            rI2,
            rII2,
            rII4,
            rI3,
            rII6,
            rIII3,
            rIII6,
            rIII9,
            rI4,
            rII8,
            rIII12,
            rIV4,
            rIV8,
            rIV12,
            rIV16,
            rI5,
            rII10,
            rIII15,
            rIV20,
            rV5,
            rV10,
            rV15,
            rV20,
            rV25,
            rI6,
            rII12,
            rIII18,
            rIV24,
            rV30,
            rVI6,
            rVI12,
            rVI18,
            rVI24,
            rVI30,
            rVI36,
            rI7,
            rII14,
            rIII21,
            rIV28,
            rV35,
            rVI42,
            rVII7,
            rVII14,
            rVII21,
            rVII28,
            rVII35,
            rVII42,
            rVII49,
            rI8,
            rII16,
            rIII24,
            rIV32,
            rV40,
            rVI48,
            rVII56,
            rVIII8,
            rVIII16,
            rVIII24,
            rVIII32,
            rVIII40,
            rVIII48,
            rVIII56,
            rVIII64
        )

        // + STAGE EIGHT
        private val rI9 = Pair("rI_9", 9)
        private val rII18 = Pair("rII_18", 18)
        private val rIII27 = Pair("rIII_27", 27)
        private val rIV36 = Pair("rIV_36", 36)
        private val rV45 = Pair("rV_45", 45)
        private val rVI54 = Pair("rVI_54", 54)
        private val rVII63 = Pair("rVII_63", 63)
        private val rIX9 = Pair("rIX_9", 9)
        private val rIX18 = Pair("rIX_18", 18)
        private val rIX27 = Pair("rIX_27", 27)
        private val rIX36 = Pair("rIX_36", 36)
        private val rIX45 = Pair("rIX_45", 45)
        private val rIX54 = Pair("rIX_54", 54)
        private val rIX63 = Pair("rIX_63", 63)
        private val rIX72 = Pair("rIX_72", 72)
        private val rIX81 = Pair("rIX_81", 81)
        private val rVIII72 = Pair("rVIII_72", 72)


        val LVL_EIGHT = listOf(
            rI1,
            rI2,
            rII2,
            rII4,
            rI3,
            rII6,
            rIII3,
            rIII6,
            rIII9,
            rI4,
            rII8,
            rIII12,
            rIV4,
            rIV8,
            rIV12,
            rIV16,
            rI5,
            rII10,
            rIII15,
            rIV20,
            rV5,
            rV10,
            rV15,
            rV20,
            rV25,
            rI6,
            rII12,
            rIII18,
            rIV24,
            rV30,
            rVI6,
            rVI12,
            rVI18,
            rVI24,
            rVI30,
            rVI36,
            rI7,
            rII14,
            rIII21,
            rIV28,
            rV35,
            rVI42,
            rVII7,
            rVII14,
            rVII21,
            rVII28,
            rVII35,
            rVII42,
            rVII49,
            rI8,
            rII16,
            rIII24,
            rIV32,
            rV40,
            rVI48,
            rVII56,
            rVIII8,
            rVIII16,
            rVIII24,
            rVIII32,
            rVIII40,
            rVIII48,
            rVIII56,
            rVIII64,
            rVIII72,
            rI9,
            rII18,
            rIII27,
            rIV36,
            rV45,
            rVI54,
            rVII63,
            rIX9,
            rIX18,
            rIX27,
            rIX36,
            rIX45,
            rIX54,
            rIX63,
            rIX72,
            rIX81
        )
    }
}