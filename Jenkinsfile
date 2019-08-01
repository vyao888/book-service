node {

    withMaven(maven:'maven') {

        stage('Checkout') {
            git url: 'https://github.com/vyao888/book-service.git', credentialsId: 'vyao888', branch: 'master'
        }

        stage('Build') {
            sh 'mvn clean install'

            def pom = readMavenPom file:'pom.xml'
            print pom.version
            env.version = pom.version
        }

        stage('Image') {
            dir ('book-service') {
                def app = docker.build "localhost:8888/book-service:${env.version}"
                app.push()
            }
        }

        stage ('Run') {
            docker.image("localhost:8888/book-service:${env.version}").run('-p book-service --name BookServiceApplication')
        }

    }

}