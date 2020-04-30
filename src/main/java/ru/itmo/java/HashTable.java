package ru.itmo.java;

public class HashTable {


    public HashTable() {
        loadFactor = DEFAULT_LOAD_FACTOR;
        size = 0;
        table = new Entry[DEFAULT_SIZE];
    }

    public HashTable(int size) {
        loadFactor = DEFAULT_LOAD_FACTOR;
        this.size = 0;
        table = new Entry[size];
    }

    public HashTable(int size, double loadFactor) {
        this(size);
        this.loadFactor = loadFactor;
    }



    Object get(Object key) {
        Entry ans = getEntry(key);
        return ans == null ? null : ans.value;
    }

    Object remove(Object key) {
        Entry entry = getEntry(key);
        if (entry == null) return null;
        size--;
        entry.deleted = true;
        return entry.value;
    }

    Object put(Object key, Object value) {
        resize();
        Entry entry = getEntry(key);
        if (entry != null) {
            Object ans = entry.value;
            entry.value = value;
            if(!entry.deleted){
                return ans;
            }
            size++;
            entry.deleted = false;
            return null;
        }

        int ind = (key.hashCode() % table.length + table.length) % table.length;
        while (table[ind] != null && !table[ind].deleted) {
            ind = (ind + 1) % table.length;
        }
        size++;
        table[ind] = new Entry(key, value);
        return null;
    }


    int size() {
        return size;
    }

    private Entry getEntry(Object key) {
        int ind = (key.hashCode() % table.length + table.length) % table.length;
        int count = 0;
        while (count < table.length && (table[ind] != null)) {
            if (table[ind].key.equals(key) && !table[ind].deleted)
                return table[ind];
            ind = (ind + 1) % table.length;
            count++;
        }
        return null;
    }

    private void resize() {
        if (size + 1 < table.length * loadFactor) {
            return;
        }
        Entry[] save = table;
        table = new Entry[table.length * 2];
        size = 0;
        for (Entry entry : save) {
            if (entry == null || entry.deleted) {
                continue;
            }
            put(entry.key, entry.value);
        }
    }

    private class Entry {
        final Object key;
        boolean deleted = false;
        Object value;
        int hash;

        Entry(Object key, Object value) {
            this.key = key;
            this.value = value;
            this.hash = key.hashCode();
        }

        Entry(Object key, Object value, boolean deleted) {
            this.key = key;
            this.value = value;
            this.deleted = deleted;
        }

    }

    private static final int DEFAULT_SIZE = 25;
    private static final double DEFAULT_LOAD_FACTOR = .7;
    private double loadFactor;
    private int size;
    private Entry[] table;
}
