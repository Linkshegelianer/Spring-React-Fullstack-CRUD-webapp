run-dist: build test lint

setup:
	gradle wrapper --gradle-version 7.4

build:
	./gradlew clean build

start:
	APP_ENV=development ./gradlew run

start-dist:
	APP_ENV=development ./build/install/app/bin/app

start-production:
	APP_ENV=production ./build/install/app/bin/app

lint:
	./gradlew checkstyleMain checkstyleTest

test:
	./gradlew test

test-report:
	./gradlew jacocoTestReport

migration:
	./gradlew generateMigrations

.PHONY: build