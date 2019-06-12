package com.jiyoun.test;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Croll {

	// 고쳐야 할 것
	/*
	 * 1. 폴더 상위폴더로 가기 2. 프로젝트 종류 안뜨는 거 수정.
	 */

	//기본적으로 사용하는 상수모음
	protected final String gitUrl = "https://github.com";
	protected final String sourceUrl = "https://raw.githubusercontent.com";

	protected final String url_repository = "?tab=repositories";

	protected final String CSSQuery_repository = "div#user-repositories-list li h3";
	protected final String CSSQuery_folder = "table.files.js-navigation-container.js-active-navigation-container tr.js-navigation-item";

	
	protected static Scanner input = new Scanner(System.in);
	protected static Map<String, String> linkMap;

	
	public Croll() {
		
	}
	
	public Croll(String userId) {
		linkMap = new HashMap<String, String>(); // linkMap을 생성한다.
		linkMap.put("userId", userId); // userId를 담는다
		linkMap.put("branch", "master"); // branch를 기본으로 저장한다.
	}
	
	// elements 확인
	public Elements getElement(String url, String condition) {
		Document doc = connectJsoup(url);
		Elements element = doc.select(condition);
		return element;
	}

	// url을 만들어냄.
	protected String url_FolderMake() {
		String url = gitUrl + "/" + linkMap.get("userId") + "/" + linkMap.get("repository") + "/tree/" + linkMap.get("branch");
		url += getLinks();
		System.out.println(url);
		return url;
	}
	
	// 프로젝트 리스트 검색
	public void findProject() {
		Elements element = getRepository(); // getRepository
		showPrint(element);
		linkMap.put("repository", input.next());
	}
	
	// repository검색
	protected Elements getRepository() {
		// tempurl을 쓰는 이유는 해당 프로젝트를 불러오는 url은 단 한번만 사용되기 때문.
		String tempurl = urlAdd(gitUrl, linkMap.get("userId")) + url_repository;
		return selectQuery(tempurl, CSSQuery_repository);
	}

	// branch를 변경하는 경우
	protected void divertBranch() {
		String branch = input.next();
		linkMap.put("branch", branch);
	}

	// Jsoup에 connect한 후, elements 불러오기
	public Elements selectQuery(String url, String cssQuery) {
		Document doc = connectJsoup(url); // connect
		Elements element = doc.select(cssQuery); // cssQuery에 맞춰 가공한 후에 반환함
		return element;
	}

	// 향후 이 부분은 UI로 보내기로 변경한다
	protected void showPrint(Elements element) {
		for (Element el : element) {
			System.out.println(el.text());
		}
	}


	public List<CrollVo> showFolderList() {
		String url = url_FolderMake();
		Elements element = selectQuery(url, CSSQuery_folder);
		List<CrollVo> result = new LinkedList<CrollVo>();
		int number = 1;

		System.out.println("============================================================");

		for (Element el : element) {
			String source = selectFromElement(el, "td.content");
			int directory = typeOfFile(selectType(el, "td.icon svg", "aria-label"));

			System.out.println(number + "\t" + source + "\t" + directory);
			result.add(new CrollVo(number++, source, directory));
		}

		System.out.println("============================================================");
		return result;
	}

	// find Directory
	private int typeOfFile(String type) {
		int result = 0;
		if (type.equals("directory")) {
			result = 1;
		} else if (type.equals("file")) {
			result = 2;
		}
		return result;
	}


	// url을 만들어주는 메서드
	protected String urlAdd(String url, String value) {
		url += "/" + value;
		return url;
	}
	
	//tree구조 탐색한 결과만들기
	protected String getLinks() {
		String url = "";
		for(int k = 0;;k++) {
			String number = "link" + k;
			System.out.println(number);
			
			if (linkMap.get(number) == null) {
				linkMap.put("count", ""+ k);
				break;
			}

			System.out.println(linkMap.get(number));
			url += "/" + linkMap.get("link" + k);
		}
		return url;
	}	
	
	// String by condition
	private String selectFromElement(Element el, String condition) {
		return el.select(condition).text();
	}

	// String by attr
	private String selectType(Element el, String selectCondition1, String selectAttr) {
		return el.select(selectCondition1).attr(selectAttr);
	}

	//Jsoup 연결
	private Document connectJsoup(String url) {
		Document doc = null;
		try {
			doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			e.printStackTrace();
			//후에 메인으로 튕김
		}
		return doc;
	}
}
