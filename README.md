# Snapshot Testing Example

This is a small example project used to demonstrate using snapshot (aka approval) testing and code/mutation coverage as a tool for changing mission-critical software following the _Law of System Behaviour_:

>Given sufficient time and usage,  
>all observable system behaviour,  
>intentional or unintentional,  
>will become depended upon.

See https://jasondecarvalho.substack.com/p/snapshot-testing for my article.

## Tools

This project uses 

- [http4k](https://www.http4k.org/) - a functional toolkit for Kotlin HTTP applications
- [PIT](https://pitest.org/) for mutation testing
- [Arcmutate](https://docs.arcmutate.com/docs/kotlin.html) for improved Kotlin support for PIT
- [selfie](https://selfie.dev) for snapshot testing

## Usage

- `./gradlew test` to run the tests
- `./gradlew jacocoTestReport` to run JaCoCo. Report: `build/reports/jacoco/index.html`
- `./gradlew pitest` to run PIT. Report: `build/reports/pitest/index.html`
