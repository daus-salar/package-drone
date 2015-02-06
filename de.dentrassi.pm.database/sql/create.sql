CREATE TABLE PROPERTIES ("KEY" VARCHAR(255) NOT NULL, "VALUE" TEXT, PRIMARY KEY("KEY"));
INSERT INTO PROPERTIES ( "KEY", "VALUE" ) VALUES ( 'database-schema-version', '7' );

CREATE TABLE CHANNELS (
    ID            VARCHAR(36) NOT NULL,
    NAME          VARCHAR(255) UNIQUE,
    
    PRIMARY KEY (ID)
);

CREATE TABLE EXT_CHAN_PROPS (
    CHANNEL_ID    VARCHAR(36) NOT NULL,
    "NS"          VARCHAR(255) NOT NULL,
    "KEY"         VARCHAR(255) NOT NULL,
    "VALUE"       LONGTEXT,
    
    PRIMARY KEY (CHANNEL_ID, "NS", "KEY" ),
    
    FOREIGN KEY (CHANNEL_ID) REFERENCES CHANNELS(ID) ON DELETE CASCADE
);

CREATE TABLE PROV_CHAN_PROPS (
    CHANNEL_ID    VARCHAR(36) NOT NULL,
    "NS"          VARCHAR(255) NOT NULL,
    "KEY"         VARCHAR(255) NOT NULL,
    "VALUE"       LONGTEXT,
    
    PRIMARY KEY ( CHANNEL_ID, "NS", "KEY" ),
    
    FOREIGN KEY ( CHANNEL_ID ) REFERENCES CHANNELS(ID) ON DELETE CASCADE
);

CREATE TABLE ARTIFACTS (
    ID            VARCHAR(36) NOT NULL,
    CHANNEL_ID    VARCHAR(36) NOT NULL,
    "TYPE"        VARCHAR(8) NOT NULL,
    
    PARENT        VARCHAR(36),
    
    NAME          VARCHAR(255) NOT NULL,
    
    DATA          LONGBLOB,
    SIZE          NUMERIC NOT NULL,
    
    CREATION_TS   DATETIME NOT NULL,
    
    PRIMARY KEY (ID),
    FOREIGN KEY (CHANNEL_ID) REFERENCES CHANNELS(ID) ON DELETE CASCADE,
    FOREIGN KEY (PARENT) REFERENCES ARTIFACTS(ID) ON DELETE CASCADE
);

CREATE TABLE VIRTUAL_ARTIFACTS (
    ID            VARCHAR(36) NOT NULL,
    NS            VARCHAR(255) NOT NULL,
    
    PRIMARY KEY (ID),
    
    FOREIGN KEY (ID) REFERENCES ARTIFACTS(ID) ON DELETE CASCADE
);

CREATE TABLE GENERATED_ARTIFACTS (
    ID            VARCHAR(36) NOT NULL,
    
    GENERATOR_ID    VARCHAR(255) NOT NULL,
    
    PRIMARY KEY (ID),
    
    FOREIGN KEY (ID) REFERENCES ARTIFACTS(ID) ON DELETE CASCADE
);

CREATE TABLE EXT_ART_PROPS (
    ART_ID        VARCHAR(36) NOT NULL,
    "NS"          VARCHAR(255) NOT NULL,
    "KEY"         VARCHAR(255) NOT NULL,
    "VALUE"       LONGTEXT,
    
    PRIMARY KEY (ART_ID, "NS", "KEY" ),
    
    FOREIGN KEY (ART_ID) REFERENCES ARTIFACTS(ID) ON DELETE CASCADE
);

CREATE TABLE PROV_ART_PROPS (
    ART_ID        VARCHAR(36) NOT NULL,
    "NS"          VARCHAR(255) NOT NULL,
    "KEY"         VARCHAR(255) NOT NULL,
    "VALUE"       LONGTEXT,
    
    PRIMARY KEY ( ART_ID, "NS", "KEY" ),
    
    FOREIGN KEY ( ART_ID ) REFERENCES ARTIFACTS(ID) ON DELETE CASCADE
);

CREATE TABLE CHANNEL_ASPECTS (
    CHANNEL_ID    VARCHAR(36) NOT NULL,
    ASPECT        VARCHAR(255) NOT NULL,
    
    PRIMARY KEY ( CHANNEL_ID, ASPECT ),
    
    FOREIGN KEY (CHANNEL_ID) REFERENCES CHANNELS(ID) ON DELETE CASCADE
);

-- USERS

CREATE TABLE USERS (
    ID              VARCHAR(36) NOT NULL,
    NAME            VARCHAR(256),
    
    REG_DATE        DATETIME NOT NULL,
    
    PASSWORD_HASH   VARCHAR(64),
    PASSWORD_SALT   VARCHAR(64),
    
    EMAIL           VARCHAR(256),
    
    EMAIL_TOK       VARCHAR(64),
    EMAIL_TOK_SALT  VARCHAR(64),
    EMAIL_TOK_TS    DATETIME,
    
    EMAIL_VERIFIED  BOOLEAN NOT NULL DEFAULT 0,
    
    DELETED         BOOLEAN NOT NULL DEFAULT 0,
    LOCKED          BOOLEAN NOT NULL DEFAULT 0,
    
    REM_TOKEN_HASH  VARCHAR(64),
    REM_TOKEN_SALT  VARCHAR(64),
    
    ROLES           LONGTEXT,
    
    PRIMARY KEY ( ID ),
    
    UNIQUE ( EMAIL )
);

-- DEPLOY AUTH

CREATE TABLE DEPLOY_GROUPS (
    ID              VARCHAR(36) NOT NULL,
    
    NAME            VARCHAR(255),
    
    PRIMARY KEY ( ID )
);

CREATE TABLE DEPLOY_KEYS (
    ID              VARCHAR(36) NOT NULL,
    
    GROUP_ID        VARCHAR(36),
    
    NAME            VARCHAR(255),
    KEY_DATA        VARCHAR(1024) NOT NULL,
    
    CREATION_TS     DATETIME NOT NULL,
    
    PRIMARY KEY ( ID ),
    FOREIGN KEY ( GROUP_ID ) REFERENCES DEPLOY_GROUPS ( ID ) ON DELETE CASCADE
);

CREATE TABLE CHANNEL_DEPLOY_GROUPS (
    CHANNEL_ID      VARCHAR(36) NOT NULL,
    GROUP_ID        VARCHAR(36) NOT NULL,
    
    PRIMARY KEY ( CHANNEL_ID, GROUP_ID ),
    FOREIGN KEY ( CHANNEL_ID ) REFERENCES CHANNELS ( ID ) ON DELETE CASCADE,
    FOREIGN KEY ( GROUP_ID ) REFERENCES DEPLOY_GROUPS ( ID ) ON DELETE CASCADE
);