# CI/CD

This project uses GitHub Actions to automate build validation, quality checks, artifact generation, and beta distribution.

The goal is to ensure code quality, reduce manual release effort, and provide a reliable delivery pipeline.

---

# Continuous Integration (CI)

The Android CI workflow runs automatically on:

* Pull Requests
* Pushes to `main`
* Pushes to `release/*` branches
* Version tags

## CI Validation Steps

### Build Verification

The workflow validates that the application can be successfully compiled.

```text
assembleDebug
```

### Unit Testing

All unit tests are executed automatically.

```text
test
```

### Static Analysis

Android Lint is executed to identify potential issues and code quality violations.

```text
lint
```

### Artifact Collection

Build outputs and reports are uploaded as workflow artifacts for inspection.

Artifacts include:

* Build outputs
* Test reports
* Lint reports

---

# Continuous Delivery (CD)

The Firebase Distribution workflow is triggered when a version tag is pushed.

Example:

```bash
git tag v1.0.0
git push origin v1.0.0
```

---

# Release Pipeline

Version Tag

↓

Build Signed Beta APK

↓

Generate Artifacts

↓

Upload to Firebase App Distribution

↓

Available for Testers

---

# Release Signing

Release builds are signed automatically using GitHub Secrets.

Sensitive credentials are never stored in the repository.

Secrets include:

* Release Keystore
* Store Password
* Key Alias
* Key Password

---

# Firebase App Distribution

Beta releases are automatically distributed through Firebase App Distribution.

Benefits:

* Fast tester onboarding
* Controlled beta releases
* Version tracking
* Simplified release management

---

# Security

The CI/CD pipeline uses GitHub Secrets for managing sensitive configuration.

Examples:

* Firebase Service Account
* Release Keystore
* Signing Credentials
* Google Services Configuration

No secrets are committed to source control.

---

# Benefits

The automated CI/CD pipeline provides:

* Consistent builds
* Automated quality checks
* Reliable release process
* Reduced manual effort
* Faster feedback cycles
* Improved developer productivity
w