# スーパー支払い君　バックエンド
本 API サーバーはDDD / Clean Argitecture に準拠している
## 環境構築

1. OpenJDK 17 のインストール
mac の場合
```
$ brew install openjdk@17
$ java --version
openjdk 17.0.6 2023-01-17 LTS
```

2. shiharaikun のインストール
```
$ git clone https://github.com/MasatoNagashima/shiharaikun.git
$ cd chiharaikun
```

3. サーバー立ち上げ
```
shiharaikun $ cd docker
docker $ docker compose up -d
docker $ cd ..
shiharaikun $ ./gradlew build
shiharaikun $ ./gradlew run
```
