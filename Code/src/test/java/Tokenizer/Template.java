package Tokenizer;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.html5parser.classes.ParserContext;
import com.html5parser.classes.Token;
import com.html5parser.classes.Token.TokenType;
import com.html5parser.parser.Parser;

public class Template {

	@Test
	public final void testEmptyString() {

		Parser parser = new Parser();
		ParserContext parserContext = new ParserContext();

		String string = "\u0000";

		parserContext = parser.tokenize(parserContext, string);
		parser.printTokens(parserContext);

		Token tok = parserContext.getTokenizerContext().getTokens().poll();

		assertTrue("No EOF token", tok.getType().equals(TokenType.end_of_file));
		assertTrue("No more tokens expected", parserContext
				.getTokenizerContext().getTokens().isEmpty());

	}

}