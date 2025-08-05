#!/bin/bash

# =============================================================================
# UYS Reference Manager Service - Docker Compose Run Script
# =============================================================================

set -e  # Exit on any error

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configuration
SERVICE_NAME="reference-manager"
COMPOSE_FILE="docker-compose.override.yml"
NETWORK_NAME="uys-network"

# Function to print colored output
print_status() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Function to check if Docker is running
check_docker() {
    if ! docker info > /dev/null 2>&1; then
        print_error "Docker is not running. Please start Docker Desktop."
        exit 1
    fi
    print_success "Docker is running"
}

# Function to check if Docker Compose is available
check_docker_compose() {
    if ! docker-compose --version > /dev/null 2>&1; then
        print_error "Docker Compose is not available. Please install Docker Compose."
        exit 1
    fi
    print_success "Docker Compose is available"
}

# Function to create network if it doesn't exist
create_network() {
    print_status "Checking Docker network: $NETWORK_NAME"
    
    if ! docker network ls --format "{{.Name}}" | grep -q "^$NETWORK_NAME$"; then
        print_status "Creating Docker network: $NETWORK_NAME"
        docker network create $NETWORK_NAME
        print_success "Network created successfully"
    else
        print_status "Network already exists"
    fi
}

# Function to start infrastructure services
start_infra() {
    print_status "Starting infrastructure services..."
    
    # Check if infra directory exists
    if [ ! -d "../infra" ]; then
        print_error "Infrastructure directory not found. Please ensure infra services are available."
        exit 1
    fi
    
    # Start infrastructure services
    cd ../infra
    if docker-compose up -d mysql redis kafka zookeeper; then
        print_success "Infrastructure services started"
    else
        print_error "Failed to start infrastructure services"
        exit 1
    fi
    cd - > /dev/null
}

# Function to wait for infrastructure services
wait_for_infra() {
    print_status "Waiting for infrastructure services to be ready..."
    
    # Wait for MySQL
    print_status "Waiting for MySQL..."
    local mysql_attempts=0
    while [ $mysql_attempts -lt 30 ]; do
        if docker exec infra-mysql-1 mysqladmin ping -h localhost -u uys_user -puys_password > /dev/null 2>&1; then
            print_success "MySQL is ready"
            break
        fi
        sleep 5
        ((mysql_attempts++))
    done
    
    if [ $mysql_attempts -eq 30 ]; then
        print_error "MySQL failed to become ready"
        exit 1
    fi
    
    # Wait for Kafka
    print_status "Waiting for Kafka..."
    local kafka_attempts=0
    while [ $kafka_attempts -lt 30 ]; do
        if docker exec infra-kafka-1 kafka-topics --bootstrap-server localhost:9092 --list > /dev/null 2>&1; then
            print_success "Kafka is ready"
            break
        fi
        sleep 5
        ((kafka_attempts++))
    done
    
    if [ $kafka_attempts -eq 30 ]; then
        print_error "Kafka failed to become ready"
        exit 1
    fi
    
    # Wait for Redis
    print_status "Waiting for Redis..."
    local redis_attempts=0
    while [ $redis_attempts -lt 30 ]; do
        if docker exec infra-redis-1 redis-cli ping > /dev/null 2>&1; then
            print_success "Redis is ready"
            break
        fi
        sleep 5
        ((redis_attempts++))
    done
    
    if [ $redis_attempts -eq 30 ]; then
        print_error "Redis failed to become ready"
        exit 1
    fi
}

# Function to build and start service
start_service() {
    print_status "Building and starting $SERVICE_NAME..."
    
    if docker-compose -f $COMPOSE_FILE up --build -d; then
        print_success "Service started successfully"
    else
        print_error "Failed to start service"
        exit 1
    fi
}

# Function to check service health
check_service_health() {
    print_status "Checking service health..."
    
    local max_attempts=30
    local attempt=1
    
    while [ $attempt -le $max_attempts ]; do
        if curl -f http://localhost:8081/api/actuator/health > /dev/null 2>&1; then
            print_success "Service is healthy and ready"
            return 0
        fi
        
        print_status "Waiting for service to be ready... (attempt $attempt/$max_attempts)"
        sleep 10
        ((attempt++))
    done
    
    print_error "Service failed to become healthy within expected time"
    return 1
}

# Function to show service logs
show_logs() {
    print_status "Showing service logs (press Ctrl+C to exit)..."
    docker-compose -f $COMPOSE_FILE logs -f $SERVICE_NAME
}

# Function to show service status
show_status() {
    print_status "Service status:"
    docker-compose -f $COMPOSE_FILE ps
}

# Function to stop service
stop_service() {
    print_status "Stopping service..."
    docker-compose -f $COMPOSE_FILE down
    print_success "Service stopped"
}

# Function to restart service
restart_service() {
    print_status "Restarting service..."
    docker-compose -f $COMPOSE_FILE restart $SERVICE_NAME
    print_success "Service restarted"
}

# Function to show service information
show_info() {
    print_status "Service Information:"
    echo "Service Name: $SERVICE_NAME"
    echo "API Endpoint: http://localhost:8081/api"
    echo "Health Check: http://localhost:8081/api/actuator/health"
    echo "Swagger UI: http://localhost:8081/api/swagger-ui.html"
    echo "Actuator: http://localhost:8081/api/actuator"
    echo ""
    print_status "Available endpoints:"
    echo "  - GET  /api/airlines"
    echo "  - POST /api/airlines"
    echo "  - GET  /api/airlines/{id}"
    echo "  - PUT  /api/airlines/{id}"
    echo "  - DELETE /api/airlines/{id}"
    echo "  - GET  /api/aircraft"
    echo "  - POST /api/aircraft"
    echo "  - GET  /api/aircraft/{id}"
    echo "  - PUT  /api/aircraft/{id}"
    echo "  - DELETE /api/aircraft/{id}"
    echo "  - GET  /api/stations"
    echo "  - POST /api/stations"
    echo "  - GET  /api/stations/{id}"
    echo "  - PUT  /api/stations/{id}"
    echo "  - DELETE /api/stations/{id}"
}

# Main execution
main() {
    print_status "Starting Docker Compose orchestration for $SERVICE_NAME"
    
    # Check prerequisites
    check_docker
    check_docker_compose
    
    # Create network
    create_network
    
    # Start infrastructure
    start_infra
    
    # Wait for infrastructure
    wait_for_infra
    
    # Start service
    start_service
    
    # Check health
    if check_service_health; then
        print_success "Service orchestration completed successfully!"
        
        # Show information
        show_info
        
        # Show status
        show_status
        
        # Ask if user wants to see logs
        echo
        read -p "Do you want to see service logs? (y/n): " -n 1 -r
        echo
        if [[ $REPLY =~ ^[Yy]$ ]]; then
            show_logs
        fi
    else
        print_error "Service orchestration failed"
        show_status
        exit 1
    fi
}

# Handle command line arguments
case "${1:-}" in
    "start")
        main
        ;;
    "stop")
        stop_service
        ;;
    "restart")
        restart_service
        ;;
    "logs")
        show_logs
        ;;
    "status")
        show_status
        ;;
    "info")
        show_info
        ;;
    "cleanup")
        stop_service
        print_status "Cleaning up infrastructure..."
        cd ../infra
        docker-compose down
        cd - > /dev/null
        print_success "Cleanup completed"
        ;;
    "help"|"-h"|"--help")
        echo "Usage: $0 [command]"
        echo "Commands:"
        echo "  start    - Start service with infrastructure"
        echo "  stop     - Stop service"
        echo "  restart  - Restart service"
        echo "  logs     - Show service logs"
        echo "  status   - Show service status"
        echo "  info     - Show service information"
        echo "  cleanup  - Stop service and infrastructure"
        echo "  help     - Show this help message"
        echo "  (no args) - Start service with infrastructure"
        ;;
    *)
        main
        ;;
esac 