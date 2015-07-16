package com.html5parser.tracer;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;

import com.html5parser.classes.Token;
import com.html5parser.tracer.Event.EventType;

public class Tracer {
	
	private HashMap<String, Event> availableEvents;
	private ArrayList<Event> parseEvents;

	public Tracer() {
		super();
		availableEvents = new LinkedHashMap<String, Event>();
		parseEvents = new ArrayList<Event>();
		try {
			initialize();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void initialize() throws Exception {
		String path = Paths.get(
				"src\\main\\java\\com\\html5parser\\tracer\\events.xml")
				.toString();
		org.w3c.dom.Document document = XMLUtils.readXMLFromFile(path);

		for (org.w3c.dom.Element e : XMLUtils.getElementsByTagName(
				document.getFirstChild(), "event")) {
			JAXBContext context = JAXBContext.newInstance(Event.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			JAXBElement<Event> jaxbElement = unmarshaller.unmarshal(e,
					Event.class);
			Event event = jaxbElement.getValue();
			availableEvents.put(event.getSection(), event);
			;
		}
	}

	public ArrayList<Event> getAvailableEvents() {
		ArrayList<Event> events = new ArrayList<Event>();
		events.addAll(availableEvents.values());
		return events;
	}

	public ArrayList<Event> getParseEvents() {
		return parseEvents;
	}

	public ArrayList<Event> getParseEvents(EventType type) {
		ArrayList<Event> events = new ArrayList<Event>();
		for (Event e : parseEvents)
			if (e.getType() == type)
				events.add(e);
		return events;
	}

	public void addParseEvent(Event event) {
		this.parseEvents.add(event);
	}

	public void addParseEvent(String section) {
		addParseEvent(section, "");
	}

	public void addParseEvent(String section, int currentChar) {
		String additionalInfo = "Input character: \""
				+ (currentChar != -1 ? String.valueOf(Character
						.toChars(currentChar)) : "EOF") + "\" (" + currentChar
				+ ")";
		addParseEvent(section, additionalInfo);
	}

	public void addParseEvent(String section, Token currentToken) {
		String additionalInfo = "Input token: "
				+ currentToken.getType()
				+ (currentToken.getValue() == null ? "" : (" \""
						+ currentToken.getValue() + "\""));
		addParseEvent(section, additionalInfo);
	}

	public void addParseEvent(String section, String additionalInfo) {
		if (availableEvents.containsKey(section)) {
			Event e = availableEvents.get(section);
			Event parseEvent = new Event(e.getType(), e.getDescription(),
					e.getSection(), additionalInfo);
			this.parseEvents.add(parseEvent);
		}
	}
}