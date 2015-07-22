package com.html5parser.tracer;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlType(propOrder = {"section", "description"})
public class Event {

	public enum EventType {
		TokenizerState, InsertionMode, Algorithm, ParseError
	}		
	private String description;
	private String section;
	private String additionalInfo;
	private EventType type;
	
	public Event(){
		super();
	}
	
	public Event(EventType type, String description, String section, String additionalInfo) {
		super();
		this.additionalInfo = additionalInfo;
		this.description = description;
		this.section = section;
		this.type = type;
	}
	
	@XmlAttribute
	public String getSection() {
		return section;
	}
	public void setSection(String section) {
		this.section = section;
	}
	
	@XmlAttribute
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}	
	
	@XmlAttribute
	public EventType getType() {
		return type;
	}
	public void setType(EventType type) {
		this.type = type;
	}
	
	@XmlAttribute
	public String getAdditionalInfo() {
		return additionalInfo;
	}
	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}
}