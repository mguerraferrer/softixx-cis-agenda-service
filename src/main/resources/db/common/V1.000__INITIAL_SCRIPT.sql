-- SCHEMA: agenda
DROP SCHEMA IF EXISTS agenda CASCADE;
CREATE SCHEMA agenda
    AUTHORIZATION postgres;
	
-- Table: agenda.appointment
DROP SEQUENCE IF EXISTS agenda.appointment_id_seq;
CREATE SEQUENCE agenda.appointment_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

ALTER SEQUENCE agenda.appointment_id_seq
    OWNER TO postgres;
	
DROP TABLE IF EXISTS agenda.appointment;
CREATE TABLE agenda.appointment
(
    id bigint NOT NULL DEFAULT nextval('agenda.appointment_id_seq'::regclass),
    person_id bigint NOT NULL,
	doctor_speciality_id bigint,
    doctor_clinical_entity_speciality_id bigint,
    private_practice_service_id bigint,
    doctor_clinical_entity_service_id bigint,
    appointment_id bigint,
    appointment_type character varying(100) COLLATE pg_catalog."default" NOT NULL,
    origin character varying(100) COLLATE pg_catalog."default" NOT NULL,
    status character varying(100) COLLATE pg_catalog."default" NOT NULL,
    confirmation character varying(100) COLLATE pg_catalog."default" NOT NULL,
    folio character varying(50) COLLATE pg_catalog."default" NOT NULL,
    appointment_date date NOT NULL,
    start_time time without time zone NOT NULL,
    end_time time without time zone NOT NULL,
    month integer NOT NULL,
    cancelled_by character varying(100) COLLATE pg_catalog."default",
    rescheduled_by character varying(100) COLLATE pg_catalog."default",
    another_person_name character varying(100) COLLATE pg_catalog."default",
    relationship_id bigint,
    additional_info text COLLATE pg_catalog."default",
    CONSTRAINT appointment_pk PRIMARY KEY (id),
    CONSTRAINT appointment_uq UNIQUE (folio),
    CONSTRAINT appointment_fk FOREIGN KEY (appointment_id)
        REFERENCES agenda.appointment (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = TRUE
)
TABLESPACE pg_default;

ALTER TABLE agenda.appointment
    OWNER to postgres;

COMMENT ON COLUMN agenda.appointment.appointment_id
    IS 'Id of the original appointment in case it has been rescheduled';

-- Table: agenda.appointment_notification
DROP SEQUENCE IF EXISTS agenda.appointment_notification_id_seq;
CREATE SEQUENCE agenda.appointment_notification_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

ALTER SEQUENCE agenda.appointment_notification_id_seq
    OWNER TO postgres;
	
DROP TABLE IF EXISTS agenda.appointment_notification;
CREATE TABLE agenda.appointment_notification
(
    id bigint NOT NULL DEFAULT nextval('agenda.appointment_notification_id_seq'::regclass),
    appointment_id bigint NOT NULL,
    notification_type character varying(100) COLLATE pg_catalog."default",
    notified_emails character varying(500) COLLATE pg_catalog."default",
    notified_mobiles character varying(100) COLLATE pg_catalog."default",
    notification_date timestamp without time zone NOT NULL,
    CONSTRAINT appointment_notification_pk PRIMARY KEY (id),
    CONSTRAINT appointment_notification_fk FOREIGN KEY (appointment_id)
        REFERENCES agenda.appointment (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = TRUE
)
TABLESPACE pg_default;

ALTER TABLE agenda.appointment_notification
    OWNER to postgres;

DROP INDEX IF EXISTS agenda.appointment_notification_idx;
CREATE INDEX appointment_notification_idx
    ON agenda.appointment_notification USING btree
    (appointment_id)
    TABLESPACE pg_default;

-- Table: agenda.appointment_reminder
DROP SEQUENCE IF EXISTS agenda.appointment_reminder_id_seq;
CREATE SEQUENCE agenda.appointment_reminder_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

ALTER SEQUENCE agenda.appointment_reminder_id_seq
    OWNER TO postgres;
	
DROP TABLE IF EXISTS agenda.appointment_reminder;
CREATE TABLE agenda.appointment_reminder
(
    id bigint NOT NULL DEFAULT nextval('agenda.appointment_reminder_id_seq'::regclass),
    appointment_id bigint NOT NULL,
    notification_type character varying(100) COLLATE pg_catalog."default",
    mails_to_notify character varying(500) COLLATE pg_catalog."default" NOT NULL,
    mobiles_to_notify character varying(100) COLLATE pg_catalog."default" NOT NULL,
    reminder_date timestamp without time zone NOT NULL,
    CONSTRAINT appointment_reminder_pk PRIMARY KEY (id),
    CONSTRAINT appointment_reminder_fk FOREIGN KEY (appointment_id)
        REFERENCES agenda.appointment (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = TRUE
)
TABLESPACE pg_default;

ALTER TABLE agenda.appointment_reminder
    OWNER to postgres;

DROP INDEX IF EXISTS agenda.appointment_reminder_idx;
CREATE INDEX appointment_reminder_idx
    ON agenda.appointment_reminder USING btree
    (appointment_id)
    TABLESPACE pg_default;
	
-- Table: agenda.medical_schedule
DROP SEQUENCE IF EXISTS agenda.medical_schedule_id_seq;
CREATE SEQUENCE agenda.medical_schedule_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

ALTER SEQUENCE agenda.medical_schedule_id_seq
    OWNER TO postgres;
	
DROP TABLE IF EXISTS agenda.medical_schedule;
CREATE TABLE agenda.medical_schedule
(
    id bigint NOT NULL DEFAULT nextval('agenda.medical_schedule_id_seq'::regclass),
    doctor_id bigint NOT NULL,
    clinical_entity_id bigint,
    private_practice_id bigint,
    end_date date NOT NULL,
    active boolean DEFAULT true,
    CONSTRAINT medical_schedule_pk PRIMARY KEY (id),
    CONSTRAINT medical_schedule_uq1 UNIQUE (doctor_id, clinical_entity_id),
    CONSTRAINT medical_schedule_uq2 UNIQUE (doctor_id, private_practice_id)
)
WITH (
    OIDS = TRUE
)
TABLESPACE pg_default;

ALTER TABLE agenda.medical_schedule
    OWNER to postgres;

DROP INDEX IF EXISTS agenda.medical_schedule_idx1;
CREATE INDEX medical_schedule_idx1
    ON agenda.medical_schedule USING btree
    (doctor_id, clinical_entity_id)
    TABLESPACE pg_default;
	
DROP INDEX IF EXISTS agenda.medical_schedule_idx2;
CREATE INDEX medical_schedule_idx2
    ON agenda.medical_schedule USING btree
    (doctor_id, private_practice_id)
    TABLESPACE pg_default;
	
-- Table: agenda.medical_schedule_nwd
DROP SEQUENCE IF EXISTS agenda.medical_schedule_nwd_id_seq;
CREATE SEQUENCE agenda.medical_schedule_nwd_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

ALTER SEQUENCE agenda.medical_schedule_nwd_id_seq
    OWNER TO postgres;
	
DROP TABLE IF EXISTS agenda.medical_schedule_nwd;
CREATE TABLE agenda.medical_schedule_nwd
(
    id bigint NOT NULL DEFAULT nextval('agenda.medical_schedule_nwd_id_seq'::regclass),
    medical_schedule_id bigint NOT NULL,
    nwd date NOT NULL,
    year integer,
    month integer,
    day integer,
    CONSTRAINT medical_schedule_nwd_pk PRIMARY KEY (id),
    CONSTRAINT medical_schedule_nwd_uq UNIQUE (medical_schedule_id, nwd),
    CONSTRAINT medical_schedule_nwd_fk1 FOREIGN KEY (medical_schedule_id)
        REFERENCES agenda.medical_schedule (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = TRUE
)
TABLESPACE pg_default;

ALTER TABLE agenda.medical_schedule_nwd
    OWNER to postgres;

DROP INDEX IF EXISTS agenda.medical_schedule_nwd_idx1;
CREATE INDEX medical_schedule_nwd_idx1
    ON agenda.medical_schedule_nwd USING btree
    (medical_schedule_id)
    TABLESPACE pg_default;
   
DROP INDEX IF EXISTS agenda.medical_schedule_nwd_idx2;
CREATE INDEX medical_schedule_nwd_idx2
    ON agenda.medical_schedule_nwd USING btree
    (medical_schedule_id, nwd)
    TABLESPACE pg_default;
	
-- Table: agenda.medical_schedule_planning
DROP SEQUENCE IF EXISTS agenda.medical_schedule_planning_id_seq;
CREATE SEQUENCE agenda.medical_schedule_planning_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

ALTER SEQUENCE agenda.medical_schedule_planning_id_seq
    OWNER TO postgres;
	
DROP TABLE IF EXISTS agenda.medical_schedule_planning;
CREATE TABLE agenda.medical_schedule_planning
(
    id bigint NOT NULL DEFAULT nextval('agenda.medical_schedule_planning_id_seq'::regclass),
    medical_schedule_id bigint NOT NULL,
    doctor_speciality_id bigint,
    doctor_clinical_entity_speciality_id bigint,
    agenda_visualization character varying(100) COLLATE pg_catalog."default" NOT NULL,
    fixed_schedule boolean DEFAULT false,
    appointment_duration integer,    
    CONSTRAINT medical_schedule_planning_pk PRIMARY KEY (id),
    CONSTRAINT medical_schedule_planning_uq1 UNIQUE (medical_schedule_id, doctor_speciality_id),
    CONSTRAINT medical_schedule_planning_uq2 UNIQUE (medical_schedule_id, doctor_clinical_entity_speciality_id),
    CONSTRAINT medical_schedule_planning_fk1 FOREIGN KEY (medical_schedule_id)
        REFERENCES agenda.medical_schedule (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = TRUE
)
TABLESPACE pg_default;

ALTER TABLE agenda.medical_schedule_planning
    OWNER to postgres;

DROP INDEX IF EXISTS agenda.medical_schedule_planning_idx1;
CREATE INDEX medical_schedule_planning_idx1
    ON agenda.medical_schedule_planning USING btree
    (medical_schedule_id)
    TABLESPACE pg_default;
   
DROP INDEX IF EXISTS agenda.medical_schedule_planning_idx2;
CREATE INDEX medical_schedule_planning_idx2
    ON agenda.medical_schedule_planning USING btree
    (medical_schedule_id, doctor_speciality_id)
    TABLESPACE pg_default;

DROP INDEX IF EXISTS agenda.medical_schedule_planning_idx3;
CREATE INDEX medical_schedule_planning_idx3
    ON agenda.medical_schedule_planning USING btree
    (medical_schedule_id, doctor_clinical_entity_speciality_id)
    TABLESPACE pg_default;

-- Table: agenda.medical_schedule_planning_fixed
DROP TABLE IF EXISTS agenda.medical_schedule_planning_fixed;
CREATE TABLE agenda.medical_schedule_planning_fixed
(
    id bigint NOT NULL,
    start_time time without time zone,
    end_time time without time zone,
    total_patients integer,
    total_extra_slot integer,
    days character varying(100) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT medical_schedule_planning_fixed_pk PRIMARY KEY (id),
    CONSTRAINT medical_schedule_planning_fixed_fk FOREIGN KEY (id)
        REFERENCES agenda.medical_schedule_planning (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        DEFERRABLE INITIALLY DEFERRED
)
WITH (
    OIDS = TRUE
)
TABLESPACE pg_default;

ALTER TABLE agenda.medical_schedule_planning_fixed
    OWNER to postgres;

-- Table: agenda.medical_schedule_planning_day
DROP SEQUENCE IF EXISTS agenda.medical_schedule_planning_day_id_seq;
CREATE SEQUENCE agenda.medical_schedule_planning_day_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

ALTER SEQUENCE agenda.medical_schedule_planning_day_id_seq
    OWNER TO postgres;
	
DROP TABLE IF EXISTS agenda.medical_schedule_planning_day;
CREATE TABLE agenda.medical_schedule_planning_day
(
    id bigint NOT NULL DEFAULT nextval('agenda.medical_schedule_planning_day_id_seq'::regclass),
    medical_schedule_planning_id bigint NOT NULL,
    day character varying(20) COLLATE pg_catalog."default" NOT NULL,
    start_time time without time zone,
    end_time time without time zone,
    total_patients integer,
    total_extra_slot integer,
    CONSTRAINT medical_schedule_planning_day_pk PRIMARY KEY (id),
    CONSTRAINT medical_schedule_planning_day_fk FOREIGN KEY (medical_schedule_planning_id)
        REFERENCES agenda.medical_schedule_planning (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = TRUE
)
TABLESPACE pg_default;

ALTER TABLE agenda.medical_schedule_planning_day
    OWNER to postgres;

DROP INDEX IF EXISTS agenda.medical_schedule_planning_day_idx;
CREATE INDEX medical_schedule_planning_day_idx
    ON agenda.medical_schedule_planning_day USING btree
    (medical_schedule_planning_id)
    TABLESPACE pg_default;