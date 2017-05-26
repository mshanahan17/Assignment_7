package Project;
/*
 * Class to produce parameterized node objects to use in linked list
 */
public class Node<T> {
	T value = null;
	Node<T> next = null;

	Node(T o) {
		value = o;
	}
	Node(T o, Node<T> n) {
		value = o;
		next = n;
	}
}
