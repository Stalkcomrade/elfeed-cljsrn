FROM clojure:onbuild

WORKDIR /tmp

ENV WATCHMAN_VERSION=4.5.0

RUN apt-get update && apt-get install -y autogen autoconf make gcc python-dev

RUN wget --quiet https://github.com/facebook/watchman/archive/v$WATCHMAN_VERSION.tar.gz -O /tmp/watchman-v$WATCHMAN_VERSION.tar.gz \
    && tar -C /tmp/ -zxf watchman-v$WATCHMAN_VERSION.tar.gz \
    && cd /tmp/watchman-4.5.0/ && ./autogen.sh && ./configure && make && make install

ENV PATH $PATH:node_modules/.bin

RUN wget -qO- https://deb.nodesource.com/setup_4.x | bash -
RUN apt-get install -y nodejs
RUN npm install -g react-native-cli re-natal

# Install 32bit support for Android SDK
RUN dpkg --add-architecture i386 && \
    apt-get update -q && \
    apt-get install -qy --no-install-recommends libstdc++6:i386 libgcc1:i386 zlib1g:i386 libncurses5:i386


RUN mkdir -p /usr/local/android-sdk-linux && \
    wget https://dl.google.com/android/repository/sdk-tools-linux-4333796.zip -O tools.zip && \
    unzip tools.zip -d /usr/local/android-sdk-linux && \
    rm tools.zip

# Set environment variable
ENV ANDROID_HOME /usr/local/android-sdk-linux
ENV PATH ${ANDROID_HOME}/tools:$ANDROID_HOME/platform-tools:$PATH

# Make license agreement
RUN mkdir $ANDROID_HOME/licenses && \
    echo 8933bad161af4178b1185d1a37fbf41ea5269c55 > $ANDROID_HOME/licenses/android-sdk-license && \
    echo d56f5187479451eabf01fb78af6dfcb131a6481e >> $ANDROID_HOME/licenses/android-sdk-license && \
    echo 24333f8a63b6825ea9c5514f83c2829b004d1fee >> $ANDROID_HOME/licenses/android-sdk-license && \
    echo 84831b9409646a918e30573bab4c9c91346d8abd > $ANDROID_HOME/licenses/android-sdk-preview-license


# Update and install using sdkmanager
RUN $ANDROID_HOME/tools/bin/sdkmanager "tools" "platform-tools" && \
    $ANDROID_HOME/tools/bin/sdkmanager "build-tools;28.0.3" && \
    $ANDROID_HOME/tools/bin/sdkmanager "platforms;android-24"

RUN $ANDROID_HOME/tools/bin/sdkmanager "system-images;android-24;google_apis;x86_64" && \
    $ANDROID_HOME/tools/bin/sdkmanager "system-images;android-24;google_apis;armeabi-v7a"

RUN $ANDROID_HOME/tools/bin/sdkmanager "extras;android;m2repository" 


RUN echo "no" | $ANDROID_HOME/tools/bin/avdmanager create avd \
                --force \
                --name test \
                --package "system-images;android-24;google_apis;armeabi-v7a" \
                --abi armeabi-v7a \
                --sdcard 512M



# # Support Gradle
ENV TERM dumb
ENV JAVA_OPTS -Xms256m -Xmx512m

ENV GRADLE_USER_HOME /usr/src/app/android/gradle_deps

RUN update-ca-certificates -f

WORKDIR /usr/src/app

# CMD ["lein", "run"]
