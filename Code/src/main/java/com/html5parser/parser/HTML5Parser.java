package com.html5parser.parser;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintStream;

import com.html5dom.Document;

public class HTML5Parser {
	public static void main(String[] args) {
		if (!(args.length == 2 || args.length == 4)) {
			System.out.println("Two or four parameters are required");
			return;
		}

		String inputType = args[0];
		String inputValue = args[1];
		Boolean trace = (args.length == 4 && args[2].equals("-t"));
		String tracerReportPath = trace ? args[3] : null;

		try {
			Document doc = null;

			Parser parser = new Parser(true, trace);

			switch (inputType) {
			case "-f":
				InputStream is = new FileInputStream(inputValue);
				doc = parser.parse(is);
				is.close();
				break;
			case "-s":
				doc = parser.parse(inputValue);
				break;
			default:
				System.out.println("Invalid option");
				return;
			}

			if (trace)
				parser.getParserContext().getTracer().toXML(tracerReportPath);

			String output = Serializer.toHtml5libFormat(doc);
			PrintStream out = new PrintStream(System.out, true, "UTF-8");
			out.println(output);
			out.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}