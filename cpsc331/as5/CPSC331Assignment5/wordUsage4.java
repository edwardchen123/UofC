package CPSC331Assignment5;

import CPSC331Assignment4.HashFunction;
import CPSC331Assignment4.SimpleHashFunction;
import CPSC331Assignment4.HashStructure;
import CPSC331Assignment4.HashStructureWithChaining;
import CPSC331Assignment4.SimpleHashTable;
import CPSC331Assignment3.Pair;
import CPSC331Assignment5.comparablePair;
import CPSC331Assignment4.inputSource;
import CPSC331Assignment5.mySort;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;
import java.lang.Integer.*;
import java.io.FileNotFoundException;
import java.lang.SecurityException;

/**
 *
 * Displays the words found in a specified text file, listed in
 * dictionary order, along with the number of occurrences of each;
 * the case of letters is ignored and all letters are displayed
 * in lower case.
 *
 *
 */

public class wordUsage4 {

  /**
   *
   * Displays the words in a given text file.
   *
   * @param source Text file whose words are to be considered
   *
   */

  public static void main(String[] args) {

    if (args.length == 1) {

      try {

       inputSource words = new inputSource(args[0]);

        // Create a hash table with chaining that can be used
        // to store mappings in which keys are String's
        // and values are Integer's. Then use this to
        // keep track of the words that are included in
        // the input file, along with the number of
        // times each appears in the file

        SimpleHashFunction<String> hashFunction
          = new SimpleHashFunction<String>(500);

        HashStructureWithChaining<String, Integer> hashStructure
          = new HashStructureWithChaining<String, Integer>(500);

        SimpleHashTable<String, Integer> t
          = new SimpleHashTable<String, Integer>(hashFunction, hashStructure);

        while (words.hasNext()) {

          String w = words.next();
          Integer usage= t.get(w);
          if (usage == null) {
            usage = Integer.valueOf(1);
          } else {
            usage = Integer.valueOf(usage.intValue()+1);
          };
          t.put(w, usage);

        };

        System.out.println("");
        System.out.print("File Processed: ");
        System.out.println(args[0]);
        System.out.println("");

        if (t.size() == 1) {
          System.out.println("One word appears in this file.");
        } else {
          System.out.print(t.size());
          System.out.println(" words appear in this file.");
        };
        System.out.println();
        System.out.println();

        // Display the words in the file, in dictionary order,
        // along its number of occurrences

        System.out.println("            Word                 Occurrences");
        System.out.println("");

        ArrayList<comparablePair<String, Integer>> sortedWords
                          = new ArrayList<comparablePair<String, Integer>>();

        for (Pair<String, Integer> current: t) {
          sortedWords.add(new comparablePair<String, Integer>(current.first(),
                                                            current.second()));
        };

        mySort.sort(sortedWords);

        int i = 0;

        for (comparablePair<String, Integer> current: sortedWords) {

          i++;
          String w = current.first();
          Integer usage = current.second();
          System.out.printf("%10d", i);
          System.out.print(": ");
          System.out.printf("%-20s", w);
          System.out.print(" ");
          System.out.printf("%11d", usage.intValue());
          System.out.println("");

        };

        System.out.println();

      } catch (FileNotFoundException ex) {

        // The specified file name could not be parsed, the
        // the file did not exist, or was a directory

        System.out.print("Sorry! Unable to find or open ");
        System.out.println("the specified file.");

      } catch (SecurityException ex) {

        // A security manager exists and has prevented access
        // to the specified file

        System.out.println("Sorry! The specified file cannot be read.");

      };

    } else {

      // The wrong number of inputs has been supplied.

      System.out.print("Sorry! A single input (the name of ");
      System.out.println("a text file) must be provided.");

    };

  }

}
