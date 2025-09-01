-- Create databases
CREATE DATABASE IF NOT EXISTS map_platform CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS userdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS questdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Create users and grant privileges
CREATE USER IF NOT EXISTS 'map_user'@'%' IDENTIFIED BY 'map_password';
CREATE USER IF NOT EXISTS 'userAuth'@'%' IDENTIFIED BY 'iwanttobehappy';

-- Grant privileges to map_user for map_platform database
GRANT ALL PRIVILEGES ON map_platform.* TO 'map_user'@'%';

-- Grant privileges to userAuth for userdb database
GRANT ALL PRIVILEGES ON userdb.* TO 'userAuth'@'%';

-- Grant privileges to root for userdb database
GRANT ALL PRIVILEGES ON userdb.* TO 'root'@'%';

-- Grant privileges to map_user for questdb database
GRANT ALL PRIVILEGES ON questdb.* TO 'map_user'@'%';

-- Apply changes
FLUSH PRIVILEGES; 