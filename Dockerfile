FROM jenkins/jenkins:lts

COPY /init.d/basic-security.groovy  /usr/share/jenkins/ref/init.groovy.d/basic-security.groovy
COPY /init.d/init.plugins.groovy /usr/share/jenkins/ref/init.groovy.d/init.plugins.groovy


