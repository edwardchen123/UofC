import CPSC331Assignment3.BST;
import CPSC331Assignment3.Pair;

/**
 *
 * A simple test of the BST class.
 * Key-value pairs, where both the key and the value are the integer i,
 * are inserted for each integer i between 1 and 10000 in increasing
 * order. The keys and values are then listed using an iterator for
 * the BST that has been produced.
 *
 */

public class BSTDemo1 {

  public static void main(String[] args) {

    // Create and add key-value pairs to a BST.

    BST<Integer, Integer> t = new BST<Integer, Integer>();

    for (int i = 1; i <= 3000; i++) {

      Integer ci = new Integer(i);
      t.put(ci, ci);

    }

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

    }

  }

}
