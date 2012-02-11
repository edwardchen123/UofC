package CPSC331Assignment5;

/*
 * For documentation of test cases, see included .pdf
 */

import CPSC331Assignment3.Pair;
import CPSC331Assignment5.pairOrdering;
import CPSC331Assignment5.mySort;
import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;

public class mySortTest
{
    @Test (expected = NullPointerException.class)
    public void test_sortSNull()
    {
        System.out.println("Test that sortS throws a NullPointerException when passed a null value");
        ArrayList<comparablePair<String, Integer>> A = null;
        mySort.sort(A);
    }

    @Test (expected = NullPointerException.class)
    public void test_sortTNull()
    {
        System.out.println("Test that sortT throws a NullPointerException when passed a null value");
        ArrayList<Pair<String, Integer>> A = null;
        lengthOrdering<String, Integer> order = null;
        mySort.sort(A, order);
    }

    @Test
    public void test_empty()
    {
        System.out.println("Test that sortS and sortT correctly handle an empty ArrayList");
        ArrayList<comparablePair<String, Integer>> A = new ArrayList<comparablePair<String, Integer>>();
        ArrayList<comparablePair<String, Integer>> B = new ArrayList<comparablePair<String, Integer>>();

        ArrayList<Pair<String, Integer>> C = new ArrayList<Pair<String, Integer>>();
        ArrayList<Pair<String, Integer>> D = new ArrayList<Pair<String, Integer>>();
        pairOrdering<String, Integer> order = new pairOrdering<String, Integer>();

        mySort.sort(A);
        mySort.sort(C, order);
        assertTrue(equalS(A, B) && equalT(C, D));
    }

    @Test
    public void test_ordered()
    {
        System.out.println("Test that sortS and sortT correctly handle a pre-sorted ArrayList");
        ArrayList<comparablePair<String, Integer>> A = putS("aaa", 0, "bbb", 1, "ccc", 2);
        ArrayList<comparablePair<String, Integer>> B = A;
        mySort.sort(A);

        ArrayList<Pair<String, Integer>> C = putT("abc", 2, "def", 1, "ghi", 0);
        ArrayList<Pair<String, Integer>> D = C;
        lengthOrdering<String, Integer> order = new lengthOrdering<String, Integer>();
        mySort.sort(C, order);

        assertTrue(equalS(A, B) && equalT(C, D));
    }

    @Test
    public void test_reverse()
    {
        System.out.println("Test that sortS and sortT correctly handle a reverse-order ArrayList");
        ArrayList<comparablePair<String, Integer>> A = putS("ccc", 2, "bbb", 1, "aaa", 0);
        ArrayList<comparablePair<String, Integer>> B = putS("aaa", 0, "bbb", 1, "ccc", 2);
        mySort.sort(A);

        ArrayList<Pair<String, Integer>> C = putT("ghi", 0, "def", 1, "abc", 2);
        ArrayList<Pair<String, Integer>> D = putT("abc", 2, "def", 1, "ghi", 0);
        lengthOrdering<String, Integer> order = new lengthOrdering<String, Integer>();
        mySort.sort(C, order);

        assertTrue(equalS(A, B) && equalT(C, D));
    }

    @Test
    public void test_duplicates()
    {
        System.out.println("Test that sortS and sortT correctly handle an ArrayList with duplicates");
        ArrayList<comparablePair<String, Integer>> A = putS("ccc", 2, "ccc", 2, "aaa", 0);
        ArrayList<comparablePair<String, Integer>> B = putS("aaa", 0, "ccc", 2, "ccc", 2);
        mySort.sort(A);

        ArrayList<Pair<String, Integer>> C = putT("def", 1, "def", 1, "abc", 2);
        ArrayList<Pair<String, Integer>> D = putT("abc", 2, "def", 1, "def", 1);
        lengthOrdering<String, Integer> order = new lengthOrdering<String, Integer>();
        mySort.sort(C, order);

        assertTrue(equalS(A, B) && equalT(C, D));
    }

    @Test
    public void test_identical()
    {
        System.out.println("Test that sortS and sortT correctly handle an ArrayList full of identical values");
        ArrayList<comparablePair<String, Integer>> A = putS("aaa", 0, "aaa", 0, "aaa", 0);
        ArrayList<comparablePair<String, Integer>> B = A;
        mySort.sort(A);

        ArrayList<Pair<String, Integer>> C = putT("abc", 0, "abc", 0, "abc", 0);
        ArrayList<Pair<String, Integer>> D = C;
        lengthOrdering<String, Integer> order = new lengthOrdering<String, Integer>();
        mySort.sort(C, order);

        assertTrue(equalS(A, B) && equalT(C, D));
    }

    @Test
    public void test_arbitrary()
    {
        System.out.println("Test that sortS and sortT correctly handle an arbitrary ArrayList");
        ArrayList<comparablePair<String, Integer>> A = putS("bbb", 1, "aaa", 0, "ccc", 2);
        ArrayList<comparablePair<String, Integer>> B = putS("aaa", 0, "bbb", 1, "ccc", 2);
        mySort.sort(A);

        ArrayList<Pair<String, Integer>> C = putT("def", 1, "abc", 2, "ghi", 0);
        ArrayList<Pair<String, Integer>> D = putT("abc", 2, "def", 1, "ghi", 0);
        lengthOrdering<String, Integer> order = new lengthOrdering<String, Integer>();
        mySort.sort(C, order);

        assertTrue(equalS(A, B) && equalT(C, D));
    }

    private ArrayList<comparablePair<String, Integer>> putS(String firstS, Integer firstI, String secondS, Integer secondI, String thirdS, Integer thirdI)
    {
        ArrayList<comparablePair<String, Integer>> A = new ArrayList<comparablePair<String, Integer>>();
        A.add(new comparablePair<String, Integer>(firstS, firstI));
        A.add(new comparablePair<String, Integer>(secondS, secondI));
        A.add(new comparablePair<String, Integer>(thirdS, thirdI));
        return A;
    }

    private ArrayList<Pair<String, Integer>> putT(String firstS, Integer firstI, String secondS, Integer secondI, String thirdS, Integer thirdI)
    {
        ArrayList<Pair<String, Integer>> A = new ArrayList<Pair<String, Integer>>();
        A.add(new Pair<String, Integer>(firstS, firstI));
        A.add(new Pair<String, Integer>(secondS, secondI));
        A.add(new Pair<String, Integer>(thirdS, thirdI));
        return A;
    }

    private boolean equalS(ArrayList<comparablePair<String, Integer>> A, ArrayList<comparablePair<String, Integer>> B)
    {
        for (int i = 0; i < A.size(); i++)
        {
            if (!(A.get(i).first()).equals(B.get(i).first()) || !(A.get(i).second()).equals(B.get(i).second()))
            {
                return false;
            }
        }
        return true;
    }

    private boolean equalT(ArrayList<Pair<String, Integer>> A, ArrayList<Pair<String, Integer>> B)
    {
        for (int i = 0; i < A.size(); i++)
        {
            if (!(A.get(i).first()).equals(B.get(i).first()) || !(A.get(i).second()).equals(B.get(i).second()))
            {
                return false;
            }
        }
        return true;
    }
}