package com.jiyoun.test;

import javax.xml.crypto.Data;

public class CommitData {
	
	private Data commitData;
	private String title;
	private String user;
	
	
	public CommitData() {
		super();
	}


	public CommitData(Data commitData, String title, String user) {
		super();
		this.commitData = commitData;
		this.title = title;
		this.user = user;
	}


	public Data getCommitData() {
		return commitData;
	}


	public void setCommitData(Data commitData) {
		this.commitData = commitData;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getUser() {
		return user;
	}


	public void setUser(String user) {
		this.user = user;
	}
	
	

}
