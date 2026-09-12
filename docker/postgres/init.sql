-- Day 6: one PostgreSQL instance, one database per service (database-per-service pattern).
CREATE DATABASE products;
CREATE DATABASE inventory;
CREATE DATABASE orders;
CREATE DATABASE payments;

GRANT ALL PRIVILEGES ON DATABASE products TO shopflow;
GRANT ALL PRIVILEGES ON DATABASE inventory TO shopflow;
GRANT ALL PRIVILEGES ON DATABASE orders TO shopflow;
GRANT ALL PRIVILEGES ON DATABASE payments TO shopflow;
