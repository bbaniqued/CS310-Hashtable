import data_structures.*;

import java.util.Iterator;

public class ProductLookup {
	private DictionaryADT<String, StockItem> lookup;

	public ProductLookup(int maxSize) {
		lookup =
				new Hashtable<String,StockItem>(maxSize);
//				new BalancedTree<String,StockItem>();
//				new BinarySearchTree<String, StockItem>();

	}

	public void addItem(String SKU, StockItem item) {
		lookup.add(SKU, item);
	}

	public StockItem getItem(String SKU) {
		return lookup.getValue(SKU);
	}

	public float getRetail(String SKU) {
		if (lookup.contains(SKU)) {
			return lookup.getValue(SKU).getRetail();
		}
		return (-.01f);
	}

	public float getCost(String SKU) {
		if (lookup.contains(SKU)) {
			return lookup.getValue(SKU).getCost();
		}
		return (-.01f);
	}

	public String getDescription(String SKU) {
		if (lookup.contains(SKU)) {
			return lookup.getValue(SKU).getDescription();
		}
		return null;
	}

	public boolean deleteItem(String SKU) {
		if (lookup.contains(SKU)) {

			lookup.delete(SKU);
			return true;
		}
		return false;
	}

	public void printAll() {
		Iterator<StockItem> itemIter = values();
		while (itemIter.hasNext()) {
			System.out.println(itemIter.next());
		}
	}

	public void print(String vendor) {
		Iterator<StockItem> itemIter = values();
		while (itemIter.hasNext()) {
			StockItem currentItem = itemIter.next();
			if (currentItem.getVendor().equalsIgnoreCase(vendor))
				System.out.println(currentItem);
		}
	}

	public Iterator<String> keys() {
		return lookup.keys();
	}

	public Iterator<StockItem> values() {
		return lookup.values();
	}

}
