public class Chat126 {
    // generates text through a Markov chain trajectory using size k kgrams and
    // number T transitions read from the command line
    public static void main(String[] args) {
        String file = StdIn.readAll(); // read in text from standard input
        int k = Integer.parseInt(args[0]); // store the size k kgrams
        final int T = Integer.parseInt(args[1]); // store size T transitions

        // Create a new language model based on the input file
        MarkovLM languageModel = new MarkovLM(file, k);

        // Create a new string to store and append the transitions
        StringBuilder kgram = new StringBuilder();
        String initialKgram = file.substring(0, k);
        kgram.append(initialKgram);

        StdOut.print(kgram);

        // Repeat T - k times to build a string of transitions size T
        for (int i = 0; i < T - k; i++) {
            // will generate a character based on that kgram
            char predictedCharacter = languageModel.predictNext(kgram.toString());
            kgram.append(predictedCharacter);

            // Print the predicted character and remove the first
            StdOut.print(predictedCharacter);
            kgram.deleteCharAt(0);
        }
        StdOut.println();
    }
}
