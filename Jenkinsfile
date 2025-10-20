pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'user-service'
        DOCKER_TAG = 'latest'
        DOCKER_USERNAME = 'dhanushgubba'
        EC2_HOST = '13.234.113.65'
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/dhanushgubba/userservice.git'
            }
        }

        stage('Build with Maven') {
            steps {
                bat 'mvn clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    bat "docker build -t %DOCKER_IMAGE%:%DOCKER_TAG% ."
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
                        bat """
                            echo %DOCKER_PASSWORD% | docker login -u %DOCKER_USERNAME% --password-stdin
                            docker tag %DOCKER_IMAGE%:%DOCKER_TAG% %DOCKER_USERNAME%/%DOCKER_IMAGE%:%DOCKER_TAG%
                            docker push %DOCKER_USERNAME%/%DOCKER_IMAGE%:%DOCKER_TAG%
                        """
                    }
                }
            }
        }

        stage('Deploy to AWS EC2') {
            steps {
                sshagent(['aws-ec2-key']) {
                    sh """
                        ssh -o StrictHostKeyChecking=no ${EC2_HOST} '
                        docker pull ${DOCKER_USERNAME}/${DOCKER_IMAGE}:${DOCKER_TAG} &&
                        docker stop ${DOCKER_IMAGE} || true &&
                        docker rm ${DOCKER_IMAGE} || true &&
                        docker run -d -p 8082:8080 --name ${DOCKER_IMAGE} ${DOCKER_USERNAME}/${DOCKER_IMAGE}:${DOCKER_TAG}
                        '
                    """
                }
            }
        }
    }

    post {
        success {
            echo '✅ Deployment completed successfully!'
        }
        failure {
            echo '❌ Deployment failed. Check logs for errors.'
        }
    }
}
