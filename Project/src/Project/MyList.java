package Project;

import java.util.Comparator;
import java.util.Iterator;


public class MyList<T> implements Iterable<T>{


	protected Comparator<T> comparator = null;//the comparator object to use on the list
	protected Node<T> head = null;//first node
	protected Node<T> tail = null;//last node
	protected Node<T> iter = null;// node to iterate list
	boolean start = false;
	protected int size = 0;//size of List
	
	/**
	 * List constructor takes a type of comparator that is desired
	 * as a parameter.
	 * @param c
	 */
	public MyList(Comparator<T> c) {
		comparator = c;
	}

	/** Add the Object to the set. */
	public void add(T value) {
		if (value == null) {//prevents list being filled with null values
			return;
		}

		Node<T> newnode = new Node<T>((T) value);
		if (head == null) {//if list is empty add node to the head and set tail equal to head
			head = tail = new Node<T>((T) value);
		} else {//uses comparator to maintain ordering
			if (comparator.compare(value, head.value) <= 0) {//first compares it to head 
				newnode.next = head;
				head = newnode;
			}
			else if (comparator.compare(value, tail.value) >= 0) {//compares it to tail
				tail.next = newnode;
				tail = newnode;
			} else {
				Node<T> next = head.next;
				Node<T> prev = head;
				while (comparator.compare(value, next.value) > 0) {//compares in order through the list
					prev = next;
					next = next.next;
				}

				prev.next = newnode;
				newnode.next = next;
			}
		}
		size++;//incriments size
	}

	public boolean isEmpty() {//check to see if list is empty
		if (head == null) {
			return true;
		}
		return false;
	}

	public int size() {//returns the size of list
		return size;
	}

	@Override
	public Iterator<T> iterator() {//calls iterator
		return (Iterator<T>) new MyIterator();
	}
	

	/**
	 * class to use the enhanced for loop
	 * @author Matt
	 *
	 */
	public class MyIterator implements Iterator<T>{

		@Override
		public boolean hasNext() {
			if (!start) {
				iter = head;
				start = true;
			}
			if (iter == null) {
				start = false;
				return false;
			} else {
				return true;
			}

		}

		@Override
		public T next() {
			if (iter == null) {
				iter = head;
				T ret = (T) iter.value;
				iter = iter.next;
				return ret;
			} else {
				T ret = (T) iter.value;
				iter = iter.next;
				return ret;
			}
		}
	}

}

/**
 * The comparator classes built for testing purposes
 * This one orders by Owner last name. If owner last name is 
 * the same then orders by first name.
 * @author Matt
 *
 */
class NameComparator implements Comparator<Portfolio> {
	public int compare(Portfolio o1, Portfolio o2) {

		int sort = o1.getOwner().getLastName().compareTo(o2.getOwner().getLastName());
		if(sort !=0){
			return sort;
		}
		return o1.getOwner().getFirstName().compareTo(o2.getOwner().getFirstName());
	}
}

/**
 * Orders by total asset value of the portfolio
 * large to small
 * @author Matt
 *
 */
class AssetComparator implements Comparator<Portfolio> {
	public int compare(Portfolio o1, Portfolio o2) {
		return Double.compare(o2.getValue(), o1.getValue());
	}
}

/**
 * Orders by managers. First it orders by manager type,
 * no manager first. Then it orders by manager name
 * @author Matt
 *
 */
class ManagerComparator implements Comparator<Portfolio> {
	public int compare(Portfolio o1, Portfolio o2) {

		if(o1.getManager() != null && o2.getManager() != null){
			int sort = o1.getManager().getType().compareTo(o2.getManager().getType());
			if(sort != 0){
				return sort;
			}
			else{
				int sort2 = o1.getManager().getLastName().compareTo(o2.getManager().getLastName());
				if(sort2 !=0){
					return sort2;
				}
				return o1.getManager().getFirstName().compareTo(o2.getManager().getFirstName());
			}
		}
		else{
			if(o1.getManager() != null){
				return 1;
			}
			else if(o2.getManager() != null){
				return -1;
			}
			else{
				return 0;
			}
		}
	}
}


