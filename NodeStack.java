public class NodeStack 
{
    protected Node top;	// reference to the head node
    protected int size;	// number of elements in the stack

    public NodeStack() 
    {	
        top = null;
        size = 0;
    }
    public int size() 
    {
        return size; 
    }
    public boolean isEmpty() 
    {
        if (top != null) 
            return false;
        return true;
    }
    public void push(char elem) //for postfix
    {
        if(isEmpty())
            top = new Node(elem, top);
        else
        {
            Node newTop = new Node(elem, top);
            top = newTop;
        }
        size++;
    }
    
    public void push(String elem) //for prefix
    {
        if(isEmpty())
            top = new Node(elem, top);
        else
        {
            Node newTop = new Node(elem, top);
            top = newTop;
        }
        size++;
    }
    public String top() throws Exception
    {
        if(top != null)
            return top.getElement();
        else
            return "\nERROR: Stack is empty";
        
    }
    public String pop() 
    {
        if(isEmpty())
            return "\nERROR: Stack is empty";
	else
	{
        	Node temp = top;
        	top = top.getNext();
        	size--;
     	   	return temp.getElement();
	}
    }
    
    public class Node
    {
        private String element="";
        private Node next;

        public Node()
        {
            this('\0', null);
        }
        public Node(String elem, Node n)
        {
            element = elem;
            next = n;
        }

        public Node(char elem, Node n) 
        {
            element += elem;
            next = n;
        }
        public String getElement() 
        {
            return element;
        }
        public Node getNext() 
        {
            return next;
        }
    }
}
