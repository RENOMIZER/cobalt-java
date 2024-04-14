package org.renomizer.packages;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import org.json.*;
import org.apache.commons.io.IOUtils;

public class VideoDownloader {
  public static void main(String[] args) throws IOException, InterruptedException, URISyntaxException {
    if (args[0].equals("--help") || args[0].equals("-h") || args[0].equals("-?")) {
      System.out.println("Usage: java -jar .\\request-test.jar DESTINATION_FOLDER FILE_NAME URL");
      System.exit(0);
    }
    else if (args.length != 3) {
      throw new ArrayIndexOutOfBoundsException("Not enough arguments. Expect 3, got " + args.length);
    }

    downloadVideo(args[0], args[1], args[2]);
  }

  public static void downloadVideo(String destDir, String fileName, String Url) throws IOException, InterruptedException, URISyntaxException {
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("https://co.wuk.sh/api/json"))
        .headers("Accept", "application/json", "Content-Type", "application/json")
        .POST(BodyPublishers.ofString(String.format("{\"url\":\"%s\"}", Url)))
        .build();

    HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

    JSONObject body = new JSONObject(response.body());
    String streamUrl = body.getString("url");

    InputStream inputStream = new URI(streamUrl).toURL().openStream();
    
    File targetFile = new File(destDir + "\\" + fileName + ".mp4");
    OutputStream outputStream = new FileOutputStream(targetFile);

    IOUtils.copy(inputStream, outputStream);
    inputStream.close();
    outputStream.close();
  }
}
