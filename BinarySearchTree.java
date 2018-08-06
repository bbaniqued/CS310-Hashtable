package data_structures;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;

public class BinarySearchTree<K extends Comparable<K>, V> implements DictionaryADT<K, V> {
	private Node<K, V> root;
	private int currentSize, modCounter;

	// Class Node
	@SuppressWarnings("hiding")
	private class Node<K, V> {
		private K key;
		private V value;
		private Node<K, V> leftChild;
		private Node<K, V> rightChild;

		public Node(K k, V v) {
			this.key = k;
			this.value = v;
			leftChild = rightChild = null;
		}
	}

	// Constructor
	public BinarySearchTree() {
		root = null;
		currentSize = 0;
		modCounter = 0;
	}

	public boolean add(K k, V v) {
		Node<K, V> newNode = new Node<K, V>(k, v);
		Node<K, V> current = root;
		currentSize++;
		modCounter++;

		if (root == null) {
			root = newNode;
			return true;
		}

		for (;;) {
			@SuppressWarnings("unchecked")
			int compare = ((Comparable<K>) k).compareTo(current.key);
			if (compare < 0) {
				if (current.leftChild == null) {
					current.leftChild = newNode;
					return true;
				}
				current = current.leftChild;
			} else if (compare > 0) {
				if (current.rightChild == null) {
					current.rightChild = newNode;
					return true;
				}
				current = current.rightChild;
			} else {
				// There is a duplicate:
				currentSize--;
				modCounter--;
				return false;
			}
		}
	}

	public boolean contains(K key) {
		if (root == null)
			return false;
		Node<K, V> current = root;
		for (;;) {
			@SuppressWarnings("unchecked")
			int compare = ((Comparable<K>) key).compareTo(current.key);
			if (compare < 0) {
				if (current.leftChild == null)
					return false;
				current = current.leftChild;
			} else if (compare > 0) {
				if (current.rightChild == null)
					return false;
				current = current.rightChild;
			} else
				return true;
		}
	}

	private boolean didRemove;

	private Node<K, V> getSuccessor(Node<K, V> n) {
		// Terminating condition: if no more left child then return current n
		if (n.leftChild == null)
			return n;
		return getSuccessor(n.leftChild);
	}

	private Node<K, V> checkAndRemove(K key, Node<K, V> current) {
		if (current == null) {
			didRemove = false;
			return current;
		}

		@SuppressWarnings("unchecked")
		int compare = ((Comparable<K>) key).compareTo(current.key);
		if (compare < 0)
			current.leftChild = checkAndRemove(key, current.leftChild);
		else if (compare > 0)
			current.rightChild = checkAndRemove(key, current.rightChild);
		else if (current.leftChild != null && current.rightChild != null) {

			current.key = getSuccessor(current.rightChild).key;

			// Delete the successor node
			current.rightChild = checkAndRemove(current.key, current.rightChild);
		} else {
			// The remove node has 1 child
			if (current.leftChild != null)
				current = current.leftChild;
			else
				current = current.rightChild;
			didRemove = true;
		}
		return current;
	}

	public boolean delete(K key) {
		root = checkAndRemove(key, root);
		if (didRemove) {
			modCounter++;
			currentSize--;
			return didRemove;
		}
		return false;
	}

	private K foundKey;

	// keyLookup calls itself recursively until key is found
	@SuppressWarnings("unchecked")
	private void keyLookup(V value, Node<K, V> current) {
		if (current == null)
			return;
		int compare = ((Comparable<V>) value).compareTo(current.value);
		if (compare == 0) {
			foundKey = current.key;
			return;
		}
		keyLookup(value, current.leftChild);
		keyLookup(value, current.rightChild);
	}

	public K getKey(V value) {
		// Call keyLookup and start looking from root node.
		keyLookup(value, root);
		return foundKey;
	}

	@SuppressWarnings("unchecked")
	public V getValue(K key) {
		if (root == null)
			return null;
		Node<K, V> current = root;
		while (((Comparable<K>) current.key).compareTo((K) key) != 0) {
			if (((Comparable<K>) key).compareTo((K) current.key) < 0)
				current = current.leftChild;
			else
				current = current.rightChild;
			if (current == null)
				return null;
		}
		return current.value;
	}

	public int size() {
		return currentSize;
	}

	public boolean isFull() {
		return false;
	}

	public boolean isEmpty() {
		return currentSize == 0;
	}

	public void clear() {
		currentSize = 0;
		modCounter = 0;
		root = null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Iterator<K> keys() {
		return new KeyIteratorHelper();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Iterator<V> values() {
		return new ValueIteratorHelper();
	}

	// ------------Key Iterator------------
	@SuppressWarnings("hiding")
	class KeyIteratorHelper<K> implements Iterator<K> {
		protected int idx;
		protected long modCheck;
		private Node<K, V>[] array;

		@SuppressWarnings("unchecked")
		public KeyIteratorHelper() {
			array = new Node[currentSize];
			modCheck = modCounter;
			inOrderFillArray((Node<K, V>) root);
			idx = 0;
		}

		public boolean hasNext() {
			if (modCheck != modCounter)
				throw new ConcurrentModificationException();
			return idx < currentSize;
		}

		public K next() {
			if (!hasNext())
				throw new NoSuchElementException();
			return (K) array[idx++].key;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

		// Traverse the tree inorder
		private void inOrderFillArray(Node<K, V> n) {
			if (n == null)
				return;
			inOrderFillArray(n.leftChild);
			array[idx++] = n;
			inOrderFillArray(n.rightChild);
		}
	}

	@SuppressWarnings("hiding")
	class ValueIteratorHelper<V> implements Iterator<V> {
		Iterator<K> keyIterator;

		public ValueIteratorHelper() {
			keyIterator = keys();
		}

		public boolean hasNext() {
			return keyIterator.hasNext();
		}

		@SuppressWarnings("unchecked")
		public V next() {
			return (V) getValue(keyIterator.next());
		}

		public void remove() {
			keyIterator.remove();
		}
	}
}
