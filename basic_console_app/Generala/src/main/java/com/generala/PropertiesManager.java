package com.generala;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class PropertiesManager {

    public static BufferedReader reader = new BufferedReader(
            new InputStreamReader(System.in)
    );

    /**
     * A calls a looping "user interface" that allows users to change the game properties.
     *
     * @param properties
     */
    public static void changeGameProperties(Properties properties)
    {
        boolean propertiesSatisfactory = false;

        try {
            while (!propertiesSatisfactory) {
                System.out.println("The game properties are set with "
                        + getNumberOfPlayers(properties) + " players and "
                        + getNumberOfRounds(properties) + " rounds. Do you wish to change them? (Y/N)");
                String s =  reader.readLine().replaceAll("\\s+", "");

                while (!Helper.isYesNoAnswer(s)) {
                    System.out.println("Please type Y or N.");
                    System.out.println("Do you wish to change the game properties? (Y/N)");
                    s = reader.readLine().replaceAll("\\s+", "");
                }
                if (s.equals("Y") || s.equals("y")) {
                    updateGameProperties(properties);
                } else {
                    propertiesSatisfactory = true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the number of rounds from the games.properties file.
     *
     * @param properties
     * @return Long
     */
    public static Long getNumberOfPlayers(Properties properties)
    {
        return Long.parseLong(properties.getProperty("players"));
    }

    /**
     * Retrieves the number of rounds from the games.properties file.
     *
     * @param properties
     * @return Long
     */
    public static Long getNumberOfRounds(Properties properties)
    {
        return Long.parseLong(properties.getProperty("rounds"));
    }

    /**
     * Retrieves the proper path for game.properties, depending if the program is ran from a jar or through the IDE.
     *
     * @return String
     */
    public static String getPropertiesPath()
    {
        Path path = Paths.get("src/main/resources");
        return Files.exists(path) ? "src/main/resources/game.properties" : "game.properties";
    }

    /**
     * Loads the game.properties file.
     *
     * @param properties The Properties object on which the file will be loaded.
     */
    public static void readGameProperties(Properties properties)
    {
        InputStream is;

        try {
            is = new FileInputStream(getPropertiesPath());
            properties.load(is);
        } catch (IOException e) {
            // if the file does not exist create it and set the properties' values.
            if (e instanceof FileNotFoundException) {
                updateGameProperties(properties);
            } else {
                e.printStackTrace();
            }
        }
    }

    /**
     * Creates or updates the game.properties file and the values in it.
     *
     * @param properties
     * @throws IOException
     */
    public static void updateGameProperties(Properties properties)
    {
        OutputStream os = null;
        String s;

        try {
            os = new FileOutputStream(getPropertiesPath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // set number of players
        System.out.println("Please set the number of players for your Generala game: ");

        try {
            s = reader.readLine().replaceAll("\\s+", "");
            while (!Helper.isNumeric(s)) {
                System.out.println("The given value for players is not numeric");
                System.out.println("Please set the number of players for your Generala game: ");
                s = reader.readLine().replaceAll("\\s+", "");
            }

            Long nrPlayers = Long.parseLong(s);
            properties.setProperty("players", Long.toString(nrPlayers));

            // set number of rounds
            System.out.println("Please set the number of rounds for your Generala game: ");

            s = reader.readLine().replaceAll("\\s+", "");
            while (!Helper.isNumeric(s)) {
                System.out.println("The given value for rounds is not numeric");
                System.out.println("Please set the number of rounds for your Generala game: ");
                s = reader.readLine().replaceAll("\\s+", "");
            }

            Long nrRounds = Long.parseLong(s);
            properties.setProperty("rounds", Long.toString(nrRounds));

            properties.store(os, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
