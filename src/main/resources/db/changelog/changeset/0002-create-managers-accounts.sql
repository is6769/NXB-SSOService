--liquibase formatted sql
--changeset is6769:0002-create-managers-accounts.sql

INSERT INTO app_users(msisdn,role,reference_id)
VALUES (
        '77777777777',
        'ROLE_MANAGER',
        null
       );