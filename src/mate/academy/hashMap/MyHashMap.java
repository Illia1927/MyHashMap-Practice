package mate.academy.hashMap;

public interface MyHashMap<K, V> extends Iterable<V> {
    boolean put(K key, V value);

    boolean remove(K key);

    V get(K key);

    int size();

    void clear();
}
