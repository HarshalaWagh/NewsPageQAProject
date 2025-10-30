@Sanity @HighPriority
Feature: CryptoNews article - high priority functional checks
  This ensures the Virginia Senate crypto commission article renders key content,
  basic SEO surface (DOM), and essential UX elements correctly.

  Background: 
    Given I open the configured news article page

  Scenario: Headline and tab title
    Then the article H1 headline should be visible and not empty
    And the browser tab title should loosely match the article headline

  Scenario: Publish date and author/section
    Then I should see a visible publish or updated date
    And I should see an author name or a section label

  Scenario: Article body and images
    Then the article body should contain multiple readable paragraphs
    And article images should be loaded and include non-empty alt text

  Scenario: Basic SEO surface tags in DOM
    Then a canonical link tag should exist and be absolute
    And Open Graph and Twitter meta tags should be present

  Scenario: Structured data presence for NewsArticle
    Then there should be JSON-LD containing "@type":"NewsArticle"
    And it should include "headline" and "datePublished"

  Scenario: Google News follow is available and points to a valid Google News URL
    Then a "Follow us on Google News" control should be visible
    And the Google News follow link should point to a valid Google News URL
    When I open the Google News follow link
    Then I should land on a Google News page for this publisher
