import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class textSearch {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        ArrayList<String> targetNames = new ArrayList<String>();
        targetNames.add("Makar Alexeevich");
        targetNames.add("Joseph Bazdeev");
        targetNames.add("Boris Drubetskoy");

        int lineCount = 0;

        try {
            FileReader fileReader = new FileReader("WarAndPeace.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            HashMap<String, Integer> namePositions = new HashMap<String, Integer>();
            for (String name : targetNames) {
                namePositions.put(name, 0);
            }

            String line = "";
            String nextLine = "";
            while ((line = bufferedReader.readLine()) != null) {
                lineCount++;

                if (containsName(line, targetNames)) {

                    // Adding names as keys to a HashMap and setting position as value
                    for (String nameKey : namePositions.keySet()) {
                        if (line.contains(nameKey)) {
                            namePositions.put(nameKey, line.indexOf(nameKey) + 1);
                        } else {
                            namePositions.put(nameKey, 0);
                        }
                    }

                    // Prints line #, position, and full name in order of occurrence
                    Map<String, Integer> sortedPositions = sortMapByValue(namePositions);
                    for (Map.Entry<String, Integer> entry : sortedPositions.entrySet()) {
                        if (entry.getValue() != 0) {
                            System.out.println(lineCount + "\t" + entry.getValue() + "\t" + entry.getKey());
                        }
                    }

                    // Checks if a line with a full name also has a partial full name
                    bufferedReader.mark(1000);
                    if ((nextLine = bufferedReader.readLine()) != null) {
                        checkForSplitName(lineCount, line, nextLine, namePositions);
                    }
                    bufferedReader.reset();
                }

                if (!containsName(line, targetNames)) {
                    // For a partial full name starting on the current line but ending on the next
                    // line
                    bufferedReader.mark(1000);
                    if ((nextLine = bufferedReader.readLine()) != null) {
                        checkForSplitName(lineCount, line, nextLine, namePositions);
                    }
                    bufferedReader.reset();
                }

            }

            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        long elapsedTime = System.currentTimeMillis() - startTime;
        System.out.println("Elapsed time: " + elapsedTime + " milliseconds");
    }

    public static boolean containsName(String currentLine, ArrayList<String> nameList) {
        boolean result = false;
        for (String name : nameList) {
            if (currentLine.contains(name))
                result = true;
        }
        return result;
    }

    public static HashMap<String, Integer> sortMapByValue(HashMap<String, Integer> input) {
        List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(input.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> pair1, Map.Entry<String, Integer> pair2) {
                return (pair1.getValue()).compareTo(pair2.getValue());
            }
        });

        HashMap<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> pair : list) {
            sortedMap.put(pair.getKey(), pair.getValue());
        }
        return sortedMap;
    }

    public static void checkForSplitName(int lineCounter, String lineInput, String nextLineInput,
            HashMap<String, Integer> namePositions) {
        HashMap<String, Integer> result = new HashMap<String, Integer>(namePositions);

        for (String nameKeyString : result.keySet()) {
            String[] nameParts = nameKeyString.split("\\s+");
            String[] lineParts = lineInput.split("\\s+");
            String[] nextLineParts = nextLineInput.split("\\s+");
            if (lineParts[lineParts.length - 1].equals(nameParts[0])) {
                if (nextLineParts[0].equals(nameParts[1])) {
                    result.put(nameKeyString, lineInput.indexOf(nameParts[0]) + 1);
                } else {
                    result.put(nameKeyString, 0);
                }
                if (result.get(nameKeyString) != 0) {
                    System.out.println(lineCounter + "\t" + result.get(nameKeyString) + "\t" + nameKeyString);
                }
            }
        }
    }
}