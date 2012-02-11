package CPSC331Assignment5;

import java.lang.Comparable;
import java.util.Comparator;

import CPSC331Assignment3.Pair;

/**
 *
 * Class provides an ordering of pairs of keys and values using
 * the natural ordering of keys
 *
 */

public class pairOrdering<K extends Comparable<K>,
                          V extends Comparable<V>>
             implements Comparator<Pair<K, V>> {

  /**
   *
   * Compares two pairs using the ordering for the second
   * elements in the pairs, with higher values listed first;
   * the natural ordering for first pairs is then used to
   * break ties.
   *
   */

  public int compare(Pair<K, V> o1, Pair<K, V> o2) {

    V t1 = o1.second();
    V t2 = o2.second();

    int result = t1.compareTo(t2);
    if (result > 0) {
      return -1;
    } else if (result < 0) {
      return 1;
    } else {
      K s1 = o1.first();
      K s2 = o2.first();
      return s1.compareTo(s2);
    }

  }

  /**
   *
   * Determines whether two pairs are equal by checking equality
   * of both the first and second elements in each.
   *
   */

  public boolean equals(Pair<K, V> o1, Pair<K, V> o2) {

    K s1 = o1.first(); V t1 = o1.second();
    K s2 = o2.first(); V t2 = o2.second();
    return ((s1.compareTo(s2) == 0) && (t1.compareTo(t2) == 0));

  }

}

