package stepdefinitions;

import hooks.Hooks;

import io.cucumber.java.en.*;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.ArticlePage;

import java.time.Duration;

public class ArticleSteps {

	private ArticlePage page;

	private void waitForDomReady() {
		try {
			WebDriverWait wait = new WebDriverWait(Hooks.getDriver(), Duration.ofSeconds(10));
			wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete'"));
		} catch (Exception ignored) {
		}
	}

	// Background
	@Given("I open the configured news article page")
	public void i_open_the_configured_news_article_page() {
		waitForDomReady();
		page = new ArticlePage(Hooks.getDriver());
	}

	// Headline and tab title
	@Then("the article H1 headline should be visible and not empty")
	public void the_article_h1_headline_should_be_visible_and_not_empty() {
		Assertions.assertTrue(page.isHeadlineVisible(), "H1 headline not visible or empty");
	}

	@Then("the browser tab title should loosely match the article headline")
	public void the_browser_tab_title_should_loosely_match_the_article_headline() {
		String h1 = page.headlineText();
		String title = Hooks.getDriver().getTitle();
		Assertions.assertNotNull(h1, "H1 is null");
		Assertions.assertFalse(h1.trim().isEmpty(), "H1 is empty");
		Assertions.assertNotNull(title, "Title is null");
		Assertions.assertFalse(title.trim().isEmpty(), "Title is empty");

		String probe = h1.substring(0, Math.min(12, h1.length())).toLowerCase();
		Assertions.assertTrue(title.toLowerCase().contains(probe),
				"Title does not loosely contain headline. Title=[" + title + "] H1=[" + h1 + "]");
	}

	// Publish date and author/section
	@Then("I should see a visible publish or updated date")
	public void i_should_see_a_visible_publish_or_updated_date() {
		Assertions.assertTrue(page.hasPublishOrUpdatedDate(), "Publish/Updated date not visible");
	}

	@Then("I should see an author name or a section label")
	public void i_should_see_an_author_name_or_a_section_label() {
		Assertions.assertTrue(page.hasAuthorOrSection(), "Author name or Section label not visible");
	}

	// Article body and images
	@Then("the article body should contain multiple readable paragraphs")
	public void the_article_body_should_contain_multiple_readable_paragraphs() {
		Assertions.assertTrue(page.hasReadableBody(), "Body lacks multiple readable paragraphs");
	}

	@Then("article images should be loaded and include non-empty alt text")
	public void article_images_should_be_loaded_and_include_non_empty_alt_text() {
		Assertions.assertTrue(page.imagesLoadedWithAlt(), "Images not loaded or missing alt text");
	}

	// Basic SEO surface tags in DOM
	@Then("a canonical link tag should exist and be absolute")
	public void a_canonical_link_tag_should_exist_and_be_absolute() {
		Assertions.assertTrue(page.hasCanonicalAbsolute(), "Canonical link missing or not absolute");
	}

	@Then("Open Graph and Twitter meta tags should be present")
	public void open_graph_and_twitter_meta_tags_should_be_present() {
		Assertions.assertTrue(page.hasSocialMetas(), "Open Graph / Twitter meta tags missing");
	}

	// Structured data presence for NewsArticle
	@Then("there should be JSON-LD containing \"@type\":\"NewsArticle\"")
	public void there_should_be_json_ld_containing_newsarticle() {
		org.junit.jupiter.api.Assertions.assertTrue(page.hasNewsArticleJsonLd(),
				"Expected NewsArticle JSON-LD with headline and datePublished, but none found.");
	}

	@Then("it should include {string} and {string}")
	public void it_should_include_and(String field1, String field2) {
		org.junit.jupiter.api.Assertions.assertTrue(page.hasNewsArticleJsonLd(),
				"JSON-LD missing required fields '" + field1 + "' and/or '" + field2 + "'.");
	}

	// Google News follow is available and points to a valid Google News URL
	@Then("a {string} control should be visible")
	public void a_named_control_should_be_visible(String controlText) {
		if (controlText.equalsIgnoreCase("Follow us on Google News")) {
			Assertions.assertTrue(page.googleNewsFollowVisible(), "'Follow us on Google News' control not visible");
		}
	}

	@Then("the Google News follow link should point to a valid Google News URL")
	public void the_google_news_follow_link_should_point_to_a_valid_Google_News_URL() {
		String href = page.googleNewsFollowHref();
		Assertions.assertNotNull(href, "Google News follow link missing");
		Assertions.assertTrue(page.googleNewsUrlLooksValid(href), "Invalid Google News URL: " + href);
	}

	@When("I open the Google News follow link")
	public void i_open_the_Google_News_follow_link() {
		org.junit.jupiter.api.Assertions.assertTrue(page.openGoogleNewsFollowAndVerify(),
				"Failed to open Google News follow page");
	}

	@Then("I should land on a Google News page for this publisher")
	public void i_should_land_on_Google_News_page_for_this_publisher() {
		String url = Hooks.getDriver().getCurrentUrl().toLowerCase();
		Assertions.assertTrue(url.contains("news.google.com"), "Not on Google News page. Current URL: " + url);
	}
}
