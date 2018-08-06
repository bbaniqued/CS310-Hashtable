package data_structures;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;

public class Hashtable<K extends Comparable<K>, V> implements DictionaryADT<K, V> {
	private int tableSize, maxSize, currentSize, modifiCounter;
	private ListADT<Wrapper<K, V>>[] list;

	@SuppressWarnings("hiding")
	protected class Wrapper<K, V> implements Comparable<Wrapper<K, V>> {
		K key;
		V value;

		public Wrapper(K key, V value) {
			this.key = key;
			this.value = value;
		}

		@SuppressWarnings("unchecked")
		public int compareTo(Wrapper<K, V> o) {
			return ((Comparable<K>) key).compareTo((K) o.key);
		}
	}

	@SuppressWarnings("unchecked")
	public Hashtable(int n) {
		maxSize = n;
		tableSize = (int) (1.3f * n);
		currentSize = 0;
		modifiCounter = 0;
		list = new LinkedListDS[tableSize];
		for (int i = 0; i < tableSize; i++)
			list[i] = new LinkedListDS<Wrapper<K, V>>();
	}

	private int getIndex(K key) {
		return ((key.hashCode() & 0x7FFFFFFF) % tableSize);
	}

	public boolean contains(K key) {
		return list[getIndex(key)].contains(new Wrapper<K, V>(key, null));
	}

	public boolean add(K key, V value) {
		if (isFull())
			return false;
		if (contains(key))
			return false;
		list[getIndex(key)].addFirst(new Wrapper<K, V>(key, value));
		currentSize++;
		modifiCounter++;
		return true;
	}

	public boolean delete(K key) {
		if (list[getIndex(key)].remove(new Wrapper<K, V>(key, null))) {
			currentSize--;
			modifiCounter++;
			return true;
		}
		return false;
	}

	public V getValue(K key) {
		Wrapper<K, V> tmp = list[getIndex(key)].find(new Wrapper<K, V>(key, null));
		if (tmp == null) {
			return null;
		}
		return tmp.value;
	}

	@SuppressWarnings("unchecked")
	public K getKey(V value) {
		for (int i = 0; i < tableSize; i++)
			for (Wrapper<K, V> n : list[i])
				if (((Comparable<V>) value).compareTo(n.value) == 0)
					return n.key;
		return null;
	}

	public int size() {
		return currentSize;
	}

	public boolean isFull() {
		return currentSize == maxSize;
	}

	public boolean isEmpty() {
		return currentSize == 0;
	}

	public void clear() {
		for (int i = 0; i < tableSize; i++)
			list[i].makeEmpty();
		currentSize = 0;
		modifiCounter = 0;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Iterator<K> keys() {
		return new KeyIteratorHelper();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Iterator<V> values() {
		return new ValueIteratorHelper();
	}

	@SuppressWarnings("hiding")
	class KeyIteratorHelper<K> implements Iterator<K> {
		private Wrapper<K, V>[] nodes;
		private int idx;
		private long modCheck;

		@SuppressWarnings({ "rawtypes", "unchecked" })
		private Wrapper[] shellSort(Wrapper array[]) {
			Wrapper[] n = array;
			int in, out, h = 1;
			Wrapper temp;
			int size = n.length;

			while (h <= size / 3)
				h = h * 3 + 1;

			while (h > 0) {
				for (out = h; out < size; out++) {
					temp = n[out];
					in = out;
					while (in > h - 1 && ((Comparable<Wrapper>) n[in - h]).compareTo(temp) >= 0) {
						n[in] = n[in - h];
						in -= h;
					}
					n[in] = temp;
				}
				h = (h - 1) / 3;
			}
			return n;
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		public KeyIteratorHelper() {
			nodes = new Wrapper[currentSize];
			idx = 0;
			modCheck = modifiCounter;
			int j = 0;
			for (int i = 0; i < tableSize; i++)
				for (Wrapper n : list[i])
					nodes[j++] = n;
			nodes = shellSort(nodes);
		}

		public boolean hasNext() {
			if (modCheck != modifiCounter)
				throw new ConcurrentModificationException();
			return idx < currentSize;
		}

		public K next() {
			if (!hasNext())
				throw new NoSuchElementException();
			return nodes[idx++].key;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	@SuppressWarnings("hiding")
	class ValueIteratorHelper<V> implements Iterator<V> {
		Iterator<K> keyIter;

		public ValueIteratorHelper() {
			keyIter = keys();
		}

		public boolean hasNext() {
			return keyIter.hasNext();
		}

		@SuppressWarnings("unchecked")
		public V next() {
			return (V) getValue(keyIter.next());
		}

		public void remove() {
			keyIter.remove();
		}
	}

	public class LinkedListDS<E> implements ListADT<E> {

		@SuppressWarnings("hiding")
		protected class Node<E> {
			E data;
			Node<E> next;

			public Node(E obj) {
				data = obj;
				next = null;
			}
		}

		private Node<E> head, tail;
		private int currentSize;

		public LinkedListDS() {
			head = tail = null;
			currentSize = 0;
		}

		public void addFirst(E obj) {
			Node<E> newNode = new Node<E>(obj);
			if (head == null)
				head = tail = newNode;
			else {
				newNode.next = head;
				head = newNode;
			}
			currentSize++;
		}

		public void addLast(E obj) {
			Node<E> newNode = new Node<E>(obj);
			if (head == null)
				head = tail = newNode;
			else {
				tail.next = newNode;
				tail = newNode;
			}
			currentSize++;
		}

		public E removeFirst() {
			if (head == null)
				return null;
			E tmp = head.data;
			if (head == tail)
				head = tail = null;
			else
				head = head.next;
			currentSize--;
			return tmp;
		}

		public E removeLast() {
			// If there is no element in list. Return null.
			if (head == null)
				return null;

			E tmp = tail.data;
			Node<E> previous = null, current = head;

			while (current != tail) {
				previous = current;
				current = current.next;
			}
			// If there is one element.
			if (previous == null)
				head = tail = null;
			else {
				previous.next = null;
				tail = previous;
			}
			currentSize--;
			return tmp;
		}

		public E peekFirst() {
			if (head == null)
				return null;
			return head.data;
		}

		public E peekLast() {
			if (tail == null)
				return null;
			return tail.data;
		}

		@SuppressWarnings("unchecked")
		public E find(E obj) {
			Node<E> tmp = head;
			while (tmp != null) {
				if (((Comparable<E>) obj).compareTo(tmp.data) == 0)
					return tmp.data;
				tmp = tmp.next;
			}
			return null;
		}

		@SuppressWarnings("unchecked")
		public boolean remove(E obj) {
			Node<E> previous = null, current = head;

			// find the note to delete first
			while (current != null && ((Comparable<E>) obj).compareTo(current.data) != 0) {
				previous = current;
				current = current.next;
			}

			// Can't find obj in the list.
			if (current == null)
				return false;

			if (current == head) {
				head = head.next;
				currentSize--;
			} else if (current == tail) {
				previous.next = null;
				tail = previous;
				currentSize--;
			} else {
				previous.next = current.next;
				currentSize--;
			}
			return true;

		}

		public void makeEmpty() {
			head = tail = null;
			currentSize = 0;
		}

		@SuppressWarnings("unchecked")
		public boolean contains(E obj) {
			Node<E> tmp = head;
			while (tmp != null) {
				if (((Comparable<E>) obj).compareTo(tmp.data) == 0)
					return true;
				tmp = tmp.next;
			}
			return false;
		}

		public boolean isEmpty() {
			return (size() == 0);
		}

		public boolean isFull() {
			return false;
		}

		public int size() {
			return currentSize;
		}

		public Iterator<E> iterator() {
			return new IteratorHelper();
		}

		class IteratorHelper implements Iterator<E> {
			Node<E> iterPtr;

			public IteratorHelper() {
				iterPtr = (Node<E>) head;
			}

			public boolean hasNext() {
				return iterPtr != null;
			}

			public E next() {
				if (!hasNext())
					throw new NoSuchElementException();
				E tmp = iterPtr.data;
				iterPtr = iterPtr.next;
				return tmp;
			}

			public void remove() {
				throw new UnsupportedOperationException();
			}
		}

	}

	public interface ListADT<E> extends Iterable<E> {

		public void addFirst(E obj);

		public void addLast(E obj);

		public E removeFirst();

		public E removeLast();

		public E peekFirst();

		public E peekLast();

		public E find(E obj);

		public boolean remove(E obj);

		public void makeEmpty();

		public boolean contains(E obj);

		public boolean isEmpty();

		public boolean isFull();

		public int size();

		public Iterator<E> iterator();

	}

}