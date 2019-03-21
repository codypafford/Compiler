import java.util.*;


class HashTable {


    private Function[] hashArray;

    public HashTable(int primeSize) {

        hashArray = new Function[primeSize];  // will store the Strings in the correct location after hashing

    }

    void createHashArray(int hashValue, Function function) {  //The Insertion
        int probe;
        ArrayList<Integer> collisionTracker = new ArrayList<>();

        if (hashArray[hashValue] == null) { //if location is empty

            hashArray[hashValue] = function;


            probe = -1;

        } else {

            if (hashValue == (hashArray.length - 1)) {    // Checks if it is the end of the array

                probe = 0;     // moves the probing index to the beginning

            } else {
                probe = hashValue + 1;   // If not the end of array, add one because its Linear Probing!
            }

        }

        while ((probe != -1) && (probe != hashValue)) {    // probe cannot equal hashValue because of next else
            //statement
            if (hashArray[probe] == null) {

                hashArray[probe] = function;

                if (collisionTracker != null) {
                    for (Integer number : collisionTracker) {
                        // System.out.println("\tCollision at " + number);
                    }
                }
                probe = -1;


            } else {

                if (probe == (hashArray.length - 1)) {     // Checks if it is the end of the array

                    probe = 0;

                } else {
                    collisionTracker.add(probe);
                    probe++;    // increments index because Linear Probing!
                }

            }
        }
    }

    Boolean SearchLinearProbe(Function funct) {    //The Search
        int hashValue = SemanticAnalyzer.findhashVal(funct.getName());
        while (hashArray[hashValue] != null) {
            Function p = new Function(hashArray[hashValue]);

            if (p.getName().equals(funct.getName())) {
             //   System.out.println("found it");
                return true;
            }
            hashValue = (hashValue + 1) % 501;

        }
      //  System.out.println("NOT FOUND");
        return false;
    }

    Function SearchByFunction(String name) {    //The Search
        // pair = The Pair made from each new Line
        int hashValue = SemanticAnalyzer.findhashVal(name);
        while (hashArray[hashValue] != null) {
            Function p = new Function(hashArray[hashValue]);

            if (p.getName().equals(name)) {
               // System.out.println("found it");
                return p;
            }
            hashValue = (hashValue + 1) % 501;

        }
     //   System.out.println("NOT FOUND");
        return null;
    }

    Boolean isThisAnArray(String name) {    //The Search
        // pair = The Pair made from each new Line
        int hashValue = SemanticAnalyzer.findhashVal(name);
        while (hashArray[hashValue] != null) {
            Function p = new Function(hashArray[hashValue]);

            if (p.getName().equals(name)) {
                // System.out.println("found it");
                return true;
            }
            hashValue = (hashValue + 1) % 501;

        }
      //  System.out.println("NOT FOUND");
        return false;
    }


    void printArray() {
        for (int i = 0; i < hashArray.length; i++) {
            if (hashArray[i] != null) {
                Function f = hashArray[i];
                 System.out.println(Function.ANSI_GREEN + f);
                System.out.println("");
            }
        }
    }
}