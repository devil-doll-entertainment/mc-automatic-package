# Command surface for MC-AutomaticPackage

default:
    @just --list

# Bootstrap toolchain and configure local git hooks
install:
    ./gradlew --version
    @git config core.hooksPath .githooks 2>/dev/null || true
    @chmod +x .githooks/pre-commit 2>/dev/null || true
    @echo "Environment and git hooks initialized."

# Run local development client (Fabric)
dev:
    ./gradlew :fabric:runClient

# Run local development client (NeoForge)
dev-neoforge:
    ./gradlew :neoforge:runClient

# Build production artifacts (Fabric and NeoForge JARs)
build:
    ./gradlew build -x test

# Execute automated test suite
test:
    ./gradlew test

# Verify type safety and compiler correctness across all modules
typecheck:
    ./gradlew compileJava compileTestJava

# Run static analysis and formatting verification
lint:
    ./gradlew spotlessCheck

# Apply code formatting automatically
format:
    ./gradlew spotlessApply

# Run full quality gate (format, lint, typecheck, test, build)
check: format lint typecheck test build
    @echo "All quality gates passed successfully!"

# Clean build artifacts and caches
clean:
    ./gradlew clean
