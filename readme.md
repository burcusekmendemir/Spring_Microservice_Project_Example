# KURULUMLAR VE PROJE TEKNOLOJİLERİ

## Docker üzerinde PostgreSQL kurulumu;
       Uygulamamızda AuthService üzerinde kullanıcı oturum açma işlemlerini ve kayıtarını ilişkisel bir
    veritabanında tutuyoruz. Veritabanı olarak PostgreSQL kullanıyoruz. PostgreSQL'i docker üzerinde
    çalıştırmak için aşağıdaki kodu kullanılır;

```bash
   docker run  --name postgreSQL -e POSTGRES_PASSWORD=root -p 5432:5432 -d postgres
```

## Docker üzerinde MongoDB kurulumu;
    MongoDB kurulumu yaparken yetkili kullanıcı bilgilerinin girilmesi gereklidir. Bu bilgileri imajların 
    Ortam değişkenlerine atayarak yapabiliyoruz. Docker bu tarz bilgileri ekleyebilmenmiz için bize ek 
    parametreler sunar.
    EK BİLGİ:
    docker üzerinde bir image eklemek istiyorsak -> docker pull [IMAGE_NAME]
    docker üzerinde bir image'ı çalıştırmak istiyorsak -> docker run [IMAGE_NAME]
    Burada önemli bir nokta vardır. docker run eğer ortamda ilgili image yok ise önce image'ı indirir sonra
    çalıştırır, yani docker run yapmak için önce image'ı pull etmemize gerek yoktur.
    -e ->enviroment docker
    -p ->port docker desktop default olarak 27017 portunu kullanır. İlki fiziksel pc portumuz ikinci yazılan docker desktop'un portudur.
    MongoDB'yi docker üzerinde çalıştırmak için aşağıdaki kod kullanılır;

```bash
    docker run --name mongodbjava13 -d -e "MONGO_INITDB_ROOT_USERNAME=mongo" -e "MONGO_INITDB_ROOT_PASSWORD=root" -p 27020:27017 mongo:jammy 
```

    MongoDB'yi yönetebilmek için bir araca ihtiaycımız var. Bu aracın adı MongoDb Compas tool'dur. Bu aracı indirip
    kurmamız gerekiyor. (Adres: https://www.mongodb.com/try/download/compass)

    Compass kurulumu bittikten sonra, açılan yeni pencerede "New Connection +" butonuna tıklıyoruz. Ekranın ortasında
    "> Advanced connection options" butonuna tıklayarak detaylı bağlantı ayarlarını yapıyoruz.
     1- açılan ekranda "Host" kısmına veritabanımızın ip adresini ve port numarasını giriyoruz Yerel bilgisayarımız
    için kullanılacak ise ya da docker desktop üzerinde ise (localhost:27017) şeklinde yazıyoruz.
     2- Authentication kısmına geçiş yaparak kurulum sırasında girdiğimiz kullanıcı adı ve şifreyi yazıyoruz. Docker
    run komutu ile çalıştırdıysak -e (environment) ile giriş yaptığımız bilgileri yazıyoruz. (admin-root)

    NOT: MongoDB'yi ilk kurulumları ve kullanımları için admin kullanıcı ile işlemleri yapabiliriz. Ancak, 
    veritabanlarını yönetmek ve işlemek için kullanmayalım. Her Db için ayrı kullanıcı ve yetkiler oluştururuz
    root kullanıcısı ve şifreleri sadece ilk kurulum için kullanılmalı ve tekrar kullanılmamalıdır. Sadece gerekli
    olduğu durumlarda müdahale için kullanırız.
    Gerekli dokümantasyonlara: (http://mongodb.com/docs/manual/) ulaşırız.

    Yetkilendirme İşlemleri:
    1- MONGOSH'a tıklayarak açıyoruz.
    2- Açılan kısımda test> şeklinde bir yer görürüz, öncelikle test DB'den kendi DB'mize geçmek için 
    use [DB_adı] yazıp enter'a basarız.
    Örn: 
    use UserProfile
    switched to db UserProfile
     UserProfile > şeklinde bir görüntü elde ederiz.
    3- Burada kullanıcı oluşturmak için gerekli komutları giriyoruz.
    db.createUser({
        user: "bilgeUser",
        pwd: "bilgeUser*",
        roles: ["readWrite","dbAdmin"]
    })

```
     db.createUser({ user: "bilgeUser", pwd: "bilgeUser*", roles: ["readWrite","dbAdmin"]})
```

## Docker üzerinde Redis Single Node Oluşturmak

     Redis, tipik olarak bir veritabanı veya önbellek olarak kullanılan bir key-value deposudur 
     ve genellikle uygulama kodu tarafından programatik olarak erişilir. Redis, genellikle 6379 
     numaralı port üzerinden iletişim kurar. Bu port, Redis'in standart portudur ve Redis 
     sunucusuna bağlanmak için kullanılır.
```bash
    docker run --name java13-redis -p 6379:6379 -d redis
```

     Aşağıdaki komut, Docker'ı kullanan bir ortamda Redis Insight adlı bir Redis GUI uygulamasını 
     çalıştıracaktır. Bu, Redis verilerini görsel olarak keşfetmek, yönetmek ve sorgulamak için 
     kullanılan bir araçtır. Komut çalıştırıldığında, Redis Insight uygulaması belirtilen port 
     üzerinde çalışacak ve tarayıcı aracılığıyla erişilebilir olmasını sağlayacaktır.
     
```bash
    docker run --name redis-gui -d -p 8001:8001 redislabs/redisinsight:1.14.0
```

##  Redis Spring Boot Entegrasyonu;

    1- İlgili bağımlılıklar eklenir.
        dependencies.gradle;
              versions --> springBoot            : '3.2.2'
              libs --> springBootStarterDataRedis: "org.springframework.boot:spring-boot-starter-data-redis:${versions.springBoot}"
        build.gradle -->  dependencies {implementation libs.springBootStarterDataRedis}


    2- Redis Config oluşturulur.
       Redis repository olarak kullanılabileceği gibi, Cache olarak da kullanılabilir, Bu nedenle Spring'te Cache'i
       ve Redis Repository aktif etmek için gerekli anotasyonları config'e eklememiz gerekir.
       Rediste cache oluşturmak için, istediğimiz methodun üzerinde @Cachable anotasyonunu eklememiz gerekir.
       Böylelikle methoda girilen değerler için bir Key oluşturuluyor ve return değeri redis üzerinde
       cachelenmiş oluyor.

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

      Elasticsearch sürümleri ile Spring sürümleri arasında bir uyum olması gerekli. 
      Çünkü eski sürümleri kullanabilmek için belli spring boot sürümlerini kullanmanız gereklidir.

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

      1- İlgili bağımlılıklar eklenir.
        dependencies.gradle;
              versions --> springBoot                         : '3.2.2'
              libs     --> springBootStarterDataElasticsearch : "org.springframework.boot:spring-boot-starter-data-elasticsearch:${versions.springBoot}"
        build.gradle -->  dependencies {implementation libs.springBootStarterDataElasticsearch}

      2- ElasticService modülü açıldı. Bu yapılandırma, uygulamanın belirtilen port üzerinde çalışacağını 
         ve Elasticsearch'e belirtilen URI, kullanıcı adı ve şifre ile erişeceğini belirtir.

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
      4- Repository ElasticsearchRepository'den kalıtım alması sağlanır.

##  Microserviceler Arası İletişim: Open Feign

         Spring Boot ile microservisler arasında bir iletişim kurmak için Open Feign kullanılır. 

         1- İlgili bağımlılıklar eklenir.

         dependencies.gradle;
              versions --> springCloud                            : '4.1.0'
              libs     --> springCloudOpenFeign                   : "org.springframework.cloud:spring-cloud-starter-openfeign:${versions.springOpenFeign}"
         build.gradle -->  dependencies {implementation libs.springCloudOpenFeign}

         2-  @EnableFeignClients anotasyonu, Spring Boot uygulamalarında Feign istemcilerinin kullanılmasının 
             etkinleştirilmesi için main class'a eklenir. Bu anotasyon, Feign istemcilerini tanımlamak için 
             kullanılan arayüzlerin otomatik olarak tespit edilip yapılandırılmasını sağlar. Böylece, Feign 
             istemcilerini kullanarak diğer mikro servislere kolayca erişim sağlanabilir.
      
```java
@SpringBootApplication
@EnableFeignClients
public class UserServiceApplication {
    public static void main(String[] args) {

        SpringApplication.run(UserServiceApplication.class, args);
    }
}
```
       3- Feign Client kullanarak microservisler arasındaki iletişimi sağlamak için bir "manager" veya "service" sınıfı 
         oluşturulur. Bu sınıf, Feign Client'i kullanarak diğer servislere HTTP isteklerinde bulunan metotları 
         içerir. Bu yaklaşım, servisler arası iletişimi kolaylaştırır ve kodun daha düzenli olmasını sağlar.

```java
@FeignClient(url = "http://localhost:7071/dev/v1/user-profile", name = "elastic-userprofile")
public interface UserManager {
     @GetMapping(FIND_ALL)
     ResponseEntity<List<UserProfile>> findAll();
}
```



##  RabbitMQ Kurulumu;

    RabbitMQ bir mesaj kuyruğu yazılımıdır ve dağıtık sistemler arasında iletişimi sağlamak için kullanılır.
    RabbitMQ iki port ile çalışır. 5672, 15672 bu portlardan;
     1- 5672 olan port Rest isteklreini işlemek için kullanılır, bu nedenle Spring Boot bu porta bağlanır.
     2- 15672 olan port arayüz webUI kısmıdır. Yönetim ekranı burasıdır.

```bash
 docker run -d --name java13-rabbit -p 5672:5672 -p 15672:15672  -e RABBITMQ_DEFAULT_USER=admin -e RABBITMQ_DEFAULT_PASS=root rabbitmq:3-management
```

     1- İlgili bağımlılıklar eklenir.
           dependencies.gradle -->  implementation 'org.springframework.boot:spring-boot-starter-amqp:3.2.2'
           build.gradle --> dependencies {implementation libs.springBootStarterAmqp}

     2- Yapılandırma dosyası oluşturulur. Bu dosya, RabbitMQ'i kullanmak için gerekli ayarlamaları yapar.

```yml
spring:
  rabbitmq:
    host: localhost
    port: 5672
    username: admin
    password: root
```
     3- Config dosyası oluşturulur. Bu dosya RabbitMQ ile ilgili ayarlamaları yapar. Queue, exchange, binding key 
        tanımlanır. 
     4- RabbitMQ paketi microservicelerde açılır. Consumer, model, producer ile sendmessage ve listener
        methodları yazılır. Modellerin microservislerde birebir aynı olması gereklidir.
     5- RabbitMQ deserilizable işleminde getirilen yeni güvenlik config nedeniyle şu ENV'nin eklenmesi
        gereklidir; "SPRING_AMQP_DESERIALIZATION_TRUST_ALL=true"


##  Config Server Kullanımı;
    
    Config server, mikroservice mimarisi içinde kullanılan ve uygulamaların yapılandırma bilgilerini merkezi 
    bir yerden almasını sağlayan bir bileşendir. Bu, farklı uygulamaların aynı yapılandırma ayarlarını paylaşmasını 
    ve yapılandırma değişikliklerinin tüm uygulamalara otomatik olarak yansıtılmasını sağlar. 

     1- İlgili bağımlılıklar eklenir. 
          dependencies.gradle;
            springCloudConfig:              "org.springframework.cloud:spring-cloud-starter-config:${versions.springCloud}",
            springCloudConfigServer:        "org.springframework.cloud:spring-cloud-config-server:${versions.springCloud}",
            springCloudConfigClient:        "org.springframework.cloud:spring-cloud-config-client:${versions.springCloud}",
        
     2- Config Server modülü oluşturulur, aşağıdaki bağımlılıklar eklenir.
           build.gradle;    dependencies {implementation libs.springCloudConfig
                                          implementation libs.springCloudConfigServer}  eklenir.

        Microserviceler için de aşağıdaki bağımlılık eklenir. 
           build.gradle;    dependencies {implementation libs.springCloudConfigClient}

     3- Main class'a eklenen @EnaleConfigServer anotasyonu ile Spring Boot tabanlı bir Config Server uygulamasını başlatmak 
        için gereken temel yapıyı sağlanır. Bu uygulama, yapılandırma dosyalarını dinamik olarak yönetmek için kullanılabilir 
        ve diğer Spring Boot uygulamalarına bu yapılandırma dosyalarını sağlayabilir.

```java
@SpringBootApplication
@EnableConfigServer
public class ConfigServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApplication.class, args);
    }
}
```
     

      4- Config Server resources file içerisine "config-repo" adlı bir klasör açılır. Bu klasör içerisine yönetilecek microservicelerin
         application.yml dosyaları eklenir ve yapılandırma dosyalarındaki gerekli değişiklikler ve düzenlemeler artık
         buradan gerçekleşir.
         Config Server mikroservisinin de yapılandırma dosyası aşağıdaki şekilde oluşturulur;

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
      5- Microserviceler içerisinde yer alan yapılandırma dosyaları da aşağıdaki şekilde oluşturulur;

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

