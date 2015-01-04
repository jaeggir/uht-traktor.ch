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
-- Name: t_event; Type: TABLE; Schema: public; Owner: uht; Tablespace: 
--

CREATE TABLE t_event (
    uuid character varying(255) NOT NULL,
    datecreated timestamp without time zone DEFAULT now(),
    lastmodified timestamp without time zone,
    allday boolean NOT NULL,
    description character varying(255) NOT NULL,
    enddate timestamp without time zone NOT NULL,
    highlight boolean NOT NULL,
    link character varying(255),
    location character varying(64) NOT NULL,
    startdate timestamp without time zone NOT NULL,
    suhv boolean NOT NULL,
    title character varying(64) NOT NULL
);


ALTER TABLE public.t_event OWNER TO uht;

--
-- Name: t_event_t_team; Type: TABLE; Schema: public; Owner: uht; Tablespace: 
--

CREATE TABLE t_event_t_team (
    t_event_uuid character varying(255) NOT NULL,
    teams_uuid character varying(255) NOT NULL
);


ALTER TABLE public.t_event_t_team OWNER TO uht;

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
-- Name: t_suhv_club; Type: TABLE; Schema: public; Owner: uht; Tablespace: 
--

CREATE TABLE t_suhv_club (
    uuid character varying(255) NOT NULL,
    datecreated timestamp without time zone DEFAULT now(),
    lastmodified timestamp without time zone,
    firstsuhvseason integer NOT NULL,
    id integer NOT NULL,
    name character varying(128) NOT NULL
);


ALTER TABLE public.t_suhv_club OWNER TO uht;

--
-- Name: t_suhv_game; Type: TABLE; Schema: public; Owner: uht; Tablespace: 
--

CREATE TABLE t_suhv_game (
    uuid character varying(255) NOT NULL,
    datecreated timestamp without time zone DEFAULT now(),
    lastmodified timestamp without time zone,
    awayteamid integer NOT NULL,
    awayteamname character varying(128) NOT NULL,
    canceled boolean NOT NULL,
    competitiontype character varying(32) NOT NULL,
    c_date timestamp without time zone,
    eventtype character varying(32) NOT NULL,
    fieldsize character varying(64),
    forfait boolean NOT NULL,
    goalsaway integer NOT NULL,
    goalshome integer NOT NULL,
    hometeamid integer NOT NULL,
    hometeamname character varying(128) NOT NULL,
    id integer NOT NULL,
    leaguecode character varying(8) NOT NULL,
    leaguetext character varying(64),
    leaguetype character varying(8) NOT NULL,
    overtime boolean NOT NULL,
    penaltyshooting boolean NOT NULL,
    played boolean NOT NULL,
    c_round integer NOT NULL,
    gym_uuid character varying(255) NOT NULL,
    team_uuid character varying(255) NOT NULL
);


ALTER TABLE public.t_suhv_game OWNER TO uht;

--
-- Name: t_suhv_gym; Type: TABLE; Schema: public; Owner: uht; Tablespace: 
--

CREATE TABLE t_suhv_gym (
    uuid character varying(255) NOT NULL,
    datecreated timestamp without time zone DEFAULT now(),
    lastmodified timestamp without time zone,
    city character varying(255),
    country character varying(2),
    id integer NOT NULL,
    lat double precision NOT NULL,
    lng double precision NOT NULL,
    name character varying(255),
    street character varying(255),
    zip integer
);


ALTER TABLE public.t_suhv_gym OWNER TO uht;

--
-- Name: t_suhv_standings; Type: TABLE; Schema: public; Owner: uht; Tablespace: 
--

CREATE TABLE t_suhv_standings (
    uuid character varying(255) NOT NULL,
    datecreated timestamp without time zone DEFAULT now(),
    lastmodified timestamp without time zone,
    bar1 integer NOT NULL,
    bar2 integer NOT NULL,
    bar3 integer NOT NULL,
    competitiontype character varying(32),
    c_group character varying(32) NOT NULL,
    grouptext character varying(32),
    leaguecode integer NOT NULL,
    leaguetext character varying(128),
    leaguetype character varying(32),
    season integer NOT NULL,
    team_uuid character varying(255) NOT NULL
);


ALTER TABLE public.t_suhv_standings OWNER TO uht;

--
-- Name: t_suhv_standings_entry; Type: TABLE; Schema: public; Owner: uht; Tablespace: 
--

CREATE TABLE t_suhv_standings_entry (
    uuid character varying(255) NOT NULL,
    datecreated timestamp without time zone DEFAULT now(),
    lastmodified timestamp without time zone,
    clubid integer NOT NULL,
    defeats integer NOT NULL,
    defeatsovertime integer NOT NULL,
    games integer NOT NULL,
    goalsreceived integer NOT NULL,
    goalsscored integer NOT NULL,
    place integer NOT NULL,
    points integer NOT NULL,
    teamid integer NOT NULL,
    teamname character varying(128) NOT NULL,
    ties integer NOT NULL,
    wins integer NOT NULL,
    winsovertime integer NOT NULL,
    standings_uuid character varying(255) NOT NULL
);


ALTER TABLE public.t_suhv_standings_entry OWNER TO uht;

--
-- Name: t_suhv_team; Type: TABLE; Schema: public; Owner: uht; Tablespace: 
--

CREATE TABLE t_suhv_team (
    uuid character varying(255) NOT NULL,
    datecreated timestamp without time zone DEFAULT now(),
    lastmodified timestamp without time zone,
    description character varying(128),
    c_group character varying(32) NOT NULL,
    grouptext character varying(32) NOT NULL,
    id integer NOT NULL,
    leaguecode integer,
    season integer NOT NULL,
    teamname character varying(128) NOT NULL,
    teamromancounter character varying(4),
    club_uuid character varying(255) NOT NULL,
    standings_uuid character varying(255) NOT NULL
);


ALTER TABLE public.t_suhv_team OWNER TO uht;

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
-- Name: t_event_pkey; Type: CONSTRAINT; Schema: public; Owner: uht; Tablespace: 
--

ALTER TABLE ONLY t_event
    ADD CONSTRAINT t_event_pkey PRIMARY KEY (uuid);


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
-- Name: t_suhv_club_pkey; Type: CONSTRAINT; Schema: public; Owner: uht; Tablespace: 
--

ALTER TABLE ONLY t_suhv_club
    ADD CONSTRAINT t_suhv_club_pkey PRIMARY KEY (uuid);


--
-- Name: t_suhv_game_pkey; Type: CONSTRAINT; Schema: public; Owner: uht; Tablespace: 
--

ALTER TABLE ONLY t_suhv_game
    ADD CONSTRAINT t_suhv_game_pkey PRIMARY KEY (uuid);


--
-- Name: t_suhv_gym_pkey; Type: CONSTRAINT; Schema: public; Owner: uht; Tablespace: 
--

ALTER TABLE ONLY t_suhv_gym
    ADD CONSTRAINT t_suhv_gym_pkey PRIMARY KEY (uuid);


--
-- Name: t_suhv_standings_entry_pkey; Type: CONSTRAINT; Schema: public; Owner: uht; Tablespace: 
--

ALTER TABLE ONLY t_suhv_standings_entry
    ADD CONSTRAINT t_suhv_standings_entry_pkey PRIMARY KEY (uuid);


--
-- Name: t_suhv_standings_pkey; Type: CONSTRAINT; Schema: public; Owner: uht; Tablespace: 
--

ALTER TABLE ONLY t_suhv_standings
    ADD CONSTRAINT t_suhv_standings_pkey PRIMARY KEY (uuid);


--
-- Name: t_suhv_team_pkey; Type: CONSTRAINT; Schema: public; Owner: uht; Tablespace: 
--

ALTER TABLE ONLY t_suhv_team
    ADD CONSTRAINT t_suhv_team_pkey PRIMARY KEY (uuid);


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
-- Name: uk_120cok6dek8f5aklb4yqh3t1w; Type: CONSTRAINT; Schema: public; Owner: uht; Tablespace: 
--

ALTER TABLE ONLY t_suhv_team
    ADD CONSTRAINT uk_120cok6dek8f5aklb4yqh3t1w UNIQUE (id, season);


--
-- Name: uk_7gshuknepkj6oy1702fqwsr8r; Type: CONSTRAINT; Schema: public; Owner: uht; Tablespace: 
--

ALTER TABLE ONLY t_user
    ADD CONSTRAINT uk_7gshuknepkj6oy1702fqwsr8r UNIQUE (login);


--
-- Name: uk_7m26sod48nvwd090ohy741di4; Type: CONSTRAINT; Schema: public; Owner: uht; Tablespace: 
--

ALTER TABLE ONLY t_suhv_team
    ADD CONSTRAINT uk_7m26sod48nvwd090ohy741di4 UNIQUE (standings_uuid);


--
-- Name: uk_7n8x7eb8lnkx3cmaww1a0n6fs; Type: CONSTRAINT; Schema: public; Owner: uht; Tablespace: 
--

ALTER TABLE ONLY t_team
    ADD CONSTRAINT uk_7n8x7eb8lnkx3cmaww1a0n6fs UNIQUE (shortname);


--
-- Name: uk_dd3mwi5u0j6g2vw68sujjnr54; Type: CONSTRAINT; Schema: public; Owner: uht; Tablespace: 
--

ALTER TABLE ONLY t_suhv_game
    ADD CONSTRAINT uk_dd3mwi5u0j6g2vw68sujjnr54 UNIQUE (id);


--
-- Name: uk_dwanmaenaonj7jxj8fr5mfuw; Type: CONSTRAINT; Schema: public; Owner: uht; Tablespace: 
--

ALTER TABLE ONLY t_suhv_club
    ADD CONSTRAINT uk_dwanmaenaonj7jxj8fr5mfuw UNIQUE (id);


--
-- Name: uk_eeb5dwa84vaa3p7rcb3s66y7k; Type: CONSTRAINT; Schema: public; Owner: uht; Tablespace: 
--

ALTER TABLE ONLY t_team_player
    ADD CONSTRAINT uk_eeb5dwa84vaa3p7rcb3s66y7k UNIQUE (team_uuid, player_uuid);


--
-- Name: uk_f7e8wd9d6tfb4sfbe7de2awro; Type: CONSTRAINT; Schema: public; Owner: uht; Tablespace: 
--

ALTER TABLE ONLY t_suhv_team
    ADD CONSTRAINT uk_f7e8wd9d6tfb4sfbe7de2awro UNIQUE (id);


--
-- Name: uk_jjfob8lq6bxlkwuphm9t9ofrs; Type: CONSTRAINT; Schema: public; Owner: uht; Tablespace: 
--

ALTER TABLE ONLY t_team
    ADD CONSTRAINT uk_jjfob8lq6bxlkwuphm9t9ofrs UNIQUE (name);


--
-- Name: uk_mtbm3vgkch6gymxafq2dmw9io; Type: CONSTRAINT; Schema: public; Owner: uht; Tablespace: 
--

ALTER TABLE ONLY t_suhv_gym
    ADD CONSTRAINT uk_mtbm3vgkch6gymxafq2dmw9io UNIQUE (id);


--
-- Name: uk_rnxy1f6vt5x39mospi2mlnod0; Type: CONSTRAINT; Schema: public; Owner: uht; Tablespace: 
--

ALTER TABLE ONLY t_suhv_standings
    ADD CONSTRAINT uk_rnxy1f6vt5x39mospi2mlnod0 UNIQUE (team_uuid);


--
-- Name: uk_s5kn3e3h14v6bf7jn7vsvq5nv; Type: CONSTRAINT; Schema: public; Owner: uht; Tablespace: 
--

ALTER TABLE ONLY t_suhv_club
    ADD CONSTRAINT uk_s5kn3e3h14v6bf7jn7vsvq5nv UNIQUE (name);


--
-- Name: fk_2ax987itrmae65p6hv04eno0x; Type: FK CONSTRAINT; Schema: public; Owner: uht
--

ALTER TABLE ONLY t_event_t_team
    ADD CONSTRAINT fk_2ax987itrmae65p6hv04eno0x FOREIGN KEY (teams_uuid) REFERENCES t_team(uuid);


--
-- Name: fk_35nuridne52e0og7i0y5lm02w; Type: FK CONSTRAINT; Schema: public; Owner: uht
--

ALTER TABLE ONLY t_news
    ADD CONSTRAINT fk_35nuridne52e0og7i0y5lm02w FOREIGN KEY (user_uuid) REFERENCES t_user(uuid);


--
-- Name: fk_7m26sod48nvwd090ohy741di4; Type: FK CONSTRAINT; Schema: public; Owner: uht
--

ALTER TABLE ONLY t_suhv_team
    ADD CONSTRAINT fk_7m26sod48nvwd090ohy741di4 FOREIGN KEY (standings_uuid) REFERENCES t_suhv_standings(uuid);


--
-- Name: fk_9hnlx829topqoaygkwlhbx492; Type: FK CONSTRAINT; Schema: public; Owner: uht
--

ALTER TABLE ONLY t_suhv_game
    ADD CONSTRAINT fk_9hnlx829topqoaygkwlhbx492 FOREIGN KEY (gym_uuid) REFERENCES t_suhv_gym(uuid);


--
-- Name: fk_bdcuvy43iogdixuugltku971f; Type: FK CONSTRAINT; Schema: public; Owner: uht
--

ALTER TABLE ONLY t_user_authority
    ADD CONSTRAINT fk_bdcuvy43iogdixuugltku971f FOREIGN KEY (user_uuid) REFERENCES t_user(uuid);


--
-- Name: fk_bhlcfqoc2a6xrhwooeqd8on6h; Type: FK CONSTRAINT; Schema: public; Owner: uht
--

ALTER TABLE ONLY t_suhv_standings_entry
    ADD CONSTRAINT fk_bhlcfqoc2a6xrhwooeqd8on6h FOREIGN KEY (standings_uuid) REFERENCES t_suhv_standings(uuid);


--
-- Name: fk_et4gdn1lv39720m8l2mvqlofn; Type: FK CONSTRAINT; Schema: public; Owner: uht
--

ALTER TABLE ONLY t_suhv_team
    ADD CONSTRAINT fk_et4gdn1lv39720m8l2mvqlofn FOREIGN KEY (club_uuid) REFERENCES t_suhv_club(uuid);


--
-- Name: fk_jav05akf2yk4jxjrs85cafw4g; Type: FK CONSTRAINT; Schema: public; Owner: uht
--

ALTER TABLE ONLY t_team_player
    ADD CONSTRAINT fk_jav05akf2yk4jxjrs85cafw4g FOREIGN KEY (team_uuid) REFERENCES t_team(uuid);


--
-- Name: fk_k6p91snerk5c0042j5oq3n690; Type: FK CONSTRAINT; Schema: public; Owner: uht
--

ALTER TABLE ONLY t_team_player
    ADD CONSTRAINT fk_k6p91snerk5c0042j5oq3n690 FOREIGN KEY (player_uuid) REFERENCES t_player(uuid);


--
-- Name: fk_kjtf8hpnxyn62g6i3kssh7p2u; Type: FK CONSTRAINT; Schema: public; Owner: uht
--

ALTER TABLE ONLY t_event_t_team
    ADD CONSTRAINT fk_kjtf8hpnxyn62g6i3kssh7p2u FOREIGN KEY (t_event_uuid) REFERENCES t_event(uuid);


--
-- Name: fk_rbu06iekrio9i3wtgwa3f2b2m; Type: FK CONSTRAINT; Schema: public; Owner: uht
--

ALTER TABLE ONLY t_suhv_game
    ADD CONSTRAINT fk_rbu06iekrio9i3wtgwa3f2b2m FOREIGN KEY (team_uuid) REFERENCES t_suhv_team(uuid);


--
-- Name: fk_rnxy1f6vt5x39mospi2mlnod0; Type: FK CONSTRAINT; Schema: public; Owner: uht
--

ALTER TABLE ONLY t_suhv_standings
    ADD CONSTRAINT fk_rnxy1f6vt5x39mospi2mlnod0 FOREIGN KEY (team_uuid) REFERENCES t_suhv_team(uuid);


--
-- PostgreSQL database dump complete
--

