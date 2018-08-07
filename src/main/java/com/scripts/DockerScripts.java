package com.scripts;

import com.google.common.io.Closeables;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.project.MavenProject;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Helper class invoked during the maven build - and is used to dynamically generate the docker-compose.yml file
 */
public class DockerScripts {

    public static void main(String[] args) {

        FileWriter writer = null;
        String appVersion = getMavenProject().getVersion(); //get the app version from the pom.xml
        String gitHash = loadGitProperty("git.commit.id.abbrev"); //load the git hash from plugin generated git.properties file
        String gitBranch = loadGitProperty("git.branch");
        try {

            //read the docker compose template into a string
            String dockerComposeContents = readFile("src/main/resources/docker/DockerComposeTemplate", Charset.defaultCharset());

            //replace project.version placeholder with the application's version + git hash
            dockerComposeContents = dockerComposeContents.replace("${project.version}", appVersion + "-" + gitHash + "-" + gitBranch);

            //write the resulting string into a new docker-compose.yml file
            writer = new FileWriter(new File("docker-compose.yml"));
            writer.write(dockerComposeContents);
        } catch (IOException e) { /* TODO: handle error */ } finally {
            try {
                writer.close();
            } catch (IOException e) { /* TODO: handle error */ }
        }
    }

    /**
     * returns a specific property value from the generated git.properties file
     * @param key
     * @return
     */
    private static String loadGitProperty(String key) {
        ClassPathResource resource = new ClassPathResource("git.properties");
        Properties p = new Properties();
        InputStream inputStream = null;
        String propertyValue = "";
        try {
            inputStream = resource.getInputStream();
            p.load(inputStream);
            propertyValue = p.getProperty(key);
        } catch (IOException e) {
            System.out.println(e.toString());
        } finally {
            Closeables.closeQuietly(inputStream);
        }
        return propertyValue;
    }

    /**
     * load the maven project obj - a class representation of the pom.xml
     * @return
     */
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

    /**
     * read a file with the specified encoding, and return the resulting string
     * @param path
     * @param encoding
     * @return
     * @throws IOException
     */
    private static String readFile(String path, Charset encoding)
            throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

}
