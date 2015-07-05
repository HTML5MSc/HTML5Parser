package com.html5dom;

public class Attribute extends Node {

	private String name;
	private String value;

	protected Attribute(String name, String value, String namespaceURI) {
		super(NodeType.ATTRIBUTE_NODE, namespaceURI);
		this.name = name;
		this.value = value;
	}

	protected Attribute(String name, String value) {
		super(NodeType.ATTRIBUTE_NODE);
		this.name = name;
		this.value = value;
	}

	@Override
	public String getNodeName() {
		return this.name;
	}

	@Override
	public String getNodeValue() {
		return this.value;
	}

	@Override
	public void setNodeValue(String nodeValue) {
		this.value = nodeValue;
	}

	@Override
	public String getOuterHtml() {
		return this.name + "=\"" + this.value + "\"";
	}

	@Override
	public String getLocalName() {
		int index = this.name.indexOf(":") + 1;
		return this.name.substring(index);
	}

}