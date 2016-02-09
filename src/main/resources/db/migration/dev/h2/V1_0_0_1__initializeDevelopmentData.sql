-- Authority
-- ---------

-- Don't worry, this will only be used in development mode, so you don't have to recreate your login token
-- after every application startup
INSERT INTO PROPERTIES (version, created, key, value) VALUES (0, sysdate, 'crowdsupport.token.secret', 'TabHNaxeyKPwC7ODV3wx6pLGp8e4hlXL924uRJ6FKu+/84w3RQH46wqN2h/s4KuvMG8a6gPhjkVqLLNWqkKASQ==');

-- User 'user' for convenience (password: user)
-- ----------------------------------------------------------------------
INSERT INTO USERS (version, created, username, password, enabled, email, imagepath) VALUES (
  0, sysdate, 'user', '$2a$10$0CxBb/xc5srMS3m4605wTeqhDq0ENuESI4RqSpdbKacukH.nk3l5O', TRUE, NULL, '/r/image/placeholder.jpg'
);

INSERT INTO USERS_ROLES (user, role) VALUES (
  (SELECT id
   FROM USERS
   WHERE username = 'user'),
  (SELECT id
   FROM ROLES
   WHERE name = 'ROLE_USER')
);

-- Geography
-- ---------
INSERT INTO STATES (version, created, name, identifier, imagepath) VALUES (0, sysdate, 'United Kingdom', 'uk', '/r/image/placeholder.jpg');

INSERT INTO CITIES (version, created, name, identifier, imagepath, state)
VALUES (0, sysdate, 'Preston', 'preston', '/r/image/placeholder.jpg', (SELECT id
                                                 FROM STATES
                                                 WHERE identifier = 'uk'));

INSERT INTO PLACES (version, created, name, identifier, imagepath, city, location, active, team, place_request) VALUES (
  0, sysdate, 'University of Central Lancashire', 'uclan', '/r/image/placeholder.jpg',
  (SELECT id
   FROM CITIES
   WHERE name = 'Preston'), 'Foster Building', TRUE,
  NULL, NULL
);

INSERT INTO TEAMS (version, created, place) VALUES (
  0, sysdate,
  (SELECT id
   FROM PLACES
   WHERE identifier = 'uclan')
);

UPDATE PLACES
SET team = (
  SELECT id
  FROM TEAMS
  WHERE place =
        (SELECT id
         FROM PLACES
         WHERE identifier = 'uclan')
)
WHERE identifier = 'uclan';

INSERT INTO TEAMS_USERS (team, user) VALUES (
  (
    SELECT id
    FROM TEAMS
    WHERE place =
          (SELECT id
           FROM PLACES
           WHERE identifier = 'uclan')
  ), (
    SELECT id
    FROM USERS
    WHERE username = 'user'
  )
);