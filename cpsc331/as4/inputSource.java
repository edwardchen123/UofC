package CPSC331Assignment4;

import java.util.Iterator;
import java.util.Scanner;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.SecurityException;
import java.lang.UnsupportedOperationException;

/**
 *
 * A text scanner that attempts to remove surrounding punctuation
 * (other than apostrophes and hyphens that are necessary parts of
 * words) and ignore case of letters in words.
 * <br />
 *
 * <p>
 *  This should be suitable for the processing of ASCII text files
 *  that contain correctly formatted English text. Since it does
 *  not include attempt to check for non-alphanumeric symbols
 *  inside text, or for apostrophes that are not followed by
 *  non-alphanumeric text (or, indeed, for a variety of other things)
 *  it is not suitable for the processing of other types of
 *  text files.
 * </p>
 *
 */

public class inputSource implements Iterator<String> {

  protected String nextString;      // To be returned by next call to next
  protected String remainingString; // Unprocessed part of string
                                    // most recently returned by the
                                    // Scanner
  protected Scanner rawIOSource;    // The scanner that does the work
                                    // of iterating over a text file

  // Useful Constants

  private static final String PUNCTUATION
    = "!\"#$%&()*+`-./\\:;,<>?@[]{}^_|~'-";

  /**
   *
   * Constructs a new inputSource that produces strings scanned
   * from a text file.
   *
   *
   * @param source A file to be scanned
   * @throws FileNotFoundException if the file does not exist, is
   *         a directory rather than a regular file, or for some
   *         other reason cannot be opened for reading
   * @throws SecurityException if a security manager exists and
   *         the <code>CheckRead</code> method denies read access
   *         to the file
   *
   */

  public inputSource(String source) throws FileNotFoundException {

    rawIOSource = new Scanner(new FileInputStream(source));

    remainingString = null;
    while ((rawIOSource.hasNext()) && 
     ((remainingString == null) || (remainingString.length() == 0))) {
      remainingString = strip((rawIOSource.next()).toLowerCase());
    };

    if ((remainingString == null) || (remainingString.length() == 0)) {
      remainingString = null;
    } else {
      int i = dash_location(remainingString);
      if (i < remainingString.length()-1) {
        nextString = strip(remainingString.substring(0, i));
        remainingString = 
           strip(remainingString.substring(i+1, remainingString.length()));
      } else {
        nextString = remainingString;
        remainingString = null;
      };
    };

  }

  /**
   *
   * Reports whether strings are left in the input source.
   *
   * @return true if strings remain in the input source.
   *
   */

  public boolean hasNext() {

    return ((nextString != null) && (nextString.length() > 0));


  }

  /**
   *
   * Returns the next string in the input source.
   *
   * @return the next string in the input source
   * @throws NoSuchElementException if there are no strings left
   *
   */

  public String next() {

    String outputString = nextString;
    nextString = null;
    
    if ((remainingString == null) || (remainingString.length() == 0)) {
      remainingString = null;
      while ((rawIOSource.hasNext()) &&
       ((remainingString == null) || (remainingString.length() == 0))) {
        remainingString = strip((rawIOSource.next()).toLowerCase());
      };
    };

    if (remainingString != null) {
      int i = dash_location(remainingString);
      if (i < remainingString.length()-1) {
        nextString = strip(remainingString.substring(0, i));
        remainingString =
           strip(remainingString.substring(i+1, remainingString.length()));
      } else {
        nextString = remainingString;
        remainingString = null;
      };
    };

    return outputString;

  }

  /**
   *
   * This class does not support the remove method.
   *
   * @throws UnSupportedOperationException if called
   */

  public void remove() {

    throw new UnsupportedOperationException();

  }

 /*
  * Function to remove punctuation from the beginning and end
  * of a given string, except that the suffix "s'" is retained.
  *
  */

  private static String strip(String rawString) {

    String currentString = rawString.trim();

    while ((currentString.length() > 0) &&
           (PUNCTUATION.indexOf(currentString.charAt(0)) >= 0)) {

      currentString 
         = (currentString.substring(1, currentString.length())).trim();

    };

    while ((currentString.length() > 0) &&
           (PUNCTUATION.indexOf(
              currentString.charAt(currentString.length()-1)) >= 0) &&
           ((currentString.length() == 1) ||
            (currentString.charAt(currentString.length()-2) != 's') ||
            (currentString.charAt(currentString.length()-1) != '\''))) {
      currentString
         = (currentString.substring(0, currentString.length()-1)).trim();

    };

    return currentString;

  }

  /*
   * Function to seach for a dash represented by two (or more)
   * hyphens in a given strip'd string, returning either the
   * location of the beginning of the first occurrence of a dash,
   * of the string length if no dash is found
   */

  private static int dash_location(String inString) {

    int i = 0;

    while ((i < inString.length()-1) &&
           ((inString.charAt(i) != '-') ||
            (inString.charAt(i+1) != '-'))) {
      i++;
    };

    if (i < inString.length()-1) {
      return i;
    } else {
      return inString.length();
    }

  }

}

