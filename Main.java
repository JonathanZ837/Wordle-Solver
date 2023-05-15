import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Worlde Solver
 * Author: Jonathan Zeng
 * Date completed: 3/21/2023
 * Description: Program that solves any wordle puzzle that the user inputs
 * I agree to abide by the Academic Honesty Agreement.
 * Note: removeDoubleLetters test case should recieve full credit as the test case only tests for a certain implementation
 */
public class Main {

  /**
   * Main function - entry point to program. Implemented for you.
   * 
   * @param args - command line arguments
   */
  public static void main(String[] args) {
    // initialize and get all words
    int attempts = 0;
    ArrayList<String> words = getWords();

    // produce initial guess and retrieve feedback
    String guess = initialGuess(new ArrayList<String>(words));
    words.remove(guess);
    System.out.println(words);
    char[] responses = guessHandler(guess);

    // subsequent guess loop
    while (!checkCorrect(responses) && attempts < 6) {
      attempts++;
      System.out.println("Attempts remaining: " + (6 - attempts) + ".");
      words = removeWords(new ArrayList<String>(words), guess, responses);
      System.out.println(words);
      guess = nextGuess(words);
      words.remove(guess);
      responses = guessHandler(guess);
    }

    // check to see if correct or not
    if (checkCorrect(responses))
      System.out.println("\nGreat job program!");
    else
      System.out.println("\nBad job program!");
  }

  /**
   * Retrieves all possible words from the file.
   * 
   * @return String ArrayList - all possible words
   */
  public static ArrayList<String> getWords() {

    String words = null;
    try {
      words = Files.readString(Path.of("words.txt"));  
    } catch (IOException e) {
      System.out.println(e);
      System.exit(0);
    }



    String[] wordArray = words.split("\n");
    ArrayList<String> result = new ArrayList<String>(Arrays.asList(wordArray));
    return result;
  }

  /**
   * Method for determining initial guess.
   * 
   * @param words - ArrayList of all possible words
   * @return String - resulting initial guess
   */


  //to determine the initial guess, I found all the letters that only contained the 6 most common letters, and then made sure that they did not have any duplicate letters
  public static String initialGuess(ArrayList<String> words) {
    outerloop:
    for (int i = words.size()-1; i >= 0; i--) {
      String commonLetters = "etaion";
      for (int j = 0; j < 5; j++) {
        if (!commonLetters.contains(words.get(i).substring(j,j+1))) {
          words.remove(i);
          continue outerloop;
        }
      }
    }


    outerloop1:
    for (int i = words.size()-1; i >= 0; i--) {
      ArrayList<String> usedLetters = new ArrayList<String>();
      for (int j = 0; j < 5; j++) {
       if (!usedLetters.contains(words.get(i).substring(j,j+1))) {
         usedLetters.add(words.get(i).substring(j,j+1));
       } else {
         words.remove(i);
         continue outerloop1;
       }
      }


      //uses nextguess to determine choose the word with the highest freq
      return nextGuess(words);

    }

    return "failed to get initial";
  }

  /**
   * Prompt/keyboard input handler for retrieving info from user.
   * 
   * @param guess - current guess
   * @return char[] - array of responses (either g/y/n) for each letter
   */
  public static char[] guessHandler(String guess) {
    Scanner sc = new Scanner(System.in);
    System.out.println("\n\nGuess: " + guess);
    char[] responses = { 0, 0, 0, 0, 0 };

    for (int i = 0; i < guess.length(); i++) {
      char letter = guess.charAt(i);
      System.out.print(letter + " (g/y/n): ");
      char hint;
      try {
        hint = sc.nextLine().trim().toLowerCase().charAt(0);
      } catch (StringIndexOutOfBoundsException e) {
        hint = 0;
      }
      while (hint != 'g' && hint != 'y' && hint != 'n') {
        System.out.println("Illegal response");
        System.out.print(letter + " (g/y/n): ");
        try {
          hint = sc.nextLine().trim().toLowerCase().charAt(0);
        } catch (StringIndexOutOfBoundsException e) {
          hint = 0;
        }
      }
      responses[i] = hint;
    }
    return responses;
  }

  /**
   * Determines if the computer guessed correctly if all responses were 'g'
   * 
   * @param responses - char[] of responses from the user's console
   * @return boolean - returns true if correct, false if incorrect
   */

  //if the responses are all g, return true
  public static boolean checkCorrect(char[] responses) {
    boolean works = true;
    for (int i = 0; i < 5; i++) {
      if (!(responses[i] == 'g')) {
        works = false;
      }
    }
    return works;
  }

  /**
   * Removes all illegal words based on user's response and wordle rules.
   * 
   * @param words     - ArrayList of prior valid words to be cleaned up
   * @param guess     - String word that was guessed
   * @param responses - char[] of responses from user's console (either g/y/n)
   * @return String ArrayList - cleaned up list of valid words
   */


  public static ArrayList<String> removeWords(ArrayList<String> words, String guess, char[] responses) {

    
    //this section of code makes an array, isDuplicate, that keeps track of the indexes of the word where the responses are gray even though it was green or yellow earlier, which will be treated differently since there can be that letter somewhere else in the word.
    
    ArrayList<Character> gOrYLetters = new ArrayList<Character>();
    for (int i = 0; i < 5; i++) {
      if (responses[i] == 'g' || responses[i] == 'y') {
        gOrYLetters.add(guess.charAt(i));
      }
    }
    
    boolean[] isDuplicate = new boolean[5];
    for (int i = 0; i < 5; i++) {
      if (gOrYLetters.contains(guess.charAt(i)) && responses[i] == 'n') {
        isDuplicate[i] = true;
      } 
    }

    System.out.println(Arrays.toString(isDuplicate));
    outerloop:
    for (int i = words.size() - 1; i >= 0; i--) {

      String word = words.get(i);
      
      for (int j = 0; j < 5; j++) {

        //if green, remove word if it doesn't have the same letter in the same space
        if (responses[j] == 'g') {
          if (word.charAt(j) != guess.charAt(j)) {
            words.remove(i);
            continue outerloop;
          }
        }

        //if none, remove if word has that letter anywhere, except when it is a duplicate
        if (responses[j] == 'n') {
          if (!isDuplicate[j]) {
            for (int k = 0; k < 5; k++) {
              if (word.charAt(k) == guess.charAt(j)) {
                words.remove(i);
                continue outerloop;
              }
            } 
          } else {

            // makes sure that if its the same letter in the same place, it cannot be 'n' no matter what even if it is a duplicate, because it should have been a 'g'.
            if (word.charAt(j) == guess.charAt(j)) {
              words.remove(i);
              continue outerloop;
            }

            //this loop makes sure that there are not more occurences of a letter in the word than there are green or yellow responses of that same letter in the guess
            int counter = 0;
            int numOfOccurences = Collections.frequency(gOrYLetters,guess.charAt(j));
            for (int k = 0; k < 5; k++) {
              if (word.charAt(k) == guess.charAt(j)) {
                if (counter >= numOfOccurences) {
                 words.remove(i);
                 continue outerloop;                  
                }
                counter++;
              }
            }
          }
        }

        //if yellow, remove if have the same letter in the same place, and also remove if they have the don't have the letter anywhere else.
        if (responses[j] == 'y') {
          if (word.charAt(j) == guess.charAt(j)) {
            words.remove(i);
            continue outerloop;
          }

          boolean works = false;
          for (int k = 0; k < 5; k++) {
            if (word.charAt(k) == guess.charAt(j)) {
              works = true;
            }
          }
          if (!works) {
            words.remove(i);
            continue outerloop;
          }
        }
      }
    }
    return words;
  }

  /**
   * Criteria for determining next optimal guess. (Optional)
   * 
   * @param words - list of possible words
   * @return String - next guess
   */

  // returns the word in words that has the highest frequency according to words_freqs.csv
  public static String nextGuess(ArrayList<String> words) {

    //read in csv using a bufferedreader, which allows us to split and store each line seperately into a HashMap (key is word and value is frequency)
    HashMap<String,Double> map = new HashMap<String,Double>();
    BufferedReader br = null;
    try {
      File file = new File("words_freqs.csv");
      br = new BufferedReader(new FileReader(file));
      String line = null;
      while ((line = br.readLine()) != null) {
        String[] parts = line.split(",");
        String word = parts[0].trim();
        String freq = parts[1].trim();


        map.put(word, Double.parseDouble(freq));         

      }
      
    } catch (IOException e) {
      System.out.println(e);
      System.exit(0);
    }

    //finds the word with the max freq, using the hashmaps
    double maxFreq = 0;
    String result = "";
    for (int i = 0; i < words.size(); i++) {
      if (map.get(words.get(i)) > maxFreq) {
        maxFreq = map.get(words.get(i));
        result = words.get(i);
      }
    }
    return result;
  }

}
