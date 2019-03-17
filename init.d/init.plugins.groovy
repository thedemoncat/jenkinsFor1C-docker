import jenkins.model.*
import com.cloudbees.plugins.credentials.*
import com.cloudbees.plugins.credentials.common.*
import com.cloudbees.plugins.credentials.domains.*
import com.cloudbees.plugins.credentials.impl.*
import com.cloudbees.jenkins.plugins.sshcredentials.impl.*
import hudson.plugins.sshslaves.*;
import hudson.model.Node.Mode
import hudson.slaves.*
import jenkins.model.Jenkins

def deployPlugin(plugin) {
  if (! plugin.isEnabled() ) {
    plugin.enable()
  }
  plugin.getDependencies().each { 
    deployPlugin(pm.getPlugin(it.shortName)) 
  }
}

def needRestart = false;

pm = Jenkins.instance.pluginManager
pm.doCheckUpdatesServer()

[   'ace-editor',
'allure-jenkins-plugin',
'ant',
'antisamy-markup-formatter',
'apache-httpcomponents-client-4-api',
'authentication-tokens',
'blueocean',
'blueocean-autofavorite',
'blueocean-bitbucket-pipeline',
'blueocean-commons',
'blueocean-config',
'blueocean-core-js',
'blueocean-dashboard',
'blueocean-display-url',
'blueocean-events',
'blueocean-github-pipeline',
'blueocean-git-pipeline',
'blueocean-i18n',
'blueocean-jira',
'blueocean-jwt',
'blueocean-personalization',
'blueocean-pipeline-api-impl',
'blueocean-pipeline-editor',
'blueocean-pipeline-scm-api',
'blueocean-rest',
'blueocean-rest-impl',
'blueocean-web',
'bouncycastle-api',
'branch-api',
'build-timeout',
'cloudbees-bitbucket-branch-source',
'cloudbees-folder',
'command-launcher',
'credentials',
'credentials-binding',
'display-url-api',
'docker-commons',
'docker-workflow',
'durable-task',
'email-ext',
'envinject',
'envinject-api',
'favorite',
'fstrigger',
'git',
'git-client',
'github',
'github-api',
'github-branch-source',
'git-server',
'gradle',
'handlebars',
'handy-uri-templates-2-api',
'htmlpublisher',
'jackson2-api',
'jdk-tool',
'jenkins-design-language',
'jira',
'jquery-detached',
'jsch',
'junit',
'ldap',
'mailer',
'mapdb-api',
'matrix-auth',
'matrix-project',
'mercurial',
'momentjs',
'pam-auth',
'pipeline-build-step',
'pipeline-github-lib',
'pipeline-graph-analysis',
'pipeline-input-step',
'pipeline-milestone-step',
'pipeline-model-api',
'pipeline-model-declarative-agent',
'pipeline-model-definition',
'pipeline-model-extensions',
'pipeline-rest-api',
'pipeline-stage-step',
'pipeline-stage-tags-metadata',
'pipeline-stage-view',
'plain-credentials',
'pubsub-light',
'resource-disposer',
'scm-api',
'script-security',
'sonar',
'sse-gateway',
'ssh-credentials',
'ssh-slaves',
'structs',
'subversion',
'thinBackup',
'timestamper',
'token-macro',
'variant',
'workflow-aggregator',
'workflow-api',
'workflow-basic-steps',
'workflow-cps',
'workflow-cps-global-lib',
'workflow-durable-task-step',
'workflow-job',
'workflow-multibranch',
'workflow-scm-step',
'workflow-step-api',
'workflow-support',
'ws-cleanup',
].each{ plugin ->
pm = Jenkins.instance.updateCenter.getPlugin(plugin);
println plugin;
println pm.getInstalled()
try {
    if (pm.getInstalled() == null) {
      println ">>>> install plugin "+plugin;
      try {
          deployment = Jenkins.instance.updateCenter.getPlugin(plugin).deploy(true)
          deployment.get()
          needRestart = true;   
      } catch (all) {
          println "Error:"+all;
      }
    }
  
} catch (e) {
  println "${e}"
}
}

pm = Jenkins.instance.pluginManager
pm.doCheckUpdatesServer()
//Jenkins.instance.updateCenter.doUpgrade(null);
plugins = pm.plugins
plugins = Jenkins.instance.updateCenter.getUpdates()

println "update plugins";
plugins.each {
  
  println it.name;
  Jenkins.instance.updateCenter.getPlugin(it.name).getNeededDependencies().each {
    println ">>>> update plugin "+it.name;
    it.deploy(true)
  }
  needRestart = true;
  it.deploy(true);
  
}

println ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
println ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"

println "need reboot "+(Jenkins.instance.updateCenter.isRestartRequiredForCompletion() | needRestart)

if (Jenkins.instance.updateCenter.isRestartRequiredForCompletion() | needRestart) {
    hudson.model.Hudson.instance.doSafeRestart(null)
}
println ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"

