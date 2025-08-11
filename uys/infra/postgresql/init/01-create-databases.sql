-- Create database for UYS archive service
-- The database uys_archive is already created by POSTGRES_DB environment variable
-- This script can be used for additional setup if needed

-- Create extensions if needed
CREATE EXTENSION IF NOT EXISTS "uuid-ossp"; 