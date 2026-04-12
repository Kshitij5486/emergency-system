CREATE SCHEMA IF NOT EXISTS users;
CREATE SCHEMA IF NOT EXISTS incidents;
CREATE SCHEMA IF NOT EXISTS dispatch;
CREATE SCHEMA IF NOT EXISTS tracking;
CREATE SCHEMA IF NOT EXISTS notifications;
CREATE SCHEMA IF NOT EXISTS analytics;

GRANT ALL PRIVILEGES ON SCHEMA users         TO emergency_user;
GRANT ALL PRIVILEGES ON SCHEMA incidents     TO emergency_user;
GRANT ALL PRIVILEGES ON SCHEMA dispatch      TO emergency_user;
GRANT ALL PRIVILEGES ON SCHEMA tracking      TO emergency_user;
GRANT ALL PRIVILEGES ON SCHEMA notifications TO emergency_user;
GRANT ALL PRIVILEGES ON SCHEMA analytics     TO emergency_user;
