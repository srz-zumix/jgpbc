import hudson.slaves.OfflineCause.SimpleOfflineCause
import hudson.util.RemotingDiagnostics

def jenkins = hudson.model.Hudson.instance

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
def target_name = manager.build.buildVariables.get("REBOOT_SLAVE")

if ( target_name == null ) {
    manager.listener.logger.println("ERROR!! REBOOT_SLAVE is null.")
}

// 同一 PC 上のスレーブをオフライン
def slaves = jenkins.slaves
slaves.each {
    def com = it.toComputer()
    def name = com.getEnvironment().get("COMPUTERNAME","")
    if( name.compareToIgnoreCase(target_name) == 0 ) {
        if( com.isOnline() ) {
            // スレーブをオフラインにしてアイドル待ち
            com.setTemporarilyOffline(true, SimpleOfflineCause.create(new OfflineMessage()))
            // 実際にアイドル待ちするのは、全部オフラインにしてから
        }
    }
}

slaves.each {
    def com = it.toComputer()
    def name = com.getEnvironment().get("COMPUTERNAME","")
    def node_name = it.getNodeName()
    if( name.compareToIgnoreCase(target_name) == 0 ) {
        if( !com.isIdle() ) {
            manager.listener.logger.println("Wait ${node_name} Slave Task...")
            while( !com.isIdle() ) {
                Thread.sleep(3000)
            }
        }
    }
}

// reboot
def script = """

    if (Functions.isWindows()) {
      'cmd /c "shutdown /f /r /t 10 -m \\\\\\\\${target_name} /c \"Restarting after Jenkins test completed\""'.execute()
    }

"""

def computer = manager.build.getBuiltOn().toComputer()
def channel = computer.getChannel()
manager.listener.logger.println(script)
RemotingDiagnostics.executeGroovy( script, channel )

// 再起動待ち
Thread.sleep(5*60*1000)

// 再接続
slaves.each {
    def com = it.toComputer()
    def name = com.getEnvironment().get("COMPUTERNAME","")
    if( name.compareToIgnoreCase(target_name) == 0 ) {
        com.setTemporarilyOffline(false, null)
    }
}

