pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'user-service'
        DOCKER_TAG = 'latest'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/dhanushgubba/userservice.git'
            }
        }

        stage('Build Maven') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    sh "docker build -t user-service:latest ."
                }
            }
        }

        stage('Push to DockerHub') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub',
                    usernameVariable: 'DOCKER_USERNAME',
                    passwordVariable: 'DOCKER_PASSWORD'
                )]) {
                    script {
                        sh """
                            echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin
                            docker tag ${DOCKER_IMAGE}:${DOCKER_TAG} $DOCKER_USERNAME/${DOCKER_IMAGE}:${DOCKER_TAG}
                            docker push $DOCKER_USERNAME/${DOCKER_IMAGE}:${DOCKER_TAG}
                        """
                    }
                }
            }
        }
        stage('Deploy to EC2') {
            steps {
                sshagent(['ec2-ssh-key']) {
                    sh '''
                        ssh -o StrictHostKeyChecking=no ubuntu@<EC2-PUBLIC-IP> "
                        docker pull $DOCKER_USERNAME/${DOCKER_IMAGE}:${DOCKER_TAG} &&
                        docker stop user-service || true &&
                        docker rm user-service || true &&
                        docker run -d -p 8082:8082 --name user-service $DOCKER_USERNAME/${DOCKER_IMAGE}:${DOCKER_TAG}
                        "
                    '''
                }
            }
        }
    }
}
