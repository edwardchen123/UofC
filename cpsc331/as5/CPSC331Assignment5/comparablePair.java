package CPSC331Assignment5;

/**
 *
 * Provides an ordered pair of elements each of a different type.
 * <br />
 *
 * <p>
 *  The first type must implement the Comparable interface; the
 *  ordering of the first element in each pair is used to determine
 *  an ordering for pairs.
 * </p>
 *
 */

public class comparablePair<S extends Comparable<S>, T> 
             implements Comparable<comparablePair<S, T>> {

  /**
   *
   * Instance Variables
   *
   */

  private S first;     // The first element of the ordered pair
  private T second;    // The second element of the ordered pair

  /**
   *
   * Constructs a new object with the inputs used as the first
   * and second elements in the pair, respectively.
   *
   * @param first element of new Pair to be created
   * @param second element of new Pair to be created
   */

  public comparablePair(S first, T second) {

    this.first = first;
    this.second = second;

  }

  /**
   *
   * Reports the first element of this ordered pair.
   *
   * @return the first element of this Pair
   *
   */

  public S first() {

    return this.first;

  }

  /**
   *
   * Reports the second element of this ordered pair.
   *
   * @return the second element of this Pair
   *
   */

  public T second() {

    return this.second;

  }

  /**
   *
   * Provides a comparison operation by using the natural ordering
   * for first elements in pairs.
   *
   */

  public int compareTo(comparablePair<S, T> other) {

    S otherFirst = other.first();
    return first.compareTo(otherFirst);

  }


}

