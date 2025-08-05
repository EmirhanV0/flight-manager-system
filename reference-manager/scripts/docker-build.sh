#!/bin/bash

# =============================================================================
# UYS Reference Manager Service - Docker Build Script
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
IMAGE_NAME="uys/reference-manager"
TAG="latest"
CONTAINER_NAME="uys-reference-manager"

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

# Function to build Docker image
build_image() {
    print_status "Building Docker image: $IMAGE_NAME:$TAG"
    
    if docker build -t $IMAGE_NAME:$TAG .; then
        print_success "Docker image built successfully"
    else
        print_error "Failed to build Docker image"
        exit 1
    fi
}

# Function to stop and remove existing container
cleanup_container() {
    print_status "Cleaning up existing container..."
    
    if docker ps -a --format "table {{.Names}}" | grep -q "^$CONTAINER_NAME$"; then
        docker stop $CONTAINER_NAME > /dev/null 2>&1 || true
        docker rm $CONTAINER_NAME > /dev/null 2>&1 || true
        print_success "Existing container cleaned up"
    else
        print_status "No existing container found"
    fi
}

# Function to run container
run_container() {
    print_status "Starting container: $CONTAINER_NAME"
    
    docker run -d \
        --name $CONTAINER_NAME \
        --network uys-network \
        -p 8081:8080 \
        -e SPRING_PROFILES_ACTIVE=docker \
        -e SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/uys_reference?useSSL=false&allowPublicKeyRetrieval=true \
        -e SPRING_DATASOURCE_USERNAME=uys_user \
        -e SPRING_DATASOURCE_PASSWORD=uys_password \
        -e SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka:9092 \
        -e SPRING_REDIS_HOST=redis \
        -e SPRING_REDIS_PORT=6379 \
        -v $(pwd)/logs:/app/logs \
        $IMAGE_NAME:$TAG
    
    if [ $? -eq 0 ]; then
        print_success "Container started successfully"
    else
        print_error "Failed to start container"
        exit 1
    fi
}

# Function to check container health
check_health() {
    print_status "Checking container health..."
    
    local max_attempts=30
    local attempt=1
    
    while [ $attempt -le $max_attempts ]; do
        if docker exec $CONTAINER_NAME curl -f http://localhost:8080/actuator/health > /dev/null 2>&1; then
            print_success "Container is healthy and ready"
            return 0
        fi
        
        print_status "Waiting for container to be ready... (attempt $attempt/$max_attempts)"
        sleep 10
        ((attempt++))
    done
    
    print_error "Container failed to become healthy within expected time"
    return 1
}

# Function to show container logs
show_logs() {
    print_status "Showing container logs (press Ctrl+C to exit)..."
    docker logs -f $CONTAINER_NAME
}

# Function to show container status
show_status() {
    print_status "Container status:"
    docker ps -a --filter "name=$CONTAINER_NAME" --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"
}

# Main execution
main() {
    print_status "Starting Docker build and run process for $SERVICE_NAME"
    
    # Check Docker
    check_docker
    
    # Build image
    build_image
    
    # Cleanup existing container
    cleanup_container
    
    # Run container
    run_container
    
    # Check health
    if check_health; then
        print_success "Service is ready!"
        print_status "Access the service at: http://localhost:8081/api"
        print_status "Health check: http://localhost:8081/api/actuator/health"
        print_status "Swagger UI: http://localhost:8081/api/swagger-ui.html"
        
        # Show status
        show_status
        
        # Ask if user wants to see logs
        echo
        read -p "Do you want to see container logs? (y/n): " -n 1 -r
        echo
        if [[ $REPLY =~ ^[Yy]$ ]]; then
            show_logs
        fi
    else
        print_error "Service failed to start properly"
        show_status
        exit 1
    fi
}

# Handle command line arguments
case "${1:-}" in
    "build")
        check_docker
        build_image
        ;;
    "run")
        check_docker
        cleanup_container
        run_container
        check_health
        ;;
    "cleanup")
        cleanup_container
        ;;
    "logs")
        show_logs
        ;;
    "status")
        show_status
        ;;
    "help"|"-h"|"--help")
        echo "Usage: $0 [command]"
        echo "Commands:"
        echo "  build    - Build Docker image only"
        echo "  run      - Run container only"
        echo "  cleanup  - Stop and remove container"
        echo "  logs     - Show container logs"
        echo "  status   - Show container status"
        echo "  help     - Show this help message"
        echo "  (no args) - Build and run container"
        ;;
    *)
        main
        ;;
esac 