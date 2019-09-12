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

['ace-editor',
'allure-jenkins-plugin',
'ant',
'blueocean',
'email-ext',
'envinject',
'fstrigger',
'git',
'junit',
'mailer',
'thinBackup',
'timestamper',
'configuration-as-code',
'ssh-slaves',
'Parameterized-Remote-Trigger',
'workflow-aggregator',
'pipeline-aws',
'pipeline-utility-steps',
'git-changelog',
'sonar'
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

