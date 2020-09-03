
public class LinkedStack
{
	protected LLNode<Character> top;
	
	public void push(char element)
	{
		LLNode<Character> newNode = new LLNode<Character>(element);
		newNode.setLink(top);
		top = newNode;
	}

	public void pop()
	{
		if(isEmpty())
			System.out.print("");
		else
			top = top.getLink();
	}

	public char top()
	{
		if(isEmpty())
		{
			return 0;
		}
		else
			return top.getInfo();
	}

	public boolean isEmpty()
	{
		return (top == null);
	}
}
