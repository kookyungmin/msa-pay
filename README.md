# Happy Pay

MSA, 헥사고날 아키텍처 기반 간편 결제 시스템

<!-- prettier-ignore-start -->
![SpringBoot](https://shields.io/badge/springboot-black?logo=springboot&style=for-the-badge%22)
![Docker](https://shields.io/badge/docker-black?logo=docker&style=for-the-badge%22)
![Kafka](https://shields.io/badge/kafka-black?logo=apache-kafka&style=for-the-badge%22)
![Mysql](https://shields.io/badge/mysql-black?logo=mysql&style=for-the-badge%22)
![Redis](https://shields.io/badge/redis-black?logo=redis&style=for-the-badge%22)
<!-- prettier-ignore-end -->

### System Requirements

- [java] 17
- [springboot] 3.5.8
- [docker] 29.1.1
- [kafka] 7.3.10
- [zookeeper] 7.3.10
- [redis] 7.2.5
- [mysql] 8.0.33

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

### Banking Service

고객의 계좌 정보 등록, 등록된 계좌 정보 조회, 입/출금, 거래내역 조회 등의 기능을 제공하는 서비스

![Banking Sequence Example](image/Banking_Sequence_Example.png)

### Money Service

고객의 충전 잔액(머니) CRUD, 충전 내역 조회 등의 기능을 제공하는 서비스

![Money_Sequence_Example](image/Money_Sequence_Example.png)

### Remittance Service

고객 간 송금 기능 및 송금 내역 정보 조회 등의 기능을 제공하는 서비스

![Remittance_Sequence_Example](image/Remittance_Sequence_Example.png)

### Payment Service

가맹점에서 Happy Pay 를 이용한 간편 결제 및 결제 내역 조회 등의 기능을 제공하는 서비스

![Payment_Sequence_Example](image/Payment_Sequence_Example.png)

### Settlement Service

완료된 결제 내역을 기준으로 가맹점에 정산된 금액을 입금하고, 수수료 수취를 위한 기능을 제공하는 서비스

![Settlement_Sequence_Example](image/Settlement_Sequence_Example.png)








