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
                    sh "docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} ."
                }
            }
        }

        stage('Push to DockerHub') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'dockerhub',
                        usernameVariable: 'DOCKER_USERNAME',
                        passwordVariable: 'DOCKER_PASSWORD'
                    )
                ]) {
                    script {
                        sh """
                            docker login -u %DOCKER_USERNAME% -p %DOCKER_PASSWORD%
                            docker tag ${DOCKER_IMAGE}:${DOCKER_TAG} %DOCKER_USERNAME%/${DOCKER_IMAGE}:${DOCKER_TAG}
                            docker push %DOCKER_USERNAME%/${DOCKER_IMAGE}:${DOCKER_TAG}
                        """
                    }
                }
            }
        }
    }
}
