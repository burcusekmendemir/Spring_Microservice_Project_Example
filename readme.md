# KURULUMLAR VE PROJE TEKNOLOJÝLERÝ

## Docker üzerinde PostgreSQL kurulumu;
       Uygulamamýzda Auth servis üzerinde kullanýcý oturum açma iþlemlerini ve kayýtarýný iliþkisel bir
    veritabanýnda tutuyoruz. Veritabaný olarak PostgreSQL kullanýyoruz. PostgreSQL'i docker üzerinde
    çalýþtýrmak için aþaðýdaki kodu kullanýlýr;

```bash
   docker run  --name postgreSQL -e POSTGRES_PASSWORD=root -p 5432:5432 -d postgres
```

## Docker üzerinde MongoDB kurulumu;
    MongoDB kurulumu yaparken yetkili kullanýcý bilgilerinin girilmesi gereklidir. Bu bilgileri imajlarýn 
    Ortam deðiþkenlerine atayarak yapabiliyoruz. Docker bu tarz bilgileri ekleyebilmenmiz için bize ek 
    parametreler sunar.
    EK BÝLGÝ:
    docker üzerinde bir image eklemek istiyorsak -> docker pull [IMAGE_NAME]
    docker üzerinde bir image'ý çalýþtýrmak istiyorsak -> docker run [IMAGE_NAME]
    Burada önemli bir nokta vardýr. docker run eðer ortamda ilgili image yok ise önce image'ý indirir sonra
    çalýþtýrýr, yani docker run yapmak için önce image'ý pull etmemize gerek yoktur.
    -e ->enviroment docker
    -p ->port docker desktop default olarak 27017 portunu kullanýr. Ýlki fiziksel pc portumuz ikinci yazýlan docker desktop'un portudur.
    MongoDB'yi docker üzerinde çalýþtýrmak için aþaðýdaki kod kullanýlýr;

```bash
    docker run --name mongodbjava13 -d -e "MONGO_INITDB_ROOT_USERNAME=mongo" -e "MONGO_INITDB_ROOT_PASSWORD=root" -p 27020:27017 mongo:jammy 
```

    MongoDB'yi yönetebilmek için bir araca ihtiaycýmýz var. Bu aracýn adý MongoDb Compas tool'dur. Bu aracý indirip
    kurmamýz gerekiyor. (Adres: https://www.mongodb.com/try/download/compass)

    Compass kurulumu bittikten sonra, açýlan yeni pencerede "New Connection +" butonuna týklýyoruz. Ekranýn ortasýnda
    "> Advanced connection options" butonuna týklayarak detaylý baðlantý ayarlarýný yapýyoruz.
     1- açýlan ekranda "Host" kýsmýna veritabanýmýzýn ip adresini ve port numarasýný giriyoruz Yerel bilgisayarýmýz
    için kullanýlacak ise ya da docker desktop üzerinde ise (localhost:27017) þeklinde yazýyoruz.
     2- Authentication kýsmýna geçiþ yaparak kurulum sýrasýnda girdiðimiz kullanýcý adý ve þifreyi yazýyoruz. Docker
    run komutu ile çalýþtýrdýysak -e (environment) ile giriþ yaptýðýmýz bilgileri yazýyoruz. (admin-root)

    NOT: MongoDB'yi ilk kurulumlarý ve kullanýmlarý için admin kullanýcý ile iþlemleri yapabiliriz. Ancak, 
    veritabanlarýný yönetmek ve iþlemek için kullanmayalým. Her Db için ayrý kullanýcý ve yetkiler oluþtururuz
    root kullanýcýsý ve þifreleri sadece ilk kurulum için kullanýlmalý ve tekrar kullanýlmamalýdýr. Sadece gerekli
    olduðu durumlarda müdahale için kullanýrýz.
    Gerekli dokümantasyonlara: (http://mongodb.com/docs/manual/) ulaþýrýz.

    Yetkilendirme Ýþlemleri:
    1- MONGOSH'a týklayarak açýyoruz.
    2- Açýlan kýsýmda test> þeklinde bir yer görürüz, öncelikle test DB'den kendi DB'mize geçmek için 
    use [DB_adý] yazýp enter'a basarýz.
    Örn: 
    use UserProfile
    switched to db UserProfile
     UserProfile > þeklinde bir görüntü elde ederiz.
    3- Burada kullanýcý oluþturmak için gerekli komutlarý giriyoruz.
    db.createUser({
        user: "bilgeUser",
        pwd: "bilgeUser*",
        roles: ["readWrite","dbAdmin"]
    })

```
     db.createUser({ user: "bilgeUser", pwd: "bilgeUser*", roles: ["readWrite","dbAdmin"]})
```

## Docker üzerinde Redis Single Node Oluþturmak

     Redis, tipik olarak bir veritabaný veya önbellek olarak kullanýlan bir key-value deposudur 
     ve genellikle uygulama kodu tarafýndan programatik olarak eriþilir. Redis, genellikle 6379 
     numaralý port üzerinden iletiþim kurar. Bu port, Redis'in standart portudur ve Redis 
     sunucusuna baðlanmak için kullanýlýr.
```bash
    docker run --name java13-redis -p 6379:6379 -d redis
```

     Aþaðýdaki komut, Docker'ý kullanan bir ortamda Redis Insight adlý bir Redis GUI uygulamasýný 
     çalýþtýracaktýr. Bu, Redis verilerini görsel olarak keþfetmek, yönetmek ve sorgulamak için 
     kullanýlan bir araçtýr. Komut çalýþtýrýldýðýnda, Redis Insight uygulamasý belirtilen port 
     üzerinde çalýþacak ve tarayýcý aracýlýðýyla eriþilebilir olmasýný saðlayacaktýr.
     
```bash
    docker run --name redis-gui -d -p 8001:8001 redislabs/redisinsight:1.14.0
```

##  Redis Spring Boot Entegrasyonu;

    1- Ýlgili baðýmlýlýklar eklenir.
        dependencies.gradle;
              versions --> springBoot            : '3.2.2'
              libs --> springBootStarterDataRedis: "org.springframework.boot:spring-boot-starter-data-redis:${versions.springBoot}"
        build.gradle -->  dependencies {implementation libs.springBootStarterDataRedis}


    2- Redis Config oluþturulur.
       Redis repository olarak kullanýlabileceði gibi, Cache olarak da kullanýlabilir, Bu nedenle Spring'te Cache'i
       ve Redis Repository aktif etmek için gerekli anotasyonlarý config'e eklememiz gerekir.
       Rediste cache oluþturmak için, istediðimiz methodun üzerinde @Cachable anotasyonunu eklememiz gerekir.
       Böylelikle methoda girilen deðerler için bir Key oluþturuluyor ve return deðeri redis üzerinde
       cachelenmiþ oluyor.

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

      Elasticsearch sürümleri ile Spring sürümleri arasýnda bir uyum olmasý gerekli. 
      Çünkü eski sürümleri kullanabilmek için belli spring boot sürümlerini kullanmanýz gereklidir.

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

      1- Ýlgili baðýmlýlýklar eklenir.
        dependencies.gradle;
              versions --> springBoot                         : '3.2.2'
              libs     --> springBootStarterDataElasticsearch : "org.springframework.boot:spring-boot-starter-data-elasticsearch:${versions.springBoot}"
        build.gradle -->  dependencies {implementation libs.springBootStarterDataElasticsearch}

      2- ElasticService modülü açýldý. Bu yapýlandýrma, uygulamanýn belirtilen port üzerinde çalýþacaðýný 
         ve Elasticsearch'e belirtilen URI, kullanýcý adý ve þifre ile eriþeceðini belirtir.

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
      4- Repository ElasticsearchRepository'den kalýtým almasý saðlanýr.

##  Microserviceler Arasý Ýletiþim: Open Feign

         Spring Boot ile microservisler arasýnda bir iletiþim kurmak için Open Feign kullanýlýr. 

         1- Ýlgili baðýmlýlýklar eklenir.

         dependencies.gradle;
              versions --> springCloud                            : '4.1.0'
              libs     --> springCloudOpenFeign                   : "org.springframework.cloud:spring-cloud-starter-openfeign:${versions.springOpenFeign}"
         build.gradle -->  dependencies {implementation libs.springCloudOpenFeign}

         2-  @EnableFeignClients anotasyonu, Spring Boot uygulamalarýnda Feign istemcilerinin kullanýlmasýnýn 
             etkinleþtirilmesi için main class'a eklenir. Bu anotasyon, Feign istemcilerini tanýmlamak için 
             kullanýlan arayüzlerin otomatik olarak tespit edilip yapýlandýrýlmasýný saðlar. Böylece, Feign 
             istemcilerini kullanarak diðer mikro servislere kolayca eriþim saðlanabilir.
      
```java
@SpringBootApplication
@EnableFeignClients
public class UserServiceApplication {
    public static void main(String[] args) {

        SpringApplication.run(UserServiceApplication.class, args);
    }
}
```
       3- Feign Client kullanarak microservisler arasýndaki iletiþimi saðlamak için bir "manager" veya "service" sýnýfý 
         oluþturulur. Bu sýnýf, Feign Client'i kullanarak diðer servislere HTTP isteklerinde bulunan metotlarý 
         içerir. Bu yaklaþým, servisler arasý iletiþimi kolaylaþtýrýr ve kodun daha düzenli olmasýný saðlar.

```java
@FeignClient(url = "http://localhost:7071/dev/v1/user-profile", name = "elastic-userprofile")
public interface UserManager {
     @GetMapping(FIND_ALL)
     ResponseEntity<List<UserProfile>> findAll();
}
```



##  RabbitMQ Kurulumu;

    RabbitMQ bir mesaj kuyruðu yazýlýmýdýr ve daðýtýk sistemler arasýnda iletiþimi saðlamak için kullanýlýr.
    RabbitMQ iki port ile çalýþýr. 5672, 15672 bu portlardan;
     1- 5672 olan port Rest isteklreini iþlemek için kullanýlýr, bu nedenle Spring Boot bu porta baðlanýr.
     2- 15672 olan port arayüz webUI kýsmýdýr. Yönetim ekraný burasýdýr.

```bash
 docker run -d --name java13-rabbit -p 5672:5672 -p 15672:15672  -e RABBITMQ_DEFAULT_USER=admin -e RABBITMQ_DEFAULT_PASS=root rabbitmq:3-management
```

     1- Ýlgili baðýmlýlýklar eklenir.
           dependencies.gradle -->  implementation 'org.springframework.boot:spring-boot-starter-amqp:3.2.2'
           build.gradle --> dependencies {implementation libs.springBootStarterAmqp}

     2- Yapýlandýrma dosyasý oluþturulur. Bu dosya, RabbitMQ'i kullanmak için gerekli ayarlamalarý yapar.

```yml
spring:
  rabbitmq:
    host: localhost
    port: 5672
    username: admin
    password: root
```
     3- Config dosyasý oluþturulur. Bu dosya RabbitMQ ile ilgili ayarlamalarý yapar. Queue, exchange, binding key 
        tanýmlanýr. 
     4- RabbitMQ paketi microservicelerde açýlýr. Consumer, model, producer ile sendmessage ve listener
        methodlarý yazýlýr. Modellerin microservislerde birebir ayný olmasý gereklidir.
     5- RabbitMQ deserilizable iþleminde getirilen yeni güvenlik config nedeniyle þu ENV'nin eklenmesi
        gereklidir; "SPRING_AMQP_DESERIALIZATION_TRUST_ALL=true"


##  Config Server Kullanýmý;
    
    Config server, mikroservice mimarisi içinde kullanýlan ve uygulamalarýn yapýlandýrma bilgilerini merkezi 
    bir yerden almasýný saðlayan bir bileþendir. Bu, farklý uygulamalarýn ayný yapýlandýrma ayarlarýný paylaþmasýný 
    ve yapýlandýrma deðiþikliklerinin tüm uygulamalara otomatik olarak yansýtýlmasýný saðlar. 

     1- Ýlgili baðýmlýlýklar eklenir. 
          dependencies.gradle;
            springCloudConfig:              "org.springframework.cloud:spring-cloud-starter-config:${versions.springCloud}",
            springCloudConfigServer:        "org.springframework.cloud:spring-cloud-config-server:${versions.springCloud}",
            springCloudConfigClient:        "org.springframework.cloud:spring-cloud-config-client:${versions.springCloud}",
        
     2- Config Server modülü oluþturulur, aþaðýdaki baðýmlýlýklar eklenir.
           build.gradle;    dependencies {implementation libs.springCloudConfig
                                          implementation libs.springCloudConfigServer}  eklenir.

        Microserviceler için de aþaðýdaki baðýmlýlýk eklenir. 
           build.gradle;    dependencies {implementation libs.springCloudConfigClient}

     3- Main class'a eklenen @EnaleConfigServer anotasyonu ile Spring Boot tabanlý bir Config Server uygulamasýný baþlatmak 
        için gereken temel yapýyý saðlanýr. Bu uygulama, yapýlandýrma dosyalarýný dinamik olarak yönetmek için kullanýlabilir 
        ve diðer Spring Boot uygulamalarýna bu yapýlandýrma dosyalarýný saðlayabilir.

```java
@SpringBootApplication
@EnableConfigServer
public class ConfigServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApplication.class, args);
    }
}
```
     

      4- Config Server resources file içerisine "config-repo" adlý bir klasör açýlýr. Bu klasör içerisine yönetilecek microservicelerin
         application.yml dosyalarý eklenir ve yapýlandýrma dosyalarýndaki gerekli deðiþiklikler ve düzenlemeler artýk
         buradan gerçekleþir.
         Config Server mikroservisinin de yapýlandýrma dosyasý aþaðýdaki þekilde oluþturulur;

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
      5- Microserviceler içerisinde yer alan yapýlandýrma dosyalarý da aþaðýdaki þekilde oluþturulur;

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

