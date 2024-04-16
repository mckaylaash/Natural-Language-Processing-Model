import java.util.Arrays;

public class MarkovLM {
    private static final int ASCII = 128; // Number of elements in ASCII
    private int k; // Store the order of the Markov chain
    private ST<String, Integer> kgrams; // Symbol table for the kgrams
    private ST<String, int[]> chars; // Symbol table for each following char

    // creates a Markov model of order k based on the specified text
    public MarkovLM(String text, int k) {
        // assigning instance variables
        this.k = k;
        kgrams = new ST<String, Integer>();
        chars = new ST<String, int[]>();

        // Create a circular string for easier iteration
        String circularText = text + text.substring(0, k);

        // iterates through the circular string and updates kgram values.
        for (int i = 0; i < text.length(); i++) {
            // Separates the circular string into kgram values
            String kgram = circularText.substring(i, i + k);

            // if the kgram has not been encountered, add to the ST
            if (!kgrams.contains(kgram)) {
                kgrams.put(kgram, 1);
                chars.put(kgram, new int[ASCII]);
            }
            // if the kgram has been encountered, update the frequency
            else kgrams.put(kgram, kgrams.get(kgram) + 1);

            // Creates a temporary array to update the character ST
            int[] temp = chars.get(kgram);

            // Iterates through the ASCII string, gets the index, and
            // updates the correlated index in the character ST
            char character = circularText.charAt(i + k);
            temp[character] = temp[character] + 1;
            chars.put(kgram, temp);
        }
    }

    // returns the order of the model (also known as k)
    public int order() {
        return k;
    }

    // returns a String representation of the model (more info below)
    public String toString() {
        // Create a new array to alphabetize the list of kgrams
        String[] sortedKgrams = new String[kgrams.size()];

        int index = 0;
        // Iterates through the keys in the kgram ST and adds to the array
        for (String kgram : kgrams.keys()) {
            sortedKgrams[index] = kgram;
            index++;
        }
        // Sorts through the array and alphabetizes
        Arrays.sort(sortedKgrams);

        StringBuilder result = new StringBuilder();
        // Iterates through the ST and adds the kgrams to the results string
        for (String key : sortedKgrams) {
            result.append(key + ": ");

            // get the character frequency array
            int[] frequencies = chars.get(key);

            // for each non-zero entry, append the character and the frequency
            // trailing space is allowed
            for (int i = 0; i < frequencies.length; i++) {
                if (frequencies[i] != 0) {
                    result.append((char) i);
                    result.append(" " + frequencies[i] + " ");
                }
            }
            result.append("\n");
        }
        return result.toString();
    }

    // returns the # of times 'kgram' appeared in the input text
    public int freq(String kgram) {
        if (kgram.length() != k)
            throw new IllegalArgumentException("Kgram must be of length k.");

        // Returns the frequency of kgram in the text, or zero if not present
        if (!kgrams.contains(kgram)) return 0;
        else return kgrams.get(kgram);
    }

    // returns the # of times 'c' followed 'kgram' in the input text
    public int freq(String kgram, char c) {
        if (kgram.length() != k)
            throw new IllegalArgumentException("Kgram must be of length k.");

        if (!kgrams.contains(kgram)) return 0;
        else {
            int[] storage = chars.get(kgram);
            return storage[c];
        }
    }

    // returns a character, chosen with weight proportional to the
    // number of times each character followed 'kgram' in the input text
    public char predictNext(String kgram) {
        // Establishing corner cases
        if (kgram.length() != k) {
            throw new IllegalArgumentException("Kgram must be of length k.");
        }
        if (!kgrams.contains(kgram)) {
            throw new IllegalArgumentException("Kgram does not appear in the text.");
        }

        double total = kgrams.get(kgram); // number of kgrams in the input text
        int[] frequencies = chars.get(kgram); // character frequency for that kgram
        double[] probabilities = new double[ASCII]; // probabilities for each kgram

        // Iterate through the array of frequencies andcalculate the probabilities
        for (int i = 0; i < ASCII; i++) {
            probabilities[i] = frequencies[i] / total;
        }
        return (char) (StdRandom.discrete(probabilities));
    }

    // tests all instance methods to make sure they're working as expected
    public static void main(String[] args) {
        // Create Markov test models
        String text1 = "banana";
        MarkovLM model1 = new MarkovLM(text1, 2);
        StdOut.println(model1);

        // Tests order and frequency methods.
        StdOut.println("order: " + model1.order());
        StdOut.println("freq(\"an\", 'a')    = " + model1.freq("an", 'a'));
        StdOut.println("freq(\"na\", 'b')    = " + model1.freq("na", 'b'));
        StdOut.println("freq(\"na\", 'a')    = " + model1.freq("na", 'a'));
        StdOut.println("freq(\"na\")         = " + model1.freq("na"));
        StdOut.println();

        // Iterates through 100 times to get an estimate for predictNext
        // functioning correctly
        int count1 = 0;
        for (int i = 0; i < 100; i++) {
            char prediction = model1.predictNext("na");
            if (prediction == 'b') count1++;
        }

        StdOut.println("expected prediction for 'b' after 'an': 0.50");
        StdOut.println("prediction for 'b' after 'an': " + (double) (count1) / 100);
        StdOut.println();

        String text2 = "one fish two fish red fish blue fish";
        MarkovLM model2 = new MarkovLM(text2, 4);
        StdOut.println("order: " + model2.order());
        StdOut.println("freq(\"ish \", 'r') = " + model2.freq("ish ", 'r'));
        StdOut.println("freq(\"ish \", 'x') = " + model2.freq("ish ", 'x'));
        StdOut.println("freq(\"ish \")      = " + model2.freq("ish "));
        StdOut.println("freq(\"tuna\")      = " + model2.freq("tuna"));


        // Iterates through 100 times to get an estimate for predictNext
        // functioning correctly
        int count2 = 0;
        for (int i = 0; i < 100; i++) {
            char prediction = model2.predictNext("fish");
            if (prediction == 'o') count2++;
        }

        StdOut.println("expected prediction for 'b' after 'an': 0.25");
        StdOut.println("prediction for 'b' after 'an': " + (double) (count2) / 100);


    }
}

// use stringbuilder
