pipeline {
 agent any
 stages {
        stage("Build") {
            withCredentials([file(credentialsId: 'env-cicd', variable: 'env-file')]) {
                sh 'php --version'
                sh 'composer install'
                sh 'composer --version'
                sh 'cp \$env-file .env'
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
