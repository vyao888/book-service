node {

    withMaven(maven:'maven') {

        stage('Checkout') {
            git url: '<span class="skimlinks-unlinked">https://github.com/vyao888/book-service.git</span>', credentialsId: 'vyao888', branch: 'master'
        }

        stage('Build') {
            sh 'mvn clean install'

            def pom = readMavenPom file:'<span class="skimlinks-unlinked">pom.xml</span>'
            print pom.version
            env.version = pom.version
        }

        stage('Image') {
            dir ('book-service') {
                def app = <span class="skimlinks-unlinked">docker.build</span> "localhost:8888/book-service:${env.version}"
                <span class="skimlinks-unlinked">app.push</span>()
            }
        }

        stage ('Run') {
            <span class="skimlinks-unlinked">docker.image("localhost:8888/book-service:${env.version}").run('-p</span>book-service --name BookServiceApplication')
        }

    }

}