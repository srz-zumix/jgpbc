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

if( manager.build.getResult().isWorseThan(hudson.model.Result.SUCCESS) ) {
    def timestr = new Date().format("HH:mm dd/MM/yy z", TimeZone.getDefault())
    def msg = "automated offline: ${manager.build.getProject().getDisplayName()}#${manager.build.number} : " + timestr
    def cause = SimpleOfflineCause.create(new OfflineMessage(msg))
    def com = manager.build.getBuiltOn().toComputer()
    if( com.isOnline() ) {
        com.setTemporarilyOffline(true, cause)
    }
}
