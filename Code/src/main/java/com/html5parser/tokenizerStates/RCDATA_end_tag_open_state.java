package com.html5parser.tokenizerStates;

import com.html5parser.classes.ASCIICharacter;
import com.html5parser.classes.ParserContext;
import com.html5parser.classes.Token;
import com.html5parser.classes.Token.TokenType;
import com.html5parser.classes.TokenizerContext;
import com.html5parser.classes.TokenizerState;
import com.html5parser.classes.token.TagToken;
import com.html5parser.factories.TokenizerStateFactory;
import com.html5parser.interfaces.ITokenizerState;

public class RCDATA_end_tag_open_state implements ITokenizerState {

	public ParserContext process(ParserContext context) {
		TokenizerStateFactory factory = TokenizerStateFactory.getInstance();
		TokenizerContext tokenizerContext = context.getTokenizerContext();
		int currentChar = tokenizerContext.getCurrentInputCharacter();
		tokenizerContext.setCurrentInputCharacter(currentChar);

		ASCIICharacter asciiCharacter = tokenizerContext
				.getCurrentASCIICharacter();

		if(context.isTracing())
			context.getTracer().addParseEvent("8.2.4.12", currentChar);
		
		switch (asciiCharacter) {

		case LATIN_CAPITAL_LETTER:
			/*
			 * Create a new end tag token, and set its tag name to the lowercase
			 * version of the current input character (add 0x0020 to the
			 * character's code point). Append the current input character to
			 * the temporary buffer. Finally, switch to the RCDATA end tag name
			 * state. (Don't emit the token yet; further details will be filled
			 * in before it is emitted.)
			 */
			tokenizerContext.setCurrentToken(new TagToken(TokenType.end_tag,
					currentChar + 0x0020));
			tokenizerContext.appendCharacterToTemporaryBuffer(currentChar);
			tokenizerContext.setNextState(factory
					.getState(TokenizerState.RCDATA_end_tag_name_state));
			break;

		case LATIN_SMALL_LETTER:
			/*
			 * Create a new end tag token, and set its tag name to the current
			 * input character. Append the current input character to the
			 * temporary buffer. Finally, switch to the RCDATA end tag name
			 * state. (Don't emit the token yet; further details will be filled
			 * in before it is emitted.)
			 */
			tokenizerContext.setCurrentToken(new TagToken(TokenType.end_tag,
					currentChar));
			tokenizerContext.appendCharacterToTemporaryBuffer(currentChar);
			tokenizerContext.setNextState(factory
					.getState(TokenizerState.RCDATA_end_tag_name_state));
			break;
		default:
			/*
			 * Switch to the RCDATA state. Emit a U+003C LESS-THAN SIGN
			 * character token and a U+002F SOLIDUS character token. Reconsume
			 * the current input character.
			 */
			tokenizerContext.setNextState(factory
					.getState(TokenizerState.RCDATA_state));
			tokenizerContext.emitCurrentToken(new Token(TokenType.character,
					0x003C));
			tokenizerContext.emitCurrentToken(new Token(TokenType.character,
					0x002F));
			tokenizerContext.setFlagReconsumeCurrentInputCharacter(true);
			break;
		}

		context.setTokenizerContext(tokenizerContext);

		return context;

	}

}
