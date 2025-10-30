package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ArticlePage {

    private final WebDriver driver;

    // Headline
    @FindBy(css = "h1")
    private WebElement h1Headline;

    // Publish/Updated date
    @FindBy(css = "time, .article__date, .post__meta time, .article-date, [itemprop='datePublished']")
    private WebElement dateElement;

    // Author or Section
    @FindBy(css = ".author, .post__author, [rel='author'], .article__author, .post__meta a[href*='author'], .article__section, .breadcrumbs a")
    private List<WebElement> authorOrSectionCandidates;

    // Article body paragraphs
    @FindBy(css = "article p, .article__content p, .post__content p, .single-post p")
    private List<WebElement> bodyParagraphs;

    // Article images within content
    @FindBy(css = "article img, .article__content img, .post__content img, .single-post img")
    private List<WebElement> contentImages;

    // Canonical
    @FindBy(css = "link[rel='canonical']")
    private WebElement canonicalLink;

    // Open Graph & Twitter metas
    @FindBy(css = "meta[property='og:title'], meta[property='og:image'], meta[name='twitter:card']")
    private List<WebElement> socialMetas;

    // JSON-LD blocks
    @FindBy(css = "script[type='application/ld+json']")
    private List<WebElement> jsonLdBlocks;

    @FindBy(css = "a[href*='news.google.com']")
    private List<WebElement> googleNewsLinks;

    public ArticlePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public String headlineText() {
        try { return h1Headline.getText().trim(); } catch (Exception e) { return ""; }
    }

    public boolean isHeadlineVisible() {
        try { return h1Headline.isDisplayed() && !headlineText().isEmpty(); } catch (Exception e) { return false; }
    }

    public boolean hasPublishOrUpdatedDate() {
        try {
            return dateElement != null && dateElement.isDisplayed() && !dateElement.getText().trim().isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean hasAuthorOrSection() {
        return authorOrSectionCandidates.stream().anyMatch(el -> {
            try { return el.isDisplayed() && !el.getText().trim().isEmpty(); } catch (Exception ignore) { return false; }
        });
    }

    public boolean hasReadableBody() {
        long count = bodyParagraphs.stream().filter(el -> {
            try {
                String t = el.getText().trim();
                return el.isDisplayed() && t.length() > 60;
            } catch (Exception ignore) { return false; }
        }).count();
        return count >= 3;
    }

    public boolean imagesLoadedWithAlt() {
        long ok = contentImages.stream().filter(img -> {
            try {
                boolean displayed = img.isDisplayed();
                String alt = img.getAttribute("alt");
                Object width = ((JavascriptExecutor)driver).executeScript("return arguments[0].naturalWidth", img);
                boolean loaded = (width instanceof Number) ? ((Number) width).intValue() > 0 : displayed;
                return loaded && alt != null && !alt.trim().isEmpty();
            } catch (Exception ignore) { return false; }
        }).count();
        return ok >= Math.min(2, contentImages.size());
    }

    public boolean hasCanonicalAbsolute() {
        try {
            if (canonicalLink == null) return false;
            String href = canonicalLink.getAttribute("href");
            return href != null && href.startsWith("http");
        } catch (Exception e) { return false; }
    }

    public boolean hasSocialMetas() {
        return socialMetas != null && socialMetas.size() >= 2;
    }

    public boolean hasNewsArticleJsonLd() {
        for (WebElement block : jsonLdBlocks) {
            try {
                String json = block.getAttribute("innerText");
                if (json != null && json.contains("\"@type\"") && json.contains("NewsArticle")) {
                    boolean hasHeadline = json.contains("\"headline\"");
                    boolean hasDatePublished = json.contains("\"datePublished\"");
                    if (hasHeadline && hasDatePublished) return true;
                }
            } catch (Exception ignore) {}
        }
        return false;
    }
    
 // Check if the "Follow us on Google News" link is visible
    public boolean googleNewsFollowVisible() {
        try {
            return googleNewsLinks.stream().anyMatch(WebElement::isDisplayed);
        } catch (Exception e) {
            return false;
        }
    }

    // Get the href value from the first Google News link
    public String googleNewsFollowHref() {
        try {
            return googleNewsLinks.stream()
                    .filter(WebElement::isDisplayed)
                    .map(el -> el.getAttribute("href"))
                    .filter(href -> href != null && !href.isEmpty())
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    // Validate if the href looks like a real Google News URL
    public boolean googleNewsUrlLooksValid(String href) {
        if (href == null) return false;
        href = href.toLowerCase();
        return href.startsWith("https://news.google.com/") &&
               (href.contains("/publications") || href.contains("/topics") || href.contains("/publisher"));
    }

    // Click the follow link and verify it opened a Google News page
 // Locator stays simple
 // @FindBy(css = "a[href*='news.google.com']")
 // private List<WebElement> googleNewsLinks;
 private boolean isNewsOrConsent(String url) {
     if (url == null) return false;
     String u = url.toLowerCase();
     return u.contains("news.google.com") || u.contains("consent.google");
 }

 public boolean openGoogleNewsFollowAndVerify() {
	    try {
	        // 1) find the first visible Google News link
	        WebElement link = null;
	        for (WebElement el : googleNewsLinks) {
	            if (el.isDisplayed()) {
	                link = el;
	                break;
	            }
	        }

	        if (link == null) {
	            System.out.println("No visible Google News link found");
	            return false;
	        }

	        // 2) get href and navigate directly
	        String href = link.getAttribute("href");
	        if (href == null || href.isEmpty()) {
	            System.out.println("Invalid Google News href");
	            return false;
	        }

	        driver.navigate().to(href);

	        // 3) wait for consent or news page
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	        wait.until(d -> d.getCurrentUrl().contains("consent.google.com") ||
	                       d.getCurrentUrl().contains("news.google.com"));

	        String currentUrl = driver.getCurrentUrl().toLowerCase();

	        // 4) If on consent page — click "Accept all"
	        if (currentUrl.contains("consent.google.com")) {
	            try {
	                WebElement acceptBtn = driver.findElement(By.xpath("//button[normalize-space()='Accept all']"));
	                acceptBtn.click();
	                System.out.println("Clicked 'Accept all' on consent page");

	                // Wait for redirect to Google News
	                wait.until(ExpectedConditions.urlContains("news.google.com"));
	            } catch (Exception e) {
	                System.out.println("Could not find 'Accept all' button: " + e.getMessage());
	            }
	        }

	        // 5) verify final page
	        String finalUrl = driver.getCurrentUrl().toLowerCase();
	        System.out.println("Final landed URL: " + finalUrl);

	        return finalUrl.contains("news.google.com");

	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}
}
