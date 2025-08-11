-- Create database for UYS archive service
CREATE DATABASE IF NOT EXISTS uys_archive;

-- Grant permissions to uys_user
GRANT ALL PRIVILEGES ON DATABASE uys_archive TO uys_user;

-- Create extension for JSONB support
CREATE EXTENSION IF NOT EXISTS "uuid-ossp"; 