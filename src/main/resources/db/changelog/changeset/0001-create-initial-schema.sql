--liquibase formatted sql
--changeset is6769:0001-create-initial-schema

create table if not exists app_users(
    id                      BIGSERIAL           PRIMARY KEY,
    msisdn                  TEXT                NOT NULL,
    role                    VARCHAR(200)        NOT NULL,
    reference_id            BIGINT
);
