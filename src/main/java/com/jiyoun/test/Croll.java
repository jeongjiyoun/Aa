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


	//url�쓣 留뚮뱾�뼱二쇰뒗 硫붿꽌�뱶
	private String urlMake(String url, String value) {
		url += "/" + value;
		return url;
	}

	//�봽濡쒖젥�듃 �꽑�깮�솕硫� 遺덈윭�삤湲�
	public String findProject(String url) {
		String tempurl = urlMake(url, "?tab=repositories");
		//tempurl�쓣 �벐�뒗 �씠�쑀�뒗 �빐�떦 �봽濡쒖젥�듃瑜� 遺덈윭�삤�뒗 url�� �떒 �븳踰덈쭔 �궗�슜�릺湲� �븣臾�.
		Elements element = getElement(tempurl, "div#user-repositories-list");
		showPrint(element,"li h3");
		
		//�뿬湲곗꽌�뒗 �엫�쓽濡� �씠�룞�븯�뒗 寃쎈줈瑜� Scanner濡� �엯�젰�쓣 諛쏅뒗�떎.
		url = urlMake(url, input.next());
		return url;
	}


	//elements 遺덈윭�삤湲�
	public Elements getElement(String url, String condition) {
		Document doc = connectJsoup(url);
		Elements element = doc.select(condition);
		return element;
	}


	//議곌굔�쓣 諛쏆븘 異쒕젰�븯湲�.
	//�뼢�썑 �씠 遺�遺꾩� UI濡� 蹂대궡湲곕줈 蹂�寃쏀븳�떎
	private void showPrint(Elements element, String condition) {
		for (Element el : element.select(condition)) {
			System.out.println(el.text());
		}
	}


	private List<CrollVo> showFolderList(Elements element) {
		List<CrollVo> result = new LinkedList<CrollVo>();
		int number = 1;

		System.out.println("============================================================");
		
		for (Element el : element.select("tr.js-navigation-item")) { 
			String source = selectFromElement(el, "td.content");
			boolean isDirectory = isDirectory(selectType(el,"td.icon svg","aria-label"));

			System.out.println(number + "\t"+ source +"\t" + isDirectory);
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
				SourceLink(dire,"master",crollResult.getSource());
			}
		}
		return url;
	}

	public void SourceLink(String directory, String branchName, String FileName) {
		String url = "https://raw.githubusercontent.com/";
		url += "jeongjiyoun";
		url += "/" + directory;
//		url += "/" + branchName;
		url += "/" + FileName;
		source(url);
	}


	private void source(String url) {
		url = url.split("/tree/")[0] + "/" + url.split("/tree/")[1];
		Document doc = connectJsoup(url);
		//pre �깭洹몄쓽 �궡�슜�쓣 湲곸뼱�삩�떎
		Elements element = doc.select("body");

		System.out.println("============================================================");

		for (Element el : element) { // �븯�쐞 �돱�뒪 湲곗궗�뱾�쓣 for臾� �룎硫댁꽌 異쒕젰
			System.out.println(el.wholeText());
		}

	}

	//留곹겕濡� �뙆�떛�븯�뿬 document濡� 媛��졇�삤�뒗 硫붿꽌�뱶
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
