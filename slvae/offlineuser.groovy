def jenkins = hudson.model.Hudson.instance
def slaves = jenkins.slaves
slaves.each {
    def com = it.toComputer()
    def cause = com.getOfflineCause()
    if( cause != null )
    {
        if( "${cause}".find("ChannelTermination") == null )
        {
            manager.listener.logger.println(it.getNodeName())
            manager.listener.logger.println("- ${cause}")
            manager.buildUnstable()
        }
    }
}
