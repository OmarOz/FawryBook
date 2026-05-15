--
-- PostgreSQL database dump
--

-- Dumped from database version 17.5 (Debian 17.5-1.pgdg120+1)
-- Dumped by pg_dump version 17.5 (Debian 17.5-1.pgdg120+1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: categories; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.categories (
                                   id uuid NOT NULL,
                                   name character varying(255) NOT NULL
);


ALTER TABLE public.categories OWNER TO postgres;

--
-- Name: comments; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.comments (
                                 created_at timestamp(6) without time zone NOT NULL,
                                 id uuid NOT NULL,
                                 post_id uuid NOT NULL,
                                 user_id uuid NOT NULL,
                                 content text NOT NULL
);


ALTER TABLE public.comments OWNER TO postgres;

--
-- Name: post_interactions; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.post_interactions (
                                          created_at timestamp(6) without time zone NOT NULL,
                                          updated_at timestamp(6) without time zone NOT NULL,
                                          id uuid NOT NULL,
                                          post_id uuid NOT NULL,
                                          user_id uuid NOT NULL,
                                          type character varying(255) NOT NULL,
                                          CONSTRAINT post_interactions_type_check CHECK (((type)::text = ANY ((ARRAY['LIKE'::character varying, 'DISLIKE'::character varying])::text[])))
);


ALTER TABLE public.post_interactions OWNER TO postgres;

--
-- Name: post_tags; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.post_tags (
                                  post_id uuid NOT NULL,
                                  tag_id uuid NOT NULL
);


ALTER TABLE public.post_tags OWNER TO postgres;

--
-- Name: posts; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.posts (
                              rating double precision,
                              created_at timestamp(6) without time zone NOT NULL,
                              updated_at timestamp(6) without time zone NOT NULL,
                              author_id uuid NOT NULL,
                              category_id uuid NOT NULL,
                              id uuid NOT NULL,
                              content text NOT NULL,
                              title character varying(255) NOT NULL
);


ALTER TABLE public.posts OWNER TO postgres;

--
-- Name: tag; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tag (
                            id uuid NOT NULL,
                            name character varying(255) NOT NULL
);


ALTER TABLE public.tag OWNER TO postgres;

--
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
                              created_at timestamp(6) without time zone NOT NULL,
                              id uuid NOT NULL,
                              email character varying(255) NOT NULL,
                              password character varying(255) NOT NULL,
                              username character varying(255) NOT NULL
);


ALTER TABLE public.users OWNER TO postgres;

--
-- Name: categories categories_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.categories
    ADD CONSTRAINT categories_name_key UNIQUE (name);


--
-- Name: categories categories_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.categories
    ADD CONSTRAINT categories_pkey PRIMARY KEY (id);


--
-- Name: comments comments_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.comments
    ADD CONSTRAINT comments_pkey PRIMARY KEY (id);


--
-- Name: post_interactions post_interactions_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.post_interactions
    ADD CONSTRAINT post_interactions_pkey PRIMARY KEY (id);


--
-- Name: post_interactions post_interactions_post_id_user_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.post_interactions
    ADD CONSTRAINT post_interactions_post_id_user_id_key UNIQUE (post_id, user_id);


--
-- Name: post_tags post_tags_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.post_tags
    ADD CONSTRAINT post_tags_pkey PRIMARY KEY (post_id, tag_id);


--
-- Name: posts posts_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.posts
    ADD CONSTRAINT posts_pkey PRIMARY KEY (id);


--
-- Name: tag tag_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tag
    ADD CONSTRAINT tag_name_key UNIQUE (name);


--
-- Name: tag tag_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tag
    ADD CONSTRAINT tag_pkey PRIMARY KEY (id);


--
-- Name: post_interactions uk7y2hn3dtt68u85yl6s8avtiqt; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.post_interactions
    ADD CONSTRAINT uk7y2hn3dtt68u85yl6s8avtiqt UNIQUE (post_id, user_id);


--
-- Name: users users_email_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_email_key UNIQUE (email);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: post_interactions fk2gxy6d4mb32khdq265d2gj2i8; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.post_interactions
    ADD CONSTRAINT fk2gxy6d4mb32khdq265d2gj2i8 FOREIGN KEY (post_id) REFERENCES public.posts(id);


--
-- Name: posts fk6xvn0811tkyo3nfjk2xvqx6ns; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.posts
    ADD CONSTRAINT fk6xvn0811tkyo3nfjk2xvqx6ns FOREIGN KEY (author_id) REFERENCES public.users(id);


--
-- Name: comments fk8omq0tc18jd43bu5tjh6jvraq; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.comments
    ADD CONSTRAINT fk8omq0tc18jd43bu5tjh6jvraq FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: post_interactions fkh04sqbi5rk3swo66qj8ixbekr; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.post_interactions
    ADD CONSTRAINT fkh04sqbi5rk3swo66qj8ixbekr FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: comments fkh4c7lvsc298whoyd4w9ta25cr; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.comments
    ADD CONSTRAINT fkh4c7lvsc298whoyd4w9ta25cr FOREIGN KEY (post_id) REFERENCES public.posts(id);


--
-- Name: posts fkijnwr3brs8vaosl80jg9rp7uc; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.posts
    ADD CONSTRAINT fkijnwr3brs8vaosl80jg9rp7uc FOREIGN KEY (category_id) REFERENCES public.categories(id);


--
-- Name: post_tags fkkifam22p4s1nm3bkmp1igcn5w; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.post_tags
    ADD CONSTRAINT fkkifam22p4s1nm3bkmp1igcn5w FOREIGN KEY (post_id) REFERENCES public.posts(id);


--
-- Name: post_tags fkp7cfgjsujc2vl3p88qfqkpws6; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.post_tags
    ADD CONSTRAINT fkp7cfgjsujc2vl3p88qfqkpws6 FOREIGN KEY (tag_id) REFERENCES public.tag(id);


--
-- PostgreSQL database dump complete
--

