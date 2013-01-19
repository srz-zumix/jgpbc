import hudson.util.RemotingDiagnostics

def res = manager.build.getResult();
if( res.isWorseThan(hudson.model.Result.SUCCESS) ) {
    def users = manager.build.getCulprits();
    if( users != null ) {
        def command = "softalkw.exe /W:";
        def find = false;
        users.each {
            command += "${it.getDisplayName()} ";
            find = true;
        }
        if( find ) {
            command += " アウトーーー！";
            manager.listener.logger.println(command);

            def computer = manager.build.getBuiltOn().toComputer();
            def channel = computer.getChannel();
            
            def script = """
                '${command}'.execute();
            """

            RemotingDiagnostics.executeGroovy( script, channel );
        }
    }
}

