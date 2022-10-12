package dicegame.common.models;

import java.util.ArrayList;
import java.util.List;

public class Logger {

    private List<String> log;

    public Logger()
    {
        this.log = new ArrayList<>();
    }

    /**
     * Adds a String to the log array list.
     *
     * @param s String to add to the log array list.
     */
    public void addToLog(String s)
    {
        if (s != null && !s.equals("")) {
            this.log.add(s);
        }
    }

    /**
     * Retrieves the log array list.
     *
     * @return List<String>
     */
    public List<String> getLog()
    {
        return this.log;
    }
}
