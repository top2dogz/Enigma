package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.*;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Izaac Ruiz
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        Machine m = readConfig();

        while (_input.hasNext()) {
            String[] rotors = new String[m.numRotors()];
            String beginning = _input.next();
            if (beginning.equals("*")) {
                rotors[0] = _input.next();
            } else {
                rotors[0] = beginning.substring(1);
            }
            for (int i = 1; i < m.numRotors(); i++) {
                rotors[i] = _input.next();
            }
            m.insertRotors(rotors);
            String setting = _input.next();
            setUp(m, setting);
            String rem = _input.nextLine();
            Scanner scan = new Scanner(rem);
            String cycles = "";
            while (scan.hasNext("(\\s)*(\\(|\\))+(\\s)")) {
                cycles += scan.next();
            }
            m.setPlugboard(new Permutation(cycles, _alphabet));
            while (_input.hasNextLine() && _input.hasNext("(?<=^|\n)\\*.*")) {
                String nextLine = _input.nextLine().replaceAll("[ \t]", "");
                printMessageLine(m.convert(nextLine));
            }

            if (_input.hasNextLine()) {
                _input.useDelimiter("[ \t*]+");
                while (_input.hasNext("((\r\n)+|(\n)+)")) {
                    String useless = _input.next();
                    useless = useless.replaceAll("\r", "");
                    for (int i = 0; i < useless.length(); i += 1) {
                        _output.print("\r\n");
                    }
                }
                _input.useDelimiter("\\s+");
            }
        }
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            String configAlpha = _config.next();
            int numRotors = _config.nextInt();
            int numPawls = _config.nextInt();
            HashMap<String, Rotor> rotorMap = new HashMap<String, Rotor>();
            while (_config.hasNext()) {
                Rotor currRotor = readRotor();
                rotorMap.put(currRotor.name(), currRotor);
            }
            _alphabet = new Alphabet();
            return new Machine(_alphabet, 2, 1, null);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        Rotor rotorResult;
        try {
            Permutation placeholderperm = new Permutation("", _alphabet);
            rotorResult = new Rotor("", placeholderperm);
            String name = _config.next();
            String type = _config.next();
            String notches = type.substring(1);
            String cycles = "";
            while (_config.hasNext("(\\s)*(\\(|\\))+(\\s)*")) {
                cycles += _config.next();
            }
            Permutation newPerm = new Permutation(cycles, _alphabet);
            char rotorType = type.charAt(0);
            if ((rotorType != 'M') | (rotorType != 'N') | (rotorType != 'R')) {
                throw new EnigmaException("Type of rotor doesn't exist");
            }
            if (rotorType == 'M') {
                if (notches.length() == 0) {
                    throw new EnigmaException("No notches specified");
                }
                rotorResult = new MovingRotor(name, newPerm, notches);
                return rotorResult;
            }
            if (rotorType == 'F') {
                if (notches.length() > 0) {
                    throw new EnigmaException
                            ("Fixed Rotors do not take notches");
                }
                rotorResult = new FixedRotor(name, newPerm);
                return rotorResult;
            }
            if (rotorType == 'R') {
                if (notches.length() > 0) {
                    throw new EnigmaException
                            ("Fixed Rotors do not take notches");
                }
                rotorResult = new Reflector(name, newPerm);
                return rotorResult;
            }
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
        return rotorResult;

    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        M.setRotors(settings);
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        for (int i = 0; i < msg.length(); i++) {
            _output.print(msg.charAt(i));
        }
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;
}
