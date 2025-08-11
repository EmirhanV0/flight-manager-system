-- Create databases for UYS services
CREATE DATABASE IF NOT EXISTS uys_reference;
CREATE DATABASE IF NOT EXISTS uys_flight;

-- Grant permissions to uys_user
GRANT ALL PRIVILEGES ON uys_reference.* TO 'uys_user'@'%';
GRANT ALL PRIVILEGES ON uys_flight.* TO 'uys_user'@'%';

FLUSH PRIVILEGES; 