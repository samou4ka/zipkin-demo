pipeline {
    agent any
    stages {
        stage('Checkout Source') {
            steps {
                 git url:'https://github.com/samou4ka/zipkin-demo.git', branch:'master'
            }
        }

        stage('Build') {
             steps {
                  bat './gradlew clean build'
             }
        }

        stage("Build docker image") {
             steps {
                 script {
                      myapp = docker.build("samou4ka/zipkin-demo:${env.BUILD_ID}")
                 }
             }
        }

        stage("Push docker image") {
             steps {
                  script {
                       docker.withRegistry('https://registry.hub.docker.com', 'dockerhub') {
                            myapp.push("latest")
                       }
                  }
             }
        }
    }
}