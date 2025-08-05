#!/bin/bash

# =============================================================================
# UYS Reference Manager Service - Production Deployment Script
# =============================================================================

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

# Configuration
SERVICE_NAME="reference-manager"
IMAGE_NAME="uys/reference-manager"
TAG="${1:-latest}"
NAMESPACE="uys-prod"

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

# Function to check prerequisites
check_prerequisites() {
    print_status "Checking prerequisites..."
    
    # Check Docker
    if ! command -v docker &> /dev/null; then
        print_error "Docker is not installed"
        exit 1
    fi
    
    # Check kubectl
    if ! command -v kubectl &> /dev/null; then
        print_error "kubectl is not installed"
        exit 1
    fi
    
    # Check if connected to cluster
    if ! kubectl cluster-info &> /dev/null; then
        print_error "Not connected to Kubernetes cluster"
        exit 1
    fi
    
    print_success "Prerequisites check passed"
}

# Function to build Docker image
build_image() {
    print_status "Building Docker image: $IMAGE_NAME:$TAG"
    
    # Build with build args
    docker build \
        --build-arg APP_VERSION=$TAG \
        --build-arg BUILD_TIME=$(date -u +"%Y-%m-%dT%H:%M:%SZ") \
        --build-arg GIT_COMMIT=$(git rev-parse --short HEAD 2>/dev/null || echo "unknown") \
        -t $IMAGE_NAME:$TAG \
        -t $IMAGE_NAME:latest \
        .
    
    if [ $? -eq 0 ]; then
        print_success "Docker image built successfully"
    else
        print_error "Failed to build Docker image"
        exit 1
    fi
}

# Function to push Docker image
push_image() {
    print_status "Pushing Docker image to registry..."
    
    # Tag for registry (assuming private registry)
    docker tag $IMAGE_NAME:$TAG registry.uys.com/$IMAGE_NAME:$TAG
    docker tag $IMAGE_NAME:latest registry.uys.com/$IMAGE_NAME:latest
    
    # Push to registry
    docker push registry.uys.com/$IMAGE_NAME:$TAG
    docker push registry.uys.com/$IMAGE_NAME:latest
    
    if [ $? -eq 0 ]; then
        print_success "Docker image pushed successfully"
    else
        print_error "Failed to push Docker image"
        exit 1
    fi
}

# Function to create namespace if not exists
create_namespace() {
    print_status "Creating namespace: $NAMESPACE"
    
    kubectl create namespace $NAMESPACE --dry-run=client -o yaml | kubectl apply -f -
    
    print_success "Namespace $NAMESPACE ready"
}

# Function to apply secrets
apply_secrets() {
    print_status "Applying secrets..."
    
    # Apply the secrets from k8s directory
    kubectl apply -f k8s/deployment.yaml -n $NAMESPACE
    
    print_success "Secrets applied"
}

# Function to deploy to Kubernetes
deploy_k8s() {
    print_status "Deploying to Kubernetes..."
    
    # Update image in deployment
    kubectl set image deployment/$SERVICE_NAME $SERVICE_NAME=registry.uys.com/$IMAGE_NAME:$TAG -n $NAMESPACE
    
    # Apply all manifests
    kubectl apply -f k8s/deployment.yaml -n $NAMESPACE
    
    # Wait for rollout
    kubectl rollout status deployment/$SERVICE_NAME -n $NAMESPACE --timeout=300s
    
    if [ $? -eq 0 ]; then
        print_success "Deployment completed successfully"
    else
        print_error "Deployment failed"
        exit 1
    fi
}

# Function to run health check
health_check() {
    print_status "Running health checks..."
    
    # Wait for pods to be ready
    kubectl wait --for=condition=ready pod -l app=$SERVICE_NAME -n $NAMESPACE --timeout=300s
    
    # Get service endpoint
    SERVICE_IP=$(kubectl get svc $SERVICE_NAME -n $NAMESPACE -o jsonpath='{.status.loadBalancer.ingress[0].ip}')
    if [ -z "$SERVICE_IP" ]; then
        SERVICE_IP=$(kubectl get svc $SERVICE_NAME -n $NAMESPACE -o jsonpath='{.spec.clusterIP}')
    fi
    
    # Health check
    for i in {1..10}; do
        if kubectl exec -n $NAMESPACE deployment/$SERVICE_NAME -- curl -f http://localhost:8080/api/actuator/health; then
            print_success "Health check passed"
            return 0
        fi
        print_warning "Health check attempt $i failed, retrying..."
        sleep 10
    done
    
    print_error "Health check failed after 10 attempts"
    return 1
}

# Function to show deployment info
show_info() {
    print_status "Deployment Information:"
    echo "------------------------"
    echo "Service: $SERVICE_NAME"
    echo "Image: registry.uys.com/$IMAGE_NAME:$TAG"
    echo "Namespace: $NAMESPACE"
    echo ""
    
    # Show pods
    kubectl get pods -l app=$SERVICE_NAME -n $NAMESPACE
    echo ""
    
    # Show service
    kubectl get svc $SERVICE_NAME -n $NAMESPACE
    echo ""
    
    # Show ingress
    kubectl get ingress -n $NAMESPACE
}

# Function to rollback deployment
rollback() {
    print_warning "Rolling back deployment..."
    kubectl rollout undo deployment/$SERVICE_NAME -n $NAMESPACE
    kubectl rollout status deployment/$SERVICE_NAME -n $NAMESPACE
    print_success "Rollback completed"
}

# Main deployment function
main() {
    print_status "Starting production deployment of $SERVICE_NAME:$TAG"
    
    check_prerequisites
    build_image
    
    # Skip push for local development
    if [ "$2" != "--local" ]; then
        push_image
    fi
    
    create_namespace
    apply_secrets
    deploy_k8s
    
    if health_check; then
        show_info
        print_success "Production deployment completed successfully!"
    else
        print_error "Deployment health check failed"
        read -p "Do you want to rollback? (y/n): " -n 1 -r
        echo
        if [[ $REPLY =~ ^[Yy]$ ]]; then
            rollback
        fi
        exit 1
    fi
}

# Help function
show_help() {
    echo "Usage: $0 [TAG] [OPTIONS]"
    echo ""
    echo "Arguments:"
    echo "  TAG        Docker image tag (default: latest)"
    echo ""
    echo "Options:"
    echo "  --local    Skip pushing to registry (for local testing)"
    echo "  --help     Show this help message"
    echo ""
    echo "Examples:"
    echo "  $0 v1.0.0                # Deploy version v1.0.0"
    echo "  $0 latest --local        # Deploy locally without pushing"
    echo "  $0 --help                # Show help"
}

# Handle command line arguments
case "$1" in
    --help)
        show_help
        exit 0
        ;;
    *)
        main "$@"
        ;;
esac