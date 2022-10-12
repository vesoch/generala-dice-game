package dicegame;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class GameProperties {

    private final Properties properties;
    private final String gameName;

    /**
     * Constructor
     */
    public GameProperties(String gameName)
    {
        this.gameName = gameName;
        this.properties = new Properties();
    }

    /**
     * Retrieves the number of rounds from the games.properties file.
     *
     * @return Long
     */
    public Long getNumberOfPlayers()
    {
        return Long.parseLong(properties.getProperty("players"));
    }

    /**
     * Retrieves the number of rounds from the games.properties file.
     *
     * @return Long
     */
    public Long getNumberOfRounds()
    {
        return Long.parseLong(properties.getProperty("rounds"));
    }

    /**
     * Load the properties file.
     */
    public void readProperties()
    {
        try {
            InputStream is = new FileInputStream(this.getPropertiesPath());
            properties.load(is);
        } catch (IOException e) {
            if (e instanceof FileNotFoundException) {
                createProperties();
            } else {
                e.printStackTrace();
            }
        }
    }

    /**
     * Creates/Updates the properties files.
     *
     * @param numberOfPlayers The number of players.
     * @param numberOfRounds  The number of rounds.
     */
    public void setProperties(Long numberOfPlayers, Long numberOfRounds)
    {
        try {
            OutputStream os = new FileOutputStream(getPropertiesPath());
            properties.setProperty("players", String.valueOf(numberOfPlayers));
            properties.setProperty("rounds", String.valueOf(numberOfRounds));
            properties.store(os, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a default properties file with 0 players and 0 rounds.
     */
    private void createProperties()
    {
        setProperties(0L, 0L);
    }

    /**
     * Retrieves the proper path for game.properties, depending if the program is ran from a jar or through the IDE.
     *
     * @return String
     */
    private String getPropertiesPath()
    {
        Path path = Paths.get("src/main/resources");
        return Files.exists(path)
                ? "src/main/resources/" + gameName + ".properties"
                : gameName + ".properties";
    }
}
