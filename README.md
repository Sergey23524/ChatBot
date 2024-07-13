# Проект - Бот для ответа на сообщения

Требования к инструментам
* java версии 17
* docker
* ngrok

### Запуск приложения

1) Проставляем значения в application.properties 
* access.token (ключ доступа сообщества)
* confirmation.group.id (id сообщества)
* confirmation.message (строка для подтверждения адреса сервера)
2) Собираем Jar файл vkbot -> install
3) Пулим образ ngrok командой:
```
 docker pull ngrok/ngrok
```
4) Запускаем контейнер ngrok в докере. Не забываем подставить свой токен NGROK_AUTHTOKEN
```
docker run --net=host -it -e NGROK_AUTHTOKEN= ngrok/ngrok:latest http 8080
```
5) Собираем образ с нашим Jar файлом
```
docker build -t vkbot .
```
6) Запускаем контейнер с нашим проектом 
```
docker run -p 8080:8080 -t vkbot
```