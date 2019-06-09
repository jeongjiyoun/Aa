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
	private boolean isOff = false;

	//elements 遺덈윭�삤湲�
	public Elements getElement(String url, String condition) {
		Document doc = connectJsoup(url);
		Elements element = doc.select(condition);
		return element;
	}

	public void startCrolling(String userId) {
		
		String url = urlMake(gitUrl, userId);
		
		while(true) {
			folderFind(findProject(url));
			if (isOff) {
				break;
			}
		}
		input.close();
	}

	//url�쓣 留뚮뱾�뼱二쇰뒗 硫붿꽌�뱶
	private String urlMake(String url, String value) {
		url += "/" + value;
		return url;
	}

	protected Elements getRepository(String userId) {
		String tempurl = repositoryUrlMake(userId);
		// tempurl을 쓰는 이유는 해당 프로젝트를 불러오는 url은 단 한번만 사용되기 때문.
		Elements element = selectQuery(tempurl, "div#user-repositories-list");
		return element;
	}

	//�봽濡쒖젥�듃 �꽑�깮�솕硫� 遺덈윭�삤湲�
	public String findProject(String url) {
		Elements element = getElement(tempurl, "div#user-repositories-list");
		showPrint(element,"li h3");

		url = urlAdd(url, input.next());
		return url;
		
	}

	protected String repositoryUrlMake(String userId) {
		return urlAdd(urlAdd(gitUrl, userId),"?tab=repositories");
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

	public String folderFind(String url) {
		Elements element = selectQuery(url,"table.files.js-navigation-container.js-active-navigation-container");
		
		List<CrollVo> result = new LinkedList<CrollVo>();
		int number = 1;

		System.out.println("============================================================");
		
		for (Element el : element.select("tr.js-navigation-item")) { 
			String source = selectFromElement(el, "td.content");
			boolean isDirectory = isDirectory(selectType(el,"td.icon svg","aria-label"));

			System.out.println(number + "\t"+ source +"\t" + isDirectory);

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
		return result;
	}

	//String by select
	private String selectFromElement(Element el, String condition) {
		return el.select(condition).text();
	}

	//String by select
	private String selectType(Element el, String selectCondition1, String selectAttr) {
		return el.select(selectCondition1).attr(selectAttr);
	}

	//find Directory
	private boolean isDirectory(String type) {
		boolean result = false;
		if (type.equals("directory")) {
			result = true;
		} 
		return result;
	}


	//String by select
	public String folderFind(String url) {
		Elements element = getElement(url, "table.files.js-navigation-container.js-active-navigation-container");
		List<CrollVo> result = showFolderList(element);
		url = folderCheck(result, url);
		return null;
	}
	

	private String folderCheck(List<CrollVo> result, String url) {
		int inputIndex = input.nextInt();
		
		CrollVo crollResult = result.get(inputIndex-1);
		
		if (crollResult.getNumber() == inputIndex) {			
			if (crollResult.isDirectory()) {

				String dire = url.split("https://github.com/jeongjiyoun/")[1] + "/";
				String projectName = dire.split("/")[0];
				url = "https://github.com/jeongjiyoun/" + projectName;
				url += "/tree";
				url += "/master/";
				url += dire.split(projectName)[0];

				url += crollResult.getSource();
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

		for (Element el : element) { // �븯�쐞 �돱�뒪 湲곗궗�뱾�쓣 for臾� �룎硫댁꽌 異쒕젰
			System.out.println(el.wholeText());
		}

	}
}
