# MSA Pay

MSA, 헥사고날 아키텍처 기반 간편 결제 시스템

<!-- prettier-ignore-start -->
![SpringBoot](https://shields.io/badge/springboot-black?logo=springboot&style=for-the-badge%22)
![Docker](https://shields.io/badge/docker-black?logo=docker&style=for-the-badge%22)
![Kafka](https://shields.io/badge/kafka-black?logo=apache-kafka&style=for-the-badge%22)
![Mysql](https://shields.io/badge/mysql-black?logo=mysql&style=for-the-badge%22)
<!-- prettier-ignore-end -->

### System Requirements

- [java] 17
- [springboot] 3.5.8
- [axonframework] 4.12.2
- [docker] 29.1.1
- [kafka] 7.3.10
- [kafka-ui] 0.7.2 
- [zookeeper] 7.3.10
- [mysql] 8.0.33
- [vault] 1.21.0

### Overview

![Overall Architecture](image/Overall_Architecture_Image.png)

일반적인 간편결제 도메인을 주제로, MSA 를 중점적으로 학습하기 위한 교육용 프로젝트

회원(Membership), 뱅킹(Banking), 머니(Money), 송금(Remittance), 결제(Payment), 정산(Settlement) 6개의 서비스로 구성되어 있으며, 각각의 독립적인 프로젝트로 구성   

각 서비스에서는 기본적인 기능을 먼저 Hexagonal Architecture 로 구현하고, 일부 기능들에 EDA, CQRS, Event Sourcing, Saga Pattern 등을 적용


![Monolithic System](image/Overview2.png)
![MSA System](image/Overview1.png)

### Membership Service

고객의 회원 가입, 회원 정보 변경, 회원 정보 조회 등의 기능을 제공하는 서비스

![Membership Sequence Example](image/Membership_Sequence_Example.png)

```
# 이미지 빌드
./gradlew :membership-service:jibDockerBuild

# 이미지 빌드 후 dockerhub push
./gradle :membership-service:jib
```

### Banking Service

고객의 계좌 정보 등록, 등록된 계좌 정보 조회, 입/출금, 거래내역 조회 등의 기능을 제공하는 서비스

![Banking Sequence Example](image/Banking_Sequence_Example.png)

```
# 이미지 빌드
./gradlew :banking-service:jibDockerBuild

# 이미지 빌드 후 dockerhub push
./gradle :banking-service:jib
```

### Money Service

고객의 충전 잔액(머니) CRUD, 충전 내역 조회 등의 기능을 제공하는 서비스

![Money_Sequence_Example](image/Money_Sequence_Example.png)

```
# 이미지 빌드
./gradlew :money-service:jibDockerBuild

# 이미지 빌드 후 dockerhub push
./gradle :money-service:jib
```

### Remittance Service

고객 간 송금 기능 및 송금 내역 정보 조회 등의 기능을 제공하는 서비스

![Remittance_Sequence_Example](image/Remittance_Sequence_Example.png)

```
# 이미지 빌드
./gradlew :remittance-service:jibDockerBuild

# 이미지 빌드 후 dockerhub push
./gradle :remittance-service:jib
```

### Payment Service

가맹점에서 Happy Pay 를 이용한 간편 결제 및 결제 내역 조회 등의 기능을 제공하는 서비스

![Payment_Sequence_Example](image/Payment_Sequence_Example.png)

```
# 이미지 빌드
./gradlew :payment-service:jibDockerBuild

# 이미지 빌드 후 dockerhub push
./gradle :payment-service:jib
``

### Settlement Service

완료된 결제 내역을 기준으로 가맹점에 정산된 금액을 입금하고, 수수료 수취를 위한 기능을 제공하는 서비스

![Settlement_Sequence_Example](image/Settlement_Sequence_Example.png)

```
# 이미지 빌드
./gradlew :settlement-service:jibDockerBuild

# 이미지 빌드 후 dockerhub push
./gradle :settlement-service:jib
``


### Axon Framework

Axon Framework 프로세스

```
[Client]
    ↓
[CommandGateway]
    ↓
[Aggregate @CommandHandler]
    ↓ apply()
[EventSourcingHandler]   ← 즉시 상태 변경
    ↓
[EventStore (Axon Server)]
    ↓ commit
[EventProcessor]
    ↓
[@EventHandler / Saga(비즈니스 로직, 흐름 제어) / Projection(읽기용 DB Write)]
```

Axon Framework 로 EDA(Event Data Architecture) 를 구축하고,

Axon Server(오케스트레이터)가 EventStore 를 통해 Aggregate 의 변경 이력(Event)들을 저장한다. 


Kafka 를 활용한 Saga Pattern 충전 프로세스

```
//1. 해피 페이 충전 요청 [머니 서비스]

//2. AxonRechargeMoneyEvent Publish (Saga Start) [머니 서비스]

//3. 뱅크 서비스로 계좌 상태 확인 [머니 서비스 -> 뱅킹 서비스로의 HTTP Request]

//3-1. 계좌 상태 정상 아니면, Sage End [머니 서비스]

//3-2. 계좌 상태 정상이면, 개인 계좌 -> 법인 계좌 송금 신청 [머니 서비스 -> 뱅킹 서비스로의 Kafka Event Produce]
// ㄴ 3-2-1. Kakfa Producer -> 펌뱅킹 Event Produce (topic: happypay.task.firmbanking) [머니 서비스]
// ㄴ 3-2-2. Kakfa Consumer -> 펌뱅킹 Event Consume (topic: happypay.task.firmbanking) [뱅킹 서비스]
// ㄴ 3-2.3. AxonFirmBankingRequestEvent Publish (Saga Start) [뱅킹 서비스]
// ㄴ 3-2.4. 펌 뱅킹 비즈니스 로직 수행 완료 (Sage End) [뱅킹 서비스]
// ㄴ 3-2.5. Kafka Producer -> 펌뱅킹 완료 Event Produce (topic: happypay.task.firmbanking.result) [뱅킹 서비스]
// ㄴ 3-2.6. Kafka Cosnumber -> 펌뱅킹 완료 Event Consume (topic: happypay.task.firmbanking.result) [머니 서비스]
// ㄴ 3-2.7. AxonFirmBankingResultEvent Publish [머니 서비스]

//4. 펌뱅킹 성공 시 AxonIncreaseMemberMoneyEvent Publish -> Projection 에서 Read DB update 및 Saga End [머니 서비스]

//5. 만약, AxonIncreaseMemberMoneyEvent 저장 시 에러 발생하면 보상 트랜잭션 실행
```



### Kafka 명령

토픽 리스트 보기

```
kafka-topics --list --bootstrap-server localhost:9092
```

Topic 생성

```
kafka-topics --create --bootstrap-server localhost:9092 \
    --partitions 1  \
    --replication-factor 1 \
    --config min.insync.replicas=1 \
    --topic happypay.logging.out.stdout
```

```
kafka-topics --create --bootstrap-server localhost:9092 \
    --partitions 1  \
    --replication-factor 1 \
    --config min.insync.replicas=1 \
    --topic happypay.task.recharge
```

```
kafka-topics --create --bootstrap-server localhost:9092 \
    --partitions 1  \
    --replication-factor 1 \
    --config min.insync.replicas=1 \
    --topic happypay.task.recharge.result
```

```
kafka-topics --create --bootstrap-server localhost:9092 \
    --partitions 1  \
    --replication-factor 1 \
    --config min.insync.replicas=1 \
    --topic happypay.task.firmbanking
```

```
kafka-topics --create --bootstrap-server localhost:9092 \
    --partitions 1  \
    --replication-factor 1 \
    --config min.insync.replicas=1 \
    --topic happypay.task.firmbanking.result
```

### Vault Policy

```
path "sys/mounts/kv-v1" {
    capabilities = ["update"]  
}

path "sys/mounts" {
    capabilities = ["read"]  
}

path "kv-v1/*" {
  capabilities = ["create", "update", "read", "delete", "list"]  
}
```

```
vault write auth/token/create policies=default
```

### Graceful Shutdown

```
SIGINT: Ctrl+C or Intellij Stop
SIGTERM: 15 (process kill 명령 했을 때)
SIGKILL: kill -9 PID

SIGINT 와 SIGTERM 명령을 받았을 때, graceful shutdown 이 되게 구성해야 함

-> 종료 신호 이후의 요청 block (Load Balancer ..)
-> 그 전에 들어온 요청은 정상적으로 처리할 때 까지 일정 시간 대기 하고, 카프카 Consumer 같은 경우는 해당 작업이 정상적으로 종료 되었을 때 commit 되게 처리 -> 중복이 생길 수 있기에 멱등키 등을 이용
-> 다시 APP 기동 시 JVM 빈들이 모두 정상적으로 로드 된 후 요청 처리하게 구성

springboot 설정 (application.yml)

server:
    shutdown: grenceful
spring:
    lifecycle:
        timeout-per-shutdown-phase: 30s
        
 K8S Pod 설정
 terminationGracePeriodSeconds: 30s
```



