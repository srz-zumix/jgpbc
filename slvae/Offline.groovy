import hudson.slaves.OfflineCause.SimpleOfflineCause

class OfflineMessage extends org.jvnet.localizer.Localizable {
  String message
  OfflineMessage(String msg) {
    super(null, null, [])
    this.message = msg
  }
  String toString() {
    this.message
  }
  String toString(java.util.Locale l) {
    toString()
  }
}

static print(manager)
{
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
}

static to_offline_caused_by_result(manager)
{
  if( manager.build.getResult().isWorseThan(hudson.model.Result.SUCCESS) ) {
    def timestr = new Date().format("HH:mm dd/MM/yy z", TimeZone.getDefault())
    def msg = "automated offline: ${manager.build.getProject().getDisplayName()}#${manager.build.number} : " + timestr
    def cause = SimpleOfflineCause.create(new OfflineMessage(msg))
    def com = manager.build.getBuiltOn().toComputer()
    if( com.isOnline() ) {
      com.setTemporarilyOffline(true, cause)
    }
  }
}

static to_offline(manager, message)
{
    def timestr = new Date().format("HH:mm dd/MM/yy z", TimeZone.getDefault())
    def msg = message + timestr
    def cause = SimpleOfflineCause.create(new OfflineMessage(msg))
    def com = manager.build.getBuiltOn().toComputer()
    if( com.isOnline() ) {
      com.setTemporarilyOffline(true, cause)
    }
  }
}


