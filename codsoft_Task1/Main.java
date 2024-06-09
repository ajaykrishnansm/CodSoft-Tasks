/*
TASK 1
NUMBER GAME
1. Generate a random number within a specified range, such as 1 to 100.
2. Prompt the user to enter their guess for the generated number.
3. Compare the user's guess with the generated number and provide feedback on whether the guess
is correct, too high, or too low.
4. Repeat steps 2 and 3 until the user guesses the correct number.
You can incorporate additional details as follows:
5. Limit the number of attempts the user has to guess the number.
6. Add the option for multiple rounds, allowing the user to play again.
7. Display the user's score, which can be based on the number of attempts taken or rounds won.
 */
import java.util.InputMismatchException;
import java.util.Scanner;

class GuessTheNumber {

    private int answer;
    private int highScore;
    private final int LIMIT;
    private int score;

    public GuessTheNumber(int LIMIT) {
        this.LIMIT = LIMIT;
        resetGame();
    }

    private void resetGame() {
        setAnswer();
        score = 0;
    }

    private void setAnswer() {
        answer = (int) (Math.random() * 100) + 1;
    }

    private void updateScore() {
        score += 5;
        if (score > highScore)
            highScore = score;
    }

    public int getHighScore() {
        return highScore;
    }

    public void startGame() {
        boolean exit = false;
        Scanner keypad = new Scanner(System.in);

        while (!exit) {
            System.out.println("\n1. Guess the number.");
            System.out.println("2. View High Score.");
            System.out.println("3. Exit.");
            System.out.print("Enter your choice: ");

            try {
                int choice = keypad.nextInt();
                switch (choice) {
                    case 1:
                        playRound(keypad);
                        break;
                    case 2:
                        System.out.println("High Score: " + getHighScore());
                        break;
                    case 3:
                        System.out.println("Total Score: " + score);
                        System.out.println("High Score: " + getHighScore());
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid Option!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Please enter a valid option.");
                keypad.next();
            }
        }

        keypad.close();
    }

    private void playRound(Scanner keypad) {
        int trialCount = 0;
        boolean guessed = false;

        while (trialCount < LIMIT && !guessed) {
            System.out.print("Enter your guess (1-100): ");
            int guessedNumber = keypad.nextInt();

            if (guessedNumber == answer) {
                updateScore();
                guessed = true;
            } else if (guessedNumber < answer) {
                System.out.println("Too low! Try again.");
            } else {
                System.out.println("Too high! Try again.");
            }
            trialCount++;
        }

        if (guessed) {
            System.out.println("Congratulations! You guessed the number.");
        } else {
            System.out.println("You've reached the limit of attempts. The number was: " + answer);
        }

        resetGame();
    }
}

public class Main {
    public static void main(String[] args) {
        GuessTheNumber game = new GuessTheNumber(5);
        game.startGame();
    }
}
