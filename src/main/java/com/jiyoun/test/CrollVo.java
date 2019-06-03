package com.jiyoun.test;

public class CrollVo {

	private int number;
	private String source;
	private boolean isDirectory;
	
	public CrollVo() {
	}
	
	public CrollVo(int number, String source, boolean isDirectory) {
		this.number = number;
		this.source = source;
		this.isDirectory = isDirectory;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public boolean isDirectory() {
		return isDirectory;
	}

	public void setDirectory(boolean isDirectory) {
		this.isDirectory = isDirectory;
	}

	@Override
	public String toString() {
		return "CrollVo [number=" + number + ", source=" + source + ", isDirectory=" + isDirectory + "]";
	}
	
}
