# SE411 Course Project

This project completes SE411 Lab 04: Maven Intro. It provides a basic JavaFX desktop application and a Maven-generated documentation website.

## Exercise 1: Maven dependencies

The application uses the `psu.se411.courseproject` namespace. Maven downloads JavaFX Controls 23 and its transitive dependencies. The JavaFX Maven plugin 0.0.8 launches `psu.se411.courseproject.Main`, which extends `Application` and displays a welcome window.

## Exercise 2: Maven Site

The POM records the project name, description, URL, Apache License 2.0, organization, and developer. The Site plugin generates this website together with project summary, dependency, plugin, license, and team reports.

## Documentation improvements

Beyond the automatically generated reports, this site includes a getting-started guide, build and launch commands, troubleshooting notes, and an architecture overview. These handwritten Markdown pages remain in source control and are regenerated with the site, avoiding edits to generated HTML.

See [Getting Started](getting-started.html) and [Architecture](architecture.html).
