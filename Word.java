//Luis: word.java

import java.util.ArrayList;
import java.util.Arrays;

public class Word implements Comparable<Word> {
    private String word;
    private int score;

    // Ethan Smith - Parallel ArrayLists for each letter
    private static final ArrayList<Character> letters = new ArrayList<>(Arrays.asList(
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
        'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'));

    // Ethan Smith - ArrayList for each letters point value based off of real Scrabble Values
    private static final ArrayList<Integer> letterScores = new ArrayList<>(Arrays.asList(
        1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 1, 1, 3, 10, 1, 1, 1, 1, 4, 4, 8, 4, 10));

    // Ethan
    public Word(String word) {
        this.word = word.toUpperCase(); // Store words in uppercase
        score = calculateScore(); // Set score to be calculated amount
    }

    // Ethan Smith - Calculate the score based on the Scrabble letter values using ArrayLists
    private int calculateScore() {
        int score = 0;
        for (char letter : word.toCharArray()) {
            int index = letters.indexOf(letter); // Find the index of the letter in the letters ArrayList
            if (index != -1) { // If letter is found, add its score to total
                score += letterScores.get(index); // Get the corresponding score from letterScores ArrayList
            }
        }
        return score; 
    }
    //Luis
    public String getWord() {
        return word;
    }
    //Luis
    public int getScore() {
        return score; // Return score of the word
    }
    //Luis
    @Override
    public int compareTo(Word other) {
        return this.word.compareTo(other.word); // Compare based on the word string
    }
    //Luis
    @Override
    public String toString() {
        return word; // Return just the word when printing
    }
    // Ethan Smith - added this to return user score of valid word
    public String getScoreDisplay() {
        return "\nScore: " + score + "\nThanks for Playing!";
    }
}