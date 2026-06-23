# sonar-demo-springboot

A deliberately flawed Spring Boot application used to **demo SonarCloud**. It builds and runs,
but is seeded with a spread of issues across every Sonar category so a scan lights up nicely.

> ⚠️ This code is intentionally insecure and buggy. Do **not** use any of it as a reference for
> real code.

## Run locally

```bash
mvn spring-boot:run
```

Then try:

- `GET http://localhost:8080/users/1`
- `GET http://localhost:8080/users/search?name=ali`
- `GET http://localhost:8080/users/login?username=admin&password=Admin@123`
- `GET http://localhost:8080/users/token?username=alice`

## Push to GitHub

```bash
cd sonar-demo-springboot
git init
git add .
git commit -m "Initial commit: Spring Boot SonarCloud demo"
git branch -M main
git remote add origin git@github.com:<your-user>/sonar-demo-springboot.git
git push -u origin main
```

## Wire up SonarCloud

1. Go to [sonarcloud.io](https://sonarcloud.io) and log in with GitHub.
2. **+ → Analyze new project** and pick this repository.
3. Choose **GitHub Actions** as the analysis method.
4. Add the `SONAR_TOKEN` it gives you as a repo secret
   (**GitHub → Settings → Secrets and variables → Actions → New repository secret**).
5. Edit `.github/workflows/build.yml` and replace `YOUR_PROJECT_KEY` and `YOUR_ORG_KEY`
   with the values SonarCloud shows you.
6. Push (or re-run the workflow) and watch the analysis appear in SonarCloud.

> Prefer not to use Actions? You can run the scanner locally instead:
> ```bash
> mvn verify sonar:sonar \
>   -Dsonar.projectKey=YOUR_PROJECT_KEY \
>   -Dsonar.organization=YOUR_ORG_KEY \
>   -Dsonar.host.url=https://sonarcloud.io \
>   -Dsonar.token=YOUR_SONAR_TOKEN
> ```

## What's seeded in here (so you know what to point at during the demo)

### 🐞 Bugs
- `UserController.getUser` — compares `String` with `==` instead of `.equals()`.
- `UserService.findById` — `list.get(0)` on a possibly-empty list (NPE).
- `UserService.searchByName` — JDBC `Connection`/`Statement`/`ResultSet` never closed (resource leak).
- `StringUtils.describe` — dereferences a possibly-null argument (NPE).

### 🔓 Vulnerabilities
- `UserController.search` / `UserService.searchByName` — SQL injection via string concatenation.
- `UserService.findById` — SQL injection via concatenated `id`.

### 🔥 Security Hotspots
- Hardcoded credentials in `UserController.login`, `UserService`, and `application.properties`.
- Weak hashing with **MD5** in `UserService.hashPassword`.
- Non-cryptographic `java.util.Random` for token generation in `StringUtils.generateToken`.

### 💨 Code Smells
- `User` exposes public mutable fields.
- Empty / swallowing `catch` blocks in `UserService`.
- Duplicated and dead `if` branches in `UserController.login`.
- Unused local variable in `UserService.authenticate`.
- High cognitive complexity in `UserService.classify`.
- `System.out.println` instead of a logger.
- Magic numbers and a public utility-class constructor in `StringUtils`.
