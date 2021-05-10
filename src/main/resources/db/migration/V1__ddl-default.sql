----------------------
--- 従業員
----------------------
drop table if exists "eim".employee;
create table "eim".employee (
  id bigserial not null
  , employee_id character varying(6) not null
  , mail character varying(255)
  , password character varying(255)
  , login_failure_count integer not null
  , last_name character varying(30) not null
  , first_name character varying(30) not null
  , last_name_kana character varying(80) not null
  , first_name_kana character varying(80) not null
  , birthday character varying(8)
  , birthplace character varying(30)
  , sex smallint
  , hire_date character varying(8)
  , main_organization jsonb default null
  , position integer not null
  , authority_type character varying(30) not null
  , status_type character varying(30) not null
  , operator character varying(255)
  , created_date timestamp(6) without time zone
  , last_modified_date timestamp(6) without time zone
  , unique (employee_id)
  , unique (mail)
  , primary key (id)
);

----------------------
--- 部署マスター
----------------------
drop table if exists "eim".department_master;
create table "eim".department_master (
  id serial not null
  , department_code character varying(2)
  , department_name character varying(30)
  , department_authority character varying(30)
  , section_code character varying(2)
  , section_name character varying(30)
  , group_code character varying(2)
  , group_name character varying(30)
  , operator character varying(255)
  , created_date timestamp(6) without time zone
  , last_modified_date timestamp(6) without time zone
  , unique (department_code, section_code, group_code)
  , primary key (id)
);
