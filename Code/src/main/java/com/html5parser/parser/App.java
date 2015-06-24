package com.html5parser.parser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.w3c.dom.Document;

import com.html5parser.classes.ParserContext;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		System.out.println("Hello World!");

		// String html = "<!Doctype html>";
		// System.out.println(parser.parse(html));
		// System.out.println(Serializer.toHtmlString(parseString(html)));
		String filePath = "C:\\Users\\hs012\\Desktop\\Amazon.co.uk.html";
		System.out.println(Serializer.toHtmlString(parseHtmlFile(filePath)));

	}

	private static Document parseString(String html) {
		Parser parser = new Parser();
		ParserContext parserContext = parser
				.tokenize(new ParserContext(), html);
		parser.printTokens(parserContext);
		return parser.parse(html);
	}

	private static Document parseHtmlFile(String filePath) {
		InputStream is;
		Document doc = null;

		try {
			is = new FileInputStream(filePath);
			Parser parser = new Parser();
			doc = parser.parse(is);
			is.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return doc;
	}
}
