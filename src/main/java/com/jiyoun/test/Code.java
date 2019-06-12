package com.jiyoun.test;

import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Code extends Croll {
	protected Croll croll;
	private boolean isOff = false;
	
	public Code(String userId) {
		croll = new Croll(userId);
		overallProcess();
	}
	
	protected void overallProcess() {
		while (true) {
			findProject();
			while (true) {
				folderFind();
				if (isOff) {
					input.close();
					return;
				}
			}
		}
	}

	// String by select
	public void folderFind() {
		List<CrollVo> result = showFolderList();
		folderCheck(result);
	}

	//folder에서 선택값이 무슨 종류인지 확인
	private void folderCheck(List<CrollVo> result) {
		int inputIndex = input.nextInt();		
		CrollVo crollResult = result.get(inputIndex - 1);
		
		if (crollResult.getNumber() == inputIndex) {
			if (crollResult.getTypeOfFile() == 1) { // directory일 경우
				int count = Integer.parseInt(linkMap.get("count"));
				linkMap.put("link"+count, crollResult.getSource());
				folderFind();
			} else if (crollResult.getTypeOfFile() == 2) { // file일 경우
				int count = Integer.parseInt(linkMap.get("count"));
				linkMap.put("link"+count, crollResult.getSource());
				SourceLink();
			} else {
				int count = Integer.parseInt(linkMap.get("count"));

				if (count != 0) { //제일 상위 폴더가 아닐 경우
					count--;
				}

				linkMap.put("link"+count, null);
				System.out.println("link"+count);
				folderFind();
			}
		}
	}
	
	//source URL 만들기
	private String url_SourceMake() {	
		String url = sourceUrl + "/" + linkMap.get("userId") + "/" + linkMap.get("repository") + "/" + linkMap.get("branch");
		url += getLinks();
		System.out.println(url);
		return url;
	}
	
	// 소스 링크 만들기
	public void SourceLink() {
		String url = url_SourceMake();
		System.out.println("최종 : " + url);
		getSource(url);
	}

	// 소스 링크를 토대로 소스 긁어오기
	private void getSource(String url) {
		Elements element = selectQuery(url,"body");

		System.out.println("============================================================");
		for (Element el : element) {
			System.out.println(el.wholeText());	// pre 태그의 내용을 긁어온다
		}
	}	

}
