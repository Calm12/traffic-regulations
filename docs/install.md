# Установка

1. git clone
1. cp web/dist/docker/.env.template web/dist/docker/.env
1. Изменить пароли от БД в web/dist/docker/.env при необходимости
1. По умолчанию здесь production профиль, если нужно изменить, то установить SPRING_PROFILE в web/dist/docker/.env в зависимости от вашей среды (dev/production)
1. Изменить пароли от БД в web/src/main/resources/application-production.yml при необходимости
1. mvnw clean package
    1. Если нужен билд архив: cd assembly && mvnw assembly:single 
1. cd web/target/build/docker
1. docker network create -d bridge backend && docker network create -d bridge frontend (вернет длинный id созданной сети)
1. Выполнить "ip a", и взять оттуда имя интерфейса созданной backend сети
1. sudo firewall-cmd --permanent --zone=trusted --add-interface=br-79c568dc0833 (подставить имя интерфейса из пункта выше)
1. systemctl restart firewalld
1. docker-compose build --no-cache
1. docker-compose up -d


## Jenkins
1. Выполнить настройки, связанные с докером
2. Добавить pipeline с репозиторием


## Установка и запуск без докера ##
1. git clone
1. По умолчанию здесь dev профиль, если нужно запустиить для production, то изменить это значение в файле web/src/main/resources/application.yml или запускать с параметром -Dspring.profiles.active=production
1. Изменить пароли от БД в web/src/main/resources/application-dev.yml
1. mvnw clean package
1. cd web/target/build
1. Start.bat/Start.sh