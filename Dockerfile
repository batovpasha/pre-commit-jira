FROM clojure:lein

WORKDIR /usr/src/app

# Target repo is mounted inside the container under /src directory. All files and folders
# have the host user as user owner and "dialout" group as group owner, not the host one
# because it can't find the host group inside the container.
# It causes the "fatal: detected dubious ownership in repository at '/src'" - user owner matches, but
# group owner does not. The following command is a workaround to fix this issue.
RUN git config --system --add safe.directory /src

COPY project.clj project.clj
RUN lein deps

COPY src/ src/
RUN mv "$(lein uberjar | sed -n 's/^Created \(.*standalone\.jar\)/\1/p')" app-standalone.jar
