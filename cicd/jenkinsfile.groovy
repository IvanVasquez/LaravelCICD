pipeline {
 agent any
 stages {
        stage("Build") {
            environment {
                ENV_FILE = file("env-cicd")
            }
            steps {
                sh 'php --version'
                sh 'composer install'
                sh 'composer --version'
                sh 'cp \$ENV_FILE .env'
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
