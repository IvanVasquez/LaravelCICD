pipeline {
 agent any
 stages {
    stage("Git Pull") {
        steps {
            git branch: 'master', url: 'https://github.com/IvanVasquez/LaravelCICD.git'
        }
    }
    stage("Composer") {
        setps {
            sh 'php -r "copy(\'https://getcomposer.org/installer\', \'composer-setup.php\');"'
            sh 'php composer-setup.php'
            sh 'php -r "unlink(\'composer-setup.php\');"'
        }
    }
    stage("Build") {
        steps {
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
    stage("Docker push") {
        environment {
            DOCKER_USERNAME = credentials("docker-user")
            DOCKER_PASSWORD = credentials("docker-password")
        }
        steps {
            sh "docker login --username ${DOCKER_USERNAME} --password ${DOCKER_PASSWORD}"
            sh "docker push ivanvasquez0713/laravel-cicd"
        }
    }
  }
}
