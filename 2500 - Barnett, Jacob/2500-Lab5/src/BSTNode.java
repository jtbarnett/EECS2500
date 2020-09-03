
//Written by Jacob Barnett on 12/7/2019

//A node in a Binary Tree
public class BSTNode<U>
{
    //the value in the node
    private U value;

    //the left, right, and parent nodes of the current node
    private BSTNode<U> left;
    private BSTNode<U> right;
    private BSTNode<U> parent;

    public BSTNode(U value)
    {
        //default construct a Binary Tree node with empty branches     	
        this(value, null, null, null);
    }
    
    public BSTNode(U value, BSTNode<U> left, BSTNode<U> right, BSTNode<U> parent)
    {
    	this.value = value;
    	this.left = left;
    	this.right = right;
    	this.parent = parent;
    }
    
    public U getValue()
    {
        return value;
    }

    
    //setters for left, right, and parent
    public void setLeft(BSTNode<U> left)
    {
        this.left = left;
    }
    
    public void setRight(BSTNode<U> right)
    {
        this.right = right;
    }
    
    public void setParent(BSTNode<U> parent)
    {
        this.parent = parent;
    }
    
    //getters for left, right, and parent
    public BSTNode<U> left()
    {
        return left;
    }

    public BSTNode<U> right()
    {
        return right;
    }
    
    public BSTNode<U> parent()
    {
        return parent;
    }

    
    public void insert(BSTNode<U> newNode, boolean left)
    {
        //insert the specified branch into the tree at this node on either the left or right sub-branch
        //the newNode is the new node we want to insert into our tree
        //the boolean left variable says whether or not to insert the new node at the left or right of the current node
        if (left)
            setLeft(newNode);
        else
            setRight(newNode);
    }
}