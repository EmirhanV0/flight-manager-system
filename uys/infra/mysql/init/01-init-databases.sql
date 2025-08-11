-- Create databases for UYS services
CREATE DATABASE IF NOT EXISTS uys_reference CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS uys_flight CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Grant permissions to uys_user
GRANT ALL PRIVILEGES ON uys_reference.* TO 'uys_user'@'%';
GRANT ALL PRIVILEGES ON uys_flight.* TO 'uys_user'@'%';

-- Flush privileges
FLUSH PRIVILEGES; 