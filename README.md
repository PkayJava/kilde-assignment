## kilde-assignment-ddl : it will create DDL (Run Once Only)
```shell
./gradlew kilde-assignment-ddl:bootRun
```

## Compile Web Framework : it is required to start kilde-assignment-web
```shell
git clone https://github.com/senior-cyber/frmk-master.git
./gradlew assemble publishToMavenLocal
```

## Setup HTML Template : : it is required to start kilde-assignment-web

```shell
git clone https://github.com/ColorlibHQ/AdminLTE.git
cd AdminLTE && git checkout v3
```

## kilde-assignment-api
```shell
./gradlew kilde-assignment-api:bootRun
```

## kilde-assignment-web : you will need to update the AdminLET which you clone from the early in application.yaml configuration
#### admin-lte: /opt/apps/github/ColorlibHQ/v3/AdminLTE
```shell
# default login : admin
# default password : admin
./gradlew kilde-assignment-web:bootRun
```