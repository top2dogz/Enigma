package enigma;

import static enigma.EnigmaException.*;

/** Superclass that represents a rotor in the enigma machine.
 *  @author Izaac Ruiz
 */
class Rotor {

    /** A rotor named NAME whose permutation is given by PERM. */
    Rotor(String name, Permutation perm) {
        this._name = name;
        this._permutation = perm;
        this._setting = 0;
    }

    /** Return my name. */
    String name() {
        return _name;
    }

    /** Return my alphabet. */
    Alphabet alphabet() {
        return _permutation.alphabet();
    }

    /** Return my permutation. */
    Permutation permutation() {
        return _permutation;
    }

    /** Return the size of my alphabet. */
    int size() {
        return _permutation.alphabet().size();
    }

    /** Return true iff I have a ratchet and can move. */
    boolean rotates() {
        return false;
    }

    /** Return true iff I reflect. */
    boolean reflecting() {
        return false;
    }

    /** Return my current setting. */
    int setting() {
        return _setting;
    }

    /** Set setting() to POSN.  */
    void set(int posn) {
        this._setting = posn;
    }

    /** Set setting() to character CPOSN. */
    void set(char cposn) {
        this._setting = _permutation.alphabet().toInt(cposn);
    }

    /** Return the conversion of P (an integer in the range 0..size()-1)
     *  according to my permutation. */
    int convertForward(int p) {
        int contactEntered = Math.floorMod
                (_setting + p, _permutation.alphabet().size());
        int contactExiting = _permutation.permute(contactEntered);
        return Math.floorMod
                (contactExiting - _setting, _permutation.alphabet().size());
    }

    /** Return the conversion of E (an integer in the range 0..size()-1)
     *  according to the inverse of my permutation. */
    int convertBackward(int e) {
        int contactEntered = Math.floorMod
                (_setting + e, _permutation.alphabet().size());
        int contactExiting = _permutation.invert(contactEntered);
        return Math.floorMod
                (contactExiting - _setting, _permutation.alphabet().size());
    }

    /** Returns true iff I am positioned to allow the rotor to my left
     *  to advance. */
    boolean atNotch() {
        return false;
    }

    /** Advance me one position, if possible. By default, does nothing. */
    void advance() {
    }

    @Override
    public String toString() {
        return "Rotor " + _name;
    }

    /** My name. */
    private final String _name;

    /** The permutation implemented by this rotor in its 0 position. */
    private Permutation _permutation;

    /** Current setting for permutation */
    private int _setting;
    // FIXME: ADDITIONAL FIELDS HERE, AS NEEDED

}
