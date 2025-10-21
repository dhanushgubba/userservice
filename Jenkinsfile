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

        stage('Build with Maven') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh "docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} ."
            }
        }


        stage('Push to DockerHub') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub',
                    usernameVariable: 'DOCKER_USERNAME',
                    passwordVariable: 'DOCKER_PASSWORD'
                )]) {
                    sh """
                        echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin
                        docker tag ${DOCKER_IMAGE}:${DOCKER_TAG} $DOCKER_USERNAME/${DOCKER_IMAGE}:${DOCKER_TAG}
                        docker push $DOCKER_USERNAME/${DOCKER_IMAGE}:${DOCKER_TAG}
                    """
                }
            }
        }
        stage('Deploy to EC2') {
			steps {
				sshagent(['ec2-ssh-key']) {
					sh """
					ssh -o StrictHostKeyChecking=no ubuntu@43.204.22.237 '
                    docker pull dhanushgubba/user-service:latest &&
                    docker stop user-service || true &&
                    docker rm user-service || true &&
                    docker run -d -p 8082:8082 --name user-service dhanushgubba/user-service:latest
                	'
           		 	"""
        		}
    		}
		}

    }
}
