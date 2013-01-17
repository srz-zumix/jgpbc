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

// ���� PC ��̃X���[�u���I�t���C��
def slaves = jenkins.slaves
slaves.each {
    def com = it.toComputer()
    def name = com.getEnvironment().get("COMPUTERNAME","")
    if( name.compareToIgnoreCase(target_name) == 0 ) {
        if( com.isOnline() ) {
            // �X���[�u���I�t���C���ɂ��ăA�C�h���҂�
            com.setTemporarilyOffline(true, SimpleOfflineCause.create(new OfflineMessage()))
            // ���ۂɃA�C�h���҂�����̂́A�S���I�t���C���ɂ��Ă���
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

// �ċN���҂�
Thread.sleep(5*60*1000)

// �Đڑ�
slaves.each {
    def com = it.toComputer()
    def name = com.getEnvironment().get("COMPUTERNAME","")
    if( name.compareToIgnoreCase(target_name) == 0 ) {
        com.setTemporarilyOffline(false, null)
    }
}

