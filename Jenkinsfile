pipeline {
    agent { //here we select only docker build agents
        docker {
            image 'maven-docker-agent:1'
            //label 'maven-docker-agent'
            //args '-v $HOME/.m2:/root/.m2' //here you can map local maven repo, this let you to reuse local artifacts
            //args '-v /var/run/docker.sock:/var/run/docker.sock'
        }
    }

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
                sh "docker build -t auth-service-${BUILD_NUMBER} ."
            }
        }

        stage('Deploy') {

            steps {
                sh "docker run --name auth-service -p 8000:8000 auth-service-${BUILD_NUMBER}"
            }
        }
    }
}