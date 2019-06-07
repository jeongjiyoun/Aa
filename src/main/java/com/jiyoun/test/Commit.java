package com.jiyoun.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.jsoup.nodes.Element;

public class Commit extends Croll{

	
	private static Scanner input = new Scanner(System.in);
	private final String gitUrl = "https://github.com";
//	private boolean isOff = false;

	@Override
	public void startCrolling(String userId) {
		String url = urlAdd(gitUrl, userId);
		getCommitList("master");
		input.close();
	}

	//branch를 변경하는 경우
	private void divertBranch() {
		String branch = input.next();
		getCommitList(branch);
	}
	
	private void getProjectList(String userId) {
		List<String> projectList = new ArrayList<String>();
		for (Element el : getRepository(userId).select("li h3")) {
			System.out.println(el.text());
		}
	}
	

	//commit리스트 긁어오기
	private void getCommitList(String branch) {

		String url = "https://github.com/jeongjiyoun/chieUniversity/commits/" + branch;
		
		
	}
	
	
	
	
	
}
