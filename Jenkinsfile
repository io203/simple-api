
def PROJECT_NAME = "simple-api"
def gitUrl = "https://github.com/io203/${PROJECT_NAME}.git"

def imgRegistry = "https://registry.hub.docker.com"

def gitOpsUrl = "github.com/io203/simple-gitOps.git"

def BUILD_ENV = "dev"

def opsBranch = "master"
def deployType = "bluegreen"
// def deployType = "canary"
def appImageName = "saturn203/${PROJECT_NAME}"
def GIT_TAG_MESSAGE;

podTemplate(label: 'simple-api-job',
    containers: [
        // cache 적용 (아직 적용안해봄)
        // containerTemplate(name: 'baseimg-build-tool', image: 'saturn203/baseimg-jdk17-skaffold-kustomize-git:v1.0', command: 'sleep', args: '-v $HOME/.m2:/root/.m2'),
        containerTemplate(name: 'baseimg-build-tool', image: 'saturn203/baseimg-jdk17-skaffold-kustomize-git:v1.0', command: 'sleep', args: '99'),
    ],
    volumes: [ 
        hostPathVolume(mountPath: '/var/run/docker.sock', hostPath: '/var/run/docker.sock'), 
    ]
) {

    node('simple-api-job') {
        stage('build') {
            // git url: 'https://github.com/io203/simple-api.git', branch: 'refs/tags/${TAG}' , credentialsId: "io203-github-token" 
            checkout([$class: 'GitSCM',
                  branches: [[name: "${params.TAG}"]],      
                  doGenerateSubmoduleConfigurations: false,
                  extensions: [],
                  gitTool: 'Default',
                  submoduleCfg: [],
                  userRemoteConfigs: [[url: "${gitUrl}", credentialsId: 'io203-github-token' ]]
                ])
                     
            container('baseimg-build-tool') {
                docker.withRegistry("${imgRegistry}","dockerhub-saturn203"){
                   sh "skaffold build -p ${BUILD_ENV} -t ${TAG}"

                    // .m2 cache 안되는 경우 docker,k8s등의 환경에서 skaffold check cache를 비활성화:  --cache-artifacts=false 
                    // sh "skaffold build -p ${BUILD_ENV} -t ${TAG} --cache-artifacts=false"
                                
                }
                GIT_TAG_MESSAGE =  gitTagMessage(TAG);
                print("=======GIT_TAG_MESSAGE=========="+GIT_TAG_MESSAGE );
            }
        }
        stage('workspace clear'){          
            cleanWs()            
        }
        stage('gitOps') {      
           
            print "======kustomization.yaml tag update====="
        
            git url: "https://${gitOpsUrl}", branch: "${opsBranch}" , credentialsId: "io203-github-token"    

            container('baseimg-build-tool') {  
                sh """
                    pwd
                    ls -al
                    cd ./${PROJECT_NAME}/${deployType}
                    ls -al
                    cat kustomization.yaml
                    kustomize edit set image ${appImageName}:${TAG}

                    # host에서 실행시는 주석처리(한번만 가능하므로 주석처리)
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


String gitTagMessage(tagName) {
    
    msg = sh(script: "git tag -n10000 -l ${tagName}", returnStdout: true)?.trim()
    if (msg) {
        return msg.substring(tagName.size()+1, msg.size())
    }
    return null
}