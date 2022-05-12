# spring-rest-api-parsing-stocks

Java Spring Boot REST API for parsing stocks data ([FinViz](https://finviz.com/)). 
Используется разграничение по ролям (ADMIN, USER).
  
_Для успешного функционирования необходимо:_
1) Подготовить БД. СУБД PostgreSQL (скрипты для создания таблиц приведены в файле DDL.sql).
2) Получить [OpenExchangeRatesApi](https://openexchangerates.org/) appId (используется для получения данных о курсе валют).
3) Указать данные почтового ящика (используется только при отправке писем с восстановлением пароля).
4) Для поиска акций используется finviz.url_screener в application.properties. Требуется указать ссылку на нужный скринер.

После запуска проекта доступен Swagger. Переходить по ссылке: http://localhost:8085/swagger-ui/#/

![swagger-1](https://github.com/Alexandr-Medvedev/spring-rest-api-parsing-stocks/blob/main/Images/Swagger-1.jpg "swagger-1")
![swagger-2](https://github.com/Alexandr-Medvedev/spring-rest-api-parsing-stocks/blob/main/Images/Swagger-2.jpg "swagger-2")

Для добавления компаний пользователем, запроса выгрузок и прочего - необходимо добавить запись в БД с валютой (USD) и данные о компаниях (есть в scheduled_tasks).

# Стек технологий
* Java 11
* Spring Boot
* Spring Data JPA
* Spring Security
* Swagger
* PostgreSQL
* Maven
* Junit
