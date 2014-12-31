#! scripts/pyenv/bin/python

import re
from os import path
from fabric.api import env, run, task
from fabric.colors import green, red
from fabric.operations import abort, local, prompt, warn

env.use_ssh_config = True

@task
def release():
    print(green("Make uht-traktor.ch release"))

    properties = get_release_properties()
    update_manifest(properties[0], "uht-rest/src/main/resources/META-INF/MANIFEST.MF")
    mvn_release(properties[0], properties[1], properties[2])

    print(green("Release done. release.version=" + properties[0]))

@task
def deploy():
    warn(red("Not yet implemented!"))

def get_release_properties():

    # get current version from maven
    current_version = get_current_version()

    # remove -SNAPSHOT for release version
    release_version = current_version.replace("-SNAPSHOT", "")

    # scm tag
    scm_tag = "uht-traktor-" + release_version

    # get next version based on current release version
    new_dev_version = release_version
    last_num = re.compile(r'(?:[^\d]*(\d+)[^\d]*)+')
    m = last_num.search(release_version)
    if m:
        next = str(int(m.group(1))+1)
        start, end = m.span(1)
        new_dev_version = release_version[:max(end-len(next), start)] + next + release_version[end:]
    new_dev_version = new_dev_version + "-SNAPSHOT"

    # prompt for the final values, give default values from above
    release_version = prompt("What is the release version: ", None, release_version)
    scm_tag = prompt("What is SCM release tag or label: ", None, scm_tag)
    new_dev_version = prompt("What is the new development version: ", None, new_dev_version)

    # new dev version must end with -SNAPSHOT as otherwise we will run into maven troubles later
    if not new_dev_version.endswith("-SNAPSHOT"):
        abort(red("Invalid new development version, must end with '-SNAPSHOT'."))

    return [release_version, scm_tag, new_dev_version]

def update_manifest(version, manifest_path):
    print(green("Update MANIFEST.MF"))
    manifest_file = manifest_path
    file_path = path.relpath(manifest_file)
    with open(file_path, "w") as manifest:
        manifest.truncate()
        manifest.write("Manifest-Version: 1.0\n")
        manifest.write("Implementation-Title: UHT Traktor\n")
        manifest.write("Implementation-Version: " + version + "\n")
        manifest.write("Implementation-Vendor-Id: ch.uhttraktor.website\n")
        manifest.write("Implementation-Vendor: UHT Traktor\n")
        manifest.close()

    local("git add " + manifest_file)
    local("git commit -m \"Update MANIFEST.MF\"")
    local("git push")

def mvn_release(release_version, scm_tag, dev_version):
    print(green("Make maven release"))
    local("mvn -B -DreleaseVersion=" + release_version + " -Dtag=" + scm_tag + " -DdevelopmentVersion=" + dev_version + " release:prepare")
    local("mvn release:perform")

def get_current_version():
    return local("mvn org.apache.maven.plugins:maven-help-plugin:evaluate -Dexpression=project.version | grep -Ev '(^\[|Download\w+:)'", capture=True)
