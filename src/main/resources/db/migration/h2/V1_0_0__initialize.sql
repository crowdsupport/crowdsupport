-- Authority
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
  role       BIGINT    NOT NULL,
  permission BIGINT    NOT NULL,
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
  user    BIGINT    NOT NULL,
  role    BIGINT    NOT NULL,
  FOREIGN KEY (user) REFERENCES USERS (id),
  FOREIGN KEY (role) REFERENCES ROLES (id)
);

-- Geography
CREATE TABLE STATES (
  id         BIGINT IDENTITY,
  version    BIGINT        NOT NULL,
  created    TIMESTAMP     NOT NULL,
  name       VARCHAR2(100) NOT NULL,
  identifier VARCHAR2(50)  NOT NULL,
  imagepath  VARCHAR2(255) NOT NULL
);

CREATE TABLE CITIES (
  id         BIGINT IDENTITY,
  version    BIGINT        NOT NULL,
  created    TIMESTAMP     NOT NULL,
  name       VARCHAR2(100) NOT NULL,
  identifier VARCHAR2(50)  NOT NULL,
  imagepath  VARCHAR2(255) NOT NULL,
  state      BIGINT        NOT NULL,
  FOREIGN KEY (state) REFERENCES STATES (id)
);

CREATE TABLE PLACES (
  id            BIGINT IDENTITY,
  version       BIGINT         NOT NULL,
  created       TIMESTAMP      NOT NULL,
  name          VARCHAR2(100)  NOT NULL,
  identifier    VARCHAR2(50)   NOT NULL,
  imagepath     VARCHAR2(255)  NOT NULL,
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
  -- TODO: refactor into PLACES
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
CREATE TABLE TEAMS (
  -- TODO refactor into PLACES
  id      BIGINT IDENTITY,
  version BIGINT    NOT NULL,
  created TIMESTAMP NOT NULL,
  place   BIGINT    NOT NULL
);
ALTER TABLE PLACES ADD FOREIGN KEY (team) REFERENCES TEAMS (id);

CREATE TABLE TEAMS_USERS (
  team    BIGINT    NOT NULL,
  user    BIGINT    NOT NULL,
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
  donation_request BIGINT    NOT NULL,
  tag              BIGINT    NOT NULL,
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