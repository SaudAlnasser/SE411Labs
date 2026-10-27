# Architecture

## Source layout

- `pom.xml`: project metadata, JavaFX dependency, compiler and launch plugins, and site reports.
- `src/main/java/psu/se411/courseproject/Main.java`: JavaFX entry point and window.
- `src/site/site.xml`: site navigation.
- `src/site/markdown/`: maintainable documentation content.
- `target/`: generated build and site output, excluded from Git.

## Application lifecycle

1. `main` calls JavaFX `launch`.
2. JavaFX initializes its runtime and invokes `start` on the application thread.
3. `start` creates a label inside a layout, attaches the scene to the stage, and shows the stage.
4. Closing the last window exits the JavaFX application.

## Maven responsibilities

The compiler plugin compiles Java source for Java 21. JavaFX Controls provides the UI library. The JavaFX plugin launches the application. The Site plugin renders custom Markdown pages, and Project Info Reports supplies metadata and dependency documentation.

This lab is an application scaffold. Course-specific features can be added within the same package structure later.
