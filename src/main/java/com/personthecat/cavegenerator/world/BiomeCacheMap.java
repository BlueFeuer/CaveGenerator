package com.personthecat.cavegenerator.world;

import java.util.LinkedHashMap;
import java.util.Map;

public class BiomeCacheMap<K, V> extends LinkedHashMap<K, V> {

    private static final int MAX_ENTRIES = 1000;

    public BiomeCacheMap() {
        super(1024, 1.0f, true);
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry eldest) {
        return size() > MAX_ENTRIES;
    }

}