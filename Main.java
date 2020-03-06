/*
Array creation methods and main method.
Written by Giancarlo Calle
*/
import java.util.Arrays;
import java.util.Random;
import java.util.Set;
import java.util.HashSet;

public class Main{

  /*
  ----------------------------------------------------
  SORT METHOD USING BST
  ----------------------------------------------------
  */

  //sorts an array using properties of a BST
  static void sort(int[] arr){
    //creates BST to store values in
    BST b = new BST();

    //grabs elements from array and stores in BST
    int i;
    for(i = 0; i < arr.length; i++){
      b.insertRec(arr[i]);
    }

    //extracts to sort
    int num;
    for(i = 0; i < arr.length; i++){
      num = b.findMinRec();
      arr[i] = num;
      b.deleteRec(num);
    }
  }

  /*
  ----------------------------------------------------
  ARRAY CREATION METHODS
  ----------------------------------------------------
  */

  //function that returns a n-array of random numbers
  public static int[] getRandomArray(int n){
    //creates array to be returned
    int[] arr = new int[n];

    //creates random generator
    Random rand = new Random();

    //creates hash set and adds random numbers to it until set is size n
    Set<Integer> unique = new HashSet<Integer>(n);
    while(unique.size() < n){
      unique.add(rand.nextInt());
    }

    //adds numbers in set to array
    int i = 0;
    for(int num: unique){
      arr[i] = num;
      i++;
    }

    return arr;
  }

  //function that returns a n-array of numbers: n, n-1, n-2,..., 1
  public static int[] getSortedArray(int n){
    //creates array to be returned
    int[] arr = new int[n];

    //adds numbers in set to array
    int i;
    for(i = 0; i < n; i++){
      arr[i] = n-i;
    }

    return arr;
  }


  /*
  ----------------------------------------------------
  MAIN METHOD
  ----------------------------------------------------
  */

  public static void main(String arg[]){
    //creates BST and AVL to store random numbers in recursively and iteratively
    BST bTest = new BST();
    BST bRandom = new BST();
    AVL aRandom = new AVL();

    //creates random array of size n
    int n = 10000;
    int[] arr = getRandomArray(n);

    //inserts into BST and AVL
    int i;
    for(i = 0; i < n; i++){
      //inserts iteratively
      aRandom.insertIter(arr[i]);
      bRandom.insertIter(arr[i]);

      //inserts recursively
      bTest.insertRec(arr[i]);
    }

    //prints levels traversed
    System.out.println("Number of levels traversed from random numbers:");
    System.out.println("BST: " + bRandom.getLevels());
    System.out.println("AVL: " + aRandom.getLevels() + "\n");

    //creates BST and AVL to store numbers from a sorted array
    BST bSorted = new BST();
    AVL aSorted = new AVL();

    //creates sorted array of size n (defined above)
    int[] sorted = getSortedArray(n);

    //inserts iteratively into BST and AVL
    for(i = 0; i < n; i++){
      aSorted.insertIter(sorted[i]);
      bSorted.insertIter(sorted[i]);
    }

    System.out.println("Number of levels traversed from sorted numbers:");
    System.out.println("BST: " + bSorted.getLevels());
    System.out.println("AVL: " + aSorted.getLevels());

  }

}
