FROM clojure:lein

WORKDIR /usr/src/app

COPY project.clj project.clj
RUN lein deps

COPY src/ src/
# COPY ./commit-msg.txt commit-msg.txt

# RUN mv "$(lein uberjar | sed -n 's/^Created \(.*standalone\.jar\)/\1/p')" app-standalone.jar
RUN lein with-profile uberjar uberjar

# CMD ["java", "-jar", "app-standalone.jar", "commit-msg.txt"]