package dicegame.common.models;

public class Die {

    private int min;
    private int max;

    /**
     * Sets the die min and max roll values to a real world six-faced die.
     */
    public Die()
    {
        this.min = 1;
        this.max = 6;
    }

    /**
     * Sets custom min and max roll values for the die.
     *
     * @param min minimum roll value
     * @param max maximum roll value
     */
    public Die(int min, int max)
    {
        this.min = min;
        this.max = max;
    }

    /**
     * Rolls the die and returns its face.
     *
     * @return int
     */
    public int roll()
    {
        return (int) Math.floor(Math.random() * (max - min + 1) + min);

        //  implementation using the Random class
        //  Random r = new Random()
        //  return r.nextInt(max) + min;
    }
}
