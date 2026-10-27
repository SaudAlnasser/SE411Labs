# Getting Started

## Requirements

- JDK 21 or newer (JavaFX 23 requires JDK 21 or newer).
- Apache Maven 3.9 or newer.
- A desktop session to display the JavaFX window.

Set `JAVA_HOME` to your JDK directory and put Maven's `bin` directory on `PATH`. Run these commands from the Lab04 directory.

## Build

```text
mvn clean verify
```

## Run the JavaFX application

```text
mvn clean javafx:run
```

A window titled **SE411 Course Project** should display a welcome message. Close it to finish the Maven run.

## Generate the documentation

```text
mvn clean site
```

Open `target/site/index.html` in a browser. The navigation includes custom guides and generated project reports. Because `clean` removes `target`, regenerate the site after any subsequent clean build.

## Troubleshooting

- An unsupported class version error usually means Maven is running an older JDK. Check `mvn -version` and `JAVA_HOME`.
- Maven needs internet access for the first dependency download.
- Run `javafx:run` through Maven so the plugin configures the JavaFX runtime correctly.
- If the window cannot display, use a local desktop session instead of a headless environment.
