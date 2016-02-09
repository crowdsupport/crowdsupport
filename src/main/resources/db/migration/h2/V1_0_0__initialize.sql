-- SCHEMA CREATION
-- ===============

-- Authority
-- ---------
CREATE TABLE ROLES (
  id          BIGINT IDENTITY,
  version     BIGINT       NOT NULL,
  created     TIMESTAMP    NOT NULL,
  name        VARCHAR2(30) NOT NULL,
  system_role BOOL         NOT NULL
);

CREATE TABLE PERMISSIONS (
  id      BIGINT IDENTITY,
  version BIGINT       NOT NULL,
  created TIMESTAMP    NOT NULL,
  name    VARCHAR2(60) NOT NULL
);

CREATE TABLE ROLES_PERMISSIONS (
  role       BIGINT NOT NULL,
  permission BIGINT NOT NULL,
  FOREIGN KEY (role) REFERENCES ROLES (id),
  FOREIGN KEY (permission) REFERENCES PERMISSIONS (id)
);

CREATE TABLE USERS (
  id        BIGINT IDENTITY,
  version   BIGINT        NOT NULL,
  created   TIMESTAMP     NOT NULL,
  username  VARCHAR2(100) NOT NULL,
  password  VARCHAR2(300) NOT NULL,
  enabled   BOOL          NOT NULL,
  email     VARCHAR2(255),
  imagepath VARCHAR2(255)
);

CREATE TABLE USERS_ROLES (
  user BIGINT NOT NULL,
  role BIGINT NOT NULL,
  FOREIGN KEY (user) REFERENCES USERS (id),
  FOREIGN KEY (role) REFERENCES ROLES (id)
);

-- Geography
-- ---------
CREATE TABLE STATES (
  id         BIGINT IDENTITY,
  version    BIGINT        NOT NULL,
  created    TIMESTAMP     NOT NULL,
  name       VARCHAR2(100) NOT NULL,
  identifier VARCHAR2(50)  NOT NULL,
  imagepath  VARCHAR2(255)
);

CREATE TABLE CITIES (
  id         BIGINT IDENTITY,
  version    BIGINT        NOT NULL,
  created    TIMESTAMP     NOT NULL,
  name       VARCHAR2(100) NOT NULL,
  identifier VARCHAR2(50)  NOT NULL,
  imagepath  VARCHAR2(255),
  state      BIGINT        NOT NULL,
  FOREIGN KEY (state) REFERENCES STATES (id)
);

CREATE TABLE PLACES (
  id            BIGINT IDENTITY,
  version       BIGINT         NOT NULL,
  created       TIMESTAMP      NOT NULL,
  name          VARCHAR2(100)  NOT NULL,
  identifier    VARCHAR2(50)   NOT NULL,
  imagepath     VARCHAR2(255),
  city          BIGINT         NOT NULL,
  location      VARCHAR2(2000) NOT NULL,
  active        BOOL           NOT NULL,
  team          BIGINT,
  place_request BIGINT,
  FOREIGN KEY (city) REFERENCES CITIES (id)
  -- foreign key for team is added later
  -- foreign key for place_request is added later
);

CREATE TABLE PLACE_REQUESTS (
  id              BIGINT IDENTITY,
  version         BIGINT    NOT NULL,
  created         TIMESTAMP NOT NULL,
  state           VARCHAR2(100),
  city            VARCHAR2(100),
  place           BIGINT,
  requesting_user BIGINT    NOT NULL,
  FOREIGN KEY (place) REFERENCES PLACES (id),
  FOREIGN KEY (requesting_user) REFERENCES USERS (id)
);
ALTER TABLE PLACES ADD FOREIGN KEY (place_request) REFERENCES PLACE_REQUESTS (id);

-- Donation Requests
-- -----------------
CREATE TABLE TEAMS (
  id      BIGINT IDENTITY,
  version BIGINT    NOT NULL,
  created TIMESTAMP NOT NULL,
  place   BIGINT    NOT NULL
);
ALTER TABLE PLACES ADD FOREIGN KEY (team) REFERENCES TEAMS (id);

CREATE TABLE TEAMS_USERS (
  team BIGINT NOT NULL,
  user BIGINT NOT NULL,
  FOREIGN KEY (team) REFERENCES TEAMS (id),
  FOREIGN KEY (user) REFERENCES USERS (id)
);

CREATE TABLE DONATION_REQUESTS (
  id          BIGINT IDENTITY,
  version     BIGINT         NOT NULL,
  created     TIMESTAMP      NOT NULL,
  place       BIGINT         NOT NULL,
  title       VARCHAR2(100)  NOT NULL,
  description VARCHAR2(2000) NOT NULL,
  valid_to    TIMESTAMP,
  quantity    INT,
  units       VARCHAR2(50),
  resolved    BOOL,
  FOREIGN KEY (place) REFERENCES PLACES (id)
);

CREATE TABLE TAGS (
  id      BIGINT IDENTITY,
  version BIGINT       NOT NULL,
  created TIMESTAMP    NOT NULL,
  name    VARCHAR2(50) NOT NULL
);

CREATE TABLE DONATION_REQUESTS_TAGS (
  donation_request BIGINT NOT NULL,
  tag              BIGINT NOT NULL,
  FOREIGN KEY (donation_request) REFERENCES DONATION_REQUESTS (id),
  FOREIGN KEY (tag) REFERENCES TAGS (id)
);

CREATE TABLE COMMENTS (
  id               BIGINT IDENTITY,
  version          BIGINT         NOT NULL,
  created          TIMESTAMP      NOT NULL,
  donation_request BIGINT         NOT NULL,
  text             VARCHAR2(1000) NOT NULL,
  author           BIGINT         NOT NULL,
  confirmed        BOOL           NOT NULL,
  quantity         INT,
  FOREIGN KEY (donation_request) REFERENCES DONATION_REQUESTS (id),
  FOREIGN KEY (author) REFERENCES USERS (id)
);

-- Properties
-- ----------
CREATE TABLE PROPERTIES (
  id      BIGINT IDENTITY,
  version BIGINT    NOT NULL,
  created TIMESTAMP NOT NULL,
  key     VARCHAR2(100),
  value   VARCHAR2(1000)
);

-- INITIAL DATA
-- ============

-- Authority
-- ---------
INSERT INTO PERMISSIONS (version, created, name) VALUES (0, sysdate, 'PROCESS_PLACE_REQUESTS');
INSERT INTO PERMISSIONS (version, created, name) VALUES (0, sysdate, 'MANAGE_ROLES');
INSERT INTO PERMISSIONS (version, created, name) VALUES (0, sysdate, 'MANAGE_USERS');
INSERT INTO PERMISSIONS (version, created, name) VALUES (0, sysdate, 'MANAGE_CITIES');
INSERT INTO PERMISSIONS (version, created, name) VALUES (0, sysdate, 'MANAGE_STATES');
INSERT INTO PERMISSIONS (version, created, name) VALUES (0, sysdate, 'MANAGE_PLACES');
INSERT INTO PERMISSIONS (version, created, name) VALUES (0, sysdate, 'QUERY_USERS');

INSERT INTO ROLES (version, created, name, system_role) VALUES (0, sysdate, 'ROLE_ADMIN', TRUE);
INSERT INTO ROLES (version, created, name, system_role) VALUES (0, sysdate, 'ROLE_USER', TRUE);

INSERT INTO ROLES_PERMISSIONS (role, permission) VALUES (
  (SELECT id
   FROM ROLES
   WHERE name = 'ROLE_ADMIN'),
  (SELECT id
   FROM PERMISSIONS
   WHERE name = 'PROCESS_PLACE_REQUESTS')
);
INSERT INTO ROLES_PERMISSIONS (role, permission) VALUES (
  (SELECT id
   FROM ROLES
   WHERE name = 'ROLE_ADMIN'),
  (SELECT id
   FROM PERMISSIONS
   WHERE name = 'MANAGE_ROLES')
);
INSERT INTO ROLES_PERMISSIONS (role, permission) VALUES (
  (SELECT id
   FROM ROLES
   WHERE name = 'ROLE_ADMIN'),
  (SELECT id
   FROM PERMISSIONS
   WHERE name = 'MANAGE_USERS')
);
INSERT INTO ROLES_PERMISSIONS (role, permission) VALUES (
  (SELECT id
   FROM ROLES
   WHERE name = 'ROLE_ADMIN'),
  (SELECT id
   FROM PERMISSIONS
   WHERE name = 'MANAGE_CITIES')
);
INSERT INTO ROLES_PERMISSIONS (role, permission) VALUES (
  (SELECT id
   FROM ROLES
   WHERE name = 'ROLE_ADMIN'),
  (SELECT id
   FROM PERMISSIONS
   WHERE name = 'MANAGE_STATES')
);
INSERT INTO ROLES_PERMISSIONS (role, permission) VALUES (
  (SELECT id
   FROM ROLES
   WHERE name = 'ROLE_ADMIN'),
  (SELECT id
   FROM PERMISSIONS
   WHERE name = 'MANAGE_PLACES')
);
INSERT INTO ROLES_PERMISSIONS (role, permission) VALUES (
  (SELECT id
   FROM ROLES
   WHERE name = 'ROLE_ADMIN'),
  (SELECT id
   FROM PERMISSIONS
   WHERE name = 'QUERY_USERS')
);

-- Default admin user, password: adminadmin (change as soon as possible!)
-- ----------------------------------------------------------------------
INSERT INTO USERS (version, created, username, password, enabled, email, imagepath) VALUES (
  0, sysdate, 'admin', '$2a$10$f45dG7EISyMIlkF0wlcAXOYDizsR21758EEuGJWAAh.kVQS1vyFoq', TRUE, NULL, '/r/image/placeholder.jpg'
);
INSERT INTO USERS_ROLES (user, role) VALUES (
  (SELECT id
   FROM USERS
   WHERE username = 'admin'),
  (SELECT id
   FROM ROLES
   WHERE name = 'ROLE_ADMIN')
);
INSERT INTO USERS_ROLES (user, role) VALUES (
  (SELECT id
   FROM USERS
   WHERE username = 'admin'),
  (SELECT id
   FROM ROLES
   WHERE name = 'ROLE_USER')
);