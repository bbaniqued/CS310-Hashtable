public class ProductTester {
	private ProductLookup lookup;

	public ProductTester(int maxSize) {
		lookup = new ProductLookup(maxSize);
		runTests();
		lookup.printAll();
	}

	private void runTests() {
		StockItem item = new StockItem("AGT-1234", "Runner", "Nike", 37.15f, 79.95f);
		lookup.addItem("AGT-1234", item);
	}

	public static void main(String[] args) {
		new ProductTester(1000);
	}
}