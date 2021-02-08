create extension if not exists pgcrypto;

update customer set password = crypt(password, gen_salt('bf', 10));