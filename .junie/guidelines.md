# Development Guidelines

## Introduction
This document outlines the development guidelines and best practices for this project. Following these guidelines ensures consistency, maintainability, and quality across the codebase.

## Code Style
- Follow the official Kotlin coding conventions
- Use meaningful variable and function names
- Keep functions small and focused on a single responsibility
- Add comments for complex logic, but prefer self-documenting code

## Git Workflow
- Create feature branches from the main branch
- Use descriptive commit messages
- Squash commits before merging to main
- Delete branches after merging

## Testing
- Write unit tests for all new features
- Maintain test coverage above 80%
- Run tests locally before pushing changes

## Documentation
- Update documentation when changing functionality
- Document public APIs with KDoc comments
- Keep README up to date

## Code Review
- All code must be reviewed before merging
- Address all review comments
- Be respectful and constructive in reviews

## Continuous Integration
- All tests must pass in CI before merging
- Fix failing builds immediately
- Monitor performance metrics

## Security
- Never commit sensitive information
- Follow security best practices
- Report security vulnerabilities immediately

## Dependencies
- Keep dependencies up to date
- Minimize external dependencies
- Document reasons for adding new dependencies