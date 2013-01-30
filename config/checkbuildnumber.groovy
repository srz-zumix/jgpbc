import hudson.model.AbstractProject

def jenkins = hudson.model.Hudson.instance

def projects = jenkins.getAllItems(AbstractProject.class)
projects.each { project ->
    def lastBuild = project.getLastBuild()
    if( lastBuild != null )
    {
        def lastNumber = lastBuild.getNumber()
        def nextNumber = project.getNextBuildNumber()
        if( nextNumber < lastNumber )
        {
            manager.listener.logger.println(project.getName())
            manager.listener.logger.println("Warning: next build number [${nextNumber}] is smaller than the number of the log [${lastNumber}].")
            manager.buildUnstable()
        }
    }
}

