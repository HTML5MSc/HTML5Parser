package com.html5parser.tracer;

public class TracerSummary {

	private boolean specialElements = false;
	private boolean formattingElements = false;
	private boolean svgElements = false;
	private boolean mathMlElements = false;
	private boolean html5FormElements = false;
	private boolean html5MediaElements = false;
	private boolean html5GraphicElements = false;
	private boolean html5SemanticStructuralElements = false;

	private int algorithms = 0;
	private int insertionModes = 0;
	private int tokenizerStates = 0;
	private int emittedTokens = 0;
	private int parseErrors = 0;

	public boolean isSpecialElements() {
		return specialElements;
	}

	public void setSpecialElements(boolean specialElements) {
		this.specialElements = specialElements;
	}

	public boolean isFormattingElements() {
		return formattingElements;
	}

	public void setFormattingElements(boolean formattingElements) {
		this.formattingElements = formattingElements;
	}

	public boolean isSvgElements() {
		return svgElements;
	}

	public void setSvgElements(boolean svgElements) {
		this.svgElements = svgElements;
	}

	public boolean isMathMlElements() {
		return mathMlElements;
	}

	public void setMathMlElements(boolean mathMlElements) {
		this.mathMlElements = mathMlElements;
	}

	public boolean isHtml5FormElements() {
		return html5FormElements;
	}

	public void setHtml5FormElements(boolean html5FormElements) {
		this.html5FormElements = html5FormElements;
	}

	public boolean isHtml5MediaElements() {
		return html5MediaElements;
	}

	public void setHtml5MediaElements(boolean html5MediaElements) {
		this.html5MediaElements = html5MediaElements;
	}

	public boolean isHtml5GraphicElements() {
		return html5GraphicElements;
	}

	public void setHtml5GraphicElements(boolean html5GraphicElements) {
		this.html5GraphicElements = html5GraphicElements;
	}

	public boolean isHtml5SemanticStructuralElements() {
		return html5SemanticStructuralElements;
	}

	public void setHtml5SemanticStructuralElements(
			boolean html5SemanticStructuralElements) {
		this.html5SemanticStructuralElements = html5SemanticStructuralElements;
	}

	public int getAlgorithms() {
		return algorithms;
	}

	public void incrementAlgorithms() {
		this.algorithms++;
	}

	public int getInsertionModes() {
		return insertionModes;
	}

	public void incrementInsertionModes() {
		this.insertionModes++;
	}

	public int getTokenizerStates() {
		return tokenizerStates;
	}

	public void incrementTokenizerStates() {
		this.tokenizerStates++;
	}
	
	public int getEmittedTokens() {
		return emittedTokens;
	}

	public void incrementEmittedTokens() {
		this.emittedTokens++;
	}

	public int getParseErrors() {
		return parseErrors;
	}

	public void incrementParseErrors() {
		this.parseErrors++;
	}
}
