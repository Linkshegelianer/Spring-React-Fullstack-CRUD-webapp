run-dist: build test lint test-report start-dist

build:
	./gradlew clean
	./gradlew installDist

start:
	./gralew bootRun --args'--spring.profiles.active=dev'

start-prod:
	./gralew bootRun --args'--spring.profiles.active=prod'

test:
	./gradlew test

lint:
	./gradlew checkstyleMain checkstyleTest

test-report:
	./gradlew jacocoTestReport

start-dist:
	./build/install/app/bin/app

migration:
	./gradlew diffChangeLog

clean:
	./gradlew clean

.PHONY: build