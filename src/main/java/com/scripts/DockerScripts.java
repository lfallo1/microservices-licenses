package com.scripts;

import com.google.common.io.Closeables;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.project.MavenProject;
import org.springframework.boot.info.GitProperties;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class DockerScripts {

    public static void main(String[] args) {

        FileWriter writer = null;
        String appVersion = getMavenProject().getVersion();
        String gitHash = loadGitProperty("git.commit.id.abbrev");
        try {
            String dockerComposeContents = readFile("src/main/resources/docker/DockerComposeTemplate", Charset.defaultCharset());
            dockerComposeContents = dockerComposeContents.replace("${project.version}", appVersion + "-" + gitHash);
            writer = new FileWriter(new File("docker-compose.yml"));
            writer.write(dockerComposeContents);
        } catch (IOException e) { /* TODO: handle error */ }
        finally {
            try {
                writer.close();
            } catch (IOException e) { /* TODO: handle error */ }
        }
    }

    private static String loadGitProperty(String key){
        ClassPathResource resource = new ClassPathResource( "git.properties" );
        Properties p = new Properties();
        InputStream inputStream = null;
        String propertyValue = "";
        try {
            inputStream = resource.getInputStream();
            p.load( inputStream );
            propertyValue = p.getProperty(key);
        } catch ( IOException e ) {
            System.out.println(e.toString());
        } finally {
            Closeables.closeQuietly( inputStream );
        }
        return propertyValue;
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
        MavenProject mp = new MavenProject(model);
        return mp;
    }

    private static String readFile(String path, Charset encoding)
            throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

}
