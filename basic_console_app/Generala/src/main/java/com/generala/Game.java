package com.generala;

import java.io.IOException;
import java.util.*;

public class Game {

    private static Map<String, Long> COMBINATION_SCORES;
    private static Long generalaWinner;

    /**
     * The general method that implements the logic of the Generala game.
     */
    public static void startGame() throws IOException
    {
        System.out.println("Let's play Generala!");

        // set game properties
        Properties properties = new Properties();
        PropertiesManager.readGameProperties(properties);
        PropertiesManager.changeGameProperties(properties);

        // set combination score values
        setCombinationScores();

        // get number of players and rounds
        Long players = PropertiesManager.getNumberOfPlayers(properties);
        Long rounds = PropertiesManager.getNumberOfRounds(properties);

        // play game
        System.out.println("Generala begins with " + players + " players and " + rounds + " rounds.");
        HashMap<Long, Long> playerScorePairs = playGame(players, rounds);

        System.out.println("\n-----------------------------------------------------");
        System.out.println("Game finished!");

        // see game results
        showWinner(playerScorePairs);
        showScoreboard(playerScorePairs);
    }

    /**
     * Calculates the score for the passed dice array.
     *
     * @param diceRolls The dice hand array.
     * @return Long
     */
    private static Map.Entry<Long, Long> calculatePoints(int[] diceRolls)
    {
        Long combinationScore = 0L;
        Long sumScore;
        int recurringDie;

        if ((recurringDie = Combination.scoreRecurring(diceRolls, 5)) > 0) {
            Map.Entry<Long, Long> pointsGenerala = pointsGenerala(recurringDie);
            combinationScore = pointsGenerala.getKey();
            sumScore = pointsGenerala.getValue();

        } else if ((recurringDie = Combination.scoreRecurring(diceRolls, 4)) > 0) {
            Map.Entry<Long, Long> pointsFourOfAKind = pointsFourOfAKind(recurringDie);
            sumScore = pointsFourOfAKind.getKey();
            combinationScore = pointsFourOfAKind.getValue();

        } else if (Combination.scoreStraight(diceRolls)) {
            System.out.println("--Hand: Straight");
            combinationScore = COMBINATION_SCORES.get("Straight");
            sumScore = (long) Arrays.stream(diceRolls).sum();

        } else if ((recurringDie = Combination.scoreRecurring(diceRolls, 3)) > 0) {
            Map.Entry<Long, Long> pointsTriple = pointsTriple(recurringDie, diceRolls);
            sumScore = pointsTriple.getKey();
            combinationScore = pointsTriple.getValue();

        } else if ((recurringDie = Combination.scoreRecurring(diceRolls, 2)) > 0) {
            combinationScore = pointsPair(recurringDie, diceRolls).getKey();
            sumScore = pointsPair(recurringDie, diceRolls).getValue();

        } else {
            System.out.println("--Hand: Chance");
            sumScore = Arrays.stream(diceRolls).asLongStream().sum();
        }

        return new AbstractMap.SimpleEntry<>(combinationScore, sumScore);
    }

    /**
     * Retrieves the entry with the highest value.
     *
     * @param playerScorePairs A playerNumber-score hashset
     * @return Map.Entry<Long, Long>
     */
    private static Map.Entry<Long, Long> getWinner(Map<Long, Long> playerScorePairs)
    {
        Map.Entry<Long, Long> maxEntry = null;

        for (Map.Entry<Long, Long> entry : playerScorePairs.entrySet()) {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
                maxEntry = entry;
            }
        }

        return maxEntry;
    }

    /**
     * @param players The number of players that will play in this round
     * @param rounds The number of rounds that will be played
     * @return HashMap<Long, Long>
     */
    private static HashMap<Long, Long> playGame(Long players, Long rounds)
    {
        HashMap<Long, Long> playerScorePairs = new HashMap<>();

        for (Long r = 1L; r <= rounds; r++) {
            // play round
            System.out.println("\n-----------------------------------------------------");
            System.out.println("Round " + r + ":");
            playRound(players, playerScorePairs);

            // end game if a player scored Generala
            if (generalaWinner != null) {
                break;
            }
        }

        return playerScorePairs;
    }

    /**
     * Plays a round of Generala, going through all players and updating their scores.
     *
     * @param players The number of players that will play in this round
     * @param playerScorePairs A playerNumber-score hashset
     * @return HashMap<Long, Long>
     */
    private static HashMap<Long, Long> playRound(Long players, HashMap<Long, Long> playerScorePairs)
    {
        for (Long p = 1L; p <= players; p++) {
            // print player game stats
            System.out.println("\n-Player " + p + ":");

            // check if player exists
            if (!playerScorePairs.containsKey(p)) {
                playerScorePairs.put(p, 0L);
            }

            // print player current score
            System.out.println("--Current score:" + playerScorePairs.get(p));

            // roll dice
            int[] diceRolls = rollDice(5, 1, 6);
            System.out.println("--Dice roll:" + Arrays.toString(diceRolls));

            // calculate and print new score
            Map.Entry<Long, Long> scorePair = calculatePoints(diceRolls);
            Long newScore = playerScorePairs.get(p) + scorePair.getKey() + scorePair.getValue();
            playerScorePairs.replace(p, newScore);
            System.out.println("--New score: " + playerScorePairs.get(p));

            //set a winner if the player scored a Generala and end round
            if (scorePair.getKey() == 50L) {
                generalaWinner = p;
                break;
            }
        }

        return playerScorePairs;
    }

    /**
     *
     *
     * @param recurringDie The die that is recurring in the array
     * @return Map.Entry<Long, Long>
     */
    private static Map.Entry<Long, Long> pointsFourOfAKind(int recurringDie)
    {
        System.out.println("--Hand: Four of a Kind");

        return new AbstractMap.SimpleEntry<>(COMBINATION_SCORES.get("Four of a kind"), 4 * (long) recurringDie);
    }

    /**
     *
     *
     * @param recurringDie The die that is recurring in the array
     * @return Map.Entry<Long, Long>
     */
    private static Map.Entry<Long, Long> pointsGenerala(int recurringDie)
    {
        System.out.println("--Hand: GENERALA!");

        return new AbstractMap.SimpleEntry<>(COMBINATION_SCORES.get("Generala"), 5 * (long) recurringDie);
    }

    /**
     * @param recurringDie
     * @return
     */
    private static Map.Entry<Long, Long> pointsPair(int recurringDie, int[] diceRolls)
    {
        Long sumScore = 2 * (long) recurringDie;
        int[] reducedDiceRolls = Arrays.stream(diceRolls).filter(
                val -> val != Combination.scoreRecurring(diceRolls, 2)
        ).toArray();

        // check for double pair
        if (Combination.scoreRecurring(reducedDiceRolls, 2) > 0) {
            System.out.println("--Hand: Double Pair");
            sumScore += 2 * (long) Combination.scoreRecurring(reducedDiceRolls, 2);

            return new AbstractMap.SimpleEntry<>(COMBINATION_SCORES.get("Double pair"), sumScore);
        } else {
            System.out.println("--Hand: Pair");

            return new AbstractMap.SimpleEntry<>(COMBINATION_SCORES.get("Pair"), sumScore);
        }
    }

    /**
     * Re
     *
     * @param recurringDie The die that is recurring in the array
     * @return int
     */
    private static Map.Entry<Long, Long> pointsTriple(int recurringDie, int[] diceRolls)
    {
        int recurring;
        Long sumScore = 3 * (long) recurringDie;
        int[] reducedDiceRolls = Arrays.stream(diceRolls).filter(val -> val != recurringDie).toArray();

        // check for full house
        if ((recurring = Combination.scoreRecurring(reducedDiceRolls, 2)) > 0) {
            System.out.println("--Hand: Full House");
            sumScore += 2 * (long) recurring;

            return new AbstractMap.SimpleEntry<>(COMBINATION_SCORES.get("Full house"), sumScore);

        } else {
            System.out.println("--Hand: Triple");

            return new AbstractMap.SimpleEntry<>(COMBINATION_SCORES.get("Triple"), sumScore);
        }
    }

    /**
     * Generates a ascending sorted array of nrOfDice elements with random
     * integers between minRoll and maxRoll to simulate dice throw.
     *
     * @return int[]
     */
    private static int[] rollDice(int nrOfDice, int minRoll, int maxRoll)
    {
        int[] diceRolls = new int[nrOfDice];

        for (int i = 0; i < nrOfDice; i++) {
            diceRolls[i] = (int) Math.floor(Math.random() * (maxRoll - minRoll + 1) + minRoll);
        }

        Arrays.sort(diceRolls);
        return diceRolls;
    }

    /**
     * Populates the COMBINATIONS_SCORES
     */
    private static void setCombinationScores()
    {
        COMBINATION_SCORES = new HashMap<>();
        COMBINATION_SCORES.put("Pair", 10L);
        COMBINATION_SCORES.put("Double pair", 15L);
        COMBINATION_SCORES.put("Triple", 20L);
        COMBINATION_SCORES.put("Full house", 25L);
        COMBINATION_SCORES.put("Straight", 30L);
        COMBINATION_SCORES.put("Four of a kind", 40L);
        COMBINATION_SCORES.put("Generala", 50L);
    }

    /**
     * Displays a scoreboard of all players and their points.
     *
     * @param playerScorePairs A playerNumber-score hashset
     */
    private static void showScoreboard(HashMap<Long, Long> playerScorePairs)
    {
        System.out.println("\nScoreboard:");
        for (Map.Entry<Long, Long> entry : Helper.sortHashMapByValuesDescending(playerScorePairs).entrySet()) {
            System.out.println("Player " + entry.getKey() + ":\t" + entry.getValue());
        }
    }

    /**
     * Displays the winner of the game, depending on the highest score
     * or if a player has scored a Generala.
     *
     * @param playerScorePairs A playerNumber-score hashset
     */
    private static void showWinner(HashMap<Long, Long> playerScorePairs)
    {
        if (generalaWinner != null) {
            System.out.println("Player " + generalaWinner + " won, since he scored Generala!");
        } else {
            Map.Entry<Long, Long> winner = getWinner(playerScorePairs);
            System.out.println("Player " + winner.getKey() + " won, since he has the highest score of "
                    + winner.getValue() + ".");
        }
    }
}
