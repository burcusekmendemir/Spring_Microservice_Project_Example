# KURULUMLAR VE PROJE TEKNOLOJ�LER�

## Docker �zerinde PostgreSQL kurulumu;
       Uygulamam�zda Auth servis �zerinde kullan�c� oturum a�ma i�lemlerini ve kay�tar�n� ili�kisel bir
    veritaban�nda tutuyoruz. Veritaban� olarak PostgreSQL kullan�yoruz. PostgreSQL'i docker �zerinde
    �al��t�rmak i�in a�a��daki kodu kullan�l�r;

```bash
   docker run  --name postgreSQL -e POSTGRES_PASSWORD=root -p 5432:5432 -d postgres
```

## Docker �zerinde MongoDB kurulumu;
    MongoDB kurulumu yaparken yetkili kullan�c� bilgilerinin girilmesi gereklidir. Bu bilgileri imajlar�n 
    Ortam de�i�kenlerine atayarak yapabiliyoruz. Docker bu tarz bilgileri ekleyebilmenmiz i�in bize ek 
    parametreler sunar.
    EK B�LG�:
    docker �zerinde bir image eklemek istiyorsak -> docker pull [IMAGE_NAME]
    docker �zerinde bir image'� �al��t�rmak istiyorsak -> docker run [IMAGE_NAME]
    Burada �nemli bir nokta vard�r. docker run e�er ortamda ilgili image yok ise �nce image'� indirir sonra
    �al��t�r�r, yani docker run yapmak i�in �nce image'� pull etmemize gerek yoktur.
    -e ->enviroment docker
    -p ->port docker desktop default olarak 27017 portunu kullan�r. �lki fiziksel pc portumuz ikinci yaz�lan docker desktop'un portudur.
    MongoDB'yi docker �zerinde �al��t�rmak i�in a�a��daki kod kullan�l�r;

```bash
    docker run --name mongodbjava13 -d -e "MONGO_INITDB_ROOT_USERNAME=mongo" -e "MONGO_INITDB_ROOT_PASSWORD=root" -p 27020:27017 mongo:jammy 
```

    MongoDB'yi y�netebilmek i�in bir araca ihtiayc�m�z var. Bu arac�n ad� MongoDb Compas tool'dur. Bu arac� indirip
    kurmam�z gerekiyor. (Adres: https://www.mongodb.com/try/download/compass)

    Compass kurulumu bittikten sonra, a��lan yeni pencerede "New Connection +" butonuna t�kl�yoruz. Ekran�n ortas�nda
    "> Advanced connection options" butonuna t�klayarak detayl� ba�lant� ayarlar�n� yap�yoruz.
     1- a��lan ekranda "Host" k�sm�na veritaban�m�z�n ip adresini ve port numaras�n� giriyoruz Yerel bilgisayar�m�z
    i�in kullan�lacak ise ya da docker desktop �zerinde ise (localhost:27017) �eklinde yaz�yoruz.
     2- Authentication k�sm�na ge�i� yaparak kurulum s�ras�nda girdi�imiz kullan�c� ad� ve �ifreyi yaz�yoruz. Docker
    run komutu ile �al��t�rd�ysak -e (environment) ile giri� yapt���m�z bilgileri yaz�yoruz. (admin-root)

    NOT: MongoDB'yi ilk kurulumlar� ve kullan�mlar� i�in admin kullan�c� ile i�lemleri yapabiliriz. Ancak, 
    veritabanlar�n� y�netmek ve i�lemek i�in kullanmayal�m. Her Db i�in ayr� kullan�c� ve yetkiler olu�tururuz
    root kullan�c�s� ve �ifreleri sadece ilk kurulum i�in kullan�lmal� ve tekrar kullan�lmamal�d�r. Sadece gerekli
    oldu�u durumlarda m�dahale i�in kullan�r�z.
    Gerekli dok�mantasyonlara: (http://mongodb.com/docs/manual/) ula��r�z.

    Yetkilendirme ��lemleri:
    1- MONGOSH'a t�klayarak a��yoruz.
    2- A��lan k�s�mda test> �eklinde bir yer g�r�r�z, �ncelikle test DB'den kendi DB'mize ge�mek i�in 
    use [DB_ad�] yaz�p enter'a basar�z.
    �rn: 
    use UserProfile
    switched to db UserProfile
     UserProfile > �eklinde bir g�r�nt� elde ederiz.
    3- Burada kullan�c� olu�turmak i�in gerekli komutlar� giriyoruz.
    db.createUser({
        user: "bilgeUser",
        pwd: "bilgeUser*",
        roles: ["readWrite","dbAdmin"]
    })

```
     db.createUser({ user: "bilgeUser", pwd: "bilgeUser*", roles: ["readWrite","dbAdmin"]})
```

## Docker �zerinde Redis Single Node Olu�turmak

     Redis, tipik olarak bir veritaban� veya �nbellek olarak kullan�lan bir key-value deposudur 
     ve genellikle uygulama kodu taraf�ndan programatik olarak eri�ilir. Redis, genellikle 6379 
     numaral� port �zerinden ileti�im kurar. Bu port, Redis'in standart portudur ve Redis 
     sunucusuna ba�lanmak i�in kullan�l�r.
```bash
    docker run --name java13-redis -p 6379:6379 -d redis
```

     A�a��daki komut, Docker'� kullanan bir ortamda Redis Insight adl� bir Redis GUI uygulamas�n� 
     �al��t�racakt�r. Bu, Redis verilerini g�rsel olarak ke�fetmek, y�netmek ve sorgulamak i�in 
     kullan�lan bir ara�t�r. Komut �al��t�r�ld���nda, Redis Insight uygulamas� belirtilen port 
     �zerinde �al��acak ve taray�c� arac�l���yla eri�ilebilir olmas�n� sa�layacakt�r.
     
```bash
    docker run --name redis-gui -d -p 8001:8001 redislabs/redisinsight:1.14.0
```

##  Redis Spring Boot Entegrasyonu;

    1- �lgili ba��ml�l�klar eklenir.
        dependencies.gradle;
              versions --> springBoot            : '3.2.2'
              libs --> springBootStarterDataRedis: "org.springframework.boot:spring-boot-starter-data-redis:${versions.springBoot}"
        build.gradle -->  dependencies {implementation libs.springBootStarterDataRedis}


    2- Redis Config olu�turulur.
       Redis repository olarak kullan�labilece�i gibi, Cache olarak da kullan�labilir, Bu nedenle Spring'te Cache'i
       ve Redis Repository aktif etmek i�in gerekli anotasyonlar� config'e eklememiz gerekir.
       Rediste cache olu�turmak i�in, istedi�imiz methodun �zerinde @Cachable anotasyonunu eklememiz gerekir.
       B�ylelikle methoda girilen de�erler i�in bir Key olu�turuluyor ve return de�eri redis �zerinde
       cachelenmi� oluyor.

```java
    @Configuration
    @EnableRedisRepositories
    @EnableCaching
    public class RedisConfig {
        @Bean
        public LettuceConnectionFactory redisConnecitonFactory(){
            return new LettuceConnectionFactory(new RedisStandaloneConfiguration("localhost", 6379));
        }
    }
```

##  Elasticsearch Kurulumu;

      Elasticsearch s�r�mleri ile Spring s�r�mleri aras�nda bir uyum olmas� gerekli. 
      ��nk� eski s�r�mleri kullanabilmek i�in belli spring boot s�r�mlerini kullanman�z gereklidir.

```bash
    docker network create java13-network
```

```bash
    docker run -d --name elasticsearch --net java13-network -p 9200:9200 -p 9300:9300 -e "xpack.security.enabled=false" -e "xpack.security.transport.ssl.enabled=false" -e "discovery.type=single-node" -e "ELASTIC_USERNAME=admin"  -e "ELASTIC_PASSWORD=root" -e "ES_JAVA_OPTS=-Xms512m -Xmx1024m" elasticsearch:8.12.1
```

```bash
    docker run -d --name kibana --net java13-network -p 5601:5601 kibana:8.12.1
```

##  Elasticsearch Spring Boot Entegrasyonu;

      1- �lgili ba��ml�l�klar eklenir.
        dependencies.gradle;
              versions --> springBoot                         : '3.2.2'
              libs     --> springBootStarterDataElasticsearch : "org.springframework.boot:spring-boot-starter-data-elasticsearch:${versions.springBoot}"
        build.gradle -->  dependencies {implementation libs.springBootStarterDataElasticsearch}

      2- ElasticService mod�l� a��ld�. Bu yap�land�rma, uygulaman�n belirtilen port �zerinde �al��aca��n� 
         ve Elasticsearch'e belirtilen URI, kullan�c� ad� ve �ifre ile eri�ece�ini belirtir.

```yml
server:
  port: 9091

spring:
  elasticsearch:
    uris: http://localhost:9200
    username: admin
    password: root
```

      3- Entity  -- @Document (indexName= "[INDEX_NAME]]") -- anotasyonu ile, index ismi belirlenir.
      4- Repository ElasticsearchRepository'den kal�t�m almas� sa�lan�r.

##  Microserviceler Aras� �leti�im: Open Feign

         Spring Boot ile microservisler aras�nda bir ileti�im kurmak i�in Open Feign kullan�l�r. 

         1- �lgili ba��ml�l�klar eklenir.

         dependencies.gradle;
              versions --> springCloud                            : '4.1.0'
              libs     --> springCloudOpenFeign                   : "org.springframework.cloud:spring-cloud-starter-openfeign:${versions.springOpenFeign}"
         build.gradle -->  dependencies {implementation libs.springCloudOpenFeign}

         2-  @EnableFeignClients anotasyonu, Spring Boot uygulamalar�nda Feign istemcilerinin kullan�lmas�n�n 
             etkinle�tirilmesi i�in main class'a eklenir. Bu anotasyon, Feign istemcilerini tan�mlamak i�in 
             kullan�lan aray�zlerin otomatik olarak tespit edilip yap�land�r�lmas�n� sa�lar. B�ylece, Feign 
             istemcilerini kullanarak di�er mikro servislere kolayca eri�im sa�lanabilir.
      
```java
@SpringBootApplication
@EnableFeignClients
public class UserServiceApplication {
    public static void main(String[] args) {

        SpringApplication.run(UserServiceApplication.class, args);
    }
}
```
       3- Feign Client kullanarak microservisler aras�ndaki ileti�imi sa�lamak i�in bir "manager" veya "service" s�n�f� 
         olu�turulur. Bu s�n�f, Feign Client'i kullanarak di�er servislere HTTP isteklerinde bulunan metotlar� 
         i�erir. Bu yakla��m, servisler aras� ileti�imi kolayla�t�r�r ve kodun daha d�zenli olmas�n� sa�lar.

```java
@FeignClient(url = "http://localhost:7071/dev/v1/user-profile", name = "elastic-userprofile")
public interface UserManager {
     @GetMapping(FIND_ALL)
     ResponseEntity<List<UserProfile>> findAll();
}
```



##  RabbitMQ Kurulumu;

    RabbitMQ bir mesaj kuyru�u yaz�l�m�d�r ve da��t�k sistemler aras�nda ileti�imi sa�lamak i�in kullan�l�r.
    RabbitMQ iki port ile �al���r. 5672, 15672 bu portlardan;
     1- 5672 olan port Rest isteklreini i�lemek i�in kullan�l�r, bu nedenle Spring Boot bu porta ba�lan�r.
     2- 15672 olan port aray�z webUI k�sm�d�r. Y�netim ekran� buras�d�r.

```bash
 docker run -d --name java13-rabbit -p 5672:5672 -p 15672:15672  -e RABBITMQ_DEFAULT_USER=admin -e RABBITMQ_DEFAULT_PASS=root rabbitmq:3-management
```

     1- �lgili ba��ml�l�klar eklenir.
           dependencies.gradle -->  implementation 'org.springframework.boot:spring-boot-starter-amqp:3.2.2'
           build.gradle --> dependencies {implementation libs.springBootStarterAmqp}

     2- Yap�land�rma dosyas� olu�turulur. Bu dosya, RabbitMQ'i kullanmak i�in gerekli ayarlamalar� yapar.

```yml
spring:
  rabbitmq:
    host: localhost
    port: 5672
    username: admin
    password: root
```
     3- Config dosyas� olu�turulur. Bu dosya RabbitMQ ile ilgili ayarlamalar� yapar. Queue, exchange, binding key 
        tan�mlan�r. 
     4- RabbitMQ paketi microservicelerde a��l�r. Consumer, model, producer ile sendmessage ve listener
        methodlar� yaz�l�r. Modellerin microservislerde birebir ayn� olmas� gereklidir.
     5- RabbitMQ deserilizable i�leminde getirilen yeni g�venlik config nedeniyle �u ENV'nin eklenmesi
        gereklidir; "SPRING_AMQP_DESERIALIZATION_TRUST_ALL=true"


##  Config Server Kullan�m�;
    
    Config server, mikroservice mimarisi i�inde kullan�lan ve uygulamalar�n yap�land�rma bilgilerini merkezi 
    bir yerden almas�n� sa�layan bir bile�endir. Bu, farkl� uygulamalar�n ayn� yap�land�rma ayarlar�n� payla�mas�n� 
    ve yap�land�rma de�i�ikliklerinin t�m uygulamalara otomatik olarak yans�t�lmas�n� sa�lar. 

     1- �lgili ba��ml�l�klar eklenir. 
          dependencies.gradle;
            springCloudConfig:              "org.springframework.cloud:spring-cloud-starter-config:${versions.springCloud}",
            springCloudConfigServer:        "org.springframework.cloud:spring-cloud-config-server:${versions.springCloud}",
            springCloudConfigClient:        "org.springframework.cloud:spring-cloud-config-client:${versions.springCloud}",
        
     2- Config Server mod�l� olu�turulur, a�a��daki ba��ml�l�klar eklenir.
           build.gradle;    dependencies {implementation libs.springCloudConfig
                                          implementation libs.springCloudConfigServer}  eklenir.

        Microserviceler i�in de a�a��daki ba��ml�l�k eklenir. 
           build.gradle;    dependencies {implementation libs.springCloudConfigClient}

     3- Main class'a eklenen @EnaleConfigServer anotasyonu ile Spring Boot tabanl� bir Config Server uygulamas�n� ba�latmak 
        i�in gereken temel yap�y� sa�lan�r. Bu uygulama, yap�land�rma dosyalar�n� dinamik olarak y�netmek i�in kullan�labilir 
        ve di�er Spring Boot uygulamalar�na bu yap�land�rma dosyalar�n� sa�layabilir.

```java
@SpringBootApplication
@EnableConfigServer
public class ConfigServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApplication.class, args);
    }
}
```
     

      4- Config Server resources file i�erisine "config-repo" adl� bir klas�r a��l�r. Bu klas�r i�erisine y�netilecek microservicelerin
         application.yml dosyalar� eklenir ve yap�land�rma dosyalar�ndaki gerekli de�i�iklikler ve d�zenlemeler art�k
         buradan ger�ekle�ir.
         Config Server mikroservisinin de yap�land�rma dosyas� a�a��daki �ekilde olu�turulur;

```yml
server:
  port: 8888

spring:
  profiles:
    active: native
  cloud:
    config:
      server:
        native:
          search-locations: classpath:/config-repo
```
      5- Microserviceler i�erisinde yer alan yap�land�rma dosyalar� da a�a��daki �ekilde olu�turulur;

```yml
spring:
  cloud:
    config:
      uri: http://localhost:8888
  config:
    import: "configserver:"
  application:
    name: auth-service-application
```

