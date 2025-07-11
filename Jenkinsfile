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
      image: bitnami/kubectl:latest
      command: ["/bin/sh"]
      args: ["-c", "apk add --no-cache gettext && sleep 600"]
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
                timeout(time: 10, unit: 'MINUTES')
            }
            steps {
                container('kubectl') {
                    sh '''
                    echo "🚀 Checking if envsubst is available..."
                    which envsubst || { echo "ERROR: envsubst not found"; exit 1; }

                    echo "🚀 Checking if deployment.yaml exists..."
                    ls -la src/main/resources/k8s/deployment.yaml || { echo "ERROR: deployment.yaml not found"; exit 1; }

                    echo "🚀 Creating deployment.yaml from template..."
                    export IMAGE_TAG=${IMAGE_TAG}
                    export DOCKER_REGISTRY=${DOCKER_REGISTRY}
                    envsubst < src/main/resources/k8s/deployment.yaml > k8s-deploy-final.yaml

                    echo "🚀 Verifying generated YAML..."
                    cat k8s-deploy-final.yaml || { echo "ERROR: Failed to generate k8s-deploy-final.yaml"; exit 1; }

                    echo "🚀 Validating YAML with dry-run..."
                    kubectl apply -f k8s-deploy-final.yaml --dry-run=client || { echo "ERROR: Invalid YAML"; exit 1; }

                    echo "🚀 Checking kubectl connectivity..."
                    kubectl version --client --short || { echo "ERROR: kubectl version failed"; exit 1; }

                    echo "🚀 Applying deployment to Kubernetes..."
                    kubectl apply -f k8s-deploy-final.yaml || { echo "ERROR: Failed to apply deployment"; exit 1; }
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