package function;

import fitnesse.responders.run.SuiteResponder;
import fitnesse.wiki.*;
import java.text.MessageFormat;

public class FitnessExample {

    public String testableHtml(PageData pageData, boolean includeSuiteSetup) throws Exception {
        return new TestableHtmlBuilder(pageData, includeSuiteSetup).surround();
    }

    private class TestableHtmlBuilder {

        private final PageData pageData;
        private final boolean includeSuiteSetup;
        private final WikiPage wikiPage;
        private String buffer = "";

        public TestableHtmlBuilder(PageData pageData, boolean includeSuiteSetup) {
            this.pageData = pageData;
            this.includeSuiteSetup = includeSuiteSetup;
            wikiPage = pageData.getWikiPage();
        }

        public String surround() throws Exception {
            if (isTestPage()) {
                includeSetups();
                buffer += pageData.getContent();
                includeTearDowns();
            }

            pageData.setContent(buffer);
            return pageData.getHtml();
        }

        private boolean isTestPage() throws Exception {
            return pageData.hasAttribute("Test");
        }

        private void includeSetups() throws Exception {
            if (includeSuiteSetup) {
                includeInherited(SuiteResponder.SUITE_SETUP_NAME, "setup");
            }
            includeInherited("SetUp", "setup");
        }

        private void includeTearDowns() throws Exception {
            includeInherited("TearDown", "teardown");
            if (includeSuiteSetup) {
                includeInherited(SuiteResponder.SUITE_TEARDOWN_NAME, "teardown");
            }
        }

        private void includeInherited(String pageName, String mode) throws Exception {
            WikiPage suiteSetup = PageCrawlerImpl.getInheritedPage(pageName, wikiPage);
            if (suiteSetup != null) {
                includePage(mode, suiteSetup);
            }
        }

        private void includePage(String mode, WikiPage suiteSetup) throws Exception {
            WikiPagePath pagePath = wikiPage.getPageCrawler().getFullPath(suiteSetup);
            String pagePathName = PathParser.render(pagePath);
            buffer += MessageFormat.format("!include -{0} .{1}\n", mode, pagePathName);
        }
    }
}
