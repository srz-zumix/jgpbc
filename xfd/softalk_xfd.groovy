import hudson.util.RemotingDiagnostics

def res = manager.build.getResult();
if( res.isWorseThan(hudson.model.Result.SUCCESS) ) {
    def command = "softalkw.exe /W:";
    def find = false;
    manager.build.changeSet.each { cs ->
        cs.items.each {
            command += "${it.getDisplayName()} ";
            find = true;
        }
    }
    
    if( find ) {
        def computer = manager.build.getBuiltOn().toComputer();
        def channel = computer.getChannel();
        
        def script = """
            '${command}'.execute();
        """

        RemotingDiagnostics.executeGroovy( script, channel );
    }
}

