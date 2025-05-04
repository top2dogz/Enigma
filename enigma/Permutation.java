package enigma;


import java.util.HashMap;


import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Izaac Ruiz
 */
class Permutation {

    /**
     * Set this Permutation to that specified by CYCLES, a string in the
     * form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     * is interpreted as a permutation in cycle notation.  Characters in the
     * alphabet that are not included in any cycle map to themselves.
     * Whitespace is ignored.
     */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        _cycles = cycles;
        _permList = new HashMap<>();
        _invPermList = new HashMap<>();
        for (int i = 0; i < alphabet().size(); i++) {
            _permList.put(_alphabet.toChar(i), _alphabet.toChar(i));
            _invPermList.put(_alphabet.toChar(i), _alphabet.toChar(i));
        }
        isolateCycles(_cycles);
    }




    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        char firstLetter = cycle.charAt(0);
        for (int i = 0; i < cycle.length(); i++) {
            if (i == cycle.length() - 1) {
                _permList.replace(cycle.charAt(i), firstLetter);
                _invPermList.replace(firstLetter, cycle.charAt(i));
            } else {
                _permList.replace(cycle.charAt(i), cycle.charAt(i + 1));
                _invPermList.replace(cycle.charAt(i + 1), cycle.charAt(i));
            }
            if (_permList.values().contains(" ") | _invPermList.values().contains(" ")) {
                throw new EnigmaException("Cycle formed incorrectly");
            }
        }
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }


    /** Returns a single cycle that contains p. **/
    private void isolateCycles(String cycles) {
        String[] cycleList = cycles.split("((\\s*[(])|([)]\\s*[(])|[)]\\s*)");
        for (String cycle: cycleList) {
            if (!cycle.equals("")) {
                addCycle(cycle);
            }
        }
    }


    /** Returns the size of the alphabet I permute. */
    int size() {
        return _permList.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        return _alphabet.toInt(permute(_alphabet.toChar(p % _alphabet.size())));


    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        return _alphabet.toInt(invert(_alphabet.toChar(c % _alphabet.size())));
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        return _permList.get(p);
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        return _invPermList.get(c);
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        for (int i = 0; i < alphabet().size(); i++) {
            if (permute(alphabet().toChar(i)) == alphabet().toChar(i)) {
                return false;
            }
        }
        return true;
    }


    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

    /** Cycles for this permutation. */
    private String _cycles;

    /** List of permutations **/
    private HashMap<Character, Character> _permList;

    /** List of inverses **/
    private HashMap<Character, Character> _invPermList;
}
