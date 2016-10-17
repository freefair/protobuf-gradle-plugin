package io.freefair.gradle.protobuf

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

/**
 * Created by larsgrefer on 12.10.16.
 */
class CompileProtoTask extends DefaultTask {

    @Input
    File source;

    @Input
    String protoc;

    @Input
    List<File> protoPaths = new ArrayList<>();

    @OutputDirectory
    File cppOut = project.file("$project.buildDir/generated-src/main/cpp");

    @OutputDirectory
    File csharpOut = project.file("$project.buildDir/generated-src/main/csharp");

    @OutputDirectory
    File javaOut = project.file("$project.buildDir/generated-src/main/java");

    @OutputDirectory
    File javananoOut = project.file("$project.buildDir/generated-src/main/javanano");

    @OutputDirectory
    File jsOut = project.file("$project.buildDir/generated-src/main/js");

    @OutputDirectory
    File objcOut = project.file("$project.buildDir/generated-src/main/objc");

    @OutputDirectory
    File phpOut = project.file("$project.buildDir/generated-src/main/php");

    @OutputDirectory
    File pythonOut = project.file("$project.buildDir/generated-src/main/python");

    @OutputDirectory
    File rubyOut = project.file("$project.buildDir/generated-src/main/ruby");

    @Input
    int version = 2;

    def protoPath(File file) {
        this.protoPaths.add(file);
    }

    @TaskAction
    compileProto() {
        def cmd = "$protoc"

        if (version >= 2) {
            [
                    "--cpp_out"     : cppOut,
                    "--java_out"    : javaOut,
                    "--python_out"  : pythonOut,
            ].each {
                if (it.value != null) {
                    it.value.mkdirs();
                    cmd += " $it.key=$it.value.absolutePath"
                }
            }
        }

        if (version >= 3) {
            [
                    "--csharp_out"  : csharpOut,
                    "--javanano_out": javananoOut,
                    "--js_out"      : jsOut,
                    "--objc_out"    : objcOut,
                    "--php_out"     : phpOut,
                    "--ruby_out"    : rubyOut,
            ].each {
                if (it.value != null) {
                    it.value.mkdirs();
                    cmd += " $it.key=$it.value.absolutePath"
                }
            }
        }

        cmd += " --proto_path=$source"

        this.protoPaths.each {
            cmd += " --proto_path=$it"
        }

        def sourceTree = project.fileTree(dir: source);

        sourceTree.include "*.proto", "**/*.proto"

        cmd += " " + sourceTree.asList().join(" ")

        logger.info("execute: $cmd");

        def execute = cmd.execute();

        execute.inputStream.eachLine {
            logger.info(it)
        }

        StringBuilder error = new StringBuilder();

        execute.errorStream.eachLine {
            logger.error(it)
            error.append(it).append("\n");
        }

        if(execute.exitValue() != 0) {
            throw new GradleException(error.toString());
        }
    }
}
