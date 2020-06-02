pipeline {
    agent any

    options {
        skipStagesAfterUnstable()
    }

    /*environment {
        M2_HOME = '/Users/weimao/apache-maven-3.6.1'
        PATH    = "${PATH}:${M2_HOME}/bin:/usr/local/bin/docker"
    }*/

    stages {
        stage ('Initialize') {
            steps {
                sh '''
                    echo "PATH = ${PATH}"
                    echo "M2_HOME = ${M2_HOME}"
                '''
            }
        }

        stage('Packaging') {
            steps {
                sh 'mvn -B -DskipTests clean package'
            }
        }

        stage('Unit Testing') {
            steps {
                sh 'mvn test'
            }

            /*post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }*/
        }

        stage('Build Image') {
            steps {
                sh "docker build -t authorization-service ."
            }
        }

        stage('Deploy') {

            steps {
                //sh "docker run -d --rm --name auth-service-${BUILD_NUMBER} -p 8000:8000 authorization-service:latest"
                sh "kubectl delete deployment auth-svc-deployment"
                sh "kubectl delete service auth-svc-service"
                sh "kubectl apply -f k8s-deployment.yml"
            }
        }
    }
}