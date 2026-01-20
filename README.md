[日本語はこちら](README_ja.md)

# Spring AI Computer Use Demo

This project is a demo application that uses Gemini’s **Computer Use** feature, implemented in [this pull request](https://github.com/spring-projects/spring-ai/pull/5335) for Spring AI.

It demonstrates how to perform automated browser interactions using Gemini’s [Computer Use capability](https://ai.google.dev/gemini-api/docs/computer-use).

## Example Execution

When you provide a prompt as a command-line argument, the application automatically operates the browser based on the given instruction.
The `GOOGLE_GENAI_PROJECT_ID` environment variable must be set to your GCP project ID.

```shell
./gradlew bootRun --args="Access https://testautomationpractice.blogspot.com, go to page 3 of the Pagination Web Table, and select the two most expensive products."
```
