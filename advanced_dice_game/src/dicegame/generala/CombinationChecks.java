package dicegame.generala;

public class CombinationChecks {

    /**
     * Checks if the dice hand has recurring dice.
     * Pair(2), Triple(3), FourOfAKind(4), Generala(5)...
     * <p>
     * Returns the face which is recurring {recurringNumber} times.
     *
     * @param diceRolls       The dice hand array.
     * @param recurringNumber The number of wanted recurring elements.
     * @return int
     */
    public static int scoreRecurringFace(int[] diceRolls, int recurringNumber)
    {
        for (int roll : diceRolls) {
            int counter = 0;
            for (int diceRoll : diceRolls) {
                if (diceRoll == roll) {
                    counter++;
                }
            }
            if (counter == recurringNumber) {
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
