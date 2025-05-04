package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author
 */
public class PermutationTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Permutation perm;
    private String alpha = UPPER_STRING;

    /** Check that perm has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                         e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                         c, perm.invert(e));
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }



    @Test
    public void checkIdTransform() {
        perm = new Permutation("", UPPER);
        checkPerm("identity", UPPER_STRING, UPPER_STRING);
    }

    @Test
    public void checkPerm() {
        Alphabet alpha = new Alphabet("ABCDEF");
        Permutation perm = new Permutation("(BACE) (D)", alpha);
        assertEquals('A', perm.permute('B'));
        assertEquals('C', perm.permute('A'));
        assertEquals('B', perm.permute('E'));
        assertEquals('E', perm.permute('C'));
        assertEquals('D', perm.permute('D'));
        assertEquals('F', perm.permute('F'));
        assertEquals(2, perm.permute(0));
        assertEquals(1, perm.permute(4));
        assertEquals(0, perm.permute(1));
        assertEquals(3, perm.permute(3));
        assertEquals(5, perm.permute(5));
        assertEquals(2, perm.permute(6));
        assertEquals(perm.alphabet(), alpha);
    }

    @Test
    public void checkInv() {
        Alphabet beta = new Alphabet("ABCDE");
        Permutation perm = new Permutation("(BAC) (D)", beta);
        assertEquals('C', perm.invert('B'));
        assertEquals('A', perm.invert('C'));
        assertEquals('B', perm.invert('A'));
        assertEquals('D', perm.invert('D'));
        assertEquals('E', perm.invert('E'));
        assertEquals(1, perm.invert(0));
        assertEquals(0, perm.invert(2));
        assertEquals(2, perm.invert(1));
        assertEquals(3, perm.invert(3));
        assertEquals(4, perm.invert(4));
        assertEquals(1, perm.invert(5));

    }

    @Test
    public void checkSize() {
        Alphabet alpha = new Alphabet();
        assertEquals(26, alpha.size());
        Alphabet noLetters = new Alphabet("");
        assertEquals(0, noLetters.size());
        Alphabet arbitraryLett = new Alphabet("ABCDEFGHIJK");
        assertEquals(11, arbitraryLett.size());
    }
    @Test
    public void checkDerangement() {
        Alphabet alpha = new Alphabet("ABCD");
        Permutation derPerm = new Permutation("(AB) (CD)", alpha);
        Permutation nonDerPerm = new Permutation("(AB) (C)", alpha);
        Permutation nonDerPerm2 = new Permutation("(AB) (C) (D)", alpha);
        assertTrue(derPerm.derangement());
        assertFalse(nonDerPerm.derangement());
        assertFalse(nonDerPerm2.derangement());
    }

}
