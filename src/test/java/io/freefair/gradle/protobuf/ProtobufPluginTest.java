package io.freefair.gradle.protobuf;

import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;

import static org.junit.Assert.*;
import static org.gradle.testkit.runner.TaskOutcome.*;

/**
 * Created by larsgrefer on 10.10.16.
 */
public class ProtobufPluginTest {

    @Rule
    public final TemporaryFolder testProjectDir = new TemporaryFolder();
    private File buildFile;

    @Before
    public void setup() throws IOException {
        buildFile = testProjectDir.newFile("build.gradle");
        testProjectDir.newFolder("src", "main", "proto");
        File file = testProjectDir.newFile("src/main/proto/Test.proto");

        writeFile(file, "package io.freefair.test;\n" +
                "\n" +
                "message Reservierung {\n" +
                "    required int32 zugnummer = 1;\n" +
                "    required bool richtung = 2;\n" +
                "    required int32 abschnitt= 3;\n" +
                "\n" +
                "    optional int64 timestamp = 20;\n" +
                "}");
    }

    @Test
    public void testHelloWorldTask() throws IOException {
        String buildFileContent = "plugins { id 'io.freefair.protobuf' }";
        writeFile(buildFile, buildFileContent);

        BuildResult result = GradleRunner.create()
                .withProjectDir(testProjectDir.getRoot())
                .withArguments("compileProto")
                .withPluginClasspath()
                .build();

        assertEquals(result.task(":compileProto").getOutcome(), SUCCESS);
    }

    private void writeFile(File destination, String content) throws IOException {
        BufferedWriter output = null;
        try {
            output = new BufferedWriter(new FileWriter(destination));
            output.write(content);
        } finally {
            if (output != null) {
                output.close();
            }
        }
    }

}