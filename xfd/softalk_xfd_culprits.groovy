def res = manager.build.getResult();
if( res.isWorseThan(hudson.model.Result.FAILURE) ) {
    def users = manager.build.getCulprits();
    if( users != null ) {
        def command = "softalk.exe /W:"
        users.each {
            manager.listener.logger.println("XFD test");
            manager.listener.logger.println(it.getDisplayName());
            command += "svn:${it.getDisplayName()}";
        }
        command += " アウトーーー！";
        manager.listener.logger.println(command);
        'softalk.exe /close'.execute();
    }
}

