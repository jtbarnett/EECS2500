
//A building block for linked data types. Contains a payload of the
//specified generic type and a reference to the next node in the list.
//If the link is null, then this node is the last node in the list
public class Node<U>
{
    //The payload of the node
    private U value;
    //The next node in the list
    private Node<U> next;

    public Node(U value)
    {
        this(value, null);
    }

    public Node(U value, Node<U> next)
    {
        this.value = value;
        this.next = next;
    }

    public void setValue(U value)
    {
        //Sets the payload of this node to the specified value
    	
        this.value = value;
    }

    public U getValue()
    {
        //return the payload of this node
    	
        return value;
    }

    public Node<U> next()
    {
        //return the next node in the list
    	
        return next;
    }

    public void linkTo(Node<U> next)
    {
        //Sets the link to the next node to point to the specified node
    	
        this.next = next;
    }
}
