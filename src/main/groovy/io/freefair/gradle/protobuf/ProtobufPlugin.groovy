package io.freefair.gradle.protobuf

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.Copy

/**
 * Created by larsgrefer on 10.10.16.
 */
class ProtobufPlugin implements Plugin<Project> {

    private Project project;
    private ProtobufExtension protobufExtension;

    @Override
    void apply(Project project) {
        this.project = project;

        protobufExtension = project.extensions.create("protobuf", ProtobufExtension);

        //"https://github.com/google/protobuf/releases/download/v$version/protoc-$version-${protocArch}.zip"

        DownloadProtocTask downloadProtocTask = project.task("downloadProtoc", type: DownloadProtocTask) {
            protocVersion = protobufExtension.version
        } as DownloadProtocTask

        Copy extractProtoc = project.task("extractProtoc", type: Copy) { Copy t ->
            t.dependsOn downloadProtocTask

            t.from(project.zipTree(downloadProtocTask.protocZipFile))
            t.destinationDir = project.file("$project.buildDir/protoc")
        } as Copy

        project.task("compileProto", type: CompileProtoTask) { CompileProtoTask t ->
            t.dependsOn extractProtoc
            t.protoc = "$extractProtoc.destinationDir/bin/protoc"
            t.protoPath project.file("$extractProtoc.destinationDir/include")
            t.source = project.file("$project.projectDir/src/main/proto")
        }
    }
}
