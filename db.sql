CREATE TABLE individuals (
  id       INTEGER      NOT NULL UNIQUE,
  name     VARCHAR(100) NOT NULL,
  gender   VARCHAR(5),
  birthday DATE,
  death    DATE,

  PRIMARY KEY (id)
);


CREATE TABLE families (
  id       INTEGER NOT NULL UNIQUE,
  married  DATE,
  divorced DATE,

  PRIMARY KEY (id)
);


CREATE TABLE individual_rel_family (
  individual_id INTEGER	             NOT NULL,
  family_id     INTEGER              NOT NULL,
  role          ENUM ('H', 'W', 'C') NOT NULL
);