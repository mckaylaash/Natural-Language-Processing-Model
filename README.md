# Natural-Language-Processing-Model
Implements symbol tables and Markov chains to create a statistical model of text and generate specified pseudo-random text .

**MarkovLM**
The probability of choosing each successive letter to depend on the preceding letters. It predicts each letter with a fixed probability. 

Specific toString() method that returns a representation of the Markov model listing each kgram, the following character, and the number of times that character is found in the input text. 

Overloaded freq() method returns the number of times the kgram appeared in the text, as well as the number of times the given character followed the kgram in the text. 

**Chat126**
Utilizes the predictNext() method of the MarkovLM class to predict the next characters. The current state of the kgram is inputted into the MarkovLM and used to predict the next character. The number of states *T* is given by the seond command line argument. 
