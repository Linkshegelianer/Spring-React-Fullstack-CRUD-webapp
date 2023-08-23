# Render.com docker deployment

FROM gradle:7.6.1-jdk17
COPY . ./app
WORKDIR ./app
RUN gradle stage
ARG PORT
EXPOSE ${PORT}
ENTRYPOINT ["build/install/app/bin/app"]
