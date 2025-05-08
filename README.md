Enigma
A simulation of the Enigma machines that Germany used during World War II to encrypt its military communications

To run the program on the command line, first compile all of the files with

    javac -g -Xlint:unchecked enigma/*.java

or, if you have it, just use

    make

After compiling, you can use the command

    java -ea enigma.Main [configuration file] [input file] [output file]

to run the program. The configuration file contains descriptions of the machine and the available rotors. The data are in free format. That is, they consist of strings of non-whitespace characters separated by arbitrary whitespace (spaces, tabs, and newlines), so that indentation, spacing, and line breaks are irrelevant. Each file has the following contents:

    A string of the form C1C2⋯Cn

where the Ci
are non-blank ASCII characters other than "*", "(" and ")". This is the alphabet of the machine, giving both the character set and the ordering of the characters around a rotor. For the examples in this document, we use just the upper-case alphabet (as did the Enigma machines), but the solution must deal with general alphabets. In the following, we will generally refer to the characters in this alphabet as "letters", even when they include digits, punctuation, or other symbols.
Two integer numerals, S>P>0
, where S is the number of rotor slots (including the reflector) and P
is the number of pawls—that is, the number of rotors that move. The moving rotors and their pawls are all to the right of any non-moving ones.

Any number of rotor descriptors. Each has the following components (separated by whitespace):

    A name containing any non-blank characters other than parentheses.
    One of the characters R, N, or M, indicating that the rotor is a reflector, a non-moving rotor, or a moving rotor, respectively. Non-moving rotors can only be used in positions 2

through S−P and moving rotors in positions S−P+1 through S

        .
        Immediately after the M for a moving rotor come(s) the letter(s) at which there is a notch on the rotor's ring (no space between M and these letters).
        The cycles of the permutation, using the notation discussed above.

For example, the German Naval Enigma machine might be described with this configuration file (see Figure 2):

          ABCDEFGHIJKLMNOPQRSTUVWXYZ
          5 3
          I MQ      (AELTPHQXRU) (BKNW) (CMOY) (DFG) (IV) (JZ) (S)
          II ME     (FIXVYOMW) (CDKLHUP) (ESZ) (BJ) (GR) (NT) (A) (Q)
          III MV    (ABDHPEJT) (CFLVMZOYQIRWUKXSG) (N)
          IV MJ     (AEPLIYWCOXMRFZBSTGJQNH) (DV) (KU)
          V MZ      (AVOLDRWFIUQ)(BZKSMNHYC) (EGTJPX)
          VI MZM    (AJQDVLEOZWIYTS) (CGMNHFUX) (BPRK)
          VII MZM   (ANOUPFRIMBZTLWKSVEGCJYDHXQ)
          VIII MZM  (AFLSETWUNDHOZVICQ) (BKJ) (GXY) (MPR)
          Beta N    (ALBEVFCYODJWUGNMQTZSKPR) (HIX)
          Gamma N   (AFNIRLBSQWVXGUZDKMTPCOYJHE)
          B R       (AE) (BN) (CK) (DQ) (FU) (GY) (HW) (IJ) (LO) (MP)
                    (RX) (SZ) (TV)
          C R       (AR) (BD) (CO) (EJ) (FN) (GT) (HK) (IV) (LM) (PW)
                    (QZ) (SX) (UY)

The input file to the program will consist of a sequence of messages to decode, each preceded by a line giving the initial settings. Given the configuration file above, a settings line looks like this:

* B Beta III IV I AXLE (YF) (ZH)

The asterisk must appear in the first column. Other items on the line may be separated from each other by tabs and blanks; adjacent items that are rotor names or consist entirely of letters from the alphabet must be so separated. This particular example means that the rotors used are reflector B, and rotors Beta, III, IV, and I, with rotor I in the rightmost, or fast, slot. The remaining parenthesized items indicate that the letter pair Y and F and the pair Z and M are steckered (swapped going in from the keyboard and going out to the lights).

In general for this particular configuration, rotor 1 is always the reflector; rotor 2 is Beta or Gamma, and each of 3-5 is one of rotors I-VIII. A rotor may not be repeated. The four letters of the following word (AXLE in the example) give the initial positions of rotors 2-5, respectively (i.e., not including the reflector). Any number of steckered pairs may follow (including none).

After each settings line comes a message on any number of lines. Each line of a message consists only of letters (here meaning characters from the specified alphabet), spaces, and tabs (0 or more). The program should ignore the blanks and tabs. The end of message is indicated either by the end of the input or by a new configuration line (distinguished by its leading asterisk). The machine is not reset between lines, but continues stepping from where it left off on the previous message line. Because the Enigma is a reciprocal cipher, a given translation may either be a decryption or encryption; you don't have to worry about which, since the process is the same in any case.

Output the translation for each message line in groups of five characters, separated by a space (the last group may have fewer characters, depending on the message length). Figure 3 contains an example that shows an encryption followed by a decryption of the encrypted message. Since we have yet to cover the details of File I/O, you will be provided the File IO machinery for this project.

             Input                              |         Output
* B Beta III IV I AXLE (HQ) (EX) (IP) (TR) (BY) | QVPQS OKOIL PUBKJ ZPISF XDW
FROM HIS SHOULDER HIAWATHA                      | BHCNS CXNUO AATZX SRCFY DGU
TOOK THE CAMERA OF ROSEWOOD                     | FLPNX GXIXT YJUJR CAUGE UNCFM KUF
MADE OF SLIDING FOLDING ROSEWOOD                | WJFGK CIIRG XODJG VCGPQ OH
NEATLY PUT IT ALL TOGETHER                      | ALWEB UHTZM OXIIV XUEFP RPR
IN ITS CASE IT LAY COMPACTLY                    | KCGVP FPYKI KITLB URVGT SFU
FOLDED INTO NEARLY NOTHING                      | SMBNK FRIIM PDOFJ VTTUG RZM
BUT HE OPENED OUT THE HINGES                    | UVCYL FDZPG IBXRE WXUEB ZQJO
PUSHED AND PULLED THE JOINTS                    | YMHIP GRRE
   AND HINGES                                   | GOHET UXDTW LCMMW AVNVJ VH
TILL IT LOOKED ALL SQUARES                      | OUFAN TQACK
   AND OBLONGS                                  | KTOZZ RDABQ NNVPO IEFQA FS
LIKE A COMPLICATED FIGURE                       | VVICV UDUER EYNPF FMNBJ VGQ
IN THE SECOND BOOK OF EUCLID                    |
                                                | FROMH ISSHO ULDER HIAWA THA
* B Beta III IV I AXLE (HQ) (EX) (IP) (TR) (BY) | TOOKT HECAM ERAOF ROSEW OOD
QVPQS OKOIL PUBKJ ZPISF XDW                     | MADEO FSLID INGFO LDING ROSEW OOD
BHCNS CXNUO AATZX SRCFY DGU                     | NEATL YPUTI TALLT OGETH ER
FLPNX GXIXT YJUJR CAUGE UNCFM KUF               | INITS CASEI TLAYC OMPAC TLY
WJFGK CIIRG XODJG VCGPQ OH                      | FOLDE DINTO NEARL YNOTH ING
ALWEB UHTZM OXIIV XUEFP RPR                     | BUTHE OPENE DOUTT HEHIN GES
KCGVP FPYKI KITLB URVGT SFU                     | PUSHE DANDP ULLED THEJO INTS
SMBNK FRIIM PDOFJ VTTUG RZM                     | ANDHI NGES
UVCYL FDZPG IBXRE WXUEB ZQJO                    | TILLI TLOOK EDALL SQUAR ES
YMHIP GRRE                                      | ANDOB LONGS
GOHET UXDTW LCMMW AVNVJ VH                      | LIKEA COMPL ICATE DFIGU RE
OUFAN TQACK                                     | INTHE SECON DBOOK OFEUC LID
KTOZZ RDABQ NNVPO IEFQA FS                      |
VVICV UDUER EYNPF FMNBJ VGQ                     |

Figure 3. Sample input and output (using the Naval cipher).
Editing Main

Within Main.java, you will be working with Scanners. We recommend that before you tackle this portion of the project you do homework 4, as there will substantial practice on that assignment. Inside of Main.java you will deal with a substantial amount of input handling (involving both valid and invalid inputs). Do not assume that all of the inputs will be well-formed and do the best to make sure that you handle all cases where the input is not what is expected.

In addition to this, in Main.java, you will need to print output in a way that matches our sample output file. The Unix convention is that every line ends with a newline (we say that newline is a line terminator rather than a line separator), so make sure that the output file also includes a newline at the end. If the output seems exactly the same as the grader's, but you are failing a test, chances are that there is a newline error.
Handling Errors

You can see a number of opportunities for input errors:

    The configuration file may have the wrong format.
    The input might not start with a setting.
    The setting line can contain the wrong number of arguments.
    The rotors might be misnamed.
    A rotor might be repeated in the setting line.
    The first rotor might not be a reflector.
    The initial positions string might be the wrong length or contain characters not in the alphabet.
    Messages containing characters from outside the alphabet.

A significant amount of a program will typically be devoted to detecting such errors, and taking corrective action. In our case, the main program is set up in such a way that the only corrective action needed is to throw an EnigmaException with an explanatory message. The existing code in the main program will catch this exception, print its message, and exit with a standard Unix error indication. The skeleton supplies a simple static utility method, error, which provides a convenient way to create error exceptions. There are examples of its use in the skeleton.
