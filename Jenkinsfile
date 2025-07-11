pipeline {
    agent {
        kubernetes {
            label 'kaniko-agent'
            defaultContainer 'kaniko'
            yamlMergeStrategy merge
            reuseNode true
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
      args: ["-c", "cat"]
      tty: true
      volumeMounts:
        - name: docker-config
          mountPath: /kaniko/.docker
        - name: workspace-volume
          mountPath: "/home/jenkins/agent"
          readOnly: false
    - name: kubectl
      image: bitnami/kubectl:latest
      command: ["/bin/sh"]
      args: ["-c", "apk add --no-cache gettext && cat"]
      tty: true
      volumeMounts:
        - name: kubeconfig
          mountPath: /root/.kube
        - name: workspace-volume
          mountPath: "/home/jenkins/agent"
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
        IMAGE_NAME = "lamld2510/core-lib"
        IMAGE_TAG = "${BUILD_NUMBER}"
        DOCKER_REGISTRY = "docker.io"
        CACHE_REPO = "lamld2510/core-lib-cache"
        KUBECONFIG = "/root/.kube/config"
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
                    echo "🔨 Building and pushing Docker image with Kaniko..."
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
                    echo "🔍 Checking if deployment.yaml exists..."
                    if [ ! -f src/main/resources/k8s/deployment.yaml ]; then
                      echo "❌ ERROR: deployment.yaml not found."
                      exit 1
                    fi

                    echo "✅ Found deployment.yaml. Preparing deployment..."
                    export IMAGE_TAG=${IMAGE_TAG}
                    export DOCKER_REGISTRY=${DOCKER_REGISTRY}
                    envsubst < src/main/resources/k8s/deployment.yaml > k8s-deploy-final.yaml

                    echo "🔍 Verifying final YAML:"
                    cat k8s-deploy-final.yaml

                    echo "🧪 Validating YAML with dry-run..."
                    kubectl apply -f k8s-deploy-final.yaml --dry-run=client || {
                      echo "❌ ERROR: Kubernetes manifest is invalid."
                      exit 1
                    }

                    echo "🔗 Testing connection to Kubernetes cluster..."
                    kubectl version --short || {
                      echo "❌ ERROR: kubectl cannot connect to cluster."
                      exit 1
                    }

                    echo "🚀 Deploying to Kubernetes..."
                    kubectl apply -f k8s-deploy-final.yaml || {
                      echo "❌ ERROR: kubectl apply failed."
                      exit 1
                    }
                    '''
                }
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'k8s-deploy-final.yaml', allowEmptyArchive: true
            echo '✅ Pipeline completed.'
        }
        failure {
            echo '❌ Pipeline failed. Please check logs above.'
        }
    }
}
