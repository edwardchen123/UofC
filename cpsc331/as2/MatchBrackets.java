package CPSC331Assignment2;
import CPSC331Assignment2.IArrayStack;

/**
 *
 * Class providing a method in which a stack is used to check whether
 * brackets in a given expression are matched, along with a simple
 * main method to provide a few simple tests.
 *
 * <p>
 *  Based on material in Section 5.4 of the Koffman
 *  and&nbsp;Wolfgang&rsquo;s text, &ldquo;Objects, Absraction,
 *  Data Structures and Design Using Java Version&nbsp;5.0:&rdquo;
 *  The method  <code>isBalanced</code> is obtained by making a very
 *  small number of changes to the method presented in Listing&nbsp;5.3
 *  on pages&nbsp;267&ndash;268.
 * </p>
 *
 * @author Wayne Eberly
*/

public class MatchBrackets {

 /**
 * Main method provides a few very simple test cases.
 */

 public static void main(String[] args) {

  // Create a few test cases.

  String[] inputs = new String[5];
  boolean[] results = new boolean[5];

  inputs[0] = "([{1+2}-(3+4)])+5";    results[0]= true;
  inputs[1] = "(([{1+2}-(3+4)]) + 5"; results[1]=false;
  inputs[2] = "([{1+2}-(3+4)])";      results[2]=true;
  inputs[3] = "([{1+2}-(3+4)]]";      results[3]=false;
  inputs[4] = "([{1+2}-(3+4)])+5)";   results[4]=false;

  for(int i=0; i < inputs.length; i++) {

    // Process the ith input

    System.out.print("Input Expression: ");
    System.out.println(inputs[i]);

    if (results[i]) {
      System.out.println("Brackets in this expression are matched.");
    } else {
      System.out.println("Brackets in this expression are not matched.");
    };

    if (isBalanced(inputs[i]) == results[i]) {
      System.out.println("Method returns the correct output.");
    } else {
      System.out.println("ERROR: Method does not return the correct output.");
    };
    System.out.println("");

  };

 }

 // Constants
 /** Set of opening parenthesis characters. */
 private static final String OPEN =  "([{";
 /** Set of closing parenthesis characters. */
 private static final String CLOSE = ")]}";

 /**
 * Method to report whether the brackets in a given expression
 * are matched.
 * <br /><br />
 *
 * <p>
 *  <strong>Precondition:</strong>
 *   <code>expression</code> is a string.<br />
 *  <strong>Postcondition:</strong><br />
 *   <ol style="list-style-type: lower-alpha"> 
 *   <li> <code>expression</code> is not changed</li>
 *   <li> Value returned is <code>true</code> if and only if
 *        all brackets in the given <code>expression</code>
 *        are matched and, furthermore, each left bracket
 *        (, [, or { is matched by a right bracket
 *        with the same type </li>
 *   </ol>
 *
 *   @param expression String to be checked
 *   @return true if and only if all brackets in the expression
 *           are matched
 */

 public static boolean isBalanced (String expression ) {

    // Create an empty stack.
    IArrayStack s = new IArrayStack();
   
   int index = 0;

   while (index < expression.length()) {

     /*
     * Loop Invariant:
     *  a) expression is not changed
     *  b) index is an integer such that
     *     0 <= index < expression.length()
     *  c) The prefix of expression with length index
     *     is a prefix for string in which brackets are matched
     *  d) The stack s includes the indices within the
     *     string OPEN of all unmatched brackets found
     *     in the prefix of expression with length index,
     *     ordered on the stack from left to right (that is,
     *     with the leftmost unmatched bracket on the bottom
     *     of the stack and with the rightmost unmatched
     *     bracket on the top
     * Loop Variant: expression.length() - index
     */

     char nextCh = expression.charAt(index);
     int leftLocation = OPEN.indexOf(nextCh);

     if (leftLocation > -1) {

       // nextCh is a left bracket, so its index in OPEN
       // should be pushed onto the stack
       s.push(leftLocation);

     } else {

       int rightLocation = CLOSE.indexOf(nextCh);
       if (rightLocation > -1) {

         // nextCh is a right bracket, so we should try to match
         // it with something on the stack

         if (s.empty()) {

           // There is nothing on the stack to match the right
           // bracket that has just been found

           return false;

         } else {

	   int prevLeftLocation = s.pop();

           if (prevLeftLocation != rightLocation) {

             // The rightmost unmatched leftmost bracket is
             // not of the same type as the right bracket
             // that has just been found

             return false;

           };

         };

       };

     };

     index++;

   };

   // All characters in the input string have now been processed.
   // Brackets in the input are therefore all balanced if and
   // only if the stack is now empty.

   return s.empty();

 }

}
