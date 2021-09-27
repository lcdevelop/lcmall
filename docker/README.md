## docker部署

### 项目打包
1. 编译jar包
```
cp lcmall-api/src/resources/application_sample.yml lcmall-api/src/resources/application.yml
修改application.yml中对应账号密码等私人信息
mvn clean package
cp lcmall-api/target/lcmall-api-*-exec.jar ./docker/lcmall/lcmall.jar
```

2. docker部署
```
cat ./lcmall-db/sql/lcmall_schema.sql > ./docker/db/init-sql/lcmall.sql
cat ./lcmall-db/sql/lcmall_table.sql >> ./docker/db/init-sql/lcmall.sql
cd ./docker
docker-compose build
docker-compose up -d
```
