//package scrabbleGame;

import java.io.*;
import java.util.*;

//Adam Barakat
//Jose Arellano
//Ethan Smith
//Luis Cruz Pereda

public class ScrabbleGame {
    
    public static void main(String[] args) {
        
        List<Word> wordList = loadWordList("CollinsScrabbleWords_2019.txt"); //load wordlist txt file
        // Collections.sort(wordList); // Sort the list of Word objects, not needed here but nice to have just in case

        int numOfLetters = 4; 
        char[] letters = letterGen(numOfLetters);
        String userWord; 
        boolean valid;

        Scanner scnr = new Scanner(System.in); // Create scanner instance

        do {
        	System.out.println("There will be " + numOfLetters + " letters for you to make a word with.");
            System.out.println("Here are your letters: " + Arrays.toString(letters).toUpperCase());
            System.out.println("Enter your word using only the " + numOfLetters + " letters given, or type 'exchange' to swap a letter, or 'end' to exit:");

            userWord = scnr.nextLine().toUpperCase(); // Convert input to uppercase

            // Luis Cruz Pereda
            if (userWord.equals("END")) { // End game if inputting END
                System.out.println("Thank you for playing! Goodbye!");
                break; // Exit the loop
            }

            //Luis Cruz Pereda
            if (userWord.equals("EXCHANGE")) { // typing exchange exchanges a letter for another letter
                letters = exchangeLetter(letters, scnr);

            } else {
                valid = validityCheck(userWord, letters);
                if (valid) {
                    boolean found = binarySearch(wordList, userWord);
                    if (found) {
                        System.out.println("Found: " + userWord + " " + new Word(userWord).getScoreDisplay()); // returns the found word and the score of that word
                        break; // End the game after finding a valid word
                    } else {
                        System.out.println("Not found: " + userWord);
                    }
                } else {
                    System.out.println("Invalid word. Please try again.");
                }
            }
        } while (true); // Loop continues until 'end' is typed or a word is found

        scnr.close(); // Close the scanner when done
    }

    //Adam Barakat function that creates a list from the file, line by line 
    public static List<Word> loadWordList(String fileName) {
        List<Word> wordList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String word;
            while ((word = br.readLine()) != null) {//each line is 1 word object, which is very efficient for our file format/structure
                wordList.add(new Word(word)); // Create a Word object and add it to the list
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return wordList;
    }

    //Adam Barakat binary search function (efficient)
    public static boolean binarySearch(List<Word> sortedList, String targetWord) {
        int left = 0; //left side index/pointer
        int right = sortedList.size() - 1; //right side index/pointer

        while (left <= right) { //searches until the left/right index meet or pass each other
            int mid = left + (right - left) / 2; //middle index
            int comparison = targetWord.compareTo(sortedList.get(mid).getWord()); //compares the target word to the current middle word 

            if (comparison == 0) { //0 means they are equal, so the word is found
                return true; 
            } else if (comparison > 0) { //greater than 0 (0 is the middle word) means its on the right side of, we change mid to the middle of the right side 
                left = mid + 1; //left side cap is now at mid+1
            } else { //less than 0 means its on the left side, so mid becomes middle of left side
                right = mid - 1; //right side cap is now at mid-1
            }
        }
        return false; // Not found
    }

    //Adam Barakat generates 4 random letters for the user to use
    private static char[] letterGen(int numOfLetters) {
        Random random = new Random();
        char[] letters = new char[numOfLetters];

        for (int i = 0; i < numOfLetters; i++) {
            letters[i] = (char) (random.nextInt(26) + 'A'); // Generate uppercase letters
        }

        return letters;
    }

    //Adam Barakat checks if a word is the right length and if it uses the right letters
    private static boolean validityCheck(String userWord, char[] letters) {
        char[] userWordArr = userWord.toCharArray();

        if (userWordArr.length > letters.length) { //returns false if over the limit
            return false;
        }

        boolean[] usedLetters = new boolean[letters.length]; // Track used letters

        for (char c : userWordArr) {
            boolean found = false;
            for (int i = 0; i < letters.length; i++) {
                if (c == letters[i] && !usedLetters[i]) { // Check if letter is available
                    usedLetters[i] = true; // Mark this letter as used
                    found = true;
                    break;
                }
            }
            if (!found) {
                return false; // Letter not found or already used
            }
        }

        return true;
    }

    //Luis Cruz Pereda
    private static char[] exchangeLetter(char[] letters, Scanner scnr) {
        System.out.println("Enter the index (0 to " + (letters.length - 1) + ") of the letter you want to exchange:");
        int index = scnr.nextInt();
        scnr.nextLine(); // Consume the newline character

        if (index < 0 || index >= letters.length) {
            System.out.println("Invalid index. No letter exchanged.");
            return letters;
        }

        letters[index] = letterGen(1)[0]; // Generate a new letter
        System.out.println("Exchanged letter at index " + index + " with '" + letters[index] + "'.");
        return letters;
    }
}

