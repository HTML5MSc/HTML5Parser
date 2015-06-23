package com.html5parser.tokenizerStates;

import com.html5parser.classes.ASCIICharacter;
import com.html5parser.classes.ParserContext;
import com.html5parser.classes.Token;
import com.html5parser.classes.Token.TokenType;
import com.html5parser.classes.TokenizerContext;
import com.html5parser.classes.TokenizerState;
import com.html5parser.factories.TokenizerStateFactory;
import com.html5parser.interfaces.ITokenizerState;

public class Script_data_escaped_less_than_sign_state implements
		ITokenizerState {

	public ParserContext process(ParserContext context) {
		TokenizerStateFactory factory = TokenizerStateFactory.getInstance();
		TokenizerContext tokenizerContext = context.getTokenizerContext();
		int currentChar = tokenizerContext.getCurrentInputCharacter();
		ASCIICharacter asciiCharacter = tokenizerContext
				.getCurrentASCIICharacter();

		switch (asciiCharacter) {
		case DASH:
			/*
			 * Set the temporary buffer to the empty string. Switch to the
			 * script data escaped end tag open state.
			 */
			tokenizerContext.setTemporaryBuffer("");
			tokenizerContext
					.setNextState(factory
							.getState(TokenizerState.Script_data_escaped_end_tag_open_state));
			break;
		case LATIN_CAPITAL_LETTER:
			/*
			 * Set the temporary buffer to the empty string. Append the
			 * lowercase version of the current input character (add 0x0020 to
			 * the character's code point) to the temporary buffer. Switch to
			 * the script data double escape start state. Emit a U+003C
			 * LESS-THAN SIGN character token and the current input character as
			 * a character token.
			 */
			tokenizerContext.setTemporaryBuffer("");
			tokenizerContext
					.appendCharacterToTemporaryBuffer(currentChar + 0x0020);
			tokenizerContext
					.setNextState(factory
							.getState(TokenizerState.Script_data_double_escape_start_state));
			tokenizerContext.emitCurrentToken(new Token(TokenType.character,
					0x003C));
			tokenizerContext.emitCurrentToken(new Token(TokenType.character,
					currentChar));
			break;
		case LATIN_SMALL_LETTER:
			/*
			 * Set the temporary buffer to the empty string. Append the current
			 * input character to the temporary buffer. Switch to the script
			 * data double escape start state. Emit a U+003C LESS-THAN SIGN
			 * character token and the current input character as a character
			 * token.
			 */
			tokenizerContext.setTemporaryBuffer("");
			tokenizerContext.appendCharacterToTemporaryBuffer(currentChar);
			tokenizerContext
					.setNextState(factory
							.getState(TokenizerState.Script_data_double_escape_start_state));
			tokenizerContext.emitCurrentToken(new Token(TokenType.character,
					0x003C));
			tokenizerContext.emitCurrentToken(new Token(TokenType.character,
					currentChar));
			break;

		default:
			/*
			 * Switch to the script data escaped state. Emit a U+003C LESS-THAN
			 * SIGN character token. Reconsume the current input character.
			 */
			tokenizerContext.setNextState(factory
					.getState(TokenizerState.Script_data_escaped_state));
			tokenizerContext.emitCurrentToken(new Token(TokenType.character,
					0x003C));
			tokenizerContext.setFlagReconsumeCurrentInputCharacter(true);
			break;
		}

		context.setTokenizerContext(tokenizerContext);
		return context;
	}

}
