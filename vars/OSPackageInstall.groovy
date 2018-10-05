#!/usr/bin/groovy

def call(packages, package_manager) {
    if (packages instanceof List) {
        package_str = packages.join(' ')
    }
    else { // assume String
        package_str = packages
    }

    if (package_manager.toLowerCase() == 'yum') {
        cmd_prefix = 'yum -y install'
    }
    else if (package_manager.toLowerCase() == 'apt') {
        cmd_prefix = 'apt install -y --no-install-recommends'
    }
    else {
        println("Package manager '${package_manager}' not supported")
        System.exit(1)
    }

    l_cmd = [cmd_prefix, package_str]
    cmd = l_cmd.join(' ')
    sh(script: cmd)
}
