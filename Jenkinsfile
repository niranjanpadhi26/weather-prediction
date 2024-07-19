pipeline {
    agent any

    tools {
        // Install the Maven version configured as "M3" and add it to the path.
        maven "mvn_3_9_8"
    }

    stages {
        stage('Maven Build') {
            steps {
                // Get some code from a GitHub repository
                git branch: 'main', url: 'https://github.com/niranjanpadhi26/weather-prediction'

                //bat "mvn -version"



                // Run Maven on a Unix agent.
                bat "mvn clean install"

                // To run Maven on a Windows agent, use
                // bat "mvn -Dmaven.test.failure.ignore=true clean package"
            }

        }
        stage('Build Docker Image') {
            steps {
                bat "docker build . -t neeru26/weather-prediction-service"
            }

        }
         stage('Pushing Docker Image') {
            steps {
                script {
                    withCredentials([string(credentialsId: 'dockerpswd', variable: 'dockerP')]) {
                        bat "docker login -u neeru26 -p ${dockerP}"
                        bat "docker push neeru26/weather-prediction-service"
                    }
                }
            }

        }
    }
}
