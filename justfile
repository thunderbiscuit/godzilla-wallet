@list:
  just --list

clean:
  ./gradlew clean

run:
  ./gradlew run

[group("Ktlint")]
[doc("Run linter and print errors.")]
check:
  ./gradlew ktlintCheck

[group("Ktlint")]
[doc("Run linter and fix errors if possible.")]
format:
  ./gradlew ktlintFormat
