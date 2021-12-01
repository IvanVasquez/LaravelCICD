pipeline {
 agent any
 stages {
        stage('Git Pull') {
            steps {
                git branch: 'master', url: 'https://github.com/IvanVasquez/LaravelCICD.git'
            }
        }
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
        stage("Docker build") {
            steps {
                sh "docker build -t ivanvasquez0713/laravel-cicd ."
            }
        }
  }
}
