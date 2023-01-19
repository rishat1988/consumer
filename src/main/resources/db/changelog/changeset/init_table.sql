--liquibase formatted sql

--changeset rishat:create-multiple-tables splitStatements:true endDelimiter:;

create table activation_code
(
    activation_code_id bigserial not null,
    create_datetime    timestamp,
    name               varchar(255),
    tariff_id          int8      not null,
    is_active         boolean   not null,
    primary key (activation_code_id)
);