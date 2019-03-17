docker run -p 8080:8080 -p 50000:50000 -v jenkins_home:/var/jenkins_home jenkins/jenkins:lts

docker run -p 8080:8080 -p 50000:50000 -v c:\temp\simple:/var/jenkins_home 24c40c7a7c8f

TODO

1.  wget http://localhost:8080/jnlpJars/jenkins-cli.jar
2. java -jar jenkins-cli.jar -s http://localhost:8080 get-job test > test.xml

    решить: ERROR: anonymous is missing the Job/ExtendedRead permission