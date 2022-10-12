package dicegame.common.models;

public class Player {

    private final Long id;
    private Long score;

    public Player(Long id)
    {
        this.id = id;
        this.score = 0L;
    }

    public Long getId()
    {
        return id;
    }

    public Long getScore()
    {
        return score;
    }

    public void addToScore(Long score)
    {
        this.score += score;
    }
}
