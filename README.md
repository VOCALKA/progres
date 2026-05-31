# Fitness Workout Planner

A simple and clear desktop Java application designed for creating workout plans and managing the subsequent countdown during exercises. 
The application utilizes the Swing graphical user interface, saves data to text files, and integrates food product information.

## Main Features

*   **Plan Creation (`Plan.java`)**: 
    *   Allows entering the plan name, exercise names, exercise duration, and rest duration.
    *   **Input Protection**: Time input fields strictly accept numbers only.
    *   **List Management**: Quick removal of an unwanted exercise from the list via a **double-click**.
    *   **Empty Data Protection**: The program prevents saving an empty plan without any exercises.
    *   Automatic saving to semicolon-separated text files (CSV style) inside the `resources` folder (including protection against overwriting an existing file by appending a number).

*   **Workout Execution (`RunPlan.java`)**:
    *   Loads a saved plan from the `resources` folder using a file chooser dialog (`JFileChooser`).
    *   **Clear UI**: Large fonts for the countdown timer and clearly color-coded states (**WORK!** / **REST**).
    *   The window is set to `setAlwaysOnTop(true)` so it remains unobstructed by other applications during exercise.
    *   **Safe Closure**: Closing the window with the 'X' button immediately stops the background timer, preventing any unwanted "Workout Finished" messages.

*   **Shared Design (`Custom.java`)**:
    *   Centralized application layout (background colors, button styling, consistent fonts, and text colors for exercises and statistics/streaks).

## Project Structure

```text
├── resources/               # Folder where text plans are saved (.txt)
└── src/
    ├── app/
    │   └── App.java         # Main launcher window of the application
    ├── custom/
    │   └── Custom.java      # Class for unified graphical design
    └── plan/
        ├── Plan.java        # Window for creating and saving plans
        └── RunPlan.java     # Window for the exercise countdown itself
```

## Technologies Used

*   **Java 23** (Swing for the graphical user interface)
*   **Maven** (Dependency management)
*   **OpenFoodFacts Java Wrapper** (0.9.3) – integration of the food database

*   ## How to Run the Application

You can run the compiled application directly from the terminal using the following command:

```bash
java -jar out/artifacts/progres_jar/progres.jar
```

### Prerequisites
* Make sure you have **Java (JDK)** installed on your machine.
* Open your terminal inside the root directory of the project (`progres`) before running the command.
