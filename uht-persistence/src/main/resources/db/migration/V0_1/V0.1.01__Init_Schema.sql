--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: t_document; Type: TABLE; Schema: public; Owner: uht; Tablespace: 
--

CREATE TABLE t_document (
    uuid character varying(255) NOT NULL,
    datecreated timestamp without time zone DEFAULT now(),
    lastmodified timestamp without time zone,
    category character varying(255) NOT NULL,
    content bytea NOT NULL,
    filename character varying(255) NOT NULL,
    size bigint NOT NULL,
    title character varying(255) NOT NULL
);


ALTER TABLE public.t_document OWNER TO uht;

--
-- Name: t_news; Type: TABLE; Schema: public; Owner: uht; Tablespace: 
--

CREATE TABLE t_news (
    uuid character varying(255) NOT NULL,
    datecreated timestamp without time zone DEFAULT now(),
    lastmodified timestamp without time zone,
    content text NOT NULL,
    publishdate timestamp without time zone,
    title character varying(255) NOT NULL,
    visible boolean NOT NULL,
    user_uuid character varying(255) NOT NULL
);


ALTER TABLE public.t_news OWNER TO uht;

--
-- Name: t_player; Type: TABLE; Schema: public; Owner: uht; Tablespace: 
--

CREATE TABLE t_player (
    uuid character varying(255) NOT NULL,
    datecreated timestamp without time zone DEFAULT now(),
    lastmodified timestamp without time zone,
    birthdate timestamp without time zone,
    firstname character varying(255) NOT NULL,
    lastname character varying(255) NOT NULL
);


ALTER TABLE public.t_player OWNER TO uht;

--
-- Name: t_team; Type: TABLE; Schema: public; Owner: uht; Tablespace: 
--

CREATE TABLE t_team (
    uuid character varying(255) NOT NULL,
    datecreated timestamp without time zone DEFAULT now(),
    lastmodified timestamp without time zone,
    enabled boolean NOT NULL,
    name character varying(255) NOT NULL,
    shortname character varying(8) NOT NULL
);


ALTER TABLE public.t_team OWNER TO uht;

--
-- Name: t_team_player; Type: TABLE; Schema: public; Owner: uht; Tablespace: 
--

CREATE TABLE t_team_player (
    uuid character varying(255) NOT NULL,
    datecreated timestamp without time zone DEFAULT now(),
    lastmodified timestamp without time zone,
    number character varying(4),
    role character varying(255) NOT NULL,
    player_uuid character varying(255) NOT NULL,
    team_uuid character varying(255) NOT NULL
);


ALTER TABLE public.t_team_player OWNER TO uht;

--
-- Name: t_user; Type: TABLE; Schema: public; Owner: uht; Tablespace: 
--

CREATE TABLE t_user (
    uuid character varying(255) NOT NULL,
    datecreated timestamp without time zone DEFAULT now(),
    lastmodified timestamp without time zone,
    email character varying(255) NOT NULL,
    enabled boolean NOT NULL,
    firstname character varying(255) NOT NULL,
    lastlogindate timestamp without time zone,
    lastname character varying(255) NOT NULL,
    login character varying(255) NOT NULL,
    password character varying(255) NOT NULL
);


ALTER TABLE public.t_user OWNER TO uht;

--
-- Name: t_user_authority; Type: TABLE; Schema: public; Owner: uht; Tablespace: 
--

CREATE TABLE t_user_authority (
    user_uuid character varying(255) NOT NULL,
    authority character varying(255)
);


ALTER TABLE public.t_user_authority OWNER TO uht;

--
-- Name: t_document_pkey; Type: CONSTRAINT; Schema: public; Owner: uht; Tablespace: 
--

ALTER TABLE ONLY t_document
    ADD CONSTRAINT t_document_pkey PRIMARY KEY (uuid);


--
-- Name: t_news_pkey; Type: CONSTRAINT; Schema: public; Owner: uht; Tablespace: 
--

ALTER TABLE ONLY t_news
    ADD CONSTRAINT t_news_pkey PRIMARY KEY (uuid);


--
-- Name: t_player_pkey; Type: CONSTRAINT; Schema: public; Owner: uht; Tablespace: 
--

ALTER TABLE ONLY t_player
    ADD CONSTRAINT t_player_pkey PRIMARY KEY (uuid);


--
-- Name: t_team_pkey; Type: CONSTRAINT; Schema: public; Owner: uht; Tablespace: 
--

ALTER TABLE ONLY t_team
    ADD CONSTRAINT t_team_pkey PRIMARY KEY (uuid);


--
-- Name: t_team_player_pkey; Type: CONSTRAINT; Schema: public; Owner: uht; Tablespace: 
--

ALTER TABLE ONLY t_team_player
    ADD CONSTRAINT t_team_player_pkey PRIMARY KEY (uuid);


--
-- Name: t_user_pkey; Type: CONSTRAINT; Schema: public; Owner: uht; Tablespace: 
--

ALTER TABLE ONLY t_user
    ADD CONSTRAINT t_user_pkey PRIMARY KEY (uuid);


--
-- Name: uk_53q42ugkktpyx4tmmen8vafwm; Type: CONSTRAINT; Schema: public; Owner: uht; Tablespace: 
--

ALTER TABLE ONLY t_team_player
    ADD CONSTRAINT uk_53q42ugkktpyx4tmmen8vafwm UNIQUE (team_uuid, player_uuid);


--
-- Name: uk_70ue2h6a1dh8u84n6eclf1otc; Type: CONSTRAINT; Schema: public; Owner: uht; Tablespace: 
--

ALTER TABLE ONLY t_team
    ADD CONSTRAINT uk_70ue2h6a1dh8u84n6eclf1otc UNIQUE (name);


--
-- Name: uk_j6oekeo96ojgrja7wq1gocui6; Type: CONSTRAINT; Schema: public; Owner: uht; Tablespace: 
--

ALTER TABLE ONLY t_team
    ADD CONSTRAINT uk_j6oekeo96ojgrja7wq1gocui6 UNIQUE (shortname);


--
-- Name: uk_sa4df0alibrd29bxd4c9e5371; Type: CONSTRAINT; Schema: public; Owner: uht; Tablespace: 
--

ALTER TABLE ONLY t_user
    ADD CONSTRAINT uk_sa4df0alibrd29bxd4c9e5371 UNIQUE (login);


--
-- Name: fk_74iacq81mvxbxhq2u0dgee7w6; Type: FK CONSTRAINT; Schema: public; Owner: uht
--

ALTER TABLE ONLY t_team_player
    ADD CONSTRAINT fk_74iacq81mvxbxhq2u0dgee7w6 FOREIGN KEY (player_uuid) REFERENCES t_player(uuid);


--
-- Name: fk_7o2kjeaecihcwod4tv783all5; Type: FK CONSTRAINT; Schema: public; Owner: uht
--

ALTER TABLE ONLY t_user_authority
    ADD CONSTRAINT fk_7o2kjeaecihcwod4tv783all5 FOREIGN KEY (user_uuid) REFERENCES t_user(uuid);


--
-- Name: fk_na1q8i4eeox12uwpyc10e2kdr; Type: FK CONSTRAINT; Schema: public; Owner: uht
--

ALTER TABLE ONLY t_team_player
    ADD CONSTRAINT fk_na1q8i4eeox12uwpyc10e2kdr FOREIGN KEY (team_uuid) REFERENCES t_team(uuid);


--
-- Name: fk_rtsqhn334mrptd2fughbb2yks; Type: FK CONSTRAINT; Schema: public; Owner: uht
--

ALTER TABLE ONLY t_news
    ADD CONSTRAINT fk_rtsqhn334mrptd2fughbb2yks FOREIGN KEY (user_uuid) REFERENCES t_user(uuid);


--
-- PostgreSQL database dump complete
--

