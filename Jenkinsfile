pipeline {
    agent any
    environment {
        DOCKER_USERNAME = credentials('dockerhub')
    }
    stages {
        stage('Clone Repository') {
            steps {
                git branch: 'main', url: 'https://github.com/dhanushgubba/userservice.git'
            }
        }
        stage('Build JAR') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }
        stage('Build Docker Image') {
            steps {
                sh "docker build -t ${DOCKER_USERNAME}/user-service:latest ."
            }
        }
        stage('Push Docker Image') {
            steps {
                sh "echo ${DOCKER_USERNAME_PSW} | docker login -u ${DOCKER_USERNAME_USR} --password-stdin"
                sh "docker push ${DOCKER_USERNAME}/user-service:latest"
            }
        }
    }
}
