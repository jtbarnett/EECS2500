
public class LinkedIntStack
{
	protected LLNode<Integer> top;
	
	public void push(int element)
	{
		LLNode<Integer> newNode = new LLNode<Integer>(element);
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

	public int top()
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
