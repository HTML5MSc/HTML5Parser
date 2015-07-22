package com.html5parser.parser;

import java.util.ArrayList;

import com.html5dom.Document;
import com.html5dom.Element;
import com.html5dom.Node;
import com.html5parser.algorithms.ListOfActiveFormattingElements;
import com.html5parser.algorithms.ResetTheInsertionModeAppropriately;
import com.html5parser.classes.ParserContext;
import com.html5parser.classes.Token;
import com.html5parser.classes.Token.TokenType;
import com.html5parser.insertionModes.InSelect;
import com.html5parser.tracer.Event;
import com.html5parser.tracer.Event.EventType;
import com.html5parser.tracer.ParseError;
import com.html5parser.tracer.Tracer;
import com.html5parser.tracer.TracerSummary;

/**
 * Hello world!
 *
 */
public class MyApp {
	public static void main(String[] args) {
		System.out.println("Hello World!");

		parse();
		// parseFragment();
		// resetInsertionMode();
		// elementInScope();
		// pushFormattingElements();
		// reconstructFormattingElements();
		// inSelectInsertionMode();
	}

	private static void parse() {
		//Parser parser = new Parser(true, true);
		Parser parser = new Parser(true);
		ParserContext parserContext = parser.getParserContext();
		Tracer tracer = new Tracer(true);
		parserContext.setTracer(tracer);
		tracer.initializeEvents();
		// String html = "<b id=a><p><b id=b></p></b>o";
		String html = "< g><a h=2 h=3 /><i></p h=2><b>Jul 10 – Jul 14</i><html t=2><svg><math><b>etst</a>";
		// "<form id=\"nav-searchbar\" action=\"http://www.amazon.co.uk/s/ref=nb_sb_noss\" method=\"get\"accept-charset=\"utf-8\" ,=\"\">"
		// String html = "<!doctype html><html lang=\"en-US\"><head>";
		// String html = "<script type=\"data\"><!--foo </script>";
		// String html = "<a><svg><tr><input></a>";

		Document doc = parser.parse(html);		
//		tracer.initializeEvents();
//		tracer.refreshParseEvents();
		// System.out.println(Serializer.toHtmlString(doc));
		System.out.println(doc.getOuterHtml(true));
		System.out.println(Serializer.toHtml5libFormat(doc).replace("]]>",
				"] ] >"));
		System.out.println(printParseErrors(parserContext));
		System.out.println(printTracer(parserContext));
		// testJDOM();
	}

	private static String printParseErrors(ParserContext parserContext) {
		// if (!parserContext.isTracing())
		// return "Tracing disabled.";
		StringBuilder sb = new StringBuilder();
		sb.append("***** ERRORS *****");
		sb.append(System.getProperty("line.separator"));

		for (ParseError err : parserContext.getParseErrors()) {
			sb.append(err.getMessage());
			sb.append(System.getProperty("line.separator"));
		}
		return sb.toString();
	}

	private static String printTracer(ParserContext parserContext) {
		if (!parserContext.isTracing())
			return "Tracing disabled.";
		StringBuilder sb = new StringBuilder();
		Tracer tracer = parserContext.getTracer();
		TracerSummary sumary = tracer.getSummary();

		ArrayList<EventType> excludeTypes = new ArrayList<Event.EventType>();
		ArrayList<String> excludeSections = new ArrayList<String>();
		excludeTypes.add(EventType.TokenizerState);
		excludeTypes.add(EventType.InsertionMode);
		excludeTypes.add(EventType.Algorithm);
		excludeSections.add("8.2.5");
		excludeSections.add("8.2.5.1_1");

		sb.append("***** EVENTS *****");
		sb.append(System.getProperty("line.separator"));
		for (Event event : tracer.getParseEvents(excludeTypes, excludeSections)) {
			sb.append("Sec: " + event.getSection() + " Desc: " + event.getDescription()
					+ " Info: " + event.getAdditionalInfo());
			sb.append(System.getProperty("line.separator"));
		}
		sb.append(System.getProperty("line.separator"));
		sb.append("***** SUMMARY *****");
		sb.append(System.getProperty("line.separator"));
		sb.append("Algorithms: " + sumary.getAlgorithms());
		sb.append(System.getProperty("line.separator"));
		sb.append("Insertion modes: " + sumary.getInsertionModes());
		sb.append(System.getProperty("line.separator"));
		sb.append("Tokenizer states: " + sumary.getTokenizerStates());
		sb.append(System.getProperty("line.separator"));
		sb.append("Parse errors: " + sumary.getParseErrors());
		sb.append(System.getProperty("line.separator"));
		sb.append("Token count: " + sumary.getEmittedTokens());
		sb.append(System.getProperty("line.separator"));
		sb.append("Formatting elements: " + sumary.isFormattingElements());
		sb.append(System.getProperty("line.separator"));
		sb.append("Special elements: " + sumary.isSpecialElements());
		sb.append(System.getProperty("line.separator"));
		sb.append("MathML elements: " + sumary.isMathMlElements());
		sb.append(System.getProperty("line.separator"));
		sb.append("SVG elements: " + sumary.isSvgElements());

		return sb.toString();
	}

	private static void parseFragment() {
		Parser parser = new Parser();
		// String html = "<b id=a><p><b id=b></p></b>o";
		// String html =
		// "<form id=\"nav-searchbar\" action=\"http://www.amazon.co.uk/s/ref=nb_sb_noss\" method=\"get\" name=\"site-search\" role=\"search\" accept-charset=\"utf-8\" ,=\"\" class=\"nav-searchbar-inner\">";
		// String html = "<div bar=\"ZZ&gt YY\"></div>";
		String html = "</select><option>";
		String contextElement = "select";
		Node doc = parser.parseFragment(html, contextElement);
		System.out.println(Serializer.toHtmlString(doc));
		System.out.println(Serializer.toHtml5libFormat(doc));
		// testJDOM();
	}

	private static void inSelectInsertionMode() {
		ParserContext parserContext = new ParserContext();
		Document doc = new Document();

		Element e1 = doc.createElement("optgroup");
		Element e2 = doc.createElement("option");
		parserContext.getOpenElements().push(e1);
		parserContext.getOpenElements().push(e2);

		parserContext.getTokenizerContext().getTokens()
				.add(new Token(TokenType.end_tag, "optgroup"));

		new InSelect().process(parserContext);

	}

	private static void reconstructFormattingElements() {
		ParserContext parserContext = new ParserContext();
		Document doc = new Document();
		ArrayList<Element> list = parserContext.getActiveFormattingElements();

		Element e1 = doc.createElement("html");
		Element e2 = doc.createElement("table");
		Element e3 = doc.createElement("template");
		Element e4 = doc.createElement("select");
		Element e5 = doc.createElement("i");
		Element e6 = doc.createElement("a");
		Element e7 = doc.createElement("select");
		// list.add(e1);
		// list.add(e3);
		// list.add(null);
		// list.add(e4);
		list.add(e5);
		list.add(null);
		list.add(e6);
		list.add(e7);

		parserContext.setDocument(doc);
		ListOfActiveFormattingElements.reconstruct(parserContext);

	}

	private static void pushFormattingElements() {
		ParserContext parserContext = new ParserContext();
		Document doc = new Document();

		ArrayList<Element> list = parserContext.getActiveFormattingElements();

		Element e1 = doc.createElement("html");
		Element e2 = doc.createElement("table");
		Element e3 = doc.createElement("template");
		Element e4 = doc.createElement("select");
		Element e5 = doc.createElement("i");
		Element e6 = doc.createElement("a");
		Element e7 = doc.createElement("select");
		list.add(e1);
		list.add(e3);
		// list.add(null);
		list.add(e4);
		list.add(e2);
		// list.add(null);
		list.add(e5);
		list.add(e6);
		System.out.println(list);
		System.out
				.println(list.subList(list.lastIndexOf(null) + 1, list.size()));

	}

	private static void elementInScope() {
		ParserContext parserContext = new ParserContext();
		Document doc = new Document();

		Element e1 = doc.createElement("html");
		Element e2 = doc.createElement("table");
		Element e3 = doc.createElement("template");
		Element e4 = doc.createElement("select");
		parserContext.getOpenElements().push(e1);
		parserContext.getOpenElements().push(e2);
		parserContext.getOpenElements().push(e3);
		parserContext.getOpenElements().push(e4);

		ResetTheInsertionModeAppropriately.Run(parserContext);
	}

	private static void resetInsertionMode() {
		ParserContext parserContext = new ParserContext();
		Document doc = new Document();

		Element e1 = doc.createElement("html");
		Element e2 = doc.createElement("table");
		Element e3 = doc.createElement("template");
		Element e4 = doc.createElement("select");
		parserContext.getOpenElements().push(e1);
		parserContext.getOpenElements().push(e2);
		parserContext.getOpenElements().push(e3);
		parserContext.getOpenElements().push(e4);

		ResetTheInsertionModeAppropriately.Run(parserContext);

	}

	// private static void testJDOM() {
	// ParserContext parserContext = new ParserContext();
	// org.jdom2.Document doc = new org.jdom2.Document();
	// doc = new org.jdom2.Document();
	// // org.jdom2.DocType doctype = new DocType("", null, null);
	// org.jdom2.DocType doctype = new DocType("html", null, null);
	// doc.addContent(doctype);
	//
	// org.jdom2.Element e1 = new org.jdom2.Element("html");
	// org.jdom2.Element e2 = new org.jdom2.Element("head");
	// org.jdom2.Element e3 = new org.jdom2.Element("body");
	// org.jdom2.Element e4 = new org.jdom2.Element("p");
	// org.jdom2.Element e5 = new org.jdom2.Element("i");
	// org.jdom2.Element e6 = new org.jdom2.Element("a");
	// // e6.setAttribute("1href12", "#");
	// e6.setAttribute("1href_12", "#");
	// e1.addContent(e2);
	// e1.addContent(e3);
	// e3.addContent(e4);
	// e4.addContent(e5);
	// e3.addContent(e6);
	// doc.addContent(e1);
	//
	// XMLOutputter xout = new XMLOutputter(Format.getPrettyFormat());
	// try {
	// xout.output(doc, System.out);
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }

	private static void tokenize() {
		Parser parser = new Parser();
		String html = "<!doctype>";

		ParserContext parserContext = parser
				.tokenize(new ParserContext(), html);
		parser.printTokens(parserContext);

		// Document doc = parser.parse("<html><foo/>");
		// System.out.println(Serializer.toHtmlString(doc));

		Document doc = new Document();

		Element e1 = doc.createElement("element1");
		Element e2 = doc.createElement("element2");
		e1.appendChild(e2);
		doc.appendChild(e1);
		// Attr att = doc.createAttribute("name");
		// att.setNodeValue("test");
		e2.appendChild(doc.createTextNode("test"));
		e1.appendChild(doc.createCDATASection("cdataTest"));
		System.out.println(Serializer.toHtmlString(doc));

	}

	// @Test
	// public void getCarAsXml() {
	// String registration = "abc123";
	// String brand = "Volvo";
	// String description = "Sedan";
	//
	// Car car = new Car(registration, brand, description);
	// XMLUtils xmlUtil = new XMLUtils();
	// String xml = xmlUtil.convertToXml(car, car.getClass());
	//
	// String xpathExpression = "/car/@registration";
	// String actual = xmlUtil.extractValue(xml, xpathExpression);
	// assertEquals(actual, registration);
	//
	// xpathExpression = "/car/brand";
	// actual = xmlUtil.extractValue(xml, xpathExpression);
	// assertEquals(actual, brand);
	//
	// xpathExpression = "/car/description";
	// actual = xmlUtil.extractValue(xml, xpathExpression);
	// assertEquals(actual, description);
	// }
}
