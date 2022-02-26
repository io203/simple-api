def PROJECT_NAME = "simple-api"
def gitUrl = "https://github.com/io203/${PROJECT_NAME}.git"

def imgRegistry = "https://registry.hub.docker.com"

def gitOpsUrl = "github.com/io203/simple-gitOps.git"

def opsBranch = "master"
def deployType = "bluegreen"

def appImageName = "saturn203/${PROJECT_NAME}"

def GIT_TAG_MESSAGE;

pipeline {
    agent {
        docker {
          
            image 'saturn203/baseimg-jdk17-skaffold-kustomize-git:v1.0'
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

                    // sh "skaffold build -p dev -t ${TAG}"
                    
                    // .m2 cache 안되는 경우 docker,k8s등의 환경에서 skaffold check cache를 비활성화:  --cache-artifacts=false 
                    sh "skaffold build -p dev -t ${TAG} --cache-artifacts=false"
                   
                }

                GIT_TAG_MESSAGE =  gitTagMessage(TAG);
                print("=======GIT_TAG_MESSAGE=========="+GIT_TAG_MESSAGE );
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
            print "======kustomization.yaml tag update====="
        
            git url: "https://${gitOpsUrl}", branch: "${opsBranch}" , credentialsId: "io203-github-token"    

            script{      
                sh """
                pwd
                ls -al
                cd ./${PROJECT_NAME}/${deployType}
                ls -al
                cat kustomization.yaml
                kustomize edit set image ${appImageName}:${TAG}

                # host에서 실행시는 주석처리
                git config --system user.email "admin@demo.com"
                git config --system user.name "admin"  


                git add . 
                git commit -am '배포버전: ${TAG} / **롤백버전 : ${GIT_TAG_MESSAGE} **'   
                git remote set-url --push origin https://${GITHUB_TOKEN}@${gitOpsUrl}
                git push origin ${opsBranch}
                """
            }
            print "======= git push finished !!!==========="
          }
      }
    }
}

String gitTagMessage(tagName) {
    
    msg = sh(script: "git tag -n10000 -l ${tagName}", returnStdout: true)?.trim()
    if (msg) {
        return msg.substring(tagName.size()+1, msg.size())
    }
    return null
}
