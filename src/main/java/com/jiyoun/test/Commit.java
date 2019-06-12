package com.jiyoun.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Commit extends Croll {
	protected Croll croll;
	private boolean isOff = false;
	
	protected final String CSSQuery_commitList = "ol.commit-group.table-list.table-list-bordered li";
	protected final String CSSQuery_commitName = "a.message.js-navigation-open";
	protected final String CSSQuery_commitAuther = "span.commit-author.user-mention";

	public Commit() {
	}
	
	public Commit(String userId) {		
		croll = new Croll(userId);
		startCrolling();
	}

	public void startCrolling() {
		getMyWholeCommitCount();
		input.close();
	}
	
	private void getCommitSource() {
		//https://github.com/jeongjiyoun/chieUniversity/commit/2d9233a44818c38ec2a940e2cadca34be5484ae9
	}
	
	// 전체 스트림
	private void getCommitList() {
		List<String> projcetList = getProjectList();
		List<String> branchList = new ArrayList<String>();
		for (String project : projcetList) {
			branchList.addAll(getBranchList(project));
		}
	}
	
	//commit으로 가는 Url만들기
	private String getUrl_commits() {
		String url = gitUrl + "/" + linkMap.get("userId") + "/" + linkMap.get("repository") + "/commits/" + linkMap.get("branch");		
		return url;
	}

	//commit Count를 반환한다.
	private Map<Date, Integer> getMyWholeCommitCount() {
		List<String> projcetList = getProjectList();
		Map<Date, Integer> commitDataList = null;
		
		for (String project : projcetList) {
			linkMap.put("repository", project); //linkMap에 적용
			
			for (String branch : getBranchList(project)) {
				linkMap.put("branch", branch); //branch에 적용
				
				String url = getUrl_commits(); //url 만들기
				Elements elements = selectQuery(url, CSSQuery_commitList); //url을 토대로 elements 받아오기
				commitDataList = makeCountCommit(elements);
			}
		}
		return commitDataList;
	}
	
	//일자별 커밋리스트 가져오는 역할
	private Map<Date, Integer> getCommitList(Date commitDate) {
		List<String> projcetList = getProjectList();
		Map<Date, Integer> commitDataList = null;
		
		for (String project : projcetList) {
			linkMap.put("repository", project); //linkMap에 적용
			
			for (String branch : getBranchList(project)) {
				linkMap.put("branch", branch); //branch에 적용
				
				String url = getUrl_commits(); //url 만들기
				Elements elements = selectQuery(url, CSSQuery_commitList); //url을 토대로 elements 받아오기
				commitDataList = makeCountCommit(elements);
			}
		}
		return commitDataList;
	}

	//Map(날짜와 갯수) 채워주는 역할
	private Map<Date, Integer> makeCountCommit(Elements elements) {
		Map<Date, Integer> commitDataList = new HashMap<Date, Integer>();
		for (Element el : elements) {
				Date dateCommit = convertDate(el.select("relative-time").attr("datetime"));
				if (commitDataList.get(dateCommit) == null) {
					commitDataList.put(dateCommit, 1);
				} else {
					commitDataList.put(dateCommit, commitDataList.get(dateCommit) + 1);
				}
		}
		return commitDataList;
	}

	//그 날짜 커밋
	private void findCommitByDay(Elements elements) {
		for (Element el : elements) {
			if (!el.select("span.commit-author.user-mention").text().equals("")) {
				System.out.println(el.select("relative-time").text());
				System.out.println(el.select(CSSQuery_commitName).text());
				System.out.println(el.select(CSSQuery_commitAuther).text());
				System.out.println(el.select("clipboard-copy").attr("value"));
//				if (commitDataList.get(dateCommit) == null) {
//					commitDataList.put(dateCommit, 1);
//				} else {
//					commitDataList.put(dateCommit, commitDataList.get(dateCommit) + 1);
//				}
			}
			break;
		}
	}
	
	private Date convertDate(String date) {
		date = date.replace("T", "_");
		date = date.replace("Z", "");
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
		Date commitDate = null;
		try {
			commitDate = sf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return commitDate;
	}

	private void getWholeCommit() {
		List<String> projcetList = getProjectList();
		for (String project : projcetList) {
			for (String branch : getBranchList(project)) {
				String url = urlAdd(urlAdd(urlAdd(urlAdd(gitUrl, linkMap.get("userId")), project), "commits"),
						branch);
				Elements elements = selectQuery(url,
						"div.commits-listing.commits-listing-padded.js-navigation-container.js-active-navigation-container");
				for (Element el : elements.select("div.table-list-cell")) {
					System.out.println(el.select("a.message.js-navigation-open").text());
					if (el.select("span.commit-author.user-mention").text().equals("")) {
						System.out.println(el.select("a.commit-author.tooltipped.tooltipped-s.user-mention").text());
					} else {
						System.out.println(el.select("span.commit-author.user-mention").text());
					}
					System.out.println(convertDate(el.select("relative-time").text()));
				}
			}
		}
	}

	// branchList 긁어오기
	private List<String> getBranchList(String project) {
		List<String> branchList = new ArrayList<String>();
		String url = urlAdd(urlAdd(urlAdd(gitUrl, linkMap.get("userId")), project), "branches/all");
		Elements elements = selectQuery(url,
				"li.Box-row.d-flex.js-branch-row.flex-items-center.Details.position-relative");
		for (Element el : elements) {
			branchList.add(el
					.select("a.branch-name.css-truncate-target.v-align-baseline.width-fit.mr-2.Details-content--shown")
					.text());
			System.out.println(el
					.select("a.branch-name.css-truncate-target.v-align-baseline.width-fit.mr-2.Details-content--shown")
					.text());
		}
		return branchList;
	}
}
