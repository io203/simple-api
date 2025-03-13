/*
k8s용 Jenkinsfile 
작성일 : 2025.03. 13
작성자: 남궁성환
*/
def PROJECT_NAME = "simple-api"
def GIT_REPOSITORY = "https://github.com/io203/${PROJECT_NAME}.git"

def DOCKER_REGISTRY = "https://index.docker.io/v1/"

def GIT_OPS_REPOSITORY = "github.com/io203/simple-gitOps.git"

def BUILD_ENV = "dev"

def OPS_BRANCH = "master"
def DEPLOY_TYPE = "bluegreen"

def APP_IMAGE_NAME = "saturn203/${PROJECT_NAME}"
def GIT_TAG_MESSAGE;


pipeline {
    environment {
        DOCKER_CREDENTIALS_ID = "my-dockerhub"
        GIT_AUTH_CREDENTIALS_ID= "github-io203"
        GITHUB_TOKEN = credentials('io203-github-token')
        // IMG_TAG = "jenkins-test1.2"
  
    }
    agent {
        kubernetes {
            label 'jenkins-agent2'  // Pod 라벨 지정(옵션)
            containerTemplate(
                name: 'baseimg-build-tool',
                image: 'saturn203/baseimg-jdk17-skaffold-kustomize-git-docker:v1.0',
                command: 'cat',    // 컨테이너 유지 명령
                ttyEnabled: true   // TTY 활성화
            )
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

                // Jenkins에서 checkout 시 detached HEAD 상태가 발생한다 인은 Jenkins가 특정 커밋을 체크아웃하기 때문이다
                // 즉 checkout 시점에서 최신 커밋을 직접 참조하여 브랜치를 HEAD에 연결하지 않기 때문이다
                checkout([$class: 'GitSCM',
                    branches: [[name: OPS_BRANCH]],
                    userRemoteConfigs: [[url: "https://${GIT_OPS_REPOSITORY}", credentialsId: GIT_AUTH_CREDENTIALS_ID ]],
                    // userRemoteConfigs: [[url: GIT_REPOSITORY, credentialsId: 'io203-github-token' ]]
                    extensions: [[$class: 'CloneOption', depth: 0]] 
                ])
                
                // git checkout master를 명시적으로 실행하면 HEAD를 master 브랜치에 다시 연결할 수 있다
                container('baseimg-build-tool') {  
                    sh """
                        # [중요] master 브랜치로 이동해야 한다 
                        git checkout ${OPS_BRANCH}

                        cd ./${PROJECT_NAME}/${DEPLOY_TYPE}
                        kustomize edit set image ${APP_IMAGE_NAME}:${TAG}

                        cat kustomization.yaml

                        # host에서 실행시는 주석처리(한번만 가능하므로 주석처리)
                        git config --system user.email "admin@demo.com"
                        git config --system user.name "admin"


                        git add .
                        git commit -am '배포버전: ${TAG} / **롤백버전 : ${GIT_TAG_MESSAGE} **'

                        ## GITHUB_TOKEN 은 GitHub PAT(personal access token)으로 jenkins credential의 secret 타입으로 설정한다 
                        git remote set-url --push origin https://${GITHUB_TOKEN}@${GIT_OPS_REPOSITORY}
                        git push origin ${OPS_BRANCH}
                    """
                }
                print "======= git push finished !!!==========="
            }
        }
    }
}

// git tag의 message를 가져온다 message에는 보통 롤벡 버전을 적는다 
String gitTagMessage(tagName) {
    
    msg = sh(script: "git tag -n10000 -l ${tagName}", returnStdout: true)?.trim()
    if (msg) {
        return msg.substring(tagName.size()+1, msg.size())
    }
    return null
}