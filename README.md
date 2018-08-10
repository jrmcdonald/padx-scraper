docker login
mvn package
mvn dockerfile:build
mvn dockerfile:tag@tag-version
mvn dockerfile:push@push-latest
mvn dockerfile:push@push-version