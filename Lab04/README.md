# Lab 04 - Maven Intro

A JavaFX application and Maven documentation site for SE411.

## Exercise 1

- Maven project: `se411-course-project` (display name: SE411 Course Project).
- Namespace: `psu.se411.courseproject`.
- JavaFX Controls 23 and JavaFX Maven plugin 0.0.8.
- `Main` extends `Application`, includes `main`, and displays a welcome window.

## Exercise 2

- Project, license, organization, and developer metadata in `pom.xml`.
- Generated summary, dependency, plugin, license, and team reports.
- Custom overview, getting-started, and architecture pages with site navigation.

## Commands

Requires JDK 21+ and Maven 3.9+. From this folder:

```text
mvn clean verify
mvn clean javafx:run
mvn clean site
```

Close the application window to finish `javafx:run`. Open `target/site/index.html` after generating the site. Generated output is ignored by Git.

The generic project name is used because a course main-project name was not supplied. Developer details follow the existing repository's author information.

## Verification

Verified with JDK 21 and Maven 3.9.9:

- `mvn clean verify site`: BUILD SUCCESS; application compiled and all eight documentation/report pages generated.
- `mvn javafx:run`: BUILD SUCCESS; the SE411 Course Project window opened and was closed normally.
- Generated overview and dependency pages checked for expected project and JavaFX content.

No automated unit tests are included because this lab contains only a basic JavaFX window and Maven configuration.
