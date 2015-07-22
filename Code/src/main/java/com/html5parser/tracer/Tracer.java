package com.html5parser.tracer;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;

import com.html5parser.tracer.Event.EventType;

public class Tracer {

	private TracerSummary summary;
	private HashMap<String, Event> availableEvents;
	private ArrayList<Event> parseEvents;
	private ArrayList<String> usedAlgorithms;
	private ArrayList<String> usedInsertionModes;
	private ArrayList<String> usedTokenizerStates;

	public Tracer() {
		this(false);
	}

	public Tracer(boolean lazyInitialization) {
		super();
		availableEvents = new LinkedHashMap<String, Event>();
		parseEvents = new ArrayList<Event>();
		summary = new TracerSummary();
		usedAlgorithms = new ArrayList<String>();
		usedInsertionModes = new ArrayList<String>();
		usedTokenizerStates = new ArrayList<String>();

		if (!lazyInitialization)
			try {
				initialize();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	public void initializeEvents() {
		try {
			initialize();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void initialize() throws Exception {
		// String path = Paths.get("tracerEvents.xml").toString();
		// org.w3c.dom.Document document = XMLUtils.readXMLFromFile(path);
		InputStream path = getClass().getResourceAsStream(
				"/com/html5parser/tracer/tracerEvents.xml");
		org.w3c.dom.Document document = XMLUtils.readXMLFromInputStream(path);

		for (org.w3c.dom.Element e : XMLUtils.getElementsByTagName(
				document.getFirstChild(), "event")) {
			JAXBContext context = JAXBContext.newInstance(Event.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			JAXBElement<Event> jaxbElement = unmarshaller.unmarshal(e,
					Event.class);
			Event event = jaxbElement.getValue();
			availableEvents.put(event.getSection(), event);
		}
	}

	public ArrayList<Event> getAvailableEvents() {
		ArrayList<Event> events = new ArrayList<Event>();
		events.addAll(availableEvents.values());
		return events;
	}

	public ArrayList<String> getAlgorithms() {
		ArrayList<String> events = new ArrayList<String>();
		for (Event e : availableEvents.values())
			if (e.getType() == EventType.Algorithm)
				events.add(e.getDescription());
		return events;
	}

	public ArrayList<String> getUsedAlgorithms() {
		return usedAlgorithms;
	}

	public ArrayList<String> getInsertionModes() {
		ArrayList<String> events = new ArrayList<String>();
		for (Event e : availableEvents.values())
			if (e.getType() == EventType.InsertionMode)
				events.add(e.getDescription());
		return events;
	}

	public ArrayList<String> getUsedInsertionModes() {
		return usedInsertionModes;
	}

	public ArrayList<String> getTokenizerStates() {
		ArrayList<String> events = new ArrayList<String>();
		for (Event e : availableEvents.values())
			if (e.getType() == EventType.TokenizerState)
				events.add(e.getDescription());
		return events;
	}

	public ArrayList<String> getUsedTokenizerStates() {
		return usedTokenizerStates;
	}

	public TracerSummary getSummary() {
		return summary;
	}

	public List<Event.EventType> getEventTypes() {
		return Arrays.asList(Event.EventType.values());
	}

	public List<String> getSections() {
		List<String> sections = new ArrayList<String>();
		for (Event e : getAvailableEvents())
			sections.add(e.getSection() + " - " + e.getDescription());
		return sections;
	}

	public ArrayList<Event> getParseEvents() {
		return parseEvents;
	}

	public void setParseEvents(ArrayList<Event> parseEvents) {
		this.parseEvents = parseEvents;
	}

	public ArrayList<Event> getParseEvents(ArrayList<EventType> excludeTypes) {
		return filterParseEvents(excludeTypes, new ArrayList<String>());
	}

	public ArrayList<Event> getParseEvents(ArrayList<EventType> excludeTypes,
			ArrayList<String> excludeSections) {
		return filterParseEvents(excludeTypes, excludeSections);
	}

	private ArrayList<Event> filterParseEvents(
			ArrayList<EventType> excludeTypes, ArrayList<String> excludeSections) {
		if (excludeTypes == null)
			excludeTypes = new ArrayList<Event.EventType>();
		if (excludeSections == null)
			excludeSections = new ArrayList<String>();

		ArrayList<Event> events = new ArrayList<Event>();
		for (Event e : parseEvents)
			if (!excludeTypes.contains(e.getType())
					&& !excludeSections.contains(e.getSection()))
				events.add(e);

		return events;
	}

	public void refreshParseEvents() {
		if (this.availableEvents.isEmpty())
			return;

		for (Event e : parseEvents)
			if (availableEvents.containsKey(e.getSection())) {
				Event available = availableEvents.get(e.getSection());
				e.setDescription(available.getDescription());
				e.setType(available.getType());
				refreshTotals(e);
			}

	}

	public void addParseEvent(Event event) {
		this.parseEvents.add(event);
	}

	public void addParseEvent(String section, String additionalInfo) {
		String description = null;
		EventType type = null;
		if (!availableEvents.isEmpty() && availableEvents.containsKey(section)) {
			Event e = availableEvents.get(section);
			refreshTotals(e);
			description = e.getDescription();
			type = e.getType();
		}

		Event parseEvent = new Event(type, description, section, additionalInfo);
		this.parseEvents.add(parseEvent);
	}

	private void refreshTotals(Event e) {
		if (e.getType() == EventType.InsertionMode) {
			if (!this.usedInsertionModes.contains(e.getDescription())) {
				this.usedInsertionModes.add(e.getDescription());
				this.summary.incrementInsertionModes();
			}
		} else if (e.getType() == EventType.Algorithm) {
			if (!this.usedAlgorithms.contains(e.getDescription())) {
				this.usedAlgorithms.add(e.getDescription());
				this.summary.incrementAlgorithms();
			}
		} else if (e.getType() == EventType.TokenizerState) {
			if (!this.usedTokenizerStates.contains(e.getDescription())) {
				this.usedTokenizerStates.add(e.getDescription());
				this.summary.incrementTokenizerStates();
			}
		}
	}

	public void addParseError(String description, String additionalInfo) {
		addParseError(null, description, additionalInfo);
	}

	public void addParseError(String section, String description,
			String additionalInfo) {
		if (!availableEvents.isEmpty() && availableEvents.containsKey(section)) {
			Event e = availableEvents.get(section);
			refreshTotals(e);
			description = e.getDescription();
		}
		Event parseEvent = new Event(EventType.ParseError, description,
				"Parse error", additionalInfo);
		this.parseEvents.add(parseEvent);

		this.summary.incrementParseErrors();
	}
}