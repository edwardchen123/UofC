package CPSC331Assignment5;

import java.util.ArrayList;
import java.util.Comparator;

/**
 *
 * Provides two versions of a sorting method.
 * <br />
 *
 */

public class mySort
{
    /**
    *
    * Sorts the given ArrayList using the natural order
    * for the given type.
    * <br />
    *
    * <p>
    *  <strong>Precondition 1:</strong>
    * </p>
    * <ol style="list-style-type: lower-alpha">
    * <li> The input ArrayList is not <code>null</code> </li>
    * </ol>
    *
    * <p>
    *  <strong>Postcondition 1:</strong>
    * </p>
    * <ol style="list-style-type: lower-alpha">
    * <li>  The given ArrayList&rsquo;s entries are now
    *       sorted in nondecreasing order, using the given
    *       type&rsquo;s <code>compareTo</code> method.
    *       The entries were reordered but were otherwise
    *       otherwise unchanged. </li>
    * </ol>
    *
    * <p>
    *  <strong>Precondition 2:</strong>
    * </p>
    * <ol style="list-style-type: lower-alpha">
    * <li> The input ArrayList is <code>null</code> </li>
    * </ol>
    *
    * <p>
    *  <strong>Postcondition 2:</strong>
    * </p>
    * <ol style="list-style-type: lower-alpha">
    * <li> A <code>NullPointerException</code> is thrown </li>
    * </ol>
    *
    * @param A The ArrayList to be sorted
    * @throws NullPointerException if the given array is <code>null</code>
    *
    */

    public static <S extends Comparable<S>> void sort(ArrayList<S> A)
    {
        if (A == null)
        {
             throw new NullPointerException();
        }
        else if (A.size() > 0)
        {
            ArrayList<S> B = splitS(A, 0, A.size());
            for (int i = 0; i < A.size(); i++)
            {
                A.set(i, B.get(i));
            }
        }
    }

    /**
    *
    * Sorts the given ArrayList using the order defined by
    * the given Comparator.
    * <br />
    *
    * <p>
    *  <strong>Precondition 1:</strong>
    * </p>
    * <ol style="list-style-type: lower-alpha">
    * <li> The input ArrayList and Comparator are  not <code>null</code> </li>
    * </ol>
    *
    * <p>
    *  <strong>Postcondition 1:</strong>
    * </p>
    * <ol style="list-style-type: lower-alpha">
    * <li>  The given ArrayList&rsquo;s entries are now
    *       sorted in nondecreasing order, using the given
    *       Comparator&rsquo;s <code>compare</code> method.
    *       The entries were reordered but were otherwise
    *       otherwise unchanged. </li>
    * </ol>
    *
    * <p>
    *  <strong>Precondition 2:</strong>
    * </p>
    * <ol style="list-style-type: lower-alpha">
    * <li> Either the input ArrayList or Comparator (or both)
    *      is <code>null</code> </li>
    * </ol>
    *
    * <p>
    *  <strong>Postcondition 2:</strong>
    * </p>
    * <ol style="list-style-type: lower-alpha">
    * <li> A <code>NullPointerException</code> is thrown </li>
    * </ol>
    *
    * @param A The ArrayList to be sorted
    * @param thisOrder Comparator used to define the ordering
    *        to be used for sorting
    * @throws NullPointerException if the given array is <code>null</code>
    *
    */

    public static <T> void sort(ArrayList<T> A, Comparator<T> thisOrder)
    {
        if (A == null || thisOrder == null)
        {
             throw new NullPointerException();
        }
        else if (A.size() > 0)
        {
            ArrayList<T> B = splitT(A, 0, A.size(), thisOrder);
            for (int i = 0; i < A.size(); i++)
            {
                A.set(i, B.get(i));
            }
        }
    }

    private static <S extends Comparable<S>> ArrayList<S> splitS(ArrayList<S> A, int f, int b)
    {
        int lengthA = b-f;
        if (lengthA == 1)
        {
            ArrayList<S> B = new ArrayList<S>(1);
            B.add(A.get(f));
            return B;
        }
        else
        {
            int edge = lengthA/2;
            return mergeS(splitS(A, f, f+edge), splitS(A, f+edge, b));
        }
    }

    private static <S extends Comparable<S>> ArrayList<S> mergeS(ArrayList<S> B, ArrayList<S> C)
    {
        int lengthB = B.size();
        int lengthC = C.size();
        ArrayList<S> A = new ArrayList<S>(lengthB + lengthC);
        int i = 0;
        int j = 0;
        while (i < lengthB || j < lengthC)
        {
            if (i == lengthB) // All elements of B are already in A
            {
                A.add(C.get(j));
                j++;
            }
            else if (j == lengthC) // All elements of C are already in A
            {
                A.add(B.get(i));
                i++;
            }
            else if ((B.get(i)).compareTo(C.get(j)) <= 0) // Element of B <= element of C
            {
                A.add(B.get(i));
                i++;
            }
            else // Element of B > element of C
            {
                A.add(C.get(j));
                j++;
            }
        }
        return A;
    }

    private static <T> ArrayList<T> splitT(ArrayList<T> A, int f, int b, Comparator<T> thisOrder)
    {
        int lengthA = b-f;
        if (lengthA == 1)
        {
            ArrayList<T> B = new ArrayList<T>(1);
            B.add(A.get(f));
            return B;
        }
        else
        {
            int edge = lengthA/2;
            return mergeT(splitT(A, f, f+edge, thisOrder), splitT(A, f+edge, b, thisOrder), thisOrder);
        }
    }

    private static <T> ArrayList<T> mergeT(ArrayList<T> B, ArrayList<T> C, Comparator<T> thisOrder)
    {
        int lengthB = B.size();
        int lengthC = C.size();
        ArrayList<T> A = new ArrayList<T>(lengthB + lengthC);
        int i = 0;
        int j = 0;
        while (i < lengthB || j < lengthC)
        {
            if (i == lengthB) // All elements of B are already in A
            {
                A.add(C.get(j));
                j++;
            }
            else if (j == lengthC) // All elements of C are already in A
            {
                A.add(B.get(i));
                i++;
            }
            else if (thisOrder.compare(B.get(i), C.get(j)) >= 0)// Element of B <= element of C
            {
                A.add(B.get(i));
                i++;
            }
            else // Element of B > element of C
            {
                A.add(C.get(j));
                j++;
            }
        }
        return A;
    }
}