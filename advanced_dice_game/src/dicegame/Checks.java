package dicegame;

public class Checks {

    /**
     * Checks if the passed string contains only numeric characters.
     *
     * @param s The string to be checked.
     * @return boolean
     */
    public static boolean isNumeric(String s)
    {
        if (s == null) {
            return false;
        }

        // remove whitespace and invisible characters
        s = s.replaceAll("\\s+", "");

        // check string char by char
        int size = s.length();
        for (int i = 0; i < size; i++) {
            if (!Character.isDigit(s.charAt(i))) {
                return false;
            }
        }

        return true;
    }
}
