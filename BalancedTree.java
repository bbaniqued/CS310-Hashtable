package data_structures;

import java.util.Iterator;
import java.util.TreeMap;

public class BalancedTree<K extends Comparable<K>, V> implements DictionaryADT<K, V> {
	private TreeMap<K, V> treeMap;

	public BalancedTree() {
		treeMap = new TreeMap<K, V>();
	}

	public boolean contains(K key) {
		return treeMap.containsKey(key);
	}

	public boolean add(K key, V value) {
		if (contains(key))
			return false;
		treeMap.put(key, value);
		return true;
	}

	public boolean delete(K key) {
		if (contains(key)) {
			treeMap.remove(key);
			return true;
		}
		return false;
	}

	public V getValue(K key) {
		return treeMap.get(key);
	}

	@SuppressWarnings("unchecked")
	public K getKey(V value) {
		Iterator<K> keyIterator = treeMap.keySet().iterator();
		Iterator<V> valueIterator = treeMap.values().iterator();
		while (keyIterator.hasNext() && valueIterator.hasNext()) {
			K nextKey = keyIterator.next();
			V nextValue = valueIterator.next();
			if (((Comparable<V>) value).compareTo(nextValue) == 0) {
				return nextKey;
			}
		}
		return null;
	}

	public int size() {
		return treeMap.size();
	}

	public boolean isFull() {
		return false;
	}

	public boolean isEmpty() {
		// inherited from Map interface
		return treeMap.isEmpty();
	}

	public void clear() {
		treeMap.clear();
	}

	public Iterator<K> keys() {
		return treeMap.keySet().iterator();
	}

	public Iterator<V> values() {
		return treeMap.values().iterator();
	}
}