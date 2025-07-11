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
      command:
        - /busybox/sh
      args:
        - -c
        - cat
      tty: true
      volumeMounts:
        - name: docker-config
          mountPath: /kaniko/.docker
    - name: kubectl
      image: bitnami/kubectl:latest
      command:
        - /bin/sh
      args:
        - -c
        - cat
      tty: true
      volumeMounts:
        - name: kubeconfig
          mountPath: /root/.kube
  volumes:
    - name: docker-config
      projected:
        sources:
          - secret:
              name: docker-config
    - name: kubeconfig
      secret:
        secretName: kubeconfig-jenkins
"""
        }
    }

    environment {
        IMAGE_NAME = "lamld2510/core-lib"
        IMAGE_TAG = "${BUILD_NUMBER}"
        DOCKER_REGISTRY = "docker.io"
        CACHE_REPO = "lamld2510/core-lib-cache"
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
                    echo "🔨 Building and pushing image..."
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
            steps {
                container('kubectl') {
                    sh '''
                    echo "🚀 Generating Kubernetes deployment YAML..."
                    envsubst < src/main/resources/k8s/deployment.yaml > k8s-deploy-final.yaml

                    echo "🚀 Applying deployment to Kubernetes cluster..."
                    kubectl apply -f k8s-deploy-final.yaml
                    '''
                }
            }
        }
    }

    post {
        always {
            echo '✅ Build & Deploy finished.'
        }
        failure {
            echo '❌ Có lỗi xảy ra trong pipeline.'
        }
    }
}
