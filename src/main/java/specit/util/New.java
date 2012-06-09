package specit.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 */
public class New {
    public static <T> ArrayList<T> arrayList () {
        return new ArrayList<T>();
    }

    public static <T> ArrayList<T> arrayList (Collection<? extends T> elements) {
        return new ArrayList<T>(elements);
    }

    public static <K,V> HashMap<K, V> hashMap() {
        return new HashMap<K, V>();
    }

    public static <K,V> HashMap<K, V> hashMap(K k1, V v1) {
        HashMap<K, V> map = new HashMap<K, V>();
        map.put(k1, v1);
        return map;
    }

    public static <K,V> HashMap<K, V> hashMap(K k1, V v1, K k2, V v2) {
        HashMap<K, V> map = new HashMap<K, V>();
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    public static <K,V> HashMap<K, V> hashMap(K k1, V v1, K k2, V v2, K k3, V v3) {
        HashMap<K, V> map = new HashMap<K, V>();
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return map;
    }


    public static <T> LinkedList<T> linkedList() {
        return new LinkedList<T>();
    }

    public static <T> ConcurrentLinkedQueue<T> concurrentLinkedQueue() {
        return new ConcurrentLinkedQueue<T>();
    }

    public static <K,V> ConcurrentHashMap<K,V> concurrentHashMap() {
        return new ConcurrentHashMap<K,V>();
    }

    public static <T> CopyOnWriteArrayList<T> copyOnWriteArrayList() {
        return new CopyOnWriteArrayList<T>();
    }
}
