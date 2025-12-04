package io.github.shawn.antlr;// Generated from LabelRule.g4 by ANTLR 4.13.1
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class LabelRuleLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, AND=5, OR=6, IN=7, GT=8, GTE=9, LT=10, 
		LTE=11, EQ=12, NEQ=13, ID=14, INT=15, FLOAT=16, STRING=17, WS=18;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "T__2", "T__3", "AND", "OR", "IN", "GT", "GTE", "LT", 
			"LTE", "EQ", "NEQ", "ID", "INT", "FLOAT", "STRING", "WS"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'('", "')'", "'.'", "','", null, null, null, "'>'", "'>='", "'<'", 
			"'<='", null, "'!='"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, "AND", "OR", "IN", "GT", "GTE", "LT", "LTE", 
			"EQ", "NEQ", "ID", "INT", "FLOAT", "STRING", "WS"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
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


	public LabelRuleLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "LabelRule.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\u0004\u0000\u0012~\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002\u0001"+
		"\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004"+
		"\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007"+
		"\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b"+
		"\u0007\u000b\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002"+
		"\u000f\u0007\u000f\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0001"+
		"\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0001"+
		"\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001"+
		"\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0003\u00046\b\u0004\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0003"+
		"\u0005>\b\u0005\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0003"+
		"\u0006D\b\u0006\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001\b\u0001"+
		"\t\u0001\t\u0001\n\u0001\n\u0001\n\u0001\u000b\u0001\u000b\u0001\u000b"+
		"\u0003\u000bS\b\u000b\u0001\f\u0001\f\u0001\f\u0001\r\u0001\r\u0005\r"+
		"Z\b\r\n\r\f\r]\t\r\u0001\u000e\u0004\u000e`\b\u000e\u000b\u000e\f\u000e"+
		"a\u0001\u000f\u0004\u000fe\b\u000f\u000b\u000f\f\u000ff\u0001\u000f\u0001"+
		"\u000f\u0004\u000fk\b\u000f\u000b\u000f\f\u000fl\u0001\u0010\u0001\u0010"+
		"\u0005\u0010q\b\u0010\n\u0010\f\u0010t\t\u0010\u0001\u0010\u0001\u0010"+
		"\u0001\u0011\u0004\u0011y\b\u0011\u000b\u0011\f\u0011z\u0001\u0011\u0001"+
		"\u0011\u0001r\u0000\u0012\u0001\u0001\u0003\u0002\u0005\u0003\u0007\u0004"+
		"\t\u0005\u000b\u0006\r\u0007\u000f\b\u0011\t\u0013\n\u0015\u000b\u0017"+
		"\f\u0019\r\u001b\u000e\u001d\u000f\u001f\u0010!\u0011#\u0012\u0001\u0000"+
		"\u0004\u0003\u0000AZ__az\u0004\u000009AZ__az\u0001\u000009\u0003\u0000"+
		"\t\n\r\r  \u0089\u0000\u0001\u0001\u0000\u0000\u0000\u0000\u0003\u0001"+
		"\u0000\u0000\u0000\u0000\u0005\u0001\u0000\u0000\u0000\u0000\u0007\u0001"+
		"\u0000\u0000\u0000\u0000\t\u0001\u0000\u0000\u0000\u0000\u000b\u0001\u0000"+
		"\u0000\u0000\u0000\r\u0001\u0000\u0000\u0000\u0000\u000f\u0001\u0000\u0000"+
		"\u0000\u0000\u0011\u0001\u0000\u0000\u0000\u0000\u0013\u0001\u0000\u0000"+
		"\u0000\u0000\u0015\u0001\u0000\u0000\u0000\u0000\u0017\u0001\u0000\u0000"+
		"\u0000\u0000\u0019\u0001\u0000\u0000\u0000\u0000\u001b\u0001\u0000\u0000"+
		"\u0000\u0000\u001d\u0001\u0000\u0000\u0000\u0000\u001f\u0001\u0000\u0000"+
		"\u0000\u0000!\u0001\u0000\u0000\u0000\u0000#\u0001\u0000\u0000\u0000\u0001"+
		"%\u0001\u0000\u0000\u0000\u0003\'\u0001\u0000\u0000\u0000\u0005)\u0001"+
		"\u0000\u0000\u0000\u0007+\u0001\u0000\u0000\u0000\t5\u0001\u0000\u0000"+
		"\u0000\u000b=\u0001\u0000\u0000\u0000\rC\u0001\u0000\u0000\u0000\u000f"+
		"E\u0001\u0000\u0000\u0000\u0011G\u0001\u0000\u0000\u0000\u0013J\u0001"+
		"\u0000\u0000\u0000\u0015L\u0001\u0000\u0000\u0000\u0017R\u0001\u0000\u0000"+
		"\u0000\u0019T\u0001\u0000\u0000\u0000\u001bW\u0001\u0000\u0000\u0000\u001d"+
		"_\u0001\u0000\u0000\u0000\u001fd\u0001\u0000\u0000\u0000!n\u0001\u0000"+
		"\u0000\u0000#x\u0001\u0000\u0000\u0000%&\u0005(\u0000\u0000&\u0002\u0001"+
		"\u0000\u0000\u0000\'(\u0005)\u0000\u0000(\u0004\u0001\u0000\u0000\u0000"+
		")*\u0005.\u0000\u0000*\u0006\u0001\u0000\u0000\u0000+,\u0005,\u0000\u0000"+
		",\b\u0001\u0000\u0000\u0000-.\u0005A\u0000\u0000./\u0005N\u0000\u0000"+
		"/6\u0005D\u0000\u000001\u0005a\u0000\u000012\u0005n\u0000\u000026\u0005"+
		"d\u0000\u000034\u0005&\u0000\u000046\u0005&\u0000\u00005-\u0001\u0000"+
		"\u0000\u000050\u0001\u0000\u0000\u000053\u0001\u0000\u0000\u00006\n\u0001"+
		"\u0000\u0000\u000078\u0005O\u0000\u00008>\u0005R\u0000\u00009:\u0005o"+
		"\u0000\u0000:>\u0005r\u0000\u0000;<\u0005|\u0000\u0000<>\u0005|\u0000"+
		"\u0000=7\u0001\u0000\u0000\u0000=9\u0001\u0000\u0000\u0000=;\u0001\u0000"+
		"\u0000\u0000>\f\u0001\u0000\u0000\u0000?@\u0005I\u0000\u0000@D\u0005N"+
		"\u0000\u0000AB\u0005i\u0000\u0000BD\u0005n\u0000\u0000C?\u0001\u0000\u0000"+
		"\u0000CA\u0001\u0000\u0000\u0000D\u000e\u0001\u0000\u0000\u0000EF\u0005"+
		">\u0000\u0000F\u0010\u0001\u0000\u0000\u0000GH\u0005>\u0000\u0000HI\u0005"+
		"=\u0000\u0000I\u0012\u0001\u0000\u0000\u0000JK\u0005<\u0000\u0000K\u0014"+
		"\u0001\u0000\u0000\u0000LM\u0005<\u0000\u0000MN\u0005=\u0000\u0000N\u0016"+
		"\u0001\u0000\u0000\u0000OS\u0005=\u0000\u0000PQ\u0005=\u0000\u0000QS\u0005"+
		"=\u0000\u0000RO\u0001\u0000\u0000\u0000RP\u0001\u0000\u0000\u0000S\u0018"+
		"\u0001\u0000\u0000\u0000TU\u0005!\u0000\u0000UV\u0005=\u0000\u0000V\u001a"+
		"\u0001\u0000\u0000\u0000W[\u0007\u0000\u0000\u0000XZ\u0007\u0001\u0000"+
		"\u0000YX\u0001\u0000\u0000\u0000Z]\u0001\u0000\u0000\u0000[Y\u0001\u0000"+
		"\u0000\u0000[\\\u0001\u0000\u0000\u0000\\\u001c\u0001\u0000\u0000\u0000"+
		"][\u0001\u0000\u0000\u0000^`\u0007\u0002\u0000\u0000_^\u0001\u0000\u0000"+
		"\u0000`a\u0001\u0000\u0000\u0000a_\u0001\u0000\u0000\u0000ab\u0001\u0000"+
		"\u0000\u0000b\u001e\u0001\u0000\u0000\u0000ce\u0007\u0002\u0000\u0000"+
		"dc\u0001\u0000\u0000\u0000ef\u0001\u0000\u0000\u0000fd\u0001\u0000\u0000"+
		"\u0000fg\u0001\u0000\u0000\u0000gh\u0001\u0000\u0000\u0000hj\u0005.\u0000"+
		"\u0000ik\u0007\u0002\u0000\u0000ji\u0001\u0000\u0000\u0000kl\u0001\u0000"+
		"\u0000\u0000lj\u0001\u0000\u0000\u0000lm\u0001\u0000\u0000\u0000m \u0001"+
		"\u0000\u0000\u0000nr\u0005\'\u0000\u0000oq\t\u0000\u0000\u0000po\u0001"+
		"\u0000\u0000\u0000qt\u0001\u0000\u0000\u0000rs\u0001\u0000\u0000\u0000"+
		"rp\u0001\u0000\u0000\u0000su\u0001\u0000\u0000\u0000tr\u0001\u0000\u0000"+
		"\u0000uv\u0005\'\u0000\u0000v\"\u0001\u0000\u0000\u0000wy\u0007\u0003"+
		"\u0000\u0000xw\u0001\u0000\u0000\u0000yz\u0001\u0000\u0000\u0000zx\u0001"+
		"\u0000\u0000\u0000z{\u0001\u0000\u0000\u0000{|\u0001\u0000\u0000\u0000"+
		"|}\u0006\u0011\u0000\u0000}$\u0001\u0000\u0000\u0000\u000b\u00005=CR["+
		"aflrz\u0001\u0006\u0000\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}