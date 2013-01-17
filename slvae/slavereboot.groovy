import hudson.slaves.OfflineCause.SimpleOfflineCause

class OfflineMessage extends org.jvnet.localizer.Localizable {
  def message
  OfflineMessage() {
    super(null, null, [])
    def timestr = new Date().format("HH:mm dd/MM/yy z", TimeZone.getTimeZone("UTC"))
    this.message = "automated reboot at end of test at " + timestr
  }
  String toString() {
    this.message
  }
  String toString(java.util.Locale l) {
    toString()
  }
}

def cause = SimpleOfflineCause.create(new OfflineMessage())
def com = manager.build.getBuiltOn().toComputer()
com.disconnect(cause)
