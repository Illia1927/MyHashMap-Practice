package mate.academy.hashMap;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class MyHashMapImpl<K, V> implements MyHashMap<K, V> {

    private Node<K, V>[] hashTable;
    private int size = 0;
    private float threshold;

    public MyHashMapImpl() {
        hashTable = new Node[16];
        threshold = hashTable.length * 0.75F;
    }

    @Override
    public boolean put(final K key, final V value) {
        if (size + 1 >= threshold) {
            threshold *= 2;
            arrayDoubling();
        }

        Node<K, V> newNode = new Node<>(key, value);
        int index = hash(key);

        if (hashTable[index] == null) {
            return simpleAdd(index, newNode);
        }

        List<Node<K, V>> nodeList = hashTable[index].getNodes();

        for (Node<K, V> node : nodeList) {

            if (keyExistButValueNew(node, newNode, value) ||
                    collisionProcessing(node, newNode, nodeList)) {
                return true;
            }
        }
        return false;
    }

    private void arrayDoubling() {
        Node<K, V>[] oldHashTable = hashTable;
        hashTable = new Node[oldHashTable.length * 2];
        size = 0;
        for (Node<K, V> node : oldHashTable) {
            if (node != null) {
                for (Node<K, V> n : node.getNodes()) {
                    put(n.key, n.value);
                }
            }
        }
    }

    private boolean collisionProcessing(
            final Node<K, V> nodeFromList,
            final Node<K, V> newNode,
            final List<Node<K, V>> nodes) {

        if (newNode.hashCode() == nodeFromList.hashCode() &&
                !Objects.equals(newNode.key, nodeFromList.key) &&
                !Objects.equals(newNode.value, nodeFromList.value)
        ) {
            nodes.add(newNode);
            size++;
            return true;
        }
        return false;
    }

    private boolean keyExistButValueNew(
            final Node<K, V> nodeFromList,
            final Node<K, V> newNode,
            final V value) {

        if (newNode.getKey().equals(nodeFromList.getKey()) &&
                !newNode.getValue().equals(nodeFromList.getValue())
        ) {

            nodeFromList.setValue(value);
            return true;
        }
        return false;
    }

    @Override
    public void clear() {
        Node<K, V>[] tab;
        threshold++;
        if ((tab = hashTable) != null && size > 0) {
            size = 0;
            for (int i = 0; i < tab.length; ++i)
                tab[i] = null;
        }
    }

    @Override
    public boolean remove(final K key) {
        int index = hash(key);
        if (hashTable[index] == null)
            return false;
        if (hashTable[index].getNodes().size() == 1) {
            hashTable[index] = null;
            return true;
        }
        return false;
    }

    @Override
    public V get(final K key) {
        int index = hash(key);
        if (index < hashTable.length &&
                hashTable[index] != null) {
            if (hashTable[index].getNodes().size() == 1) {
                return hashTable[index].getNodes().get(0).getValue();
            }

            List<Node<K, V>> list = hashTable[index].getNodes();
            for (Node<K, V> node : list) {
                if (key.equals(node.getKey())) {
                    return node.getValue();
                }
            }
        }
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    private int hash(final K key) {
        int hash = 31;
        hash = hash * 17 + key.hashCode();
        return hash % hashTable.length;
    }

    @Override
    public Iterator<V> iterator() {
        return new Iterator<V>() {
            int counterArray = 0;
            int valuesCounter = 0;
            Iterator<Node<K, V>> subIterator = null;

            @Override
            public boolean hasNext() {
                if (valuesCounter == size)
                    return false;
                if (subIterator == null || !subIterator.hasNext()) {
                    if (moveToNextCell()) {
                        subIterator = hashTable[counterArray].getNodes().iterator();
                    } else {
                        return false;
                    }
                }
                return subIterator.hasNext();
            }

            private boolean moveToNextCell() {
                counterArray++;
                while (hashTable[counterArray] == null) {
                    counterArray++;
                }
                return hashTable[counterArray] != null;
            }

            @Override
            public V next() {
                valuesCounter++;
                return subIterator.next().getValue();
            }
        };
    }

    private boolean simpleAdd(int index, Node<K, V> newNode) {
        hashTable[index] = new Node<>(null, null);
        hashTable[index].getNodes().add(newNode);
        size++;
        return true;
    }

    private class Node<K, V> {
        private List<Node<K, V>> nodes;
        private int hash;
        private K key;
        private V value;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
            nodes = new LinkedList<>();
        }

        private List<Node<K, V>> getNodes() {
            return nodes;
        }

        private int indexOfBucket() {
            int varieble = 0;
            if(hash < 0)
            varieble = hashCode() % hashTable.length;
            return varieble;
        }

        private K getKey() {
            return key;
        }

        private V getValue() {
            return value;
        }

        private void setValue(V value) {
            this.value = value;
        }

        @Override
        public int hashCode() {
            hash = 31;
            hash = hash * 17 + key.hashCode();
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;

            if (obj instanceof Node) {
                Node<K, V> node = (Node) obj;
                return (Objects.equals(key, node.getKey()) &&
                        Objects.equals(value, node.getValue()) &&
                        Objects.equals(hash, node.hashCode()));
            }

            return false;
        }
    }
}
