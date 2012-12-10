package CPSC331Assignment3;

/**
 *
 * Provides an ordered pair of elements each of a different type.
 *
 */

public class Pair<S, T> {

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
   */

  public Pair(S first, T second) {

    this.first = first;
    this.second = second;

  }

  /**
   *
   * Reports the first element of this ordered pair.
   *
   */

  public S first() {

    return this.first;

  }

  /**
   *
   * Reports the second element of this ordered pair.
   *
   */

  public T second() {

    return this.second;

  }

}

