pipeline {
    agent {
        kubernetes {
            label 'kubectl-test-agent'
            defaultContainer 'kubectl'
            yaml """
apiVersion: v1
kind: Pod
spec:
  containers:
  - name: kubectl
    image: bitnami/kubectl:latest
    command: ["/bin/sh"]
    args: ["-c", "sleep 3600"]
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
        stage('Test Kubectl') {
            steps {
                container('kubectl') {
                    sh '''
                    echo "✅ Đang test kết nối tới Kubernetes..."
                    kubectl cluster-info
                    kubectl get nodes
                    kubectl get pods -A
                    '''
                }
            }
        }
    }
}
