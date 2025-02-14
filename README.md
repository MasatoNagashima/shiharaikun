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

3. .env ファイルの作成
root ディレクトリに下記の .env ファイルを作成
 ```shell
DB_URL=jdbc:postgresql://localhost:5432/super_shiharai_kun
DB_USER=myuser
DB_PASSWORD=mypassword
DB_DRIVER=org.postgresql.Driver
DB_POOL_SIZE=10

JWT_ISSUER=
JWT_DOMAIN=
JWT_AUDIENCE=
JWT_REALM=
JWT_SECRET=
JWT_EXPIRES_IN=
```

4. サーバー立ち上げ
```
shiharaikun $ cd docker
docker $ docker compose up -d
docker $ cd ..
shiharaikun $     export $(cat .env)
shiharaikun $ ./gradlew build
shiharaikun $ ./gradlew run
```
