# Edge service

Xiang Jin, Cody Volz en Wouter Vanaelten


# Thema

Wij hebben gekozen voor het thema films. Hierbij hebben 4 microservices:

 - Genre microservice: **PostgreSQL** [Github](https://github.com/woutervanaelten/genre-service)
 - Cast microservice: **Mongodb** [Github](https://github.com/r0766008/cast-service)
 - Movie microservice: **Mysql** [Github](https://github.com/woutervanaelten/movie-service)
 - Edge frontend: [Github](https://github.com/r0766008/MovieFrontEnd)
 - Edge Selenium testing: [Github](https://github.com/r0766008/MovieTesting)
 - Edge microservice

## Architectuur
![DatabaseArchitectuur (1)](https://user-images.githubusercontent.com/45122439/104125545-b9d5f880-5357-11eb-91b9-2519e8696914.png)
## Postman
Hieronder kan u de werking van onze verschillende requests zien. De gegevens zijn niet correct ten opzichte van de realiteit, maar ze tonen wel aan dat onze edge service werkt.
## GET1
![getGenresAll](https://user-images.githubusercontent.com/45122439/104125436-ff45f600-5356-11eb-81b2-0c919dcb9e85.png)
## GET2
![getMoviesAll](https://user-images.githubusercontent.com/45122439/104125439-040aaa00-5357-11eb-837d-fc218e8884b4.png)
## GET3
![getMoviesTitle](https://user-images.githubusercontent.com/45122439/104125441-079e3100-5357-11eb-85e1-f3f4f2cba6e6.png)
## GET4
![getMoviesGenre](https://user-images.githubusercontent.com/45122439/104125442-08cf5e00-5357-11eb-84a4-732a6ec392b6.png)
## GET5
![getMoviesAbbreviation](https://user-images.githubusercontent.com/45122439/104125448-0ff66c00-5357-11eb-94ba-7bc7ddfba94c.png)
## GET6
![getMoviesCast](https://user-images.githubusercontent.com/45122439/104125443-0a008b00-5357-11eb-92e1-f199e9b8c9c6.png)
## POST 1
![postMovies](https://user-images.githubusercontent.com/45122439/104125472-374d3900-5357-11eb-922d-7a68b276fc58.png)
## POST 2
![postCast](https://user-images.githubusercontent.com/45122439/104125475-39af9300-5357-11eb-846d-25cda25adc98.png)
## PUT 1
![putMovies](https://user-images.githubusercontent.com/45122439/104125485-4b913600-5357-11eb-9ff2-0689da20d260.png)
## PUT 2
![putCast](https://user-images.githubusercontent.com/45122439/104125486-4d5af980-5357-11eb-91f4-8ffd2fc28d46.png)
## DELETE 1
![deleteMovies](https://user-images.githubusercontent.com/45122439/104125492-58ae2500-5357-11eb-9144-f2e0dbab60f8.png)
## DELETE 2
![deleteCast](https://user-images.githubusercontent.com/45122439/104125496-5ea40600-5357-11eb-9d57-3f540c704807.png)
## Swagger
![swagger](https://user-images.githubusercontent.com/45122439/104125193-885c2d80-5355-11eb-8e90-a4df91f29c44.png)
