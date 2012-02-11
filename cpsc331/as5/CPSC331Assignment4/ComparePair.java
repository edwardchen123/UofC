package CPSC331Assignment4;

import java.lang.Comparable;
import java.util.Comparator;

import CPSC331Assignment3.Pair;

/**
 *
 * Class provides an ordering of pairs of keys and values using
 * the natural ordering of keys
 *
 */

public class ComparePair<K extends Comparable<K>, V>
             implements Comparator<Pair<K, V>> {

  /**
   *
   * Compares two pairs ussing the ordering for the first
   * elements in the pairs.
   *
   */

  public int compare(Pair<K, V> o1, Pair<K, V> o2) {

    K s1 = o1.first();
    K s2 = o2.first();
    return s1.compareTo(s2);

  }

  /**
   *
   * Determines whether two pairs are equal by checking equality
   * of the first elements in each.
   *
   */

  public boolean equals(Pair<K, V> o1, Pair<K, V> o2) {

    K s1 = o1.first();
    K s2 = o2.first();
    return (s1.compareTo(s2) == 0);

  }

}

