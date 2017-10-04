/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.datastructures;

import eu.openmos.model.Product;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Introsys
 */
public class ProductManager 
{
    private static final Object lock = new Object();
    private static volatile ProductManager instance = null;

    private final List<Product> products;

    protected ProductManager() 
    {
        products = new ArrayList<>();
    }
    
    public static ProductManager getInstance() 
    {
        ProductManager i = instance;
        if (i == null) {
            synchronized (lock) {
                // While we were waiting for the lock, another 
                i = instance; // thread may have instantiated the object.
                if (i == null) {
                    i = new ProductManager();
                    instance = i;
                }
            }
        }
        return i;
    }
    
    public List<Product> getProductList()
    {
        ProductManager aux = ProductManager.getInstance();
        return aux.products;
    }
    
    
}
