package com.jiyoun.test;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
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
	private static Scanner input = new Scanner(System.in);
	private final String gitUrl = "https://github.com";
//	private boolean isOff = false;

	public void startCrolling(String userId) {
		folderFind(findProject(userId));
		input.close();
	}

	protected Elements getRepository(String userId) {
		String tempurl = repositoryUrlMake(userId);
		// tempurl을 쓰는 이유는 해당 프로젝트를 불러오는 url은 단 한번만 사용되기 때문.
		Elements element = selectQuery(tempurl, "div#user-repositories-list");
		return element;
	}
	
	protected String repositoryUrlMake(String userId) {
		return urlAdd(urlAdd(gitUrl, userId),"?tab=repositories");
	}
	
	// 프로젝트 선택화면 불러오기
	public String findProject(String userId) {
		showPrint(getRepository(userId), "li h3");
		String url = urlAdd(gitUrl,userId);

		// 여기서는 임의로 이동하는 경로를 Scanner로 입력을 받는다.
		url = urlAdd(url, input.next());
		return url;
	}

	// elements 불러오기
	public Elements selectQuery(String url, String cssQuery) {
		Document doc = connectJsoup(url);
		Elements element = doc.select(cssQuery);
		return element;
	}
	
	// 조건을 받아 출력하기.
	// 향후 이 부분은 UI로 보내기로 변경한다
	protected void showPrint(Elements element, String condition) {
		for (Element el : element.select(condition)) {
			System.out.println(el.text());
		}
	}

	//폴더 이동
	public String folderFind(String url) {
		Elements element = selectQuery(url,"table.files.js-navigation-container.js-active-navigation-container");
		
		int number = 1;
		List<CrollVo> result = new LinkedList<CrollVo>();

		System.out.println("============================================================");

		for (Element el : element.select("tr.js-navigation-item")) {
			boolean isDirectory = false;
			String source = el.select("td.content").text();
			String type = el.select("td.icon").select("svg").attr("aria-label");

			if (type.equals("directory")) {
				isDirectory = true;
			}

			System.out.println(number + "\t" + source + "\t" + isDirectory);
			result.add(new CrollVo(number++, source, isDirectory));
		}

		System.out.println("============================================================");

		int inputIndex = input.nextInt();
		CrollVo crollResult = result.get(inputIndex - 1);
		if (crollResult.getNumber() == inputIndex) {

			if (crollResult.isDirectory()) {
				String dire = url.split("https://github.com/jeongjiyoun/")[1] + "/";
				String projectName = dire.split("/")[0];
				url = "https://github.com/jeongjiyoun/" + projectName;
				url += "/tree";
				url += "/master/";
				url += dire.split(projectName)[0];
				System.out.println(url);
				url += crollResult.getSource();
				System.out.println(url);
				folderFind(url);
			} else {
				String dire = url.split("https://github.com/jeongjiyoun/")[1];
				System.out.println(dire);
				SourceLink("jeongjiyoun", dire, "master", crollResult.getSource());
			}
		}

		return url;
	}

	// url을 만들어주는 메서드
	protected String urlAdd(String url, String value) {
		url += "/" + value;
		System.out.println(value);
		return url;
	}
	
	public void SourceLink(String userName, String directory, String branchName, String FileName) {
		String url = "https://raw.githubusercontent.com";
		// "https://raw.githubusercontent.com/
		//jeongjiyoun/chieUniversity/master/java/com/university/chie/controller/AdminController.java";
		url = urlAdd(url,userName);
		url = urlAdd(url,directory);
		url = urlAdd(url,branchName);
		url = urlAdd(url,FileName);
		System.out.println(url);
		url = url.split("/tree/")[0] + "/" + url.split("/tree/")[1];
		System.out.println("최종 : " + url);
		source(url);
	}

	//소스 링크를 토대로 소스 긁어오기
	private void source(String url) {
		Document doc = connectJsoup(url);
		// pre 태그의 내용을 긁어온다
		Elements element = doc.select("body");

		System.out.println("============================================================");

		for (Element el : element) { // 하위 뉴스 기사들을 for문 돌면서 출력
			System.out.println(el.wholeText());
		}

		System.out.println("============================================================");

	}

	// 링크로 파싱하여 document로 가져오는 메서드
	private Document connectJsoup(String url) {
		Document doc = null;
		try {
			doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return doc;
	}
}
