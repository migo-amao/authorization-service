--
--Users: john/john@123 kelly/kelly@123
--Password encrypted using CodeachesBCryptPasswordEncoder.java (4 rounds)
--
INSERT INTO users (username,password,enabled)
    VALUES ('john', '$2a$04$xqJH/AWpC89pBBFb7i9VU.zoWbOrE2gvdFcfTAOE1bCF5.tNvVXXu', TRUE);
INSERT INTO users (username,password,enabled)
    VALUES ('kelly','$2a$04$IpZnGqXXgNvvMbqlg/tc7uJUM.1nj/5KtqnFlxRpRN2RqWUFV4lg6', TRUE);

INSERT INTO authorities (username, authority) VALUES ('john', 'INVENTORY_VIEW');
INSERT INTO authorities (username, authority) VALUES ('john', 'INVENTORY_ADD');
INSERT INTO authorities (username, authority) VALUES ('kelly', 'INVENTORY_VIEW');

INSERT INTO groups (id, group_name) VALUES (1, 'INVENTORY_GROUP_1');
INSERT INTO groups (id, group_name) VALUES (2, 'INVENTORY_GROUP_2');

INSERT INTO group_authorities (group_id, authority) VALUES (1, 'INVENTORY_VIEW');
INSERT INTO group_authorities (group_id, authority) VALUES (1, 'INVENTORY_ADD');
INSERT INTO group_authorities (group_id, authority) VALUES (1, 'ROLE_USER');
INSERT INTO group_authorities (group_id, authority) VALUES (2, 'ROLE_USER');
INSERT INTO group_authorities (group_id, authority) VALUES (2, 'INVENTORY_VIEW');


INSERT INTO group_members (username, group_id) VALUES ('john', 1);
INSERT INTO group_members (username, group_id) VALUES ('kelly', 2);

--
--Client: edge-service/secret
--Password encrypted using CodeachesBCryptPasswordEncoder.java (4 rounds)
--
INSERT INTO
  oauth_client_details (
    client_id,
    client_secret,
    resource_ids,
    scope,
    authorized_grant_types,
    access_token_validity,
    refresh_token_validity,
    web_server_redirect_uri,
    autoapprove
  )
VALUES
  (
    'edge-service',
    '$2a$10$vCXMWCn7fDZWOcLnIEhmK.74dvK1Eh8ae2WrWlhr2ETPLoxQctN4.',
    'carInventory',
    'read,write',
    'authorization_code,check_token,refresh_token,password',
    1000000,
    1000000,
    'http://localhost:8180/edge-service/login/oauth2/code/api-gateway',
    'read,write'
  );