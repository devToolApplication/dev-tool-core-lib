pipeline {
    agent {
        kubernetes {
            label 'kaniko-agent'
            defaultContainer 'kaniko'
            yaml """
apiVersion: v1
kind: Pod
metadata:
  labels:
    jenkins: kaniko
spec:
  containers:
    - name: kaniko
      image: gcr.io/kaniko-project/executor:debug
      command: ["/busybox/sh"]
      args: ["-c", "sleep 600"]
      volumeMounts:
        - name: docker-config
          mountPath: /kaniko/.docker
        - mountPath: "/home/jenkins/agent"
          name: "workspace-volume"
          readOnly: false

    - name: kubectl
      image: alpine:3.19
      command: ["/bin/sh"]
      args:
        - "-c"
        - |
          apk add --no-cache curl gettext bash &&
          curl -LO https://dl.k8s.io/release/v1.29.0/bin/linux/amd64/kubectl &&
          chmod +x kubectl &&
          mv kubectl /usr/local/bin/ &&
          sleep 600
      volumeMounts:
        - name: kubeconfig
          mountPath: /root/.kube
        - mountPath: "/home/jenkins/agent"
          name: "workspace-volume"
          readOnly: false

  volumes:
    - name: docker-config
      projected:
        sources:
          - secret:
              name: docker-config
    - name: kubeconfig
      secret:
        secretName: kubeconfig-jenkins
    - name: workspace-volume
      emptyDir: {}
"""
        }
    }

    environment {
        SERVICE_NAME = "core-lib"
        DOCKER_USERNAME = "lamld2510"
        IMAGE_TAG = "${BUILD_NUMBER}"
        DOCKER_REGISTRY = "docker.io"
        IMAGE_NAME = "${DOCKER_USERNAME}/${SERVICE_NAME}"
        CACHE_REPO = "${DOCKER_USERNAME}/${SERVICE_NAME}-cache"
        NAMESPACE = "dev"
    }

    stages {
        stage('Checkout') {
            steps {
                git credentialsId: 'github-jenkins', url: 'https://github.com/devToolApplication/dev-tool-core-lib.git'
            }
        }

        stage('Build & Push with Kaniko + Cache') {
            steps {
                container('kaniko') {
                    sh '''
                    echo "🔨 Building image with Kaniko..."
                    /kaniko/executor \
                      --dockerfile=Dockerfile \
                      --context=dir://$(pwd) \
                      --destination=${DOCKER_REGISTRY}/${IMAGE_NAME}:${IMAGE_TAG} \
                      --destination=${DOCKER_REGISTRY}/${IMAGE_NAME}:latest \
                      --cache=true \
                      --cache-repo=${DOCKER_REGISTRY}/${CACHE_REPO} \
                      --verbosity=info
                    '''
                }
            }
        }

        stage('Deploy to Kubernetes') {
            options {
                timeout(time: 3, unit: 'MINUTES')
            }
            steps {
                container('kubectl') {
                    sh '''
                    echo "🔍 Checking envsubst availability..."
                    command -v envsubst || { echo "❌ ERROR: envsubst not found"; exit 1; }

                    DEPLOY_FILE=src/main/resources/k8s/deployment.yaml
                    echo "📄 Checking $DEPLOY_FILE..."
                    [ -f "$DEPLOY_FILE" ] || { echo "❌ ERROR: $DEPLOY_FILE not found."; exit 1; }

                    echo "🛠️  Creating deployment manifest..."
                    export IMAGE_TAG=${IMAGE_TAG}
                    export DOCKER_REGISTRY=${DOCKER_REGISTRY}
                    export DOCKER_USERNAME=${DOCKER_USERNAME}
                    export SERVICE_NAME=${SERVICE_NAME}
                    export NAMESPACE=${NAMESPACE}
                    envsubst < "$DEPLOY_FILE" > k8s-deploy-final.yaml

                    echo "📄 Generated YAML:"
                    cat k8s-deploy-final.yaml

                    echo "📦 Checking if namespace '$NAMESPACE' exists..."
                    kubectl get namespace "$NAMESPACE" >/dev/null 2>&1 || {
                        echo "🆕 Namespace '$NAMESPACE' not found. Creating..."
                        kubectl create namespace "$NAMESPACE" || {
                            echo "❌ ERROR: Failed to create namespace '$NAMESPACE'"; exit 1;
                        }
                    }

                    echo "🧪 Validating YAML with dry-run..."
                    kubectl apply -f k8s-deploy-final.yaml --dry-run=client || {
                        echo "❌ ERROR: Invalid manifest."; exit 1;
                    }

                    echo "🚀 Deploying to Kubernetes..."
                    kubectl apply -f k8s-deploy-final.yaml || {
                        echo "❌ ERROR: kubectl apply failed."; exit 1;
                    }
                    '''
                }
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'k8s-deploy-final.yaml', allowEmptyArchive: true
            echo '✅ Build & Deploy finished.'
        }
        failure {
            echo '❌ Build or Deploy failed. Check logs for details.'
        }
    }
}
