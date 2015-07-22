package com.html5dom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class Element extends Node {

	String nodeName;
	LinkedHashMap<String, Attribute> attributes;

	protected Element(String nodeName) {
		this(nodeName, null);
	}

	protected Element(String nodeName, String namespaceURI) {
		super(NodeType.ELEMENT_NODE, namespaceURI);
		this.nodeName = nodeName;
		this.nodeName = nodeName;
		this.attributes = new LinkedHashMap<String, Attribute>();
	}

	public List<Attribute> getAttributes() {
		return new ArrayList<Attribute>(this.attributes.values());
	}

	public Attribute getAttributeNode(String name) {
		return this.attributes.get(name);
	}

	public Attribute getAttributeNodeNS(String namespaceURI, String localName) {
		Attribute attr = this.attributes.get(localName);
		return attr.namespaceURI.equals(namespaceURI) ? attr : null;
	}

	public String getAttribute(String name) {
		return this.getAttributeNode(name).getNodeValue();
	}

	public String getAttributeNS(String namespaceURI, String localName) {
		Attribute attr = this.getAttributeNodeNS(namespaceURI, localName);
		return attr == null ? null : attr.getNodeValue();
	}

	public Attribute setAttribute(String name, String value) {
		return setAttributeNS(null, name, value);
	}

	public Attribute setAttributeNS(String namespace, String name, String value) {
		Attribute attr = new Attribute(name, value, namespace);
		this.attributes.put(name, attr);
		return attr;
	}

	public String getTagName() {
		return this.nodeName;
	}

	public boolean hasAttribute(String name) {
		return this.attributes.containsKey(name);
	}

	public boolean isHTMLElement() {
		if (this.namespaceURI != null
				&& this.namespaceURI.equals(Constants.HTML_NAMESPACE))
			return true;
		return false;
	}

	public boolean isMathMLElement() {
		if (this.namespaceURI != null
				&& this.namespaceURI.equals(Constants.MathML_NAMESPACE))
			return true;
		return false;
	}

	public boolean isSVGElement() {
		if (this.namespaceURI != null
				&& this.namespaceURI.equals(Constants.SVG_NAMESPACE))
			return true;
		return false;
	}

	public boolean isFormattingElement() {
		if (Arrays.asList(Constants.FORMATTING_ELEMENTS)
				.contains(this.nodeName) && this.isHTMLElement())
			return true;
		return false;
	}

	public boolean isSpecialElement() {
		if (Arrays.asList(Constants.SPECIAL_HTML_ELEMENTS).contains(
				this.nodeName)
				&& this.isHTMLElement())
			return true;
		if (Arrays.asList(Constants.SPECIAL_MATHML_ELEMENTS).contains(
				this.nodeName)
				&& this.isMathMLElement())
			return true;
		if (Arrays.asList(Constants.SPECIAL_SVG_ELEMENTS).contains(
				this.nodeName)
				&& this.isSVGElement())
			return true;
		return false;
	}

	public boolean isVoidElement() {
		if (Arrays.asList(Constants.VOID_ELEMENTS).contains(this.nodeName)
				&& this.isHTMLElement())
			return true;
		return false;
	}

	public boolean isRawTextElement() {
		if (Arrays.asList(Constants.RAW_TEXT_ELEMENTS).contains(this.nodeName)
				&& this.isHTMLElement())
			return true;
		return false;
	}

	public boolean isEscapableRawTextElement() {
		if (Arrays.asList(Constants.ESCAPABLE_RAW_TEXT_ELEMENTS).contains(
				this.nodeName)
				&& this.isHTMLElement())
			return true;
		return false;
	}

	public boolean isForeignElement() {
		return this.isSVGElement() || this.isMathMLElement();
	}

	public boolean isNormalElement() {
		return this.isHTMLElement() && !this.isVoidElement()
				&& !this.isRawTextElement() && this.isEscapableRawTextElement();
	}

	public boolean isHtml5FormElement() {
		if (Arrays.asList(Constants.HTML5_FORM_ELEMENTS).contains(this.nodeName)
				&& this.isHTMLElement())
			return true;
		return false;
	}
	
	public boolean isHtml5GraphicElement() {
		if (Arrays.asList(Constants.HTML5_GRAPHIC_ELEMENTS).contains(this.nodeName)
				&& this.isHTMLElement())
			return true;
		return false;
	}
	
	public boolean isHtml5SemanticStructuralElement() {
		if (Arrays.asList(Constants.HTML5_SEMANTIC_STRUCTURAL_ELEMENTS).contains(this.nodeName)
				&& this.isHTMLElement())
			return true;
		return false;
	}
	
	public boolean isHtml5MediaElement() {
		if (Arrays.asList(Constants.HTML5_MEDIA_ELEMENTS).contains(this.nodeName)
				&& this.isHTMLElement())
			return true;
		return false;
	}
	
	@Override
	public String getNodeName() {
		return this.nodeName;
	}

	@Override
	public String getNodeValue() {
		return null;
	}

	@Override
	public void setNodeValue(String nodeValue) {
	}

	@Override
	public String getOuterHtml() {
		return this.getStartTagHtml();
	}

	@Override
	public String getLocalName() {
		int index = this.nodeName.indexOf(":");
		return index == -1 ? this.nodeName : this.nodeName.substring(index);
	}

	protected String getStartTagHtml() {
		StringBuilder sb = new StringBuilder();
		sb.append("<").append(this.nodeName);
		for (Attribute att : this.attributes.values())
			sb.append(" ").append(att.getOuterHtml());
		sb.append(">");
		return sb.toString();
	}

	protected String getEndTagHtml() {
		return this.isVoidElement() ? "" : ("</" + this.getNodeName() + ">");
	}
}
