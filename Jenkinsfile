
def PROJECT_NAME = "simple-api"
def GIT_REPOSITORY = "https://github.com/io203/${PROJECT_NAME}.git"

def DOCKER_REGISTRY = "https://index.docker.io/v1/"

def GIT_OPS_REPOSITORY = "github.com/io203/simple-gitOps.git"

def BUILD_ENV = "dev"

def OPS_BRANCH = "master"
def DEPLOY_TYPE = "bluegreen"
// def deployType = "canary"
def APP_IMAGE_NAME = "saturn203/${PROJECT_NAME}"
def GIT_TAG_MESSAGE;


def BASEIMG_BUILD_TOOL_POD ='''
apiVersion: v1
kind: Pod
metadata:
  labels:
    jenkins: agent
spec:
  containers:
  - name: baseimg-build-tool
    image: saturn203/baseimg-jdk17-skaffold-kustomize-git-docker:v1.0
    command: ['cat']
    tty: true
'''


pipeline {
    environment {       
        DOCKER_CREDENTIALS_ID = "my-dockerhub"
        GIT_AUTH_CREDENTIALS_ID= "github-io203"
        // IMG_TAG = "jenkins-test1.2"
  
    }
    agent {
        kubernetes {
            yaml "${BASEIMG_BUILD_TOOL_POD}"
        }
    }
    stages {
        stage('Checkout') {
            steps {            
                checkout([$class: 'GitSCM',
                    // branches: [[name: "main"]],
                    branches: [[name: "${params.TAG}"]],
                    // userRemoteConfigs: [[url: GIT_REPOSITORY ]]
                    userRemoteConfigs: [[url: GIT_REPOSITORY, credentialsId: GIT_AUTH_CREDENTIALS_ID ]]
                ])
            }
        }
        stage('Build') {
            steps {
                // docker.withRegistry 안에서는 원칙적으로 script block을 사용할수 없다 따라서 script block을 감싸면 가능하다
                script {
                    container('baseimg-build-tool') {
                        // Jenkins plugin : Docker Pipeline (id: docker-workflow)
                        // docker client가 있는 이미지가 있어야 한다
                        docker.withRegistry(DOCKER_REGISTRY,DOCKER_CREDENTIALS_ID){
                            sh "skaffold build -p ${BUILD_ENV} -t ${TAG}"
                        }
                        GIT_TAG_MESSAGE =  gitTagMessage(TAG);
                        print("=======GIT_TAG_MESSAGE=========="+GIT_TAG_MESSAGE );
                    }
                }
            }
            
        }
        stage('workspace clear'){    
            steps{
                cleanWs()
            }            
        }
        stage('gitOps'){
            steps{
                print "======kustomization.yaml tag update====="
            
                checkout([$class: 'GitSCM',
                    branches: [[name: OPS_BRANCH]],
                    userRemoteConfigs: [[url: "https://${GIT_OPS_REPOSITORY}", credentialsId: GIT_AUTH_CREDENTIALS_ID ]]
                    // userRemoteConfigs: [[url: GIT_REPOSITORY, credentialsId: 'io203-github-token' ]]
                ])
                container('baseimg-build-tool') {  
                sh """
                    pwd
                    ls -al
                    cd ./${PROJECT_NAME}/${DEPLOY_TYPE}
                    ls -al
                    cat kustomization.yaml
                    kustomize edit set image ${APP_IMAGE_NAME}:${TAG}

                    # host에서 실행시는 주석처리(한번만 가능하므로 주석처리)
                    git config --system user.email "admin@demo.com"
                    git config --system user.name "admin"  


                    git add . 
                    git commit -am '배포버전: ${TAG} / **롤백버전 : ${GIT_TAG_MESSAGE} **'   
                    git remote set-url --push origin https://${GITHUB_TOKEN}@${GIT_OPS_REPOSITORY}
                    git push origin ${OPS_BRANCH}
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