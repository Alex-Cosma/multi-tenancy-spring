create table if not exists book
(
    id                   bigint                not null
        constraint "bookPK"
        primary key,
    title            varchar(255)          not null
        constraint tenant_tenant_id_key
        unique
);
