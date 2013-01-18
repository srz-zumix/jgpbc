def res = manager.build.getResult();
if( res.isWorseThan(hudson.model.Result.FAILURE) ) {
    def command = "softalk.exe /W:"
    manager.build.changeSet.each { cs ->
        cs.items.each {
            manager.listener.logger.println("XFD test");
            manager.listener.logger.println(it);
            command += "svn:${it.getDisplayName()}";
        }
    }
    command += " アウトーーー！";
    manager.listener.logger.println(command);
    'softalk.exe /close'.execute();
}

