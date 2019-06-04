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
	
	private static Scanner input = new Scanner(System.in);
	private final String gitUrl = "https://github.com";	
	private boolean isOff = false;
	
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


	//프로젝트 선택화면 불러오기
	public String findProject(String url) {
		String tempurl = urlMake(url, "?tab=repositories");
		//tempurl을 쓰는 이유는 해당 프로젝트를 불러오는 url은 단 한번만 사용되기 때문.
	
		showPrint(getElement(tempurl, "div#user-repositories-list"),"li h3");
		
		//여기서는 임의로 이동하는 경로를 Scanner로 입력을 받는다.
		url = urlMake(url, input.next());
		return url;
	}
	
	
	//url을 만들어주는 메서드
	private String urlMake(String url, String value) {
		url += "/" + value;
		return url;
	}
	
	//링크로 파싱하여 document로 가져오는 메서드
	private Document connectJsoup(String url) {
		Document doc = null;
		try {
			doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return doc;
	}

	//elements 불러오기
	public Elements getElement(String url, String condition) {
		Document doc = connectJsoup(url);
		Elements element = doc.select(condition);
		return element;
	}
	
	//조건을 받아 출력하기.
	//향후 이 부분은 UI로 보내기로 변경한다
	private void showPrint(Elements element, String condition) {
		for (Element el : element.select(condition)) {
			System.out.println(el.text());
		}
	}

	private void wholeTextPrint(Elements element) {
		for (Element el : element) {
			System.out.println(el.wholeText());
		}
	}

	//조건을 받아 출력하기.
	//향후 이 부분은 UI로 보내기로 변경한다
	private void showPrintAndList(Elements element, String condition, List<CrollVo> list) {
		int number = 1;
		for (Element el : element.select("condition")) {
			boolean isDirectory = false;
			String source = el.select("td.content").text();
			String type = el.select("td.icon").select("svg").attr("aria-label");

			if (type.equals("directory")) {
				isDirectory = true;
			}
			
			System.out.println(number + "\t"+ source +"\t" + isDirectory);
			list.add(new CrollVo(number++, source, isDirectory));
		}
	}
	
	public String folderFind(String url) {
		
		Elements element = 	getElement(url, "table.files.js-navigation-container.js-active-navigation-container");
		List<CrollVo> result = new LinkedList<CrollVo>();
		System.out.println(element);
		System.out.println("============================================================");
		showPrintAndList(element, "tr.js-navigation-item", result);
		
		System.out.println("============================================================");
		
		int inputIndex = input.nextInt();
		CrollVo crollResult = result.get(inputIndex-1);
		if (crollResult.getNumber() == inputIndex) {
			
			if (crollResult.isDirectory()) {
				System.out.println(url);
				String dire = url.split("https://github.com/jeongjiyoun/")[1] + "/";
				String projectName = dire.split("/")[0];
				System.out.println(projectName + ": projectName");
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
				SourceLink(dire, crollResult.getSource());
			}
		}
		
		return url;
	}
	
	public void SourceLink(String directory, String FileName) {
		String url = "https://raw.githubusercontent.com/";
		url += "jeongjiyoun";
		url += "/" + directory;
		url += "/" + FileName;
		source(url);
	}
	
	private void source(String url) {
		url = url.split("/tree/")[0] + "/" + url.split("/tree/")[1];

		Elements element = getElement(url, "body");

		System.out.println("============================================================");

		wholeTextPrint(element);

		System.out.println("============================================================");

	}


}
