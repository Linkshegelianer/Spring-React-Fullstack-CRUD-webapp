run-dist: build test lint test-report run

build:
	./gradlew clean
	./gradlew installDist

test:
	./gradlew test

lint:
	./gradlew checkstyleMain checkstyleTest

test-report:
	./gradlew jacocoTestReport

run:
	PROFILE=dev ./build/install/app/bin/app

migration:
	./gradlew diffChangeLog

.PHONY: build