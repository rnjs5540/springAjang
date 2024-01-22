# 베이스 이미지 선택
FROM openjdk:17-alpine

# 포트 지정
EXPOSE 8080

# JAR 파일 복사
ARG JAR_FILE=library-app-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} springAjang.jar

# 컨테이너 실행시 실행될 명령어
ENTRYPOINT ["java","-jar","/springAjang.jar"]
#"-Dspring.profiles.active=prod",
