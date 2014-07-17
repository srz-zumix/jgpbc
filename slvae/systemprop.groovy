def jenkins = hudson.model.Hudson.instance
def slaves = jenkins.slaves
slaves.each {
    def com = it.toComputer()
    def properties = com.getSystemProperties()
    if( properties != null )
    {
        manager.listener.logger.println(it.getNodeName())
        manager.listener.logger.println(properties["java.version"])
    }
}
