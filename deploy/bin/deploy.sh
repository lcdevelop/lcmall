#!/bin/bash

SCRIPT_PATH="$( cd "$( dirname "$0"  )" && pwd  )"
BASE_DIR=${SCRIPT_PATH}/../../

# 编译管理后台的前端模块
cd ${BASE_DIR}/lcmall-admin && tyarn build || exit 1
echo "build admin success"

# 拷贝前端模块产出物
rm -rf ${BASE_DIR}/docker/admin/html/dist && cp -r ${BASE_DIR}/lcmall-admin/dist ${BASE_DIR}/docker/admin/html/dist || exit 1
echo "finish copy admin"

# 确认配置文件已自己编辑好，没有就从${BASE_DIR}/lcmall-api/src/main/resources/application_sample.yml拷贝并修改
[ -f ${BASE_DIR}/docker/lcmall/conf.d/application.yml ] || exit 1
echo "test application.yml ok"

# 编译后端模块
cd ${BASE_DIR} && mvn clean package || exit 1
echo "mvn build success"

# 拷贝后端模块产出物
cp ${BASE_DIR}/lcmall-api/target/lcmall-api-*-exec.jar ${BASE_DIR}/docker/lcmall/lcmall.jar || exit 1
echo "copy success"

# 构造数据库初始化语句
cat ${BASE_DIR}/lcmall-db/sql/lcmall_schema.sql > ${BASE_DIR}/docker/db/init-sql/lcmall.sql || exit 1
cat ${BASE_DIR}/lcmall-db/sql/lcmall_table.sql >> ${BASE_DIR}/docker/db/init-sql/lcmall.sql || exit 1
echo "gen sql success"

cd ${BASE_DIR}/docker || exit 1

# 停服
docker-compose down || exit 1
echo "docker down success"

# 构建镜像
docker-compose build || exit 1
echo "docker build success"

# 启动服务
docker-compose up -d || exit 1
echo "docker up success"

exit 0

