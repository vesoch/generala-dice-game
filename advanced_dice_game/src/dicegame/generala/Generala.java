package dicegame.generala;

import dicegame.common.DiceGame;
import dicegame.common.models.Player;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

public class Generala extends DiceGame {

    /**
     * Stores the id of the player that scored a Generala, if any.
     */
    private Player generalaWinner;

    /**
     * Constructor
     */
    public Generala(Long numberOfPlayers, Long numberOfRounds)
    {
        super(numberOfPlayers, numberOfRounds);
        this.COMBINATION_SCORES.put("pair", 10L);
        this.COMBINATION_SCORES.put("doublePair", 15L);
        this.COMBINATION_SCORES.put("triple", 20L);
        this.COMBINATION_SCORES.put("fullHouse", 25L);
        this.COMBINATION_SCORES.put("straight", 30L);
        this.COMBINATION_SCORES.put("fourOfAKind", 40L);
        this.COMBINATION_SCORES.put("generala", 50L);
    }

    /**
     * Retrieves the combination name, based on the score.
     *
     * @param combinationScore The combination score.
     * @return String
     */
    public String getCombination(Long combinationScore)
    {
        if (combinationScore == 10L) {
            return "Pair";
        } else if (combinationScore == 15L) {
            return "Double Pair";
        } else if (combinationScore == 20L) {
            return "Triple";
        } else if (combinationScore == 25L) {
            return "Full House";
        } else if (combinationScore == 30L) {
            return "Straight";
        } else if (combinationScore == 40L) {
            return "Four Of A Kind";
        } else if (combinationScore == 50L) {
            return "GENERALA";
        }
        return "Chance";
    }

    /**
     * Calculates the score for the passed dice array.
     *
     * @param diceRolls The dice hand array.
     * @return Long
     */
    public Map.Entry<Long, Long> getDiceHandPoints(int[] diceRolls)
    {
        Long combinationScore = 0L;
        Long sumScore;
        int recurringDie;

        if ((recurringDie = CombinationChecks.scoreRecurringFace(diceRolls, 5)) > 0) {
            Map.Entry<Long, Long> pointsGenerala = pointsGenerala(recurringDie);
            combinationScore = pointsGenerala.getKey();
            sumScore = pointsGenerala.getValue();

        } else if ((recurringDie = CombinationChecks.scoreRecurringFace(diceRolls, 4)) > 0) {
            Map.Entry<Long, Long> pointsFourOfAKind = pointsFourOfAKind(recurringDie);
            combinationScore = pointsFourOfAKind.getKey();
            sumScore = pointsFourOfAKind.getValue();

        } else if (CombinationChecks.scoreStraight(diceRolls)) {
            combinationScore = COMBINATION_SCORES.get("straight");
            sumScore = (long) Arrays.stream(diceRolls).sum();

        } else if ((recurringDie = CombinationChecks.scoreRecurringFace(diceRolls, 3)) > 0) {
            Map.Entry<Long, Long> pointsTriple = pointsTriple(recurringDie, diceRolls);
            combinationScore = pointsTriple.getKey();
            sumScore = pointsTriple.getValue();

        } else if ((recurringDie = CombinationChecks.scoreRecurringFace(diceRolls, 2)) > 0) {
            combinationScore = pointsPair(recurringDie, diceRolls).getKey();
            sumScore = pointsPair(recurringDie, diceRolls).getValue();

        } else {
            sumScore = Arrays.stream(diceRolls).asLongStream().sum();
        }

        return new AbstractMap.SimpleEntry<>(combinationScore, sumScore);
    }

    /**
     * Retrieves the player who has won by getting a Generala hand.
     *
     * @return Player
     */
    public Player getGeneralaWinner()
    {
        return generalaWinner;
    }

    /**
     * Finds a player by Id and updates his score.
     * Sets a Generala winner if the player has scored a Generala.
     *
     * @param playerId  The Id of the searched for player.
     * @param scorePair A hashmap pair of the combination and sum.
     */
    public void updatePlayerScore(Long playerId, Map.Entry<Long, Long> scorePair)
    {
        Optional<Player> playerFound = players.stream().filter(p -> p.getId().equals(playerId)).findFirst();

        if (playerFound.isPresent()) {
            Player player = playerFound.get();

            Long newScore = scorePair.getKey() + scorePair.getValue();
            player.addToScore(newScore);

            if (scorePair.getKey() == 50L) {
                this.generalaWinner = player;
            }
        }
    }

    /**
     * Returns a map of the combination and sum scores of a Four of a Kind combination.
     *
     * @param recurringDie The die that is recurring in the array
     * @return Map.Entry<Long, Long>
     */
    private Map.Entry<Long, Long> pointsFourOfAKind(int recurringDie)
    {
        return new AbstractMap.SimpleEntry<>(COMBINATION_SCORES.get("fourOfAKind"), 4 * (long) recurringDie);
    }

    /**
     * Returns a map of the combination and sum scores of a Generala combination.
     *
     * @param recurringDie The die that is recurring in the array
     * @return Map.Entry<Long, Long>
     */
    private Map.Entry<Long, Long> pointsGenerala(int recurringDie)
    {
        return new AbstractMap.SimpleEntry<>(COMBINATION_SCORES.get("generala"), 5 * (long) recurringDie);
    }

    /**
     * Checks if the hand is a Pair or Double Pair combination.
     * Returns a map of the combination and sum scores.
     *
     * @param recurringDie The die that is recurring in the array
     * @return Map.Entry<Long, Long>
     */
    private Map.Entry<Long, Long> pointsPair(int recurringDie, int[] diceRolls)
    {
        Long sumScore = 2 * (long) recurringDie;
        int[] reducedDiceRolls = Arrays.stream(diceRolls).filter(
                val -> val != CombinationChecks.scoreRecurringFace(diceRolls, 2)
        ).toArray();

        // check for double pair
        if (CombinationChecks.scoreRecurringFace(reducedDiceRolls, 2) > 0) {
            sumScore += 2 * (long) CombinationChecks.scoreRecurringFace(reducedDiceRolls, 2);
            return new AbstractMap.SimpleEntry<>(COMBINATION_SCORES.get("doublePair"), sumScore);
        } else {
            return new AbstractMap.SimpleEntry<>(COMBINATION_SCORES.get("pair"), sumScore);
        }
    }

    /**
     * Checks if the hand is a Triple or Full House combination.
     * Returns a map of the combination and sum scores.
     *
     * @param recurringDie The die that is recurring in the array
     * @return Map.Entry<Long, Long>
     */
    private Map.Entry<Long, Long> pointsTriple(int recurringDie, int[] diceRolls)
    {
        int recurring;
        Long sumScore = 3 * (long) recurringDie;
        int[] reducedDiceRolls = Arrays.stream(diceRolls).filter(val -> val != recurringDie).toArray();

        // check for full house
        if ((recurring = CombinationChecks.scoreRecurringFace(reducedDiceRolls, 2)) > 0) {
            sumScore += 2 * (long) recurring;

            return new AbstractMap.SimpleEntry<>(COMBINATION_SCORES.get("fullHouse"), sumScore);
        } else {
            return new AbstractMap.SimpleEntry<>(COMBINATION_SCORES.get("triple"), sumScore);
        }
    }
}
