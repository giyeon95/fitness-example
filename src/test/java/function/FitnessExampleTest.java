package function;

import static org.assertj.core.api.Assertions.*;

import fitnesse.wiki.*;
import org.junit.Before;
import org.junit.Test;

public class FitnessExampleTest {
    private PageData pageData;
    private PageCrawler crawler;
    private WikiPage root;
    private WikiPage testPage;

    public static final String EXPECTED_1 = """
        <div class="setup">
        	<div style="float: right;" class="meta"><a href="javascript:expandAll();">Expand All</a> | <a href="javascript:collapseAll();">Collapse All</a></div>
        	<a href="javascript:toggleCollapsable('-3259831513856385699');">
        		<img src="/files/images/collapsableOpen.gif" class="left" id="img-3259831513856385699"/>
        	</a>
        &nbsp;<span class="meta">Set Up: <a href="SuiteSetUp">.SuiteSetUp</a> <a href="SuiteSetUp?edit&amp;redirectToReferer=true&amp;redirectAction=">(edit)</a></span>
        	<div class="collapsable" id="-3259831513856385699">suiteSetUp</div>
        </div>
        <div class="setup">
        	<div style="float: right;" class="meta"><a href="javascript:expandAll();">Expand All</a> | <a href="javascript:collapseAll();">Collapse All</a></div>
        	<a href="javascript:toggleCollapsable('-394350254769825805');">
        		<img src="/files/images/collapsableOpen.gif" class="left" id="img-394350254769825805"/>
        	</a>
        &nbsp;<span class="meta">Set Up: <a href="SetUp">.SetUp</a> <a href="SetUp?edit&amp;redirectToReferer=true&amp;redirectAction=">(edit)</a></span>
        	<div class="collapsable" id="-394350254769825805">setup</div>
        </div>
        <span class="meta">variable defined: TEST_SYSTEM=slim</span><br/>the content!include -teardown <a href="TearDown">.TearDown</a><br/><div class="teardown">
        	<div style="float: right;" class="meta"><a href="javascript:expandAll();">Expand All</a> | <a href="javascript:collapseAll();">Collapse All</a></div>
        	<a href="javascript:toggleCollapsable('5467553944239166707');">
        		<img src="/files/images/collapsableOpen.gif" class="left" id="img5467553944239166707"/>
        	</a>
        &nbsp;<span class="meta">Tear Down: <a href="SuiteTearDown">.SuiteTearDown</a> <a href="SuiteTearDown?edit&amp;redirectToReferer=true&amp;redirectAction=">(edit)</a></span>
        	<div class="collapsable" id="5467553944239166707">suiteTearDown</div>
        </div>
        """;

    public static final String EXPECTED_2 = """
<div class="setup">
	<div style="float: right;" class="meta"><a href="javascript:expandAll();">Expand All</a> | <a href="javascript:collapseAll();">Collapse All</a></div>
	<a href="javascript:toggleCollapsable('1721083259963344123');">
		<img src="/files/images/collapsableOpen.gif" class="left" id="img1721083259963344123"/>
	</a>
&nbsp;<span class="meta">Set Up: <a href="SetUp">.SetUp</a> <a href="SetUp?edit&amp;redirectToReferer=true&amp;redirectAction=">(edit)</a></span>
	<div class="collapsable" id="1721083259963344123">setup</div>
</div>
<div class="setup">
	<div style="float: right;" class="meta"><a href="javascript:expandAll();">Expand All</a> | <a href="javascript:collapseAll();">Collapse All</a></div>
	<a href="javascript:toggleCollapsable('8151900119707213899');">
		<img src="/files/images/collapsableOpen.gif" class="left" id="img8151900119707213899"/>
	</a>
&nbsp;<span class="meta">Set Up: <a href="SuiteSetUp">.SuiteSetUp</a> <a href="SuiteSetUp?edit&amp;redirectToReferer=true&amp;redirectAction=">(edit)</a></span>
	<div class="collapsable" id="8151900119707213899">suiteSetUp</div>
</div>
<div class="setup">
	<div style="float: right;" class="meta"><a href="javascript:expandAll();">Expand All</a> | <a href="javascript:collapseAll();">Collapse All</a></div>
	<a href="javascript:toggleCollapsable('6400916735671284319');">
		<img src="/files/images/collapsableOpen.gif" class="left" id="img6400916735671284319"/>
	</a>
&nbsp;<span class="meta">Set Up: <a href="SetUp">.SetUp</a> <a href="SetUp?edit&amp;redirectToReferer=true&amp;redirectAction=">(edit)</a></span>
	<div class="collapsable" id="6400916735671284319">setup</div>
</div>
<span class="meta">variable defined: TEST_SYSTEM=slim</span><br/>the content!include -teardown <a href="TearDown">.TearDown</a><br/><div class="teardown">
	<div style="float: right;" class="meta"><a href="javascript:expandAll();">Expand All</a> | <a href="javascript:collapseAll();">Collapse All</a></div>
	<a href="javascript:toggleCollapsable('705450822030738256');">
		<img src="/files/images/collapsableOpen.gif" class="left" id="img705450822030738256"/>
	</a>
&nbsp;<span class="meta">Tear Down: <a href="SuiteTearDown">.SuiteTearDown</a> <a href="SuiteTearDown?edit&amp;redirectToReferer=true&amp;redirectAction=">(edit)</a></span>
	<div class="collapsable" id="705450822030738256">suiteTearDown</div>
</div>
<div class="teardown">
	<div style="float: right;" class="meta"><a href="javascript:expandAll();">Expand All</a> | <a href="javascript:collapseAll();">Collapse All</a></div>
	<a href="javascript:toggleCollapsable('-5227951482338801748');">
		<img src="/files/images/collapsableOpen.gif" class="left" id="img-5227951482338801748"/>
	</a>
&nbsp;<span class="meta">Tear Down: <a href="TearDown">.TearDown</a> <a href="TearDown?edit&amp;redirectToReferer=true&amp;redirectAction=">(edit)</a></span>
	<div class="collapsable" id="-5227951482338801748">teardown</div>
</div>
        """;

    @Before
    public void setUp() throws Exception {
        root = InMemoryPage.makeRoot("RooT");
        crawler = root.getPageCrawler();
        testPage = addPage("TestPage", "!define TEST_SYSTEM {slim}\n" + "the content");
        addPage("SetUp", "setup");
        addPage("TearDown", "teardown");
        addPage("SuiteSetUp", "suiteSetUp");
        addPage("SuiteTearDown", "suiteTearDown");

        crawler.addPage(testPage, PathParser.parse("ScenarioLibrary"), "scenario library 2");

        pageData = testPage.getData();
    }

    private WikiPage addPage(String pageName, String content) throws Exception {
        return crawler.addPage(root, PathParser.parse(pageName), content);
    }

    private String removeMagicNumber(String expectedResult) {
        return expectedResult.replaceAll("[-]*\\d+", "");
    }

    @Test
    public void testableHtml() throws Exception {
        String testableHtml = new FitnessExample().testableHtml(pageData, true);
        String testableHtml2 = new FitnessExample().testableHtml(pageData, false);

        assertThat(removeMagicNumber(testableHtml)).isEqualTo(removeMagicNumber(EXPECTED_1));
        assertThat(removeMagicNumber(testableHtml2)).isEqualTo(removeMagicNumber(EXPECTED_2));
    }
}
