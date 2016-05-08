import java.util.LinkedList;
public class LinkedQueue<AnyType> 
{
private LinkedList<AnyType> queue;
public LinkedQueue() { queue = new LinkedList<>(); }
public int size() { return queue.size(); }
public boolean isEmpty() { return queue.isEmpty(); }
public void enqueue(AnyType e) { queue.add(e); }
public AnyType first() { return queue.get(0); }
public AnyType dequeue() { return queue.remove(0); }
}