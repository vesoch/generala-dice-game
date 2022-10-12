package dicegame.generala;

import dicegame.Checks;
import dicegame.GameProperties;
import dicegame.Helper;
import dicegame.common.models.Controller;
import dicegame.common.models.Logger;
import dicegame.common.models.Player;
import dicegame.generala.models.GeneralaDie;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.Arrays;
import java.util.Map;
import java.util.ResourceBundle;

public class GeneralaController extends Controller {

    private Generala game;
    private Long currentRound, currentPlayer;

    @FXML
    private Button btnSetProperties, btnStartGame, btnRoll, btnNextPlayer, btnNextRound;

    @FXML
    private ImageView imageDie1, imageDie2, imageDie3, imageDie4, imageDie5;

    @FXML
    private TextField tfPlayers, tfRounds;

    @FXML
    private Label lblPlayer, lblRound, lblResult;

    @FXML
    private ListView lvLog, lvScoreBoard;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        this.properties = new GameProperties("Generala");
        this.logger = new Logger();

        this.btnRoll.setDisable(true);
        this.btnNextPlayer.setDisable(true);
        this.btnNextRound.setDisable(true);

        this.properties.readProperties();

        this.tfPlayers.setText(this.properties.getNumberOfPlayers().toString());
        this.tfRounds.setText(this.properties.getNumberOfRounds().toString());
    }

    //region Button clicks

    /**
     * Handles the Next Player button.
     */
    public void nextPlayer(ActionEvent e)
    {
        this.currentPlayer++;

        this.clearDice();
        this.btnNextPlayer.setDisable(true);
        updateLog(lvLog, "It's Player " + this.currentPlayer + "'s turn.");
        this.lblPlayer.setText("Player " + this.currentPlayer);

        this.btnRoll.setDisable(false);
    }

    /**
     * Handles the Next Round button.
     */
    public void nextRound(ActionEvent e)
    {
        this.currentRound++;

        this.clearDice();
        btnNextRound.setDisable(true);
        updateLog(lvLog, "Round " + this.currentRound + " started.");
        this.lblRound.setText("Round " + this.currentRound);

        this.currentPlayer = 0L;
        this.nextPlayer(e);
    }

    /**
     * Handles the Roll button.
     */
    public void rollDice(ActionEvent e)
    {
        // create a Die object and roll it 5 times
        GeneralaDie die = new GeneralaDie();
        int[] diceRolls = new int[5];
        for (int i = 0; i < diceRolls.length; i++) {
            diceRolls[i] = die.roll();
        }

        // sorts and visualizes the dice
        Arrays.sort(diceRolls);
        this.visualizeDice(diceRolls);

        // update the player's score in the list
        Map.Entry<Long, Long> scorePair = this.game.getDiceHandPoints(diceRolls);
        this.game.updatePlayerScore(currentPlayer, scorePair);
        this.updateScoreboard();

        // update log and result label
        String combinationName = this.game.getCombination(scorePair.getKey());
        this.lblResult.setText(combinationName);
        updateLog(lvLog, "Player " + this.currentPlayer + " rolled " + Arrays.toString(diceRolls));
        updateLog(lvLog, "Player " + this.currentPlayer + "'s combination is " + combinationName);

        // disable/enable buttons depending on the game state
        this.btnRoll.setDisable(true);

        if (this.game.getGeneralaWinner() == null) {
            if (this.currentPlayer != (long) this.game.getPlayersByScoreDescending().size()) {
                // end player turn
                this.btnNextPlayer.setDisable(false);
            } else {
                // end round
                updateLog(this.lvLog, "Round " + this.currentRound + " ended.");
                this.btnNextRound.setDisable(false);
            }
        } else {
            // end game due to a Generala being scored
            updateLog(lvLog, "Player " + this.game.getGeneralaWinner() + " wins since he scored a GENERALA");
            this.btnSetProperties.setDisable(false);
            this.btnStartGame.setDisable(false);
        }

        if (this.currentPlayer.equals(this.game.getNumberOfPlayers())
                && this.currentRound.equals(this.game.getNumberOfRounds())
        ) {
            // end game due to completing the las round
            updateLog(lvLog, "Player " + this.game.getWinnerByScore().getId() + " wins due to high score.");
            this.btnNextRound.setDisable(true);
            this.btnSetProperties.setDisable(false);
            this.btnStartGame.setDisable(false);
        }
    }

    /**
     * Sets the game properties to the
     *
     * @param e
     */
    public void setProperties(ActionEvent e)
    {
        if (tfPlayers.getText() != null && tfRounds.getText() != null
                && Checks.isNumeric(tfPlayers.getText()) && Checks.isNumeric(tfRounds.getText())
        ) {
            this.properties.setProperties(Long.parseLong(tfPlayers.getText()), Long.parseLong(tfRounds.getText()));

            updateLog(lvLog, "Properties changed! Players set to " + properties.getNumberOfPlayers()
                    + ", Rounds set to " + properties.getNumberOfRounds() + ".");
        } else {
            if (tfPlayers == null) {
                updateLog(lvLog, "Players cannot be set to 0.");
            } else if (!Checks.isNumeric(tfPlayers.getText())) {
                updateLog(lvLog, "Players number must be numeric.");
            }

            if (tfRounds == null) {
                updateLog(lvLog, "Rounds cannot be set to 0.");
            } else if (!Checks.isNumeric(tfRounds.getText())) {
                updateLog(lvLog, "Rounds number must be numeric.");
            }
        }
    }

    /**
     * Starts a Generala game
     */
    public void startGame(ActionEvent e)
    {
        // enable/disable buttons
        this.btnSetProperties.setDisable(true);
        this.btnStartGame.setDisable(true);
        this.btnRoll.setDisable(false);

        // initialize a new Generala game and populate it with players
        this.game = new Generala(this.properties.getNumberOfPlayers(), this.properties.getNumberOfRounds());
        for (Long i = 0L; i < this.game.getNumberOfPlayers(); i++) {
            try {
                Player p = new Player(i + 1L);
                this.game.addPlayer(p);
            } catch (RuntimeException ex) {
                ex.printStackTrace();
            }
        }

        // clear dice and reset current player/rounds if there was a game played before
        this.clearDice();
        this.updateScoreboard();
        this.currentPlayer = 1L;
        this.currentRound = 1L;

        // update labels
        this.lblRound.setText("Round " + this.currentRound);
        this.lblPlayer.setText("Player " + this.currentPlayer);

        // update log
        updateLog(this.lvLog, "Game started with "
                + this.game.getNumberOfPlayers() + " players and "
                + this.game.getNumberOfRounds() + " rounds.");
        updateLog(this.lvLog, "Round " + this.currentRound + " started.");
        updateLog(lvLog, "It's Player " + this.currentPlayer + "'s turn.");
    }
    //endregion

    //region Private methods

    /**
     * Clears the dice Image Views
     */
    private void clearDice()
    {
        this.imageDie1.setImage(null);
        this.imageDie2.setImage(null);
        this.imageDie3.setImage(null);
        this.imageDie4.setImage(null);
        this.imageDie5.setImage(null);
    }

    /**
     * Retrieves the image for the asked for side.
     *
     * @param side The side which image would be retrieved
     * @return Image
     */
    private Image getDieSide(int side)
    {
        return Helper.getImage("side" + side, "png");
    }

    /**
     * Updates the scoreboard List View with player scores in descending order.
     */
    private void updateScoreboard()
    {
        try {
            lvScoreBoard.getItems().clear();

            for (Player p : this.game.getPlayersByScoreDescending()) {
                lvScoreBoard.getItems().add(
                        "Player " + p.getId() + ": " + p.getScore()
                );
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    /**
     * Visualizes the dice into the Image Views.
     *
     * @param diceRolls Array with dice rolls.
     */
    private void visualizeDice(int[] diceRolls)
    {
        if (diceRolls.length > 5) {
            throw new IllegalArgumentException(
                    "The passed array size should be exactly 5"
            );
        }

        this.imageDie1.setImage(getDieSide(diceRolls[0]));
        this.imageDie2.setImage(getDieSide(diceRolls[1]));
        this.imageDie3.setImage(getDieSide(diceRolls[2]));
        this.imageDie4.setImage(getDieSide(diceRolls[3]));
        this.imageDie5.setImage(getDieSide(diceRolls[4]));
    }
    //endregion
}
