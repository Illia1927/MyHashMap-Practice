package mate.academy.hashMap;

public class Main {
    public static void main(String[] args) {
        ReferenceMyHashMap<String, String> myHashMap = new ReferenceMyHashMap<>();
        myHashMap.put("1", "2");
        myHashMap.put("2", "10");
        myHashMap.put("3", "123");
        myHashMap.put("4", "12311");
        myHashMap.put("5", "132131");

        for (int i = 1; i < myHashMap.size() + 1; i++) {
            System.out.println("Value " + i + " in myHashMap : " + myHashMap.get(String.valueOf(i)));
        }

        System.out.println("Processing delete 1 element of myHashmap : " + myHashMap.remove("5"));

        System.out.println("Size of myHashmap(before using the 'clear' method) : " + myHashMap.size());

        myHashMap.clear();
        System.out.println("Size of myHashmap(after using the 'clear' method) : " + myHashMap.size());

    }
}
