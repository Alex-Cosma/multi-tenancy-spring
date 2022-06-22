create table if not exists tenant_pool
(
    id           bigint                   not null
        constraint "tenant_poolPK"
        primary key,
    date_created timestamp with time zone not null,
    schema    varchar(255)             not null,
    used         boolean                  not null
);

create unique index if not exists tenant_id_uniq_1458124480054
    on tenant_pool (schema);

