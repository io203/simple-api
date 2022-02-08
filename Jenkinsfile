def PROJECT_NAME = "simple-api"
def gitUrl = "https://github.com/io203/${PROJECT_NAME}.git"

// def imgRegistry = "https://registry.hub.docker.com"

def gitOpsUrl = "github.com/io203/simple-gitOps.git"
def opsBranch = "master"





/////////////////////////////

pipeline {
     environment {        
         PATH = "$PATH:/usr/local/bin/"  //maven, skaffold, argocd,jq path
       }
    agent any   
    
    stages {   
        stage('Build') {           
            steps {                  
                checkout scm: [
                        $class: "GitSCM", 
                        userRemoteConfigs: [[url: "${gitUrl}",
                        credentialsId: "io203-github-token" ]], 
                        branches: [[name: "refs/tags/${TAG}"]]],
                    poll: false
                script{                                     
                    // docker.withRegistry("${imgRegistry}","dockerhub-saturn203"){
                    //     sh "skaffold build -p dev -t ${TAG}"                   
                    // }

                    // mac local 일때만 사용 linux 환경에서는 docker.withRegistry 사용 
                    sh "skaffold build -p dev -t ${TAG}"  

                    
                }
            }
        } 
        stage('workspace clear'){
            steps {
                cleanWs()
            }
        }
        stage('GitOps update') {   
            steps{
                print "======kustomization.yaml tag update====="
                git url: "https://${gitOpsUrl}", branch: "master" , credentialsId: "io203-github-token"    
                               
                script{
                    sh """
                        pwd
                        ls -al
                        cd ./simple-api/bluegreen
                        ls -al
                        cat kustomization.yaml
                        kustomize edit set image saturn203/simple-api:${TAG}
                        
                        # 로컬외에는 주석 제거한다 
                        # git config --system user.email "admin@demo.com"
                        # git config --system user.name "admin"  

    
                        git add . 
                        git commit -am 'update image tag ${TAG}'   
                        git remote set-url --push origin https://${GITHUB_TOKEN}@${gitOpsUrl}
                        git push origin ${opsBranch}
                    """
                 } 
                
                print "git push finished !!!"
            }

        }

        
    }
}



