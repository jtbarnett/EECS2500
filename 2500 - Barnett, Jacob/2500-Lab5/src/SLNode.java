
//Written by Jacob Barnett on 11/19/2019

//This is the node structure for our skip list word counter
public class SLNode<U>
{
	//the value in the node
	private U value;
	
	private SLNode<U> up = null;
	private SLNode<U> down = null;
	private SLNode<U> left = null;
	private SLNode<U> right = null;
	
	//negative and positive infinity to show head and tail of list
	public static String negInf = "-oo";
	public static String posInf = "+oo";
	
	public SLNode(U value)
	{
		//defualt constructor with only value input
		
		this(value, null, null, null, null);
	}
	
	public SLNode(U value, SLNode<U> up, SLNode<U> down, SLNode<U> left, SLNode<U> right)
	{
		//constructor with all inputs given
		
		this.value = value;
		this.up = up;
		this.down = down;
		this.left = left;
		this.right = right;
	}
	
	//All setter and getter for value (i.e. Word)
	public void setValue(U value)
	{
		this.value = value;
	}
	
	public U getValue()
	{
		return value;
	}
	
	//All set link methods
	public void linkUp(SLNode<U> up)
	{
		this.up = up;
	}
	
	public void linkDown(SLNode<U> down)
	{
		this.down = down;
	}
	
	public void linkLeft(SLNode<U> left)
	{
		this.left = left;
	}
	
	public void linkRight(SLNode<U> right)
	{
		this.right = right;
	}
	
	
	//All get link methods
	public SLNode<U> up()
	{
		return up;
	}
	
	public SLNode<U> down()
	{
		return down;
	}
	
	public SLNode<U> left()
	{
		return left;
	}
	
	public SLNode<U> right()
	{
		return right;
	}
}

