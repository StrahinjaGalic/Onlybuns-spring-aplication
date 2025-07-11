package com.onlybuns.onlybuns.Service;

import org.springframework.stereotype.Component;
import java.util.BitSet;
import java.util.List;

@Component
public class BloomFilter {
    
    private static final int DEFAULT_SIZE = 10000;
    private static final int[] HASH_SEEDS = {3, 5, 7, 11, 13, 17, 19, 23};
    
    private BitSet bitSet;
    private int size;
    private int[] hashSeeds;
    
    public BloomFilter() {
        this.size = DEFAULT_SIZE;
        this.bitSet = new BitSet(size);
        this.hashSeeds = HASH_SEEDS;
    }
    
    public BloomFilter(int size, int[] hashSeeds) {
        this.size = size;
        this.bitSet = new BitSet(size);
        this.hashSeeds = hashSeeds;
    }
    
    /**
     * Add an element to the Bloom filter
     */
    public void add(String element) {
        for (int seed : hashSeeds) {
            int hash = hash(element, seed);
            bitSet.set(hash);
        }
    }
    
    /**
     * Check if an element might be in the set
     * @return true if element might be in the set, false if definitely not in the set
     */
    public boolean mightContain(String element) {
        for (int seed : hashSeeds) {
            int hash = hash(element, seed);
            if (!bitSet.get(hash)) {
                return false; // Definitely not in the set
            }
        }
        return true; // Might be in the set
    }
    
    /**
     * Initialize the Bloom filter with existing usernames
     */
    public void initializeWith(List<String> usernames) {
        // Clear existing data
        bitSet.clear();
        
        // Add all existing usernames
        for (String username : usernames) {
            add(username);
        }
    }
    
    /**
     * Hash function using polynomial rolling hash
     */
    private int hash(String element, int seed) {
        int hash = 0;
        for (int i = 0; i < element.length(); i++) {
            hash = (hash * seed + element.charAt(i)) % size;
        }
        return Math.abs(hash) % size;
    }
    
    /**
     * Get current size of the bit array
     */
    public int getSize() {
        return size;
    }
    
    /**
     * Get number of set bits (approximate number of elements)
     */
    public int getApproximateElementCount() {
        return bitSet.cardinality();
    }
    
    /**
     * Clear the filter
     */
    public void clear() {
        bitSet.clear();
    }
    
    /**
     * Estimate false positive probability
     */
    public double estimateFalsePositiveRate(int numberOfElements) {
        double ratio = (double) numberOfElements / size;
        return Math.pow(1 - Math.exp(-hashSeeds.length * ratio), hashSeeds.length);
    }
}
