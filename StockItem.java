import java.util.Iterator;
import data_structures.*;

public class StockItem implements Comparable<StockItem> {
    String SKU;
    String description;
    String vendor;
    float cost;
    float retail;
 
    public StockItem(String SKU, String description, String vendor,
                     float cost, float retail){
    	this.SKU = SKU;
    	this.description = description;
    	this.vendor = vendor;
    	this.cost = cost;
    	this.retail = retail;
    }
 
    public int compareTo(StockItem n){
    	return SKU.compareTo(n.SKU);
    }

    public int hashCode(){
    	return SKU.hashCode();
    }

    public String getDescription() {
    	return this.description;
    }
    
    public String getVendor(){
    	return this.vendor;
    }
    
    public float getCost(){
    	return this.cost;
    }
    
    public float getRetail(){
    	return this.retail;
    }
 
    public String toString(){
    	return "SKU: " + this.SKU 
    			+ " Description: " + this.description 
    			+ " Vendor: " + this.vendor 
    			+ " Cost: " + this.cost 
    			+ " Retail: " + this.retail;
    }
} 