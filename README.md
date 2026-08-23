# Artemis Financial
CS305 - Software Security, SNHU, 2026
### A pure Java web app that features a secure hashing tool. Takes an arbitrary string, passes it through SHA-256 and displays a hex-encoded hash.

<br><br>
<div align="center">
  
[![View Live Demo](https://img.shields.io/badge/View%20Live%20Demo-4f46e5?style=for-the-badge&logo=springboot&logoColor=white)](https://erikhays.dev/artemis-financial)

</div>
<br><br>

## Scenario

You are designing a new feature for an established fintech company, Artemis Financial, who wants to integrate a checksum verification within their existing application. Given a small codebase to start from, you are to implement the new feature while minimizing any potential software security vulnerabilities. 

## Approach

A defense-in-depth approach was used in development so that each layer of the architecture was secured. The main vulnerabilities were first identified.

| # | Layer | Vulnerability | Fix |
|---|-------|---------------|-----|
| 1 | Cryptography | No TLS encryption | Enabled TLS with a self-signed certificate and forced HTTP traffic to redirect to HTTPS |
| 2 | Input Validation | Public API routes had no validation checks | Added null checks and a max-length constraint |
| 3 | Error Handling | Generic error messages could potentially leak implementation details | Custom exception classes and a 'GlobalExceptionHandler' were added |
| 4 | Data Access | Improper encapsulation publicly exposes data members | All class data and methods are modified 'PRIVATE' excluding public API endpoints |
| 5 | Supply Chain | Third-party libraries and tools can introduce vulnerabilities | OWASP Dependency-Check was used to identify vulnerable packages |
| 6 | API | Exposed endpoints present an attack vector for RCE and DoS | HTTP-method specific annotation and validation added |
| 7 | Code Quality/Syntax | Can introduce bugs or unintended behavior | Cleaned up conventions through the use of a linter |

## Architecture

```
artemis-financial/
├── src
│   ├── config/
│   │   └── HttpsRedirectConfig.java     # Forces HTTP → HTTPS redirect
│   │
│   └── sslserver/
│       ├── SslServerApplication.java    # Spring Boot entry point
│       ├── HashController.java          # Routes: GET / , POST /hash — input validation + SHA-256 hashing
│       ├── GlobalExceptionHandler.java  # Catches all exceptions, logs server-side, shows generic message to user
│       ├── HashingException.java        # Custom exception for hashing failures
│       └── TestRouteException.java      # Custom exception used by /test-error to verify the handler
│
└── src/main/resources/
    ├── application.properties       # Local config — HTTPS on :8443, self-signed keystore
    ├── keystore.p12                 # Self-signed TLS certificate/keystore
    └── templates/
        └── index.html               # Thymeleaf UI — hash form + result display


```

## Takeaways

Developing software applications that are secure to threats from the outside world has become an increasingly complex and crucial facet of the software development lifecycle (SDLC). Imagine building a piece of software as if it were a physical object like an office building. You would never open the building to the public without first ensuring that every door had a lock on it and only the people with authorized access had a way to get in. To make things worse, the cybersecurity landscape is evolving faster than ever. Because technology has become so ubiquitous in modern society, more people than ever are highly motivated to exploit the software that underlies it. As each member on a software development team contributes individually to different components of the finished product, it is the role of the developer to take ownership and accountability of every aspect of the software’s security holistically. 

