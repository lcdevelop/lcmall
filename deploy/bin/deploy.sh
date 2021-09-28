#!/bin/bash

SCRIPT_PATH="$( cd "$( dirname "$0"  )" && pwd  )"
BASE_DIR=${SCRIPT_PATH}/../../

cd ${BASE_DIR}/lcmall-admin && tyarn build || exit 1
echo "build admin success"

rm -rf ${BASE_DIR}/docker/admin/html/dist && cp -r ${BASE_DIR}/lcmall-admin/dist ${BASE_DIR}/docker/admin/html/dist || exit 1
echo "finish copy admin"

[ -f ${BASE_DIR}/lcmall-api/src/main/resources/application.yml ] || exit 1
echo "test application.yml ok"

cd ${BASE_DIR} && mvn clean package || exit 1
echo "mvn build success"

cp ${BASE_DIR}/lcmall-api/target/lcmall-api-*-exec.jar ${BASE_DIR}/docker/lcmall/lcmall.jar || exit 1
echo "copy success"

cat ${BASE_DIR}/lcmall-db/sql/lcmall_schema.sql > ${BASE_DIR}/docker/db/init-sql/lcmall.sql || exit 1
cat ${BASE_DIR}/lcmall-db/sql/lcmall_table.sql >> ${BASE_DIR}/docker/db/init-sql/lcmall.sql || exit 1
echo "gen sql success"

cd ${BASE_DIR}/docker || exit 1

docker-compose down || exit 1
echo "docker down success"

docker-compose build || exit 1
echo "docker build success"

docker-compose up -d || exit 1
echo "docker up success"

exit 0

