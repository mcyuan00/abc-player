// Generated from Header.g4 by ANTLR 4.5.1

package abc.parser;
// Do not edit this .java file! Edit the .g4 file and re-run Antlr.

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class HeaderLexer extends Lexer {
  static { RuntimeMetaData.checkVersion("4.5.1", RuntimeMetaData.VERSION); }

  protected static final DFA[] _decisionToDFA;
  protected static final PredictionContextCache _sharedContextCache =
    new PredictionContextCache();
  public static final int
    T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
    T__9=10, T__10=11, T__11=12, T__12=13, OTHER=14, NOTE=15, KEYACCIDENTAL=16, 
    MINOR=17, NEWLINE=18, DIGIT=19, WHITESPACE=20;
  public static String[] modeNames = {
    "DEFAULT_MODE"
  };

  public static final String[] ruleNames = {
    "T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", 
    "T__9", "T__10", "T__11", "T__12", "OTHER", "NOTE", "KEYACCIDENTAL", 
    "MINOR", "NEWLINE", "DIGIT", "WHITESPACE"
  };

  private static final String[] _LITERAL_NAMES = {
    null, "'X:'", "'T:'", "'C:'", "'L:'", "'/'", "'Q:'", "'='", "'M:'", 
    "'C'", "'C|'", "'V:'", "'K:'", "'%'", null, null, null, "'m'"
  };
  private static final String[] _SYMBOLIC_NAMES = {
    null, null, null, null, null, null, null, null, null, null, null, null, 
    null, null, "OTHER", "NOTE", "KEYACCIDENTAL", "MINOR", "NEWLINE", "DIGIT", 
    "WHITESPACE"
  };
  public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

  /**
   * @deprecated Use {@link #VOCABULARY} instead.
   */
  @Deprecated
  public static final String[] tokenNames;
  static {
    tokenNames = new String[_SYMBOLIC_NAMES.length];
    for (int i = 0; i < tokenNames.length; i++) {
      tokenNames[i] = VOCABULARY.getLiteralName(i);
      if (tokenNames[i] == null) {
        tokenNames[i] = VOCABULARY.getSymbolicName(i);
      }

      if (tokenNames[i] == null) {
        tokenNames[i] = "<INVALID>";
      }
    }
  }

  @Override
  @Deprecated
  public String[] getTokenNames() {
    return tokenNames;
  }

  @Override

  public Vocabulary getVocabulary() {
    return VOCABULARY;
  }


      // This method makes the parser stop running if it encounters
      // invalid input and throw a RuntimeException.
      public void reportErrorsAsExceptions() {
          // To prevent any reports to standard error, add this line:
          //removeErrorListeners();
          
          addErrorListener(new BaseErrorListener() {
              public void syntaxError(Recognizer<?, ?> recognizer,
                                      Object offendingSymbol, 
                                      int line, int charPositionInLine,
                                      String msg, RecognitionException e) {
                  throw new ParseCancellationException(msg, e);
              }
          });
      }


  public HeaderLexer(CharStream input) {
    super(input);
    _interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
  }

  @Override
  public String getGrammarFileName() { return "Header.g4"; }

  @Override
  public String[] getRuleNames() { return ruleNames; }

  @Override
  public String getSerializedATN() { return _serializedATN; }

  @Override
  public String[] getModeNames() { return modeNames; }

  @Override
  public ATN getATN() { return _ATN; }

  public static final String _serializedATN =
    "\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\26e\b\1\4\2\t\2"+
      "\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
      "\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4"+
      "\22\t\22\4\23\t\23\4\24\t\24\4\25\t\25\3\2\3\2\3\2\3\3\3\3\3\3\3\4"+
      "\3\4\3\4\3\5\3\5\3\5\3\6\3\6\3\7\3\7\3\7\3\b\3\b\3\t\3\t\3\t\3\n\3"+
      "\n\3\13\3\13\3\13\3\f\3\f\3\f\3\r\3\r\3\r\3\16\3\16\3\17\5\17P\n\17"+
      "\3\20\3\20\3\21\3\21\3\22\3\22\3\23\5\23Y\n\23\3\23\3\23\3\24\3\24"+
      "\3\25\6\25`\n\25\r\25\16\25a\3\25\3\25\2\2\26\3\3\5\4\7\5\t\6\13\7"+
      "\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24"+
      "\'\25)\26\3\2\t\13\2$$))//\61\61<@J\\^^aac|\3\2CI\4\2%%dd\3\2\17\17"+
      "\3\2\f\f\3\2\62;\3\2\"\"f\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t"+
      "\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3"+
      "\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3"+
      "\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2"+
      ")\3\2\2\2\3+\3\2\2\2\5.\3\2\2\2\7\61\3\2\2\2\t\64\3\2\2\2\13\67\3"+
      "\2\2\2\r9\3\2\2\2\17<\3\2\2\2\21>\3\2\2\2\23A\3\2\2\2\25C\3\2\2\2"+
      "\27F\3\2\2\2\31I\3\2\2\2\33L\3\2\2\2\35O\3\2\2\2\37Q\3\2\2\2!S\3\2"+
      "\2\2#U\3\2\2\2%X\3\2\2\2\'\\\3\2\2\2)_\3\2\2\2+,\7Z\2\2,-\7<\2\2-"+
      "\4\3\2\2\2./\7V\2\2/\60\7<\2\2\60\6\3\2\2\2\61\62\7E\2\2\62\63\7<"+
      "\2\2\63\b\3\2\2\2\64\65\7N\2\2\65\66\7<\2\2\66\n\3\2\2\2\678\7\61"+
      "\2\28\f\3\2\2\29:\7S\2\2:;\7<\2\2;\16\3\2\2\2<=\7?\2\2=\20\3\2\2\2"+
      ">?\7O\2\2?@\7<\2\2@\22\3\2\2\2AB\7E\2\2B\24\3\2\2\2CD\7E\2\2DE\7~"+
      "\2\2E\26\3\2\2\2FG\7X\2\2GH\7<\2\2H\30\3\2\2\2IJ\7M\2\2JK\7<\2\2K"+
      "\32\3\2\2\2LM\7\'\2\2M\34\3\2\2\2NP\t\2\2\2ON\3\2\2\2P\36\3\2\2\2"+
      "QR\t\3\2\2R \3\2\2\2ST\t\4\2\2T\"\3\2\2\2UV\7o\2\2V$\3\2\2\2WY\t\5"+
      "\2\2XW\3\2\2\2XY\3\2\2\2YZ\3\2\2\2Z[\t\6\2\2[&\3\2\2\2\\]\t\7\2\2"+
      "](\3\2\2\2^`\t\b\2\2_^\3\2\2\2`a\3\2\2\2a_\3\2\2\2ab\3\2\2\2bc\3\2"+
      "\2\2cd\b\25\2\2d*\3\2\2\2\6\2OXa\3\b\2\2";
  public static final ATN _ATN =
    new ATNDeserializer().deserialize(_serializedATN.toCharArray());
  static {
    _decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
    for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
      _decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
    }
  }
}