pipeline {
    agent {
        kubernetes {
            label 'kubectl-test-agent'
            defaultContainer 'kubectl'
            yaml """
apiVersion: v1
kind: Pod
metadata:
  labels:
    jenkins: kubectl-test
spec:
  containers:
    - name: kubectl
      image: bitnami/kubectl:latest
      command: ["/bin/sh"]
      args: ["-c", "cat"]  # giữ container sống để Jenkins chạy command
      tty: true
      volumeMounts:
        - name: kubeconfig
          mountPath: /root/.kube
  volumes:
    - name: kubeconfig
      secret:
        secretName: kubeconfig-jenkins
"""
        }
    }

    environment {
        KUBECONFIG = "/root/.kube/config"
    }

    stages {
        stage('Test kubectl access') {
            steps {
                container('kubectl') {
                    sh '''
                    echo "🔍 Testing kubectl connection to the cluster..."
                    kubectl cluster-info

                    echo "✅ Listing all namespaces:"
                    kubectl get ns
                    '''
                }
            }
        }
    }

    post {
        always {
            echo '✅ kubectl test completed.'
        }
        failure {
            echo '❌ kubectl test failed. Check KUBECONFIG or network.'
        }
    }
}
