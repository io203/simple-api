def PROJECT_NAME = "simple-api"
def gitUrl = "https://github.com/io203/${PROJECT_NAME}.git"

def imgRegistry = "https://registry.hub.docker.com"

def gitOpsUrl = "github.com/io203/simple-gitOps.git"
def opsBranch = "master"

pipeline {
    agent {
        docker {
          
            image 'saturn203/base-jdk17-skaffold-kustomize:v1.2'
            reuseNode true             
        }
    }
    stages {
      stage('Build') {
          steps {
            checkout([$class: 'GitSCM',
                  branches: [[name: "${params.TAG}"]],      
                  doGenerateSubmoduleConfigurations: false,
                  extensions: [],
                  gitTool: 'Default',
                  submoduleCfg: [],
                  userRemoteConfigs: [[url: "${gitUrl}", credentialsId: 'io203-github-token' ]]
                ])
            
            script{              
                docker.withRegistry("${imgRegistry}","dockerhub-saturn203"){
                    sh "skaffold build -p dev -t ${TAG} --cache-artifacts=false"
                }
            }
            
          }
      }
  
  
      stage('Clean') {
          steps {
              cleanWs()
          }
      }
  
      stage('gitOps') {
          steps {
            git url: "https://${gitOpsUrl}", branch: "master" , credentialsId: "io203-github-token" 

            script{      
                sh """
                pwd
                ls -al
                cd ./simple-api/bluegreen
                ls -al
                cat kustomization.yaml
                kustomize edit set image saturn203/simple-api:${TAG}
                git config --system user.email "admin@demo.com"
                git config --system user.name "admin"  


                git add . 
                git commit -am 'update image tag ${TAG}'   
                git remote set-url --push origin https://${GITHUB_TOKEN}@${gitOpsUrl}
                git push origin ${opsBranch}
                """
            }
          }
      }
    }
}
