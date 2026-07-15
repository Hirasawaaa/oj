FROM eclipse-temurin:17-jdk
RUN sed -i 's|URIs: http://archive.ubuntu.com/ubuntu/|URIs: http://mirrors.aliyun.com/ubuntu/|g' /etc/apt/sources.list.d/ubuntu.sources && \
    sed -i 's|URIs: http://security.ubuntu.com/ubuntu/|URIs: http://mirrors.aliyun.com/ubuntu/|g' /etc/apt/sources.list.d/ubuntu.sources && \
    apt-get update && apt-get install -y \
    time \
    gcc g++ \
    python3 python3-pip \
    pypy3 \
    nodejs npm \
    && rm -rf /var/lib/apt/lists/*
WORKDIR /code