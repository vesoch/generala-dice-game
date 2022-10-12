# Generala Dice Game

<!-- ABOUT -->
## About

This repository holds solutions to the training task to implement the Generala dice game.
* basic_console_app holds a simple implementation of the game.
* TODO: An implementation of the game using Spring Boot.

<!-- ASSIGNMENT -->
## Assignment

Write a program that implements Generala dice game between X players in Y rounds. In that
game on each round each player rolls 5 dice at a time. You have to evaluate whether these
5 dice are forming one or more combinations and calculate the player's score. The score is
the sum of all the dice included in the picked combination plus the score constant for the
combination.

The combinations required to be implemented are:

````
Pair - when you have two equal dice (example: 2,2,4,5,1) score constant is 10.
Double pair - is two different pairs (example: 2,2,3,3,6) score constant is 15.
Triple - three equal dice (example - 3,3,3,2,5) score constant is 20.
Full house - a triple and a pair (example: 3,3,3,2,2) score constant is 25.
Straight - a sequence of dice (possibilities: 1,2,3,4,5 or 2,3,4,5,6) score constant is 30.
Four of a kind - four dice with equal number (example - 4,4,4,4,2) score constant is 40.
Generala - five dice with the same number (example: 6,6,6,6,6) score constant is 50.
````

Score calculation example: 3,3,3,4,5 - Triple - score is 3+3+3+20 = 29.

When a player rolls their dice, they can form more than one combination. In this case the
one having the biggest score is picked. A player can record score from each combination
only once during a game. So only one " Pair ", one " Double pair ", and so on.
For example, the dice numbers 3,3,4,2,4 form the following combinations:

* a " Pair " with 16 points,
* a " Pair " with 18 points,
* a " Double pair " with 29 points.

Since the " Double pair " is highest it is picked and 29 points are added to the player's score.
If the player has used this combination previously (no matter the actual dice numbers that
formed it), the " Pair " with 18 points is picked. If the player has already used " Pair " as well,
they do not have a new combination to pick so no points are added for that roll.
Whenever a player scores “G enerala” , they automatically win the game. If nobody scores
Generala, the winner is the one with the highest score.

### Implementation details:
* The values of players and rounds must be read from a properties file.
* Rolling a die can be simulated by choosing 1, 2, 3, 4, 5, or 6 at random.
* On each round the application goes over each player and prints their current score,
simulated dice roll and new score. For example:
````
>>> round 3:
> player 1:
current score: 12
dice roll: 3, 4, 5, 5, 6 -> Pair (20)
new score: 22
> player 2:
````

* If “ Generala ” is scored, that round and the game end directly without going through
any remaining players for that round.
* When the game ends, it must print each player and their final score in decreasing
order (i.e. the winner being first).
* The source code of the application and an executable JAR must be provided.
### Built With

* [JDK 16](https://openjdk.java.net/projects/jdk/16/)

<!-- LICENSE -->
## License
Distributed under the [MIT](https://opensource.org/licenses/MIT) License.

<!-- CONTACT -->
