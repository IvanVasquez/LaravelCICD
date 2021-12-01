pipeline {
 agent any
 stages {
        stage("Build") {
            steps{
                sh 'php --version'
                sh 'composer install'
                sh 'composer --version'
                withCredentials([file(credentialsId: 'env-cicd', variable: 'FILE')]) {
                    sh 'cp $FILE .env'
                    sh 'chmod 664 .env'
                }
                sh 'php artisan key:generate'
                sh 'cp .env .env.testing'
                sh 'php artisan migrate'
            }
        }
        stage("Unit test") {
            steps {
                sh 'php artisan test'
            }
        }
  }
}
