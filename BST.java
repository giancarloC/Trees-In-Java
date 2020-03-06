/*
Binary Search Tree Class
Written by Giancarlo Calle
*/
import java.util.Stack;

public class BST{
  //Node struct used in the tree
  class Node{
    int data;
    Node right;
    Node left;

    //Node constructor
    Node(int data){
      this.data = data;
    }
  }

  //BST constructor
  public BST(){
    this.root = null;
    this.levels = 0;
  }

  //will be root of the tree
  Node root;

  //records the amount of levels of the tree traversed w/ methods
  int levels;
  public int getLevels(){
    return levels;
  }

  //stack used to store parents for methods
  private Stack<Node> stack = new Stack<>();

  //prints nodes in BST
  private void printHelper(Node node){
    if(node == null)
      return;
    printHelper(node.left);
    System.out.print(node.data + " ");
    printHelper(node.right);
  }

  void print(){
    printHelper(root);
    System.out.print("\n");
  }

  /*
  ----------------------------------------------------
  RECUSIVE METHODS
  ----------------------------------------------------
  */

  //insertRec recursive helper
  void insertRecHelper(Node node, int val){
    //value of node used to compare
    int valCheck = node.data;

    //edge case if val already exists in tree
    if(valCheck == val)
      return;

    //checks if node should be on the right of current node
    if(val > valCheck){
      if(node.right == null)
        node.right = new Node(val);
      else
        insertRecHelper(node.right, val);
    }

    //checks if node should be on the right of current node
    else{
      if(node.left == null)
        node.left = new Node(val);
      else
        insertRecHelper(node.left, val);
    }
  }

  //Inserts into the tree recursively
  void insertRec(int val){
    //checks if tree is empty
    if(root == null)
      root = new Node(val);
    else
      insertRecHelper(root, val);
  }

  //returns the node at the very left of the tree from node recursively
  private Node leftMostNodeRec(Node node){
    if(node.left == null)
      return node;
    return leftMostNodeRec(node.left);
  }

  //returns the node at the very right of the tree from node recursively
  private Node rightMostNodeRec(Node node){
    if(node.right == null)
      return node;
    return rightMostNodeRec(node.right);
  }

  //populates stack with parent nodes
  private void parentsRec(Node curr, Node goal){
    //reached node
    if(curr == goal)
      return;

    //adds node to stack and continues to find the node
    stack.push(curr);
    if(curr.data > goal.data)
      parentsRec(curr.left, goal);
    else
      parentsRec(curr.right, goal);
  }

  //clears the stack
  private void clearStack(){
    while(stack.size() > 0)
      stack.pop();
  }

  //finds the smallest node that is bigger than "node"
  Node findNextRec(Node node){
    //case: smaller node is the left most node in the right subtree
    if(node.right != null)
      return leftMostNodeRec(node.right);

    //case: if node is root, there is no next node
    if(root == node)
      return null;

    //populates stack with parents recursively
    parentsRec(root, node);

    //case: smallest parent that is bigger is next node
    Node parent;
    while(stack.size() != 0){
      parent = stack.pop();
      if(parent.data > node.data){
        clearStack();
        return parent;
      }
    }

    //No next node exists
    return null;
  }

  //finds the biggest node that is smaller than "node"
  Node findPrevRec(Node node){
    //case: smaller node is the right most node in the left subtree
    if(node.left != null)
      return rightMostNodeRec(node.left);

    //case: if node is root, there is no next node
    if(root == node)
      return null;

    //populates stack with parents recursively
    parentsRec(root, node);

    //case: smallest parent that is bigger is next node
    Node parent;
    while(stack.size() != 0){
      parent = stack.pop();
      if(parent.data < node.data){
        clearStack();
        return parent;
      }
    }

    //No next node exists
    return null;
  }

  //finds the minimum number in the tree recursively
  int findMinRec(){
    return leftMostNodeRec(root).data;
  }

  //finds the maximum number in the tree recursively
  int findMaxRec(){
    return rightMostNodeRec(root).data;
  }

  //helper function for deleteRec()
  private Node deleteRecHelper(Node node, int val){
    //edge case if val does not exist in the tree
    if(node == null)
      return null;

    //finds node to delete
    if(node.data > val){
      node.left = deleteRecHelper(node.left, val);
      return node;
    }
    else if(node.data < val){
      node.right = deleteRecHelper(node.right, val);
      return node;
    }

    //case: simply delete node if it has no children
    if(node.left == null && node.right == null){
      return null;
    }

    //case: replace node with child if it only has one child
    if(node.left == null)
      return node.right;
    else if(node.right == null)
      return node.left;

    //case: deleting a node with two children
    Node r = findNextRec(node);
    int rData = r.data;
    node.data = rData; //replaces node data with next node data
    node.right = deleteRecHelper(node.right, rData); //deletes next node
    return node;
  }

  //deletes node that contains number in the tree
  void deleteRec(int val){
    //checks if deleting root
    if(root.data == val){
      root = deleteRecHelper(root, val);
      return;
    }

    deleteRecHelper(root, val);
  }

  /*
  ----------------------------------------------------
  ITERATIVE METHODS
  ----------------------------------------------------
  */

  void insertIter(int val){
    //checks if tree is empty
    if(root == null){
      root = new Node(val);
      return;
    }

    //prepares variables used to loop
    Node curr = root;
    int valCheck;

    //loops until finds place to insert node
    while(true){
      valCheck = curr.data;

      //checks if it should be to the left
      if(val < valCheck){
        //checks if available to insert
        if(curr.left == null){
          curr.left = new Node(val);
          return;
        }
        curr = curr.left;
      }

      //checks if it should be to the right
      else{
        if(curr.right == null){
          curr.right = new Node(val);
          return;
        }
        curr = curr.right;
      }

      //records number of levels traversed
      levels++;
    }
  }

  private Node leftMostNodeIter(Node node){
    while(node.left != null)
      node = node.left;
    return node;
  }

  private Node rightMostNodeIter(Node node){
    while(node.right != null)
      node = node.right;
    return node;
  }

  private void parentsIter(Node curr, Node goal){
    while(curr != goal){
      stack.push(curr);
      if(curr.data > goal.data)
        curr = curr.left;
      else
        curr = curr.right;
    }
  }

  Node findNextIter(Node node){
    //case: smaller node is the left most node in the right subtree
    if(node.right != null)
      return leftMostNodeIter(node.right);

    //case: if node is root, there is no next node
    if(root == node)
      return null;

    //populates stack with parents recursively
    parentsIter(root, node);

    //case: smallest parent that is bigger is next node
    Node parent;
    while(stack.size() != 0){
      parent = stack.pop();
      if(parent.data > node.data){
        clearStack();
        return parent;
      }
    }

    //No next node exists
    return null;
  }

  //finds the biggest node that is smaller than "node"
  Node findPrevIter(Node node){
    //case: smaller node is the right most node in the left subtree
    if(node.left != null)
      return rightMostNodeIter(node.left);

    //case: if node is root, there is no next node
    if(root == node)
      return null;

    //populates stack with parents recursively
    parentsIter(root, node);

    //case: smallest parent that is bigger is next node
    Node parent;
    while(stack.size() != 0){
      parent = stack.pop();
      if(parent.data < node.data){
        clearStack();
        return parent;
      }
    }

    //No next node exists
    return null;
  }

  //finds the minimum number in the tree iteratively
  int findMinIter(){
    return leftMostNodeIter(root).data;
  }

  //finds the maximum number in the tree iteratively
  int findMaxIter(){
    return rightMostNodeIter(root).data;
  }

  //deletes node that contains number in the tree
  void deleteIter(int val){
    //checks if deleting root
    if(root.data == val){
      root = null;
      return;
    }

    //prepares parent node to change it's pointers to other nodes
    Node parent = root;
    Node node;
    if(parent.data > val)
      node = parent.left;
    else
      node = parent.right;

    //loops until finds node to delete or null
    while(node != null){
      //counts how many levels are traversed
      levels++;

      //finds node to delete
      if(node.data > val){
        parent = node;
        node = node.left;
        continue;
      }
      else if(node.data < val){
        parent = node;
        node = node.right;
        continue;
      }

      //returns if node to delete does not exist
      if(node == null)
        return;

      //shows if node is left child or not
      boolean isLeft = true;
      if(node.data > parent.data)
        isLeft = false;

      //case: delete node if it has no children
      if(node.left == null && node.right == null){
        if(isLeft)
          parent.left = null;
        else
          parent.right = null;
        return;
      }

      //case: replace node with child if it only has one child
      if(node.left == null){
        if(isLeft)
          parent.left = node.right;
        else
          parent.right = node.right;
        return;
      }
      else if(node.right == null){
        if(isLeft)
          parent.left = node.left;
        else
          parent.right = node.left;
        return;
      }

      //case deleting a node with two children
      Node replacement = findNextIter(node);
      int replacementData = replacement.data;
      int oldData = node.data;
      node.data = replacementData;

      //prepares to loop to delete in order successor node
      val = replacementData;
      if(oldData > val)
        node = parent.left;
      else
        node = parent.right;
    }
  }
}

//end of file
