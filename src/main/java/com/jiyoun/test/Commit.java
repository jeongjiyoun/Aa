package com.jiyoun.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Commit extends Croll {

	public Commit() {		
	}
	
	public Commit(String userId) {		
	}

	public void startCrolling(String userId) {
		getMyWholeCommit();
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

	private Map<Date, Integer> getMyWholeCommit() {
		List<String> projcetList = getProjectList();
		Map<Date, Integer> commitDataList = new HashMap<Date, Integer>();
		for (String project : projcetList) {
			for (String branch : getBranchList(project)) {
				String url = urlMake(urlMake(urlMake(urlMake(gitUrl, linkMap.get("userId")), project), "commits"),
						branch);
				Elements elements = selectQuery(url,
						"div.commits-listing.commits-listing-padded.js-navigation-container.js-active-navigation-container");
				for (Element el : elements.select("div.table-list-cell")) {
					if (!el.select("span.commit-author.user-mention").text().equals("")) {
						Date dateCommit = convertDate(el.select("relative-time").text());
						System.out.println(el.select("a.message.js-navigation-open").text());
						System.out.println(el.select("span.commit-author.user-mention").text());
						System.out.println(el.select("li.commit.commits-list-item.js-commits-list-item.table-list-item.js-navigation-item.js-details-container.Details.js-socket-channel.js-updatable-content.navigation-focus").attr("data-channel"));
//						if (commitDataList.get(dateCommit) == 0) {
//							commitDataList.put(dateCommit, 1);
//						} else {
//							commitDataList.put(dateCommit, commitDataList.get(dateCommit) + 1);
//						}
					}
				}
			}
		}
		return commitDataList;
	}

	private Date convertDate(String date) {
		date = date.replace(",", "");
		SimpleDateFormat sf = new SimpleDateFormat("MMM d yyyy", Locale.US);
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
				String url = urlMake(urlMake(urlMake(urlMake(gitUrl, linkMap.get("userId")), project), "commits"),
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

//	private getCommitList

	private List<String> getProjectList() {
		List<String> projectList = new ArrayList<String>();
		for (Element el : getRepository().select("li h3")) {
			System.out.println(el.text());
			projectList.add(el.text());
		}
		return projectList;
	}

	// branchList 긁어오기
	private List<String> getBranchList(String project) {
		List<String> branchList = new ArrayList<String>();
		String url = urlMake(urlMake(urlMake(gitUrl, linkMap.get("userId")), project), "branches/all");
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
