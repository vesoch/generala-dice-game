package dicegame.common;

import dicegame.common.models.Player;

import java.util.*;

public abstract class DiceGame {

    /**
     * Stores the possible combinations of the current game.
     */
    protected final Map<String, Long> COMBINATION_SCORES;

    private final Long numberOfPlayers;

    private final Long numberOfRounds;

    /**
     * List of all players.
     */
    protected List<Player> players;

    /**
     * Constructor
     */
    public DiceGame(Long numberOfPlayers, Long numberOfRounds)
    {
        this.numberOfPlayers = numberOfPlayers;
        this.numberOfRounds = numberOfRounds;
        this.players = new ArrayList<>();
        this.COMBINATION_SCORES = new HashMap<>();
    }

    /**
     * Adds a Player object to the players List.
     *
     * @param newPlayer The new Player object to be added.
     */
    public void addPlayer(Player newPlayer)
    {
        if (this.players.size() == numberOfPlayers) {
            throw new RuntimeException("Max number of players in game");
        }
        this.players.add(newPlayer);
    }

    /**
     * Retrieves the number of players
     * @return Long
     */
    public Long getNumberOfPlayers()
    {
        return this.numberOfPlayers;
    }

    /**
     * Retrieves the number of rounds
     * @return Long
     */
    public Long getNumberOfRounds()
    {
        return this.numberOfRounds;
    }

    /**
     * Returns a hashmap of player-scores in descending order, based on player score.
     *
     * @return List<Player>
     */
    public List<Player> getPlayersByScoreDescending()
    {
        List<Player> playersByScoreDescending = players;
        playersByScoreDescending.sort(Comparator.comparing(Player::getScore).reversed());

        return playersByScoreDescending;
    }

    /**
     * Retrieves the player with the highest score.
     *
     * @return Player
     */
    public Player getWinnerByScore()
    {
        return Collections.max(players, Comparator.comparing(Player::getScore));
    }
}
