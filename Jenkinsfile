pipeline {
    agent any

    environment {
        // Defined environment variables (best practice to separate build parameters)
        DOCKER_IMAGE = 'user-service'
        DOCKER_TAG = 'latest'
        DOCKER_USERNAME = 'dhanushgubba'
        EC2_HOST = '65.0.184.163'
        
        // AWS SSH Key ID for EC2 deployment
        SSH_CREDENTIAL_ID = 'aws-ec2-key'
        // DockerHub credential ID
        DOCKER_CREDENTIAL_ID = 'dockerhub'
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/dhanushgubba/userservice.git'
            }
        }

        stage('Build with Maven') {
            steps {
                // Uses 'bat' command for Windows agent
                bat 'mvn clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: env.DOCKER_CREDENTIAL_ID,
            		usernameVariable: 'DOCKER_USERNAME_ENV', // Use a temporary variable name
            		passwordVariable: 'DOCKER_PASSWORD_ENV'   // Use a temporary variable name
        	    )]) {
                    // Windows command to log in and build
                    bat """
                        echo %DOCKER_PASSWORD_ENV% | docker login -u %DOCKER_USERNAME_ENV% --password-stdin
                        docker build -t %DOCKER_IMAGE%:%DOCKER_TAG% .
                    """
                }
            }
        }


        stage('Push to DockerHub') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: env.DOCKER_CREDENTIAL_ID,
                    usernameVariable: 'DOCKER_USERNAME_ENV',
                    passwordVariable: 'DOCKER_PASSWORD_ENV'
                )]) {
                    // Removed unnecessary 'script' block
                    // Windows command to log in, tag, and push
                    bat """
                        echo %DOCKER_PASSWORD_ENV% | docker login -u %DOCKER_USERNAME_ENV% --password-stdin
                        docker tag %DOCKER_IMAGE%:%DOCKER_TAG% %DOCKER_USERNAME%/%DOCKER_IMAGE%:%DOCKER_TAG%
                        docker push %DOCKER_USERNAME%/%DOCKER_IMAGE%:%DOCKER_TAG%
                    """
                }
            }
        }

        stage('Deploy to AWS EC2') {
            steps {
                sshagent([env.SSH_CREDENTIAL_ID]) {
                    // The 'sh' step is generally required for SSH/remote execution
                    // even if the Jenkins agent is Windows, because the EC2 host is Linux.
                    // This assumes the 'sh' program (like Git Bash or Cygwin) is installed
                    // and in the PATH on the Windows Jenkins agent.
                    sh """
                        ssh -o StrictHostKeyChecking=no ${EC2_HOST} '
                        # Pull latest image
                        docker pull ${DOCKER_USERNAME}/${DOCKER_IMAGE}:${DOCKER_TAG} &&

                        # Stop existing container (if running) and ignore errors
                        docker stop ${DOCKER_IMAGE} || true &&
                        
                        # Remove existing container (if it exists) and ignore errors
                        docker rm ${DOCKER_IMAGE} || true &&

                        # Run new container
                        docker run -d -p 8082:8082 --name ${DOCKER_IMAGE} ${DOCKER_USERNAME}/${DOCKER_IMAGE}:${DOCKER_TAG}
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