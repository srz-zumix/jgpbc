
static print(logger, prop)
{
  def jenkins = hudson.model.Hudson.instance
  def slaves = jenkins.slaves
  slaves.each {
    def com = it.toComputer()
    def properties = com.getSystemProperties()
    if( properties != null )
    {
      logger.println(it.getNodeName())
      logger.println(properties[prop])
    }
  }
}
