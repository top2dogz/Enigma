package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Izaac Ruiz
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        this._name = name;
        this._notches = notches.toCharArray();
        this._setting = 0;
        this.perm = perm;

    }

    @Override
    boolean atNotch() {
        for (Character el: this._notches) {
            if (el.equals(perm.alphabet().toChar(this.setting()))) {
                return true;
            }
        }
        return false;

    }

    @Override
    void advance() {
        set((_setting + 1) % alphabet().size());
    }

    @Override
    boolean rotates() {
        return true;
    }

    private char[] _notches;
    private int _setting;
    private Permutation perm;
    private String _name;
}
