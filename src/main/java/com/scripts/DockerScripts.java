package com.scripts;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DockerScripts {

    public static void main(String[] args) {

        FileWriter writer = null;

        try {
            String dockerComposeContents = readFile("src/main/resources/docker/DockerFileTemplate", Charset.defaultCharset());
            dockerComposeContents = dockerComposeContents.replace("${project.version}", getMavenProject().getVersion());
            writer = new FileWriter(new File("docker-compose.yml"));
            writer.write(dockerComposeContents);
        } catch (IOException e) { /* TODO: handle error */ }
        finally {
            try {
                writer.close();
            } catch (IOException e) { /* TODO: handle error */ }
        }
    }

    private static MavenProject getMavenProject() {
        Model model = null;
        FileReader reader = null;
        File pomFile = new File("pom.xml");
        MavenXpp3Reader mavenreader = new MavenXpp3Reader();
        try {
            reader = new FileReader(pomFile);
            model = mavenreader.read(reader);
            model.setPomFile(pomFile);
        } catch (Exception ex) {
        }
        return new MavenProject(model);
    }

    private static String readFile(String path, Charset encoding)
            throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

}
