package com.jiyoun.test;

public class CrollVo {

	private int number;
	private String source;
	private int typeOfFile;
	/* 0 : 상위폴더
	 * 1 : 디렉토리(하위폴더)
	 * 2 : 파일
	*/
	
	public CrollVo() {
	}

	public CrollVo(int number, String source, int typeOfFile) {
		this.number = number;
		this.source = source;
		this.typeOfFile = typeOfFile;
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

	public int getTypeOfFile() {
		return typeOfFile;
	}

	public void setTypeOfFile(int typeOfFile) {
		this.typeOfFile = typeOfFile;
	}

	@Override
	public String toString() {
		return "CrollVo [number=" + number + ", source=" + source + ", typeOfFile=" + typeOfFile + "]";
	}
	
}
