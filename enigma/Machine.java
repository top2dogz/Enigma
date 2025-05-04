package enigma;



import java.util.ArrayList;
import java.util.HashMap;


import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author Izaac Ruiz
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            HashMap<String, Rotor> allRotors) {
        _alphabet = alpha;
        _numRotors = numRotors;
        _pawls = pawls;
        _allRotors = allRotors;
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _pawls;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        for (String rotor: rotors) {
            if (!_allRotors.containsKey(rotor)) {
                throw new EnigmaException
                        ("Rotor specified is not in allRotors");}
            _machineRotors.add(_allRotors.get(rotor));
        }
        if (!_machineRotors.get(0).reflecting()) {
            throw new EnigmaException("Machine not constructed correctly");
        }

    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        for (int i = 0; i < numRotors()-1; i++) {
            if (!_machineRotors.get(i).reflecting()) {
                _machineRotors.get(i).set(setting.charAt(i - 1));
            }
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        this._plugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        int pawlLeft = numPawls();
        int saveState = c;
        boolean plugboardImplemented = (!_plugboard.equals(""));

        for (int i = 0; i < numRotors(); i++) {
            if (!_machineRotors.get(i).reflecting()) {
                if (i == numRotors()-1) {
                    _machineRotors.get(i).advance();
                    pawlLeft -= 1;
                }
                else if (_machineRotors.get(i+1).atNotch() && _machineRotors.get(i).rotates()) {
                    _machineRotors.get(i).advance();
                    pawlLeft -= 1;
                }
                else if (_machineRotors.get(i).atNotch() && _machineRotors.get(i-1).rotates()) {
                    _machineRotors.get(i-1).advance();
                    pawlLeft -= 1;
                }
            }
        }
        if (pawlLeft != 0) {
            throw new EnigmaException("Too many pawls given");
        }
        if (plugboardImplemented) {
            saveState = _plugboard.permute(saveState);
        }
        for (int f = numRotors()-1; f >= 0; f--) {
            saveState = _machineRotors.get(f).convertForward(saveState);
        }
        for (int b = 0; b < numRotors()-1; b++) {
            saveState = _machineRotors.get(b).convertBackward(saveState);
        }
        if (plugboardImplemented) {
            saveState = _plugboard.invert(saveState);
        }
        return saveState;


    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        StringBuilder msgString = new StringBuilder(msg);
        for (int i = 0; i < msg.length(); i++){
            msgString.setCharAt(i, _alphabet.toChar(convert(msg.charAt(i))));
        }
        return msgString.toString();
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

    private int _numRotors;
    private int _pawls;
    private HashMap<String, Rotor> _allRotors;
    private Permutation _plugboard;
    private ArrayList<Rotor> _machineRotors;
}
