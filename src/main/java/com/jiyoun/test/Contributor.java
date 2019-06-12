package com.jiyoun.test;

public class Contributor extends Croll {

	protected Croll croll;
	private boolean isOff = false;
	
	public Contributor(String userId) {
		croll = new Croll(userId);
		init();
	}
	
	protected void init() {
		//프로젝트를 선택하는 과정이 들어가야함
		findProject();
		String url = urlMake_contributor();
		
//		Elements elements = getElement(url,"div#contributors");
//		System.out.println(elements);
//		for (Element element : elements) {
//			System.out.println(element.select("a.text-normal"));
//		}
	}
	
	private String urlMake_contributor() {
		//https://github.com/jeongjiyoun/chieUniversity/graphs/contributors
		String url = gitUrl + "/" + linkMap.get("userId") + "/" + linkMap.get("repository") + "/graphs/contributors" ;
		System.out.println("프로젝트 기여자 Url : " + url);
		return url;
	}
	
}
