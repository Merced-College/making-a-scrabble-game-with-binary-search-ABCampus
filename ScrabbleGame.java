import java.io.*;
import java.util.*;

//Adam Barakat
//Jose Arellano
//Ethan Smith
//Luis Cruz Pereda

public class ScrabbleGame {
    //Adam - (main method)
    public static void main(String[] args) {
        List<Word> wordList = loadWordList("CollinsScrabbleWords_2019.txt");
        int numOfLetters = 4;
        char[] letters = letterGen(numOfLetters);
        Scanner scnr = new Scanner(System.in);

        System.out.println("Welcome to Scrabble. Two players will compete for the best word (points).");
        game(wordList, letters, scnr, numOfLetters);//this main loop contains most of the calling for game 

        scnr.close();
    }

    //Jose - 2 player system implemented
    private static void game(List<Word> wordList, char[] letters, Scanner scnr, int numOfLetters) {//main loop
        String player1Word, player2Word;//player word vars
    
        while (true) {
            
            displayLetters(letters, numOfLetters);//displays current letter set
    
            player1Word = getPlayerWord(scnr, "Player 1", letters, numOfLetters);//gets player
            if (player1Word.equals("END")) break;//end is the keyword to stop the program
            if (player1Word.equals("EXCHANGE")) {//exchange is the keyword to change a letter
                letters = exchangeLetter(letters, scnr);
                continue;
            }
            
            player2Word = getPlayerWord(scnr, "Player 2", letters, numOfLetters);//similar to player1 system
            if (player2Word.equals("END")) break;
            
            //vars that track if the word is in the list or not
            boolean found1 = binarySearch(wordList, player1Word);
            boolean found2 = binarySearch(wordList, player2Word);
    
            displayWordCheckResults(player1Word, player2Word, found1, found2);//displays whether or not the words were found
    
            determineWinner(player1Word, player2Word, found1, found2);//winner determination
            System.out.println();
            letters = letterGen(numOfLetters);//generates new letters each 'game'
        }
    }
    
    //Adam/Jose - 2 player system and letter display - organized in it's own function
    private static void displayLetters(char[] letters, int numOfLetters) {//displays the letters to the player(s)
        System.out.println("There will be " + numOfLetters + " letters for you to make a word with.");
        System.out.println("Here are your letters: " + Arrays.toString(letters).toUpperCase());//letters are generated in lowercase for some reason
    }

    //Adam/Jose - 2 player system and get word - organized in it's own function
    private static String getPlayerWord(Scanner scnr, String player, char[] letters, int numOfLetters) {//gets the player word from input
        String playerWord;
        boolean validWord = false;
    
        do {
            System.out.println(player + ", enter your word using only the " + numOfLetters + " letters given, or type 'exchange' to swap a letter, or 'end' to exit:");
            playerWord = scnr.nextLine().toUpperCase();
    
            if (playerWord.equals("END") || playerWord.equals("EXCHANGE")) {//checks for exchange/end input before checking the word
                return playerWord;
            }
    
            if (!validityCheck(playerWord, letters)) {//asks for another word if an invalid one is entered
                System.out.println("Invalid word. " + player + ", please try again.");
            } else {
                validWord = true;
            }
        } while (!validWord);//this will always run at least once, only continuing when a valid word is entered
    
        return playerWord;
    }
    

    //Adam/Jose - 2 player system and work checking - organized in it's own function
    private static void displayWordCheckResults(String player1Word, String player2Word, boolean found1, boolean found2) {//displays to the players the word existance
        if (!found1) {
            System.out.println("Player 1's word '" + player1Word + "' was not found in the word list.");
        } else {
            System.out.println("Player 1's word '" + player1Word + "' exists.");
        }

        if (!found2) {
            System.out.println("Player 2's word '" + player2Word + "' was not found in the word list.");
        } else {
            System.out.println("Player 2's word '" + player2Word + "' exists.");
        }
    }

    //Adam/Jose - 2 player system and checking logic - organized in it's own function
    private static void determineWinner(String player1Word, String player2Word, boolean found1, boolean found2) {
        if (found1 && found2) {//assuming both are found, it gets determined by score (some letters are worth more)
            int player1Score = new Word(player1Word).getScore();
            int player2Score = new Word(player2Word).getScore();
            if (player1Score > player2Score) {
                System.out.println("Player 1 wins with the word: " + player1Word + " (Score: " + player1Score + ")");
            } else if (player2Score > player1Score) {
                System.out.println("Player 2 wins with the word: " + player2Word + " (Score: " + player2Score + ")");
            } else {
                System.out.println("It's a tie! Both words have the same score.");
            }
        } else if (found1) {//player X will win regardless of score if the other is not found.
            System.out.println("Player 1 wins as Player 2's word was not found.");
        } else if (found2) {
            System.out.println("Player 2 wins as Player 1's word was not found.");
        } else {
            System.out.println("Neither player has an existing word. No winner this round.");
        }
    }

    //Adam - loading word list
    public static List<Word> loadWordList(String fileName) {//the txt file is formatted in a way to make it super efficient to use buffered reader
        List<Word> wordList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String word;
            while ((word = br.readLine()) != null) {//each line is read as one object, so its very quick
                wordList.add(new Word(word));//1 line = 1 word
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wordList;
    }

    //Adam - binarySearch
    public static boolean binarySearch(List<Word> sortedList, String targetWord) {//binary search algorithm to find the word in the word list
        int left = 0;//left index starts at 0
        int right = sortedList.size() - 1;//right index starts at the highest index

        while (left <= right) {
            int mid = left + (right - left) / 2;
            int comparison = targetWord.compareTo(sortedList.get(mid).getWord());

            if (comparison == 0) return true;//found letter
            if (comparison > 0) left = mid + 1;//value is right of mid, so left index moves to mid
            else right = mid - 1;//opposite of above, value is left of mid, so right is moved to mid
        }
        return false;
    }

    //Ethan - letterGen
    private static char[] letterGen(int numOfLetters) {//letter generation algorithm to generate X number of letters
        Random random = new Random();
        char[] letters = new char[numOfLetters];
        for (int i = 0; i < numOfLetters; i++) {
            letters[i] = (char) (random.nextInt(26) + 'A');//random integer (each matched with a letter) as the current index of the letter array
        }
        return letters;
    }

    //Adam - validityCheck
    private static boolean validityCheck(String userWord, char[] letters) {//validation check
        char[] userWordArr = userWord.toCharArray();
        if (userWordArr.length > letters.length) return false;//first checks if its the right length as longer than 4 letters is obviously not going to wrk

        boolean[] usedLetters = new boolean[letters.length];//bool array that tracks which letters were used
        for (char c : userWordArr) {
            boolean found = false;//each letter is "not found" by default, assuming its found, it become "found", if it's not, it stays not found, if a single letter
            for (int i = 0; i < letters.length; i++) {//is not found, then the entire thing returns false
                if (c == letters[i] && !usedLetters[i]) {
                    usedLetters[i] = true;
                    found = true;
                    break;
                }
            }
            if (!found) return false;
        }
        return true;
    }

    //Luis - exchangeLetter
    private static char[] exchangeLetter(char[] letters, Scanner scnr) {//letter exchange
        System.out.println("Enter the index (0 to " + (letters.length - 1) + ") of the letter you want to exchange:");
        int index = scnr.nextInt();
        scnr.nextLine();

        if (index < 0 || index >= letters.length) {//makes sure index is valid
            System.out.println("Invalid index. No letter exchanged.");
            return letters;
        }

        letters[index] = letterGen(1)[0];//uses letter gen to generate a single random letter to exchange with the said letter
        System.out.println("Exchanged letter at index " + index + " with '" + letters[index] + "'.");
        return letters;
    }
}
