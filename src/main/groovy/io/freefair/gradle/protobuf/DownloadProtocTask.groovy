package io.freefair.gradle.protobuf

import okhttp3.OkHttpClient
import okhttp3.Request
import okio.Okio
import okio.Sink
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

class DownloadProtocTask extends DefaultTask {

    @Input
    String protocVersion;

    @Input
    String protocArch = ProtoUtil.getProtocArch();

    @OutputFile
    File protocZipFile = new File(this.getTemporaryDir(), "protoc.zip");

    @TaskAction
    def downloadProtocZip() throws IOException {

        OkHttpClient client = new OkHttpClient();

        Request get = new Request.Builder().get()
                .url(ProtoUtil.getProtocZipLink(protocVersion, protocArch))
                .build();

        protocZipFile.getParentFile().mkdirs();

        Sink sink = Okio.sink(protocZipFile);

        client.newCall(get).execute().body().source().readAll(sink);
    }

}
