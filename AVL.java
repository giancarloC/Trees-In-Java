/*
AVL Tree Class
Written by Giancarlo Calle
*/
import java.util.Stack;
import java.util.Queue;
import java.util.LinkedList;

public class AVL{
  //Node struct used in the tree
  class Node{
    int data;
    Node right;
    Node left;

    //constructor
    Node(int data){
      this.data = data;
    }
  }

  //will be root of the tree
  Node root;

  //stack used to store parents for methods
  private Stack<Node> stack = new Stack<>();

  //prints nodes in AVL
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

  //method for right rotation using grand parent node
  public Node rotationR(Node p){
    //gets child node
    Node c = p.left;
    Node cRight = c.right;

    //changes pointers
    c.right = p;
    p.left = cRight;

    //if p was root, changes root
    if(root == p){
      root = c;
    }

    return c;
  }

  //method for left rotation using parent node
  private Node rotationL(Node p){
    //gets child node
    Node c = p.right;
    Node cLeft = c.left;

    //changes pointers
    c.left = p;
    p.right = cLeft;

    //if gp is root, changes root
    if(root == p)
      root = c;

    return c;
  }

  /*
  ----------------------------------------------------
  RECUSIVE METHODS
  ----------------------------------------------------
  */

  //recursively returns height of tree
  private int heightRec(Node node){
    //edge case if node is null
    if(node == null)
      return -1;

    //grabs heights from left and right subtrees
    int left = heightRec(node.left) + 1;
    int right = heightRec(node.right) + 1;

    //returns bigger number
    return (left > right ? left : right);
  }


  //returns the balance factor (bf) of a node recursively
  private int bfRec(Node node){
    return heightRec(node.left) - heightRec(node.right);
  }

  //balances tree based on root (node)
  private Node balanceRec(Node node){
    //cannot balance a null node
    if(node == null)
      return null;

    //grabs balance factor
    int bf = bfRec(node);

    //do nothing if balance factor is fine
    if(bf <= 1 && bf >= -1)
      return node;

    Node replace;
    if(bf > 1){ //node is left heavy
      if(bfRec(node.left) <= -1){ //left node is right heavy
        node.left = rotationL(node.left);
      }
      replace = rotationR(node);
    }

    else{ //node is right heavy
      if(bfRec(node.right) >= 1){ //right node is left heavy
        node.right = rotationR(node.right);
      }
      replace = rotationL(node);
    }

    return replace;
  }

  //insertRec recursive helper
  Node insertRecHelper(Node node, int val){
    //creates node
    if(node == null)
      return new Node(val);

    //value of node used to compare
    int valCheck = node.data;

    //edge case if val already exists in tree
    if(valCheck == val)
      return node;

    //checks if node should be on the right of current node
    if(val > valCheck)
      node.right = insertRecHelper(node.right, val);

    //checks if node should be on the left of current node
    else
      node.left = insertRecHelper(node.left, val);

    return balanceRec(node);
  }

  //Inserts into the tree recursively
  void insertRec(int val){
    root = insertRecHelper(root, val);
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
      //return node.left;
    }
    else if(node.data < val){
      node.right = deleteRecHelper(node.right, val);
      //return node.right;
    }

    //value has been found
    else{
      //case: simply delete node if it has no children
      if(node.left == null && node.right == null){
        node = null;
      }

      //case: replace node with child if it only has one child
      else if(node.left == null)
        node = node.right;
      else if(node.right == null)
        node = node.left;

      //case: deleting a node with two children
      else{
        Node r = findNextRec(node);
        int rData = r.data;
        node.data = rData; //replaces node data with next node data
        node.right = deleteRecHelper(node.right, rData); //deletes next node
      }
    }

    return balanceRec(node);

  }

  //deletes node that contains number in the tree
  void deleteRec(int val){
    //deletes recursively
    root = deleteRecHelper(root, val);
  }

  /*
  ----------------------------------------------------
  ITERATIVE METHODS
  ----------------------------------------------------
  */

  //iteratively returns height of tree
  private int heightIter(Node node){
    //edge case if node is null
    if(node == null)
      return -1;

    //variable to hold height
    int height = 0;

    //creates queue to add all nodes to queue to find height
    Queue<Node> nodes = new LinkedList<>();
    nodes.add(node);
    int size = nodes.size();

    //loops until entire tree has been traversed
    while(size > 0){
      //grabs nodes from the next level and adds the children
      while(size > 0){
        Node pop = nodes.remove();
        if(pop.left != null)
          nodes.add(pop.left);
        if(pop.right != null)
          nodes.add(pop.right);
        size--;
      }

      //adds to height for each level and loops again if more children
      size = nodes.size();
      height++;
    }

    return height;
  }

  //returns the balance factor (bf) of a node iteratively
  private int bfIter(Node node){
    return heightIter(node.left) - heightIter(node.right);
  }

  //balances tree based on root (node) iteratively
  private Node balanceIter(Node node){
    //cannot balance a null node
    if(node == null)
      return null;

    //grabs balance factor
    int bf = bfIter(node);

    //do nothing if balance factor is fine
    if(bf <= 1 && bf >= -1)
      return node;

    Node replace;
    if(bf > 1){ //node is left heavy
      if(bfIter(node.left) <= -1){ //left node is right heavy
        node.left = rotationL(node.left);
      }
      replace = rotationR(node);
    }

    else{ //node is right heavy
      if(bfIter(node.right) >= 1){ //right node is left heavy
        node.right = rotationR(node.right);
      }
      replace = rotationL(node);
    }

    return replace;
  }

  void insertIter(int val){
    //checks if root is empty
    if(root == null){
      root = new Node(val);
      return;
    }

    //prepares variables used to loop
    Node curr = root;
    int valCheck;

    //loops until finds place to insert node
    while(true){
      //adds parent to stack
      stack.push(curr);
      valCheck = curr.data;

      //checks if it should be to the left
      if(val < valCheck){
        //checks if available to insert
        if(curr.left == null){
          curr.left = new Node(val);
          break;
        }
        curr = curr.left;
      }

      //checks if it should be to the right
      else{
        if(curr.right == null){
          curr.right = new Node(val);
          break;
        }
        curr = curr.right;
      }
    }

    //balances up nodes
    curr = stack.pop();
    Node parent;
    while(stack.size() > 0){
      parent = stack.pop();

      //adds balanced node to subtree
      if(parent.data > curr.data)
        parent.left = balanceIter(curr);
      else
        parent.right = balanceIter(curr);

      curr = parent;
    }

    root = balanceIter(curr);
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
    //handles if val is root
    Node curr = root;
    if(root.data == val){
      //case if root has no children
      if(root.left == null && root.right == null){
        root = null;
        return;
      }

      //case: replace root with child if it only has one child
      else if(root.left == null){
        root = root.right;
        return;
      }
      else if(root.right == null){
        root = root.left;
        return;
      }

      //Deletes a node with two children. Finds successor and replaces
      Node r = findNextIter(root);
      int rData = r.data;
      int oldData = root.data;
      root.data = rData;

      //adds root to stack to prepare deletion of inorder successor
      stack.push(root);

      //prepares to loop to delete in order successor node
      if(oldData > rData)
        curr = root.left;
      else
        curr = root.right;

      val = rData;
    }



    //loops until finds node to delete or null
    Node parent;
    while(curr != null){
      //finds node to delete and adds node to stack to balance later
      if(curr.data > val){
        stack.push(curr);
        curr = curr.left;
        continue;
      }
      else if(curr.data < val){
        stack.push(curr);
        curr = curr.right;
        continue;
      }

      parent = stack.pop();

      //case: delete node if it has no children
      if(curr.left == null && curr.right == null){
        if(parent.data > curr.data)
          parent.left = null;
        else
          parent.right = null;
        stack.push(parent);
        break;
      }

      //case: replace node with child if it only has one child
      if(curr.left == null){
        if(parent.data > curr.data)
          parent.left = curr.right;
        else
          parent.right = curr.right;
        stack.push(parent);
        break;
      }
      else if(curr.right == null){
        if(parent.data > curr.data)
          parent.left = curr.left;
        else
          parent.right = curr.left;
        stack.push(parent);
        break;
      }

      //Deletes a node with two children. Finds successor and replaces
      Node r = findNextIter(curr);
      int rData = r.data;
      int oldData = curr.data;
      curr.data = rData;

      //adds to stack to loop again
      stack.push(parent);
      stack.push(curr);

      //prepares to loop to delete in order successor node
      if(oldData > rData)
        curr = curr.left;
      else
        curr = parent.right;

      val = rData;
    }

    //balances up nodes
    curr = stack.pop();
    while(stack.size() > 0){
      parent = stack.pop();

      //adds balanced node to subtree
      if(parent.data > curr.data)
        parent.left = balanceIter(curr);
      else
        parent.right = balanceIter(curr);

      curr = parent;
    }

    root = balanceIter(curr);
  }
}

//end of file
