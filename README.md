🧪 NewsPage QA Automation

This project automates UI testing for news article pages. It verifies that key elements—such as the article headline, publish date, author name, images, and links—are displayed correctly


🧰 Tech Stack

<img width="797" height="451" alt="image" src="https://github.com/user-attachments/assets/f91d10ae-419c-4f7b-8c14-1fe766d62a63" />


📁 Project Structure

<img width="801" height="452" alt="image" src="https://github.com/user-attachments/assets/ea433238-5407-42ff-b196-f84c1fdd6746" />


🚀 Run Tests Locally

#Headed (browser visible):
  mvn clean test -DHEADLESS=false

#Headless (faster, used in CI):
  mvn clean test -DHEADLESS=true


#The test report will be generated at:
  target/cucumber-report.html

☁️ GitHub Actions (CI)

#Tests run automatically in headless mode on every push.

  Workflow file: .github/workflows/tests.yml



