# Project #2 Wordle Solver

## Goal:
We are going to design a program to help you guess the Wordle! In this program, you will parse a word list and use a variety of different tactics to try to always guess the word in the fewest number of attempts possible. To view the actual game and get the gist of how it works, go here: [https://www.nytimes.com/games/wordle/index.html](https://www.nytimes.com/games/wordle/index.html).

<br>

## Demo:
Below is an example of how your program should look and feel.

![Wordle Demo](wordle_demo.mp4)

<br>

## Program Setup:
*   This project contains two data files and one Java source file:  
    *   `words.txt` - text file containing all valid five-letter words for Wordle.
    *   `words_freqs.csv` - csv file containing the frequency of all valid five-letter words for Wordle. The frequency is a percentage occurrence within the English language. The higher the value the higher the occurrence rate. Be aware that they are very small float values.
    *   `Main.java` - a Java source file with starter code and method stubs for you to implement.
*   Create a design plan document entitled `design.md` within your replit project.

<br>

## Design Plan Instructions:
You must create a document within your project entitled `design.md`. It needs to completely and succinctly address the following points. You may answer these questions in whatever format you wish: bullet points, paragraph, graphically, pseudocode, etc.
1. What are your criteria for programmatically determining your first pick?
2. How will you represent the data about the results of the guesses (what letters were green/yellow/grey, where they were located, etc.)? What data types will you use? Make sure you consider all of the following cases:
    * How will you store the incorrect letters that are guessed (grey)?
    * How will you store the correct letters that you know the location of (green)?
    * How will you store letters that you know are correct but are in the wrong location (yellow)? How will you know what locations you already tried?
    * How will you deal with duplicate letters? Warning: this part can be a huge headache.
3. What are your criteria for all subsequent picks?

<br>

## Program Instructions:
**Hints**:
*   File I/O: https://replit.com/@MichaelDArgenio/cs4240-file-io-example-java 
*   Looping through ArrayLists and removing words: https://replit.com/@MichaelDArgenio/Loops-with-ArraysArrayLists  

Below is a description of all the functionality expected broken down by method:
*   `main()` - Implemented for you. Contains the main function calls and the guessing loop.
*   `getWords()` - You are responsible for implementing this method. This method should read in all the words from `words.txt` and returning the words as an `ArrayList<String>`. There are unit tests for this method. The unit test tests this method in isolation from all other methods. Make sure it passes the unit tests before proceeding!
*   `initialGuess()` - You must implement and test this method on your own. It should programmatically determine the initial guess. You can't hardcode your favorite word to be the first guess. Write some code to determine the most optimal guess. For me, I tried to guess a word that contained the most common letters but did not contain repeat letters. You may do something similar or determine your own methodology.
*   `guessHandler()` - Implemented for you. This method is responsible for displaying the guess and retrieving the responses from the user via the console.
*   `checkCorrect()` - You are responsible for implementing this method. This method checks the `char[]` of responses from the user to see if all of the responses were `'g'` and returns a `boolean` value. If the computer guessed every letter correct (all responses were `'g'`), it should return `true`. In all other cases, it should return `false`. There are unit tests for this method. The unit test tests this method in isolation from all other methods. Make sure it passes the unit tests before proceeding!
*   `removeWords()` - You are responsible for implementing this method. This is the most difficult portion of this project and will take a bulk of the time. This method takes three input parameters: an `ArrayList<String>` of possible `words`, a `String` representing the last `guess`, and a `char[]` representing the `responses` based on the last `guess`. Using `guess` and `responses`, you need to remove all words that are invalid from the list `words`. You should return back a list of `words` that are valid at the end based on the **Guessing Rules** below. There are unit tests for this method. The unit test tests this method in isolation from all other methods. Make sure it passes the unit tests before proceeding!
*   `nextGuess()` - You must implement and test this method on your own. You will need to choose a word from the list of valid `words` and return the chosen word from the method. There does not have to be any complexity here; you can just choose any valid word. If you have time after completing the project, you can attempt some of the stretch goals and add additional functionality to this method to try to guess the most optimal word.

**Guessing Rules:**
*   If a letter was green in a prior guess, all subsequent guesses must have that letter in that location.
*   If a letter was yellow in a prior guess, all subsequent guesses must contain that letter, but it must _not_ be in the locations where it is known not to be.
*   If a letter was grey in a prior guess, it cannot be used in any subsequent guesses. There are some caveats for grey in the case of the letter appearing twice. See the information below:
    *   If the same letter is both grey and green for a single guess, it should not appear in any location other than where it was green. Make sure that your grey logic does not remove the letter from the green location.
    *   If the same letter is grey twice in a single guess, it should not appear in any location.
    *   If the same letter is grey and yellow for a single guess, it can not be at the grey or the yellow location. However, it has to be in at most, one other location.

**Double Letter Help**:  
Here is some general pseudocode for double letters. You really only need to consider double letters in the case where the response is `'n'`, meaning that the letter was grey and is not present in the word. We want to make sure our algorithm is not too aggressively removing all words with that letter present because it could have been marked as `'g'` or `'y'` in another position. This is how it should be handled:  

```
for each response:
  if response == 'g':
    ...
  else if response == 'y':
    ...
  else if response == 'n'
    if letter at response position occurs in guess twice and other time is yellow:
      remove any words with the grey letter only at that position
    else if letter at response position occurs in guess twice and other time is green:
      remove any words with the grey letter at all positions except green position
    else (two grey duplicates or single grey letter):
      remove any words with the grey letter at any position
```

<br>

## Testing:
`main()` and `guessHandler()` are implemented for you, so there is no testing required. Be sure to leave these methods as they are.   

There are unit tests provided to help you test your `getWords()`, `removeWords()`, and `checkCorrect()` methods. These unit tests run in isolation and allow you to just test the individual methods even if other aspects of your program does not work.  

It is up to you to find ways to test your `initialGuess()` and `nextGuess()` methods as well as the program as a whole. Make sure you use a rigorous testing process to test all possible variations to ensure your program works. You want to make sure your program is _always_ able to get it in under 6 guesses. Below are some Wordle app options for you to exhaustively test your program.  
*   [https://www.nytimes.com/games/wordle/index.html](https://www.nytimes.com/games/wordle/index.html) - Wordle official
*   [https://wordle.michaeldargenio.repl.co/](https://wordle.michaeldargenio.repl.co/) - Wordle clone that allows you to test with random words multiple times a day
*   [https://hellowordl.net/](https://hellowordl.net/) - Wordle variant that allows endless plays (typically more difficult than the official Wordle)

<br>

## Stretch Goals:
*   Incorporate word frequency in your program!
    *   There are a number of weird words like "aahed" and "aalii" that are technically valid guesses but would not be a wordle solution. Think about them as the weird words you would only know for Scrabble. The solutions are going to be mostly common words.  
    *   I have provided a csv file, `words_freqs.csv`, that contains the frequency of all valid five-letter words for Wordle. The frequency is a percentage occurrence within the English language. The higher the value the higher the occurrence rate. Be aware that they are very small float values.
    *   This data comes from a Luminoso project called [Exquisite Corpus](https://github.com/LuminosoInsight/exquisite-corpus), whose goal is to download good, varied, multilingual corpus data, process it appropriately, and combine it into unified resources.
*   Develop a secondary guessing criteria.
    *   Basically, a different methodology that somewhat mirrors your first guess criteria in that it tries to obtain the most information, but that it also uses the information gleaned from the first guess.
    *   Its goal is to try to retrieve the most information possible, not necessarily guessing a more common word.
*   Incorporate entropy in your program.  
    *   This helps you leverage information theory to optimize your guesses to obtain the most information with each guess.
    *   This is certainly a large undertaking but could be interesting to investigate. Take a look at this very thorough video discussing the ideas behind it.
    *   3blue1brown "Solving Wordle using information theory" - [https://www.youtube.com/watch?v=v68zYyaEmEA](https://www.youtube.com/watch?v=v68zYyaEmEA)
    *   3blue1brown followup - [https://www.youtube.com/watch?v=fRed0Xmc2Wg](https://www.youtube.com/watch?v=fRed0Xmc2Wg)
*   Develop an implementation of the Wordle game to use your Solver and run test runs to see how well it performs!