import CPSC331Assignment3.BST;
import CPSC331Assignment3.Pair;
import CPSC331Assignment3.BST.BSTIterator;

import java.util.Random;

/**
 *
 * A simple test of the BST class.
 * 10000 key-value pairs are inserted in which the keys are randomly
 * generated integers and the values are the integers i from 1 to
 * 1000 (in increasing order). The keys and values are then listed using
 * an iterator for the BST that has been produced.
 *
 */

public class BSTDemo2 {

  public static void main(String[] args) {

    // Create and add key-value pairs to a BST.

    BST<Integer, Integer> t = new BST<Integer, Integer>();
    Random numbers = new Random(10023875);    // Use your ID as the seed here!

    for (int i = 1; i <= 10000; i++) {

      Integer c1 = numbers.nextInt();
      Integer c2 = new Integer(i);
      t.put(c1, c2);

    };

    // List the keys and values in increasing order by key

    BST<Integer, Integer>.BSTIterator iter = t.iterator();

    while (iter.hasNext()) {

      Pair<Integer, Integer> current = iter.next();
      Integer c1 = current.first();
      Integer c2 = current.second();
      System.out.printf("%12s", c1.toString());
      System.out.print(" ");
      System.out.printf("%12s", c2.toString());
      System.out.println("");

    };

  }

}
