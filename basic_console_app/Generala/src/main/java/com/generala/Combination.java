package com.generala;

public class Combination {

    /**
     * Checks if the dice hand has recurring elements.
     * Pair(2), Triple(3), FourOfAKind(4), Generala(5)...
     *
     * @param diceRolls   The dice hand array.
     * @param recurringNr The number of wanted recurring elements.
     * @return int
     */
    public static int scoreRecurring(int[] diceRolls, int recurringNr)
    {
        for (int roll : diceRolls) {
            int counter = 0;
            for (int diceRoll : diceRolls) {
                if (diceRoll == roll) {
                    counter++;
                }
            }
            if (counter == recurringNr) {
                return roll;
            }
        }

        return 0;
    }

    /**
     * Checks if the dice hand is a Straight.
     *
     * @param diceRolls The dice hand array.
     * @return boolean
     */
    public static boolean scoreStraight(int[] diceRolls)
    {
        return diceRolls[0] == 1 &&
                diceRolls[1] == 2 &&
                diceRolls[2] == 3 &&
                diceRolls[3] == 4 &&
                diceRolls[4] == 5
                ||
                diceRolls[0] == 2 &&
                diceRolls[1] == 3 &&
                diceRolls[2] == 4 &&
                diceRolls[3] == 5 &&
                diceRolls[4] == 6;
    }
}
