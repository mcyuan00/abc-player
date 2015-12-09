package abc.player;


import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.print.PrintException;
import javax.swing.JDialog;

import org.antlr.v4.gui.Trees;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.Utils;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Test;

import abc.parser.HeaderLexer;
import abc.parser.HeaderParser;

public class ParseTest {

    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }



    /* HeaderParser
     * Testing partitions:
     *   Basics:  
     *       - index, title, key in order 
     *       - index/title/key out of order -- expect error
     *       - missing index/title/key -- expect error   
     *   Index
     *       - valid index, must be positive
     *       - invalid index (missing X: or \n or negative)         
     *   Title
     *      - valid title
     *      - invalid title (missing T: or \n)      
     *   Composer:
     *       - valid composer
     *       - invalid composer (missing C: or \n)          
     *   Meter:
     *       - digit/digit
     *       - C
     *       - C|
     *       - invalid meter (missing M: or \n, too many fractions)     
     *   Length:
     *      - valid length
     *      - invalid length (missing L: or \n, too many fractions)
     *   Tempo:
     *      - valid tempo
     *      - invalid tempo (missing Q: or \n, containing non-numerical chars)    
     *   Voice:
     *      - valid voice
     *      - valid voices (multiple)
     *      - invalid voice (missing V: or \n)     
     *   Key:
     *      - valid key
     *      - key note is lowercase -- expect error
     *      - invalid key note
     *      - missing K: or \n 
     *   Comment (% followed by any text, not treated as a field)
     *   Whitespace (all whitespace should be ignored/skipped)
     */

    //    @Test
    public void testHeaderParser(){
        CharStream stream = new ANTLRInputStream("X:1\nT:hello world\nK:A\n");
        HeaderLexer lexer = new HeaderLexer(stream);
        TokenStream tokens = new CommonTokenStream(lexer);

        HeaderParser parser = new HeaderParser(tokens);
        ParseTree tree = parser.root();

        //        For debugging:
        //        Future<JDialog> inspect = Trees.inspect(tree, parser);
        //        
        //        Option 1:
        //        try {
        //            Thread.sleep(10000);
        //        } catch (InterruptedException e) {
        //            e.printStackTrace();
        //        }
        //        
        //        Option 2:
        //        try {
        //            Utils.waitForClose(inspect.get());
        //        } catch (Exception e) {
        //            e.printStackTrace();
        //        }
    }

    // header with basic required fields in order: index, title, key
    // valid index, valid title, valid key
    @Test
    public void testHeaderRequiredFields(){
        Header header = Parser.parseHeader("X:1\nT:hello world\nK:A\n");
        assertEquals(1, header.index());
        assertEquals("helloworld", header.title());
        assertEquals(KeySignature.valueOf("A_MAJOR"), header.keySignature());
    }

    // header with basic required fields not in order: title, key, index
    @Test (expected=Exception.class)
    public void testHeaderIndexNotFirst(){
        Header header = Parser.parseHeader("T:hello world\nK:A\nX:1\n");
    }

    // header with basic required field missing: title, key
    @Test (expected=Exception.class)
    public void testHeaderIndexMissing(){
        Header header = Parser.parseHeader("T:hello world\nK:A\n");
    }

    // header with basic required field missing: index, key
    @Test (expected=Exception.class)
    public void testHeaderTitleMissing(){
        Header header = Parser.parseHeader("X:1\nK:A\n");
    }

    // invalid index (missing X:)
    @Test (expected=Exception.class)
    public void testInvalidIndexMissingX(){
        Header header = Parser.parseHeader("1\nT:hello world\nK:A\n");
        assertEquals(1, header.index());
    }

    // invalid index (missing \n)
    @Test (expected=Exception.class)
    public void testInvalidIndexMissingNewline(){
        Header header = Parser.parseHeader("X:1T:hello world\nK:A\n");
        assertEquals(1, header.index());
        assertEquals("helloworld", header.title());
    }

    // invalid index (negative)
    @Test (expected=Exception.class)
    public void testInvalidIndexNegative(){
        Header header = Parser.parseHeader("X:-1\nT:hello world\nK:A\n");
        assertEquals(1, header.index());
        //treats - as extraneous input
    }

    // invalid title (missing T:)
    @Test (expected=Exception.class)
    public void testInvalidTitleMissingT(){
        Header header = Parser.parseHeader("X:1\nhello world\nK:A\n");
        assertEquals("helloworld", header.title());
    }

    // invalid title (missing \n)
    @Test (expected=Exception.class)
    public void testInvalidTitleMissingNewline(){
        Header header = Parser.parseHeader("X:1\nT:hello worldK:A\n");
        assertEquals("helloworld", header.title());
    }

    // valid composer
    @Test
    public void testValidComposer(){
        Header header = Parser.parseHeader("X:1\nT:hello world\nC:1.pOo2\nK:A\n");
        assertEquals("1.pOo2", header.composer());
    }

    // invalid composer (missing C:)
    @Test (expected=Exception.class)
    public void testInvalidComposerMissingC(){
        Header header = Parser.parseHeader("X:1\nT:hello world\nunknown\nK:A\n");
        assertEquals("unknown", header.composer());
    }

    // invalid composer (missing \n)
    @Test (expected=Exception.class)
    public void testInvalidComposerMissingNewline(){
        Header header = Parser.parseHeader("X:1\nT:hello world\nC:unknownK:A\n");
        assertEquals("unknown", header.composer());
    }

    // valid meter (digit/digit)
    @Test
    public void testValidMeterFraction(){
        Header header = Parser.parseHeader("X:1\nT:hello world\nM:6/8\nK:A\n");
        assertEquals(new Fraction(6,8), header.meter());
    }

    // valid meter (common time)
    @Test
    public void testValidMeterCommon(){
        Header header = Parser.parseHeader("X:1\nT:hello world\nM:C\nK:A\n");
        assertEquals(new Fraction(4,4), header.meter());
        //illegal argument exception, test should not be expecting this error
    }

    // valid meter (cut time)
    @Test
    public void testValidMeterCut(){
        Header header = Parser.parseHeader("X:1\nT:hello world\nM:C|\nK:A\n");
        assertEquals(new Fraction(2,2), header.meter());
        //illegal argument exception, test should not be expecting this error
    }

    // invalid meter (missing M:)
    @Test (expected=Exception.class)
    public void testInvalidMeterMissingM(){
        Header header = Parser.parseHeader("X:1\nT:hello world\n6/8\nK:A\n");
        assertEquals(new Fraction(6,8), header.meter());
    }

    // invalid meter (missing \n)
    @Test (expected=Exception.class)
    public void testInvalidMeterMissingNewline(){
        Header header = Parser.parseHeader("X:1\nT:hello world\nM:6/8K:A\n");
        assertEquals(new Fraction(6,8), header.meter());
    }

    // invalid meter (too many fractions)
    @Test (expected = Exception.class)
    public void testInvalidMeterMultipleFractions(){
        Header header = Parser.parseHeader("X:1\nT:hello world\nM:6/8/2\nK:A\n");
        assertEquals(new Fraction(6,8), header.meter());
    }

    // valid length
    @Test
    public void testValidLength(){
        Header header = Parser.parseHeader("X:1\nT:hello world\nL:1/4\nK:A\n");
        assertEquals(new Fraction(1,4), header.noteLength());
        //returning 1/8 as length instead of 1/4
    }

    // invalid length (missing L:)
    @Test (expected=Exception.class)
    public void testInvalidLengthMissingL(){
        Header header = Parser.parseHeader("X:1\nT:hello world\n1/4\nK:A\n");
        assertEquals(new Fraction(1,4), header.noteLength());
    }

    // invalid length (missing \n)
    @Test (expected=Exception.class)
    public void testInvalidLengthMissingNewline(){
        Header header = Parser.parseHeader("X:1\nT:hello world\nL:1/4K:A\n");
    }

    // invalid length (too many fractions)
    @Test (expected=Exception.class)
    public void testInvalidLengthMultipleFractions(){
        Header header = Parser.parseHeader("X:1\nT:hello world\nL:1/4/3\nK:A\n");
        assertEquals(new Fraction(1,4), header.noteLength());
        //also returns 1/8 instead of 1/4
    }

    // valid tempo
    @Test
    public void testValidTempo(){
        Header header = Parser.parseHeader("X:1\nT:hello world\nQ:1/8=100\nK:A\n");
        assertEquals(100, header.tempo());
        //illegal argument exception,  test should not be expecting this error
    }

    // invalid tempo (missing Q:)
    @Test (expected=Exception.class)
    public void testInvalidTempoMissingQ(){
        Header header = Parser.parseHeader("X:1\nT:hello world\n1/8=100\nK:A\n");
        assertEquals(100, header.tempo());
    }

    // invalid tempo (missing \n)
    @Test (expected=Exception.class)
    public void testInvalidTempoMissingNewline(){
        Header header = Parser.parseHeader("X:1\nT:hello world\nQ:1/8=100K:A\n");
        assertEquals(100, header.tempo());
    }

    // invalid tempo (non-numerical characters)
    @Test (expected=Exception.class)
    public void testInvalidTempoNonNumerical(){
        Header header = Parser.parseHeader("X:1\nT:hello world\nQ:a=100K:A\n");
        assertEquals(100, header.tempo());
    }

    // valid voice
    @Test
    public void testValidVoice(){
        Header header = Parser.parseHeader("X:1\nT:hello world\nV:21vd.\nK:A\n");
        assertEquals(("21vd."), header.voices().get(0));
    }

    // valid voices (multiple)
    @Test
    public void testValidVoiceMultiple(){
        Header header = Parser.parseHeader("X:1\nT:hello world\nV:voice1\nV:voice2\nK:A\n");
        assertTrue(header.voices().contains("voice1"));
        assertTrue(header.voices().contains("voice2"));
    }

    // invalid voice (missing V:)
    @Test (expected=Exception.class)
    public void testInvalidVoiceMissingV(){
        Header header = Parser.parseHeader("X:1\nT:hello world\nvoice1\nK:A\n");
        assertTrue(header.voices().contains("voice1"));
    }

    // invalid voice (missing \n)
    @Test (expected=Exception.class)
    public void testInvalidVoiceMissingNewline(){
        Header header = Parser.parseHeader("X:1\nT:hello world\nV:voice1K:A\n");
        assertEquals("voice1", header.voices());
    }

    // valid key (minor)
    @Test
    public void testValidKeyMinor(){
        Header header = Parser.parseHeader("X:1\nT:hello world\nK:Am\n");
        assertEquals(KeySignature.valueOf("A_MINOR"), header.keySignature());
    }

    // valid key (sharp)
    @Test
    public void testValidKeySharp(){
        Header header = Parser.parseHeader("X:1\nT:hello world\nK:A#m\n");
        assertEquals(KeySignature.valueOf("A_SHARP_MINOR"), header.keySignature());
        //illegal argument exception, test should not be expecting this error!
    }

    // valid key (flat)
    @Test
    public void testValidKeyFlat(){
        Header header = Parser.parseHeader("X:1\nT:hello world\nK:Ab\n");
        assertEquals(KeySignature.valueOf("A_FLAT_MAJOR"), header.keySignature());
    }

    // invalid key (note letter is lowercase)
    @Test (expected=Exception.class)
    public void testInvalidKeyLowercase(){
        Header header = Parser.parseHeader("X:1\nT:hello world\nK:a\n");
        assertEquals(KeySignature.valueOf("A_MAJOR"), header.keySignature());
    }

    // invalid key (missing K:)
    @Test (expected=Exception.class)
    public void testInvalidKeyMissingK(){
        Header header = Parser.parseHeader("X:1\nT:hello world\nA\n");
        assertEquals(KeySignature.valueOf("A_MAJOR"), header.keySignature());
    }

    // invalid key (missing \n)
    @Test (expected=Exception.class)
    public void testInvalidKeyMissingNewline(){
        Header header = Parser.parseHeader("X:1\nT:hello world\nK:A");
        assertEquals(KeySignature.valueOf("A_MAJOR"), header.keySignature());
    }

    // comment
    @Test
    public void testComment(){
        Header header = Parser.parseHeader("X:1%comment\nT:hello world\nK:A\n");
        assertEquals(1, header.index());
        assertEquals("helloworld", header.title());
        assertEquals(KeySignature.valueOf("A_MAJOR"), header.keySignature());
    }

    // whitespace
    @Test
    public void testWhitespace(){
        Header header = Parser.parseHeader("X:1  \nT: hel lo wor ld\nK: A \n");
        assertEquals(1, header.index());
        assertEquals("helloworld", header.title());
        assertEquals(KeySignature.valueOf("A_MAJOR"), header.keySignature());
    }
    
    // symbols
    @Test
    public void testSymbols(){
        Header header = Parser.parseHeader("X:1\nT:.~`!@$^&*()-_+{[}]|\\;:\'\"<>,?'&\nK:A\n");
        assertEquals(".~`!@$^&*()-_+{[}]|\\;:'\"<>,?'&", header.title());
    }


    /***
     * TODO: FIGURE OUT HOW TO DEAL WITH ACCIDENTALS IN MEASURES
     */

    //test body parse
    /*
     * for duration n/m --> absent numerator assumed to be 1, absent denominator assumed to be 2
     * Note:
     *      - note letter --> lowercase, uppercase
     *      - , or ' (octave change) --> 0, 1, >1
     *      - accidental --> __, _, ^, ^^
     *      - duration --> default length, n, n/m, /m, n/, /
     * Rest:
     *      - duration --> default length, n, n/m, /m, n/, /
     * Chord:
     *      - number of notes --> 1, 2, >2
     *      - length of notes --> 1, >1
     *      - different lengths --> no, yes within chord, yes with additional notes
     *      - (yes within chord = [C2E4], yes with additional notes = [C2E4]G2)
     * Tuplet:
     *      - duplet --> notes only, chords only, notes and chords
     *      - triplet --> notes only, chords only, notes and chords
     * Normal measure:
     *      - one element (note or rest)
     *      - multiple elements (notes and rests)
     * Start repeat measure:
     *      - start repeat --> |: or :
     *      - (only : if start repeat is preceded by another measure bc measures end in |)
     *      - one element --> note or rest
     *      - multiple elements --> notes and rests
     * End repeat measure:
     *      - one element --> note or rest
     *      - multiple elements --> notes and rests
     * Doublebar measure:
     *      - doublebar --> || or [| or |]
     *      - one element --> note or rest
     *      - multiple elements --> notes and rests
     * First ending measure:
     *      - first ending length --> one measure, multiple measures
     * Second ending measure:
     *      - second ending length --> one measure, multiple measures
     */

    // covers normal measure (one element), note (uppercase note letter), note (default length duration)
    @Test
    public void testUppercaseNoteDefaultLength(){
        String test = "A|";
        Measure measure = (Measure) Parser.parseMusic(test, new Fraction(1,4), KeySignature.valueOf("C_MAJOR"));
        assertEquals(new Fraction(1,4), measure.duration());
        List<Music> expected = new ArrayList<>();
        Note note = new Note(new Fraction(1,4), 'A', 0);
        expected.add(note);
        assertEquals(expected, measure.getElements());
    }
    
    // covers normal measure (one element), note (lowercase note letter), note (length n), note (one octave above)
    @Test
    public void testLowercaseNoteLengthN(){
        String test = "a2|";
        Measure measure = (Measure) Parser.parseMusic(test, new Fraction(1,4), KeySignature.valueOf("C_MAJOR"));
        assertEquals(new Fraction(1,2), measure.duration());
        List<Music> expected = new ArrayList<>();
        Note note = new Note(new Fraction(1,2), 'A', 1);
        expected.add(note);
        assertEquals(expected, measure.getElements());
    }
    
    // covers normal measure (one element), note (lowercase note letter), note (length n/m)
    @Test
    public void testLowercaseNoteLengthNSlashM(){
        String test = "a3/2|";
        Measure measure = (Measure) Parser.parseMusic(test, new Fraction(1,4), KeySignature.valueOf("C_MAJOR"));
        assertEquals(new Fraction(3,8), measure.duration());
    }
    
    // covers normal measure (one element), note (lowercase note letter), note (length n/)
    @Test
    public void testLowercaseNoteLengthNSlash(){
        String test = "a3/|";
        Measure measure = (Measure) Parser.parseMusic(test, new Fraction(1,4), KeySignature.valueOf("C_MAJOR"));
        assertEquals(new Fraction(3,8), measure.duration());
    }
    
    // covers normal measure (one element), note (lowercase note letter), note (length /m)
    @Test
    public void testLowercaseNoteLengthSlashM(){
        String test = "a/3|";
        Measure measure = (Measure) Parser.parseMusic(test, new Fraction(1,4), KeySignature.valueOf("C_MAJOR"));
        assertEquals(new Fraction(1,12), measure.duration());
    }
    
    // covers normal measure (one element), note (lowercase note letter), note (length /)
    @Test
    public void testLowercaseNoteLengthSlash(){
        String test = "a/|";
        Measure measure = (Measure) Parser.parseMusic(test, new Fraction(1,4), KeySignature.valueOf("C_MAJOR"));
        assertEquals(new Fraction(1,8), measure.duration());
    }
    
    // covers normal measure (one element), note (lowercase note letter), note (sharp accidental)
    @Test
    public void testLowercaseNoteLengthNSharp(){
        String test = "^a|";
        Measure measure = (Measure) Parser.parseMusic(test, new Fraction(1,4), KeySignature.valueOf("C_MAJOR"));
        assertEquals(new Fraction(1,4), measure.duration());
        List<Music> expected = new ArrayList<>();
        Note note = new Note(new Fraction(1,4), 'A', 1, 1);
        expected.add(note);
        assertEquals(expected, measure.getElements());
    }
    
    // covers normal measure (one element), note (lowercase note letter), note (double sharp accidental)
    @Test
    public void testLowercaseNoteDoubleSharp(){
        String test = "^^a|";
        Measure measure = (Measure) Parser.parseMusic(test, new Fraction(1,4), KeySignature.valueOf("C_MAJOR"));
        assertEquals(new Fraction(1,4), measure.duration());
        List<Music> expected = new ArrayList<>();
        Note note = new Note(new Fraction(1,4), 'A', 1, 2);
        expected.add(note);
        assertEquals(expected, measure.getElements());
    }
    
    // covers normal measure (one element), note (lowercase note letter), note (natural)
    @Test
    public void testLowercaseNoteNatural(){
        String test = "=f|";
        Measure measure = (Measure) Parser.parseMusic(test, new Fraction(1,4), KeySignature.valueOf("G_MAJOR"));
        assertEquals(new Fraction(1,4), measure.duration());
        List<Music> expected = new ArrayList<>();
        Note note = new Note(new Fraction(1,4), 'F', 1, 0);
        expected.add(note);
        assertEquals(expected, measure.getElements());
    }
    
    // covers normal measure (multiple elements), note (lowercase note letter), note (natural)
    @Test
    public void testLowercaseNoteSharpThenNatural(){
        String test = "^a =a|";
        Measure measure = (Measure) Parser.parseMusic(test, new Fraction(1,4), KeySignature.valueOf("C_MAJOR"));
        assertEquals(new Fraction(1,2), measure.duration());
        List<Music> expected = new ArrayList<>();
        Note note1 = new Note(new Fraction(1,4), 'A', 1, 1);
        Note note2 = new Note(new Fraction(1,4), 'A', 1, 0);
        expected.add(note1);
        expected.add(note2);
        assertEquals(expected, measure.getElements());
    }
    
    // covers normal measure (one element), note (lowercase note letter), note (flat accidental)
    @Test
    public void testLowercaseNoteFlat(){
        String test = "_a|";
        Measure measure = (Measure) Parser.parseMusic(test, new Fraction(1,4), KeySignature.valueOf("C_MAJOR"));
        assertEquals(new Fraction(1,4), measure.duration());
        List<Music> expected = new ArrayList<>();
        Note note = new Note(new Fraction(1,4), 'A', 1, -1);
        expected.add(note);
        assertEquals(expected, measure.getElements());
    }
    
    // covers normal measure (one element), note (lowercase note letter), note (double flat accidental)
    @Test
    public void testLowercaseNoteDoubleFlat(){
        String test = "__a|";
        Measure measure = (Measure) Parser.parseMusic(test, new Fraction(1,4), KeySignature.valueOf("C_MAJOR"));
        assertEquals(new Fraction(1,4), measure.duration());
        List<Music> expected = new ArrayList<>();
        Note note = new Note(new Fraction(1,4), 'A', 1, -2);
        expected.add(note);
        assertEquals(expected, measure.getElements());
    }
    
    // covers normal measure (one element), note (lowercase note letter), note (two octaves above)
    @Test
    public void testLowercaseNoteOneApostrophe(){
        String test = "a'|";
        Measure measure = (Measure) Parser.parseMusic(test, new Fraction(1,4), KeySignature.valueOf("C_MAJOR"));
        assertEquals(new Fraction(1,4), measure.duration());
        List<Music> expected = new ArrayList<>();
        Note note = new Note(new Fraction(1,4), 'A', 2);
        expected.add(note);
        assertEquals(expected, measure.getElements());
    }
    
    // covers normal measure (one element), note (lowercase note letter), note (three octaves above)
    @Test
    public void testLowercaseNoteTwoApostrophes(){
        String test = "a''|";
        Measure measure = (Measure) Parser.parseMusic(test, new Fraction(1,4), KeySignature.valueOf("C_MAJOR"));
        assertEquals(new Fraction(1,4), measure.duration());
        List<Music> expected = new ArrayList<>();
        Note note = new Note(new Fraction(1,4), 'A', 3);
        expected.add(note);
        assertEquals(expected, measure.getElements());
    }
    
    // covers normal measure (one element), note (uppercase note letter), note (one octave below)
    @Test
    public void testUppercaseNoteOneComma(){
        String test = "A,|";
        Measure measure = (Measure) Parser.parseMusic(test, new Fraction(1,4), KeySignature.valueOf("C_MAJOR"));
        assertEquals(new Fraction(1,4), measure.duration());
        List<Music> expected = new ArrayList<>();
        Note note = new Note(new Fraction(1,4), 'A', -1);
        expected.add(note);
        assertEquals(expected, measure.getElements());
    }
    
    // covers normal measure (one element), note (uppercase note letter), note (two octaves below)
    @Test
    public void testUppercaseNoteTwoCommas(){
        String test = "A,,|";
        Measure measure = (Measure) Parser.parseMusic(test, new Fraction(1,4), KeySignature.valueOf("C_MAJOR"));
        assertEquals(new Fraction(1,4), measure.duration());
        List<Music> expected = new ArrayList<>();
        Note note = new Note(new Fraction(1,4), 'A', -2);
        expected.add(note);
        assertEquals(expected, measure.getElements());
    }
    
    // covers normal measure (one element), note (lowercase note letter), note (octaves cancel)
    @Test
    public void testLowercaseNoteOneComma(){
        String test = "a,|";
        Measure measure = (Measure) Parser.parseMusic(test, new Fraction(1,4), KeySignature.valueOf("C_MAJOR"));
        assertEquals(new Fraction(1,4), measure.duration());
        List<Music> expected = new ArrayList<>();
        Note note = new Note(new Fraction(1,4), 'A', 0);
        expected.add(note);
        assertEquals(expected, measure.getElements());
    }
    
    // covers normal measure (one element), rest (default length)
    @Test
    public void testRestDefaultLength(){
        String test = "z|";
        Music measure = Parser.parseMusic(test, new Fraction(1,4), KeySignature.valueOf("C_MAJOR"));
        assertEquals(new Fraction(1,4), measure.duration());
    }
    
    // covers normal measure (one element), rest (length n)
    @Test
    public void testRestLengthN(){
        String test = "z2|";
        Music measure = Parser.parseMusic(test, new Fraction(1,4), KeySignature.valueOf("C_MAJOR"));
        assertEquals(new Fraction(1,2), measure.duration());
    }
    
    // covers normal measure (one element), rest (length n/m)
    @Test
    public void testRestLengthNSlashM(){
        String test = "z3/2|";
        Measure measure = (Measure) Parser.parseMusic(test, new Fraction(1,4), KeySignature.valueOf("C_MAJOR"));
        assertEquals(new Fraction(3,8), measure.duration());
    }
    
    // covers normal measure (one element), rest (length n/)
    @Test
    public void testRestLengthNSlash(){
        String test = "z3/|";
        Measure measure = (Measure) Parser.parseMusic(test, new Fraction(1,4), KeySignature.valueOf("C_MAJOR"));
        assertEquals(new Fraction(3,8), measure.duration());
    }
    
    // covers normal measure (one element), rest (length /m)
    @Test
    public void testRestLengthSlashM(){
        String test = "z/3|";
        Measure measure = (Measure) Parser.parseMusic(test, new Fraction(1,4), KeySignature.valueOf("C_MAJOR"));
        assertEquals(new Fraction(1,12), measure.duration());
    }
    
    // covers normal measure (one element), rest (length /)
    @Test
    public void testRestLengthSlash(){
        String test = "z/|";
        Measure measure = (Measure) Parser.parseMusic(test, new Fraction(1,4), KeySignature.valueOf("C_MAJOR"));
        assertEquals(new Fraction(1,8), measure.duration());
    }
    
    @Test
    public void testChordSingleNote(){
        String test = "[A]|";    
        Measure measure = (Measure) Parser.parseMusic(test, new Fraction(1,4), KeySignature.valueOf("C_MAJOR"));
        assertEquals(new Fraction(1,4), measure.duration());
        List<Music> expected = new ArrayList<>();
        List<Music> chordNotes = new ArrayList<>();
        Note note = new Note(new Fraction(1,4), 'A', 0);
        chordNotes.add(note);
        Chord chord = new Chord(chordNotes);
        expected.add(chord);
        assertEquals(expected, measure.getElements());
    }
}
