create table membership.membership (
    id bigint not null auto_increment,
    name varchar(255),
    email varchar(255),
    address varchar(255),
    is_corp bit not null,
    is_valid bit not null,
    primary key (id)
) engine=InnoDB;

create table banking.registered_bank_account (
     id bigint not null auto_increment,
     membership_id varchar(255),
     bank_name varchar(255),
     bank_account_number varchar(255),
     is_valid bit not null,
     primary key (id)
) engine=InnoDB;

alter table banking.registered_bank_account
    add constraint uk_member_bank_account
        unique (membership_id, bank_name, bank_account_number);

create table banking.firm_banking_request (
  id bigint not null auto_increment,
  error_msg varchar(255),
  from_bank_account_number varchar(255),
  from_bank_name varchar(255),
  money_amount integer not null,
  status enum ('FAILED','REQUESTED','SUCCESS'),
  to_bank_account_number varchar(255),
  to_bank_name varchar(255),
  primary key (id)
) engine=InnoDB;

create table money.member_money (
  balance integer not null,
  id bigint not null auto_increment,
  membership_id varchar(255),
  primary key (id)
) engine=InnoDB;

create table money.money_changing_request (
    money_amount integer not null,
    id bigint not null auto_increment,
    message varchar(255),
    target_membership_id varchar(255),
    request_type enum ('DECREASE','INCREASE'),
    status enum ('CANCELLED','FAILED','REQUESTED','SUCCESS'),
    primary key (id)
) engine=InnoDB;

create table remittance.remittance_request (
    amount integer not null,
    id bigint not null,
    error_msg varchar(255),
    from_membership_id varchar(255),
    to_bank_account_number varchar(255),
    to_bank_name varchar(255),
    to_membership_id varchar(255),
    remittance_status enum ('FAILED','REQUESTED','SUCCESS'),
    remittance_type enum ('BANK','MEMBERSHIP'),
    primary key (id)
) engine=InnoDB;

CREATE TABLE money.token_entry (
     processor_name VARCHAR(255) NOT NULL,
     segment INT NOT NULL,
     owner VARCHAR(255),
     timestamp VARCHAR(255) NOT NULL,
     token BLOB,
     token_type VARCHAR(255),
     PRIMARY KEY (processor_name, segment)
);

alter table money.member_money add column event_stream_id varchar(255);

CREATE TABLE banking.token_entry (
   processor_name VARCHAR(255) NOT NULL,
   segment INT NOT NULL,
   owner VARCHAR(255),
   timestamp VARCHAR(255) NOT NULL,
   token BLOB,
   token_type VARCHAR(255),
   PRIMARY KEY (processor_name, segment)
);

alter table banking.firm_banking_request add column event_stream_id varchar(255);
alter table banking.registered_bank_account add column event_stream_id varchar(255);

create table banking.saga_entry (
    saga_id varchar(255) not null,
    saga_type varchar(255),
    revision varchar(255),
    serialized_saga blob,
    primary key (saga_id)
);

CREATE TABLE banking.association_value_entry (
     id BIGINT NOT NULL,
     association_key VARCHAR(255) NOT NULL,
     association_value VARCHAR(255),
     saga_id VARCHAR(255) NOT NULL,
     saga_type VARCHAR(255),
     PRIMARY KEY (id),
     INDEX idx_assoc_key_value (association_key, association_value),
     INDEX idx_saga_id_type (saga_id, saga_type)
);

CREATE TABLE banking.association_value_entry_seq (
    next_val BIGINT
);

INSERT INTO banking.association_value_entry_seq VALUES (1);

CREATE TABLE banking.saga_entry_seq (
    next_val BIGINT
);

INSERT INTO banking.saga_entry_seq VALUES (1);


create table money.saga_entry (
    saga_id varchar(255) not null,
    saga_type varchar(255),
    revision varchar(255),
    serialized_saga blob,
    primary key (saga_id)
);

CREATE TABLE money.association_value_entry (
    id BIGINT NOT NULL,
    association_key VARCHAR(255) NOT NULL,
    association_value VARCHAR(255),
    saga_id VARCHAR(255) NOT NULL,
    saga_type VARCHAR(255),
    PRIMARY KEY (id),
    INDEX idx_assoc_key_value (association_key, association_value),
    INDEX idx_saga_id_type (saga_id, saga_type)
);

CREATE TABLE money.association_value_entry_seq (
    next_val BIGINT
);

INSERT INTO money.association_value_entry_seq VALUES (1);

CREATE TABLE money.saga_entry_seq (
    next_val BIGINT
);

INSERT INTO money.saga_entry_seq VALUES (1);


insert into money.member_money(membership_id, balance)
select substring_index(from_bank_account_number, '-', -1), sum(money_amount)
from banking.firm_banking_request
group by from_bank_account_number;