drop table if exists "eim".audit_event;
create table "eim".audit_event (
  id bigserial not null
  , occurred_time timestamp(6) without time zone
  , category character varying(30)
  , message character varying(1024)
  , username character varying
  , primary key (id)
);
