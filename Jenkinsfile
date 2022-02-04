
def PROJECT_NAME = "simple-api"
def gitUrl = "https://github.com/io203/${PROJECT_NAME}.git"

def imgRegistry = "https://registry.hub.docker.com"

def gitOpsUrl = "github.com/io203/simple-gitOps.git"
def opsBranch = "master"


podTemplate(label: 'simple-api-job',
    containers: [
        containerTemplate(name: 'skaffold', image: 'gcr.io/k8s-skaffold/skaffold:v1.32.0', command: 'sleep', args: '99d'),
    ],
    volumes: [ 
        hostPathVolume(mountPath: '/var/run/docker.sock', hostPath: '/var/run/docker.sock'), 
    ]
) {

    node('simple-api-job') {
        stage('build') {
            // git url: 'https://github.com/io203/simple-api.git', branch: 'refs/tags/${TAG}' , credentialsId: "io203-github-token" 
            checkout scm: [
                    $class: 'GitSCM', 
                    userRemoteConfigs: [[url: "${gitUrl}",
                    credentialsId: 'io203-github-token' ]], 
                     branches: [[name: 'refs/tags/${TAG}']]],
                     poll: false
                     
            container('skaffold') {
                docker.withRegistry("${imgRegistry}","dockerhub-saturn203"){
                    sh "skaffold build -p dev -t ${TAG}"                   
                }
            }
        }
        stage('workspace clear'){
          
            cleanWs()
            
        }
        stage('gitOps') {      
           
            git url: 'https://${gitOpsUrl}', branch: 'master' , credentialsId: "io203-github-token" 
            container('skaffold') {  
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