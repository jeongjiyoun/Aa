# Jsoup-Crawling
CrawlingTest

Jsoup is java library for crawling.
This is sources for crawling using Jsoup.

The example shows a way to crawl a github site.

But Please you should be careful to use it.
Before you parsing the page, you must check the site(which you want to parsing) permits pasrsing.


Here is the way to check whether the site permits parsing.

1. you go to root page.
 example) When you want to parsing the github, you would go to page
 https://github.com/
 
2. add a robots.txt
 example) Add a 'robots.txt' after root page address.
 https://github.com/robots.txt
 
3. you can find a allowed and disallowed page
