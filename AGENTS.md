# AGENTS.md

Codex working guide for this repository. Use this together with any task-specific user instructions.

## Repo Layout

This repo is currently **not** the `backend/` + `frontend/` monorepo described in the draft note.

The structure that exists today is:

- `front/` - Vite + React 19 frontend using npm artifacts (`package-lock.json` is present)
- repo root - Spring Boot backend using Gradle and Java 17

Always run commands from the relevant directory:

- frontend work: `C:\Users\soobi\git\Jamping\front`
- backend work: `C:\Users\soobi\git\Jamping`

Do not assume older project notes are accurate if they conflict with the current filesystem.

## Core Working Style

### 1. Think Before Coding

- State assumptions explicitly when they affect the implementation.
- If the request is ambiguous in a meaningful way, pause and clarify instead of silently choosing.
- Surface simpler approaches when they exist.
- Push back on unnecessary complexity.

### 2. Simplicity First

- Write the minimum code that solves the requested problem.
- Do not add speculative abstractions, options, or future-proofing.
- Do not add error handling for unrealistic scenarios.
- If the solution feels bigger than the problem, simplify it.

### 3. Surgical Changes

- Touch only files and lines needed for the task.
- Match local style and patterns.
- Do not refactor adjacent code unless the task requires it.
- Remove only the unused code created by your own changes.
- If you notice unrelated issues, mention them separately instead of cleaning them up.

### 4. Goal-Driven Execution

Turn each task into a verifiable goal.

Examples:

- bug fix -> reproduce it, fix it, verify the reproduction no longer fails
- validation change -> add or update tests for invalid input, then make them pass
- refactor -> verify behavior before and after

For multi-step tasks, use a short plan with a verification target for each step.

## Project-Specific Rules

### Frontend

- Work from `front/`.
- Current toolchain is Vite + React 19, not Next.js.
- Prefer the existing package manager artifacts in the repo. Right now that means npm is the safest default because `package-lock.json` is committed and `pnpm-lock.yaml` is not present.
- Before adding new patterns or libraries, check what is already used in `front/src`.

### Backend

- Work from the repo root.
- Current backend is Spring Boot + Gradle on Java 17
- Check `src/main/resources/application.properties` and `application-local.properties` before assuming runtime configuration.
- Prefer small controller/service/repository changes that match the existing package structure.

## First-Run Setup

When starting fresh, help the user set up local configuration before coding if the project cannot run without it.

### Frontend setup

Run from `front/`.

Suggested checks:

1. install dependencies with `npm install` if `node_modules` is missing or stale
2. confirm whether a frontend `.env` file is needed
3. if env values are required but not documented, ask the user for them and explain where they should go

At the moment, no frontend env usage was found from a quick search of `front/src`, so do not invent a `.env` contract unless the code actually starts requiring one.

### Backend setup

Run from the repo root.

Suggested checks:

1. confirm Java 17 is available
2. confirm MySQL is running locally
3. verify the local datasource settings match the user's machine
4. run the app with Gradle only after local config is ready

Current checked config:

- active profile: `local`
- local datasource points to MySQL on `localhost:3306/jamping`

Important:

- `src/main/resources/application-local.properties` currently contains datasource and OAuth credentials directly in the repository.
- Treat these as local/project config that may need to be replaced on another machine.
- If setup fails because these values are invalid for the user, guide them to provide their own MySQL and OAuth values instead of hardcoding new secrets into tracked files.

## When User Input Is Required

Guide the user when setup depends on values that cannot be inferred locally.

Typical examples:

- database host, port, username, password
- OAuth client IDs and secrets
- redirect URLs for local login flows
- any frontend API base URL if the frontend later adds env-based configuration

When asking, be specific about:

- which value is missing
- which file it belongs in
- an example format

## Verification Expectations

Before finishing a task:

- run the smallest relevant verification you can
- prefer targeted checks over broad ones
- if you could not verify, say exactly what was not run and why

Good defaults:

- frontend UI/code changes: targeted build or relevant local check from `front/`
- backend logic changes: targeted test or app startup check from repo root

## Safety Notes

- Do not overwrite user changes outside the task.
- Do not migrate toolchains in documentation or code unless the user explicitly asks.
- If a note in the prompt conflicts with the real repo, trust the real repo and call out the mismatch clearly.
