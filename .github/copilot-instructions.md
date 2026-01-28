# GitHub Copilot Instructions for CarBeeper

## Project Overview

CarBeeper is a Java Swing application that simulates a car key fob with various buttons controlling car functions. The application demonstrates complex button interaction patterns including single clicks, double clicks, click-and-hold, and double-click-and-hold functionality.

### Key Features
- Power button (on/off toggle)
- Trunk button (open/close toggle)
- Alarm button (on/off toggle)
- Lock button (single click for driver door, double click for all doors)
- Windows button (single/double click and hold for incremental control)
- Flat tire button (random simulation based on button clicks)

## Technology Stack

- **Language**: Java 25
- **Build Tool**: Maven
- **GUI Framework**: Swing
- **Logging**: Apache Log4j2
- **Testing**: JUnit Jupiter 5, Mockito
- **Parent POM**: com.skvllprodvctions:parent:1.0.0

## Code Style and Conventions

### General Guidelines
- Follow existing code style and formatting conventions
- Use meaningful variable and method names that reflect their purpose
- Maintain the current indentation style (spaces, not tabs)
- Keep methods focused and concise
- Use existing logging patterns with Log4j2 instead of System.out

### Java Conventions
- Use `@Serial` annotation for serialVersionUID fields
- Follow JavaDoc conventions for class and method documentation
- Include `@version since X.X` tags in class-level JavaDocs
- Use enums (like `State`) for state management
- Leverage static imports for constants (e.g., `import static carbeeper.Constants.*`)

### State Management
- Use the `State` enum for all state representations
- States include: ON/OFF, OPEN/CLOSED, LOCKED/UNLOCKED, UP/DOWN, INFLATED/FLAT
- Always use state getters/setters rather than direct field access when appropriate

### Button Handlers
- Complex button logic should be extracted into separate handler classes (see `WindowButtonHandler`)
- Use `ButtonClicked` enum to represent button types
- Maintain consistent patterns for single click, double click, and hold behaviors

## Testing Requirements

### Test Structure
- Extend `BaseCarBeeperTest` when you need to create mock MouseEvents for testing button interactions
  - `BaseCarBeeperTest` provides a `createMouseEvent()` helper method
  - Do not extend it for tests that don't require mouse event simulation
- Use JUnit 5 annotations: `@Test`, `@BeforeEach`, `@DisplayName`
- Use Mockito for mocking when necessary
- Test file location: `src/test/java/carbeeper/`

### Test Coverage
- Test default states on initialization
- Test state transitions for each button
- Test complex interactions (double-click, click-and-hold)
- Verify button handler logic independently
- Use descriptive test names that explain what is being tested

### Test Best Practices
- Initialize mocks with `MockitoAnnotations.openMocks(this)` which returns an AutoCloseable that should be closed
  - The newer `openMocks()` method provides better resource management than the deprecated `initMocks()`
  - Example with proper resource management:
    ```java
    private AutoCloseable mocks;
    
    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        beeper = new CarBeeper();
    }
    
    @AfterEach
    void tearDown() throws Exception {
        if (mocks != null) {
            mocks.close();
        }
    }
    ```
  - Note: Existing tests use deprecated `initMocks()` method and don't close resources
- Create new CarBeeper instance in `@BeforeEach` setup
- Use assertions that provide clear failure messages
- Test both positive and negative cases

## Build and Development Workflow

### Maven Commands
- **Build**: `mvn clean install`
- **Test**: `mvn test`
- **Skip Tests**: `mvn install -DskipTests -DskipITs`
- **Package**: `mvn package` (creates jar with dependencies)
- **Run**: `java -jar dist/<version>/CarBeeper-<version>.jar`

### Project Structure
```
src/
├── main/
│   ├── java/carbeeper/
│   │   ├── Main.java              # Application entry point
│   │   ├── CarBeeper.java         # Main GUI and logic
│   │   ├── ButtonClicked.java     # Button type enum
│   │   ├── State.java             # State enum
│   │   ├── Constants.java         # Application constants
│   │   └── WindowButtonHandler.java # Complex window logic
│   └── resources/
│       └── log4j2.xml             # Logging configuration
└── test/
    └── java/carbeeper/
        ├── BaseCarBeeperTest.java # Base test class
        └── CarBeeperTest.java     # Main test class
```

### Output Directory
- Built artifacts go to: `dist/<version>/`
- JAR naming: `CarBeeper-<version>.jar`

## Logging

### Log4j2 Usage
- Use class-specific loggers: `private static final Logger LOGGER = LogManager.getLogger(ClassName.class);`
- Log levels:
  - `LOGGER.info()` - Application flow and state changes
  - `LOGGER.error()` - Errors and exceptions
  - `LOGGER.debug()` - Detailed debugging information
- Always log exceptions with context: `LOGGER.error("Could not create Car Beeper because {}", e.getMessage())`

### Logging Best Practices
- Log method entry/exit for complex operations
- Log state changes for debugging
- Never use `System.out.println()` or `System.err.println()`
- Keep log messages concise but informative

## Dependencies

### Production Dependencies
- `log4j-core` - Logging implementation
- `log4j-api` - Logging API

### Test Dependencies
- `junit-jupiter-api` - Testing framework
- `mockito-core` - Mocking framework

### Adding Dependencies
- All dependency versions are managed in the parent POM
- Do not specify versions in this project's pom.xml
- Consult parent POM dependency management section before adding new dependencies

## GUI Development

### Swing Components
- Main frame extends `JFrame`
- Use `GridBagLayout` and `GridBagConstraints` for layout
- Button images stored in `images/` directory
- Text area with `DefaultCaret` for auto-scrolling

### Button Functionality
- Use `MouseListener` for complex click patterns
- Use `Timer` for click-and-hold incremental updates
- Maintain consistent button state feedback in text area
- Clear button functionality should preserve application state

## Version History and Documentation

### Version Updates
- Update version in `pom.xml`
- Update `@version since` tags in JavaDocs
- Document changes in Main.java's extensive history section
- Maintain backwards compatibility when possible

### Comments
- Use comments sparingly - prefer self-documenting code
- Document complex algorithms and non-obvious logic
- Include purpose and algorithm in class-level JavaDocs
- Historical notes should remain in Main.java

## Common Patterns

### State Flipping
```java
// Good: Using state enum
powerState = (powerState == State.ON) ? State.OFF : State.ON;

// Bad: Using booleans or integers
carOn = !carOn;
```

### Button Click Handling
```java
// Single click
if (e.getClickCount() == 1) {
    // Handle single click
}

// Double click
if (e.getClickCount() == 2) {
    // Handle double click
}
```

### Logging Pattern
```java
LOGGER.info("Action performed: {}", actionDescription);
```

## Special Considerations

### Random Number Generation
- Flat tire functionality uses `Random` class
- Random numbers determine tire state changes based on button click count
- Logic: Tire becomes flat when `buttonClicks == randomNumber` or when `randomNumber > 10 && buttonClicks == randomNumber / 10` (integer division)
  - Example: If randomNumber is 50, a tire becomes flat when buttonClicks equals 50 or when buttonClicks equals 5
- Different random number ranges (0-24, 25-49, 50-74, 75-100) determine which tire becomes flat

### Window Handler
- `WindowButtonHandler` is a complex class handling all window button scenarios
- Uses Timer for incremental window movement
- Maintains separate state for each window
- Handles single/double click and click-and-hold patterns

### Thread Safety
- Use `SwingUtilities.invokeLater()` for GUI updates
- Ensure proper event dispatch thread usage
- Timer operations should be thread-safe

## When Making Changes

1. **Understand the context**: Review related code and existing patterns
2. **Maintain consistency**: Follow existing code style and patterns
3. **Test thoroughly**: Add/update tests for any logic changes
4. **Log appropriately**: Add logging for new functionality
5. **Update documentation**: Update JavaDocs and version tags
6. **Build and verify**: Run `mvn clean install` before committing
7. **Check logs**: Ensure logging works correctly with changes

## Error Handling

- Log all exceptions with context
- Provide user feedback through the text area when appropriate
- Use try-catch blocks for operations that may fail
- Don't swallow exceptions - always log them

## Code Quality

- Keep methods under 50 lines when possible
- Avoid deep nesting (max 3-4 levels)
- Extract complex logic into separate methods or classes
- Use meaningful variable names (avoid single letters except in loops)
- Follow DRY principle (Don't Repeat Yourself)
