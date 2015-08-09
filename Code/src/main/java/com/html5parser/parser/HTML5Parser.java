package com.html5parser.parser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import com.html5dom.Document;

/**
 * Hello world!
 *
 */
public class HTML5Parser {
	public static void main(String[] args) {
		if (!(args.length == 2 || args.length == 4)) {
			System.out.println("Two or four parameters are required");
			return;
		}

		Document doc = null;
		String html = null;

		switch (args[0]) {
		case "-f":
			html = readFile(args[1]);
			if (html == null)
				return;
			break;
		case "-s":
			html = args[1];
			break;
		default:
			System.out.println("Invalid option");
			return;
		}
		try {
			Boolean trace = (args.length == 4 && args[2].equals("-t"));
			Parser parser = new Parser(true, trace);
			doc = parser.parse(html);
			if (trace)
				parser.getParserContext().getTracer().toXML(args[3]);

			String output = Serializer.toHtml5libFormat(doc);
			PrintStream out = new PrintStream(System.out, true, "UTF-8");
			out.println(output);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static String readFile(String path) {
		StringBuilder sb = new StringBuilder();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(
					path), "UTF-8"));
			String line;
			if ((line = br.readLine()) != null)
				sb.append(line);
			while ((line = br.readLine()) != null) {
				sb.append(System.getProperty("line.separator"));
				sb.append(line);
			}
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString();
	}
}
