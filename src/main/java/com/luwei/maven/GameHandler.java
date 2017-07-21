package com.luwei.maven;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.StringUtils;
import org.stringtemplate.v4.ST;

import java.io.*;
import java.nio.file.Files;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lhcpig on 2017/7/20.
 */
@Mojo(name = "run")
public class GameHandler extends AbstractMojo {
    private static final String DEFAULT_INPUT_DIR = "/src/main/protobuf/".replace('/', File.separatorChar);
    @Parameter(required = true, property = "greeting")
    private String greeting;

    @Parameter(property = "inputDirectories")
    private File[] inputDirectories;
    @Component
    private MavenProject project;
    private String template = getTemplate();
    private String handlerDir;

    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("-----------------------start create handler---------------------------------");
        String protobufDir = project.getBasedir().getPath() + DEFAULT_INPUT_DIR;
        String clientProto = protobufDir + "Client.proto";

        String pack = "/com/luwei/game/handler/test/".replace('/', File.separatorChar);
        String path = project.getBuild().getSourceDirectory() + pack;
        handlerDir = project.getBuild().getSourceDirectory() + pack;
        File handlerDirFile = new File(handlerDir);
        handlerDirFile.mkdirs();
        try {
            Pattern p = Pattern.compile("//(\\d)+:(\\w+) (.*)$");

            List<String> lines = Files.readAllLines(new File(clientProto).toPath());
            for (String line : lines) {
                Matcher matcher = p.matcher(line);
                if (matcher.matches()) {
                    getLog().info(line);
                    int cmd = Integer.parseInt(matcher.group(1));
                    String msg = matcher.group(2);
                    String comment = matcher.group(3);
                    createHandler(cmd, msg, comment);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (File inputDirectory : inputDirectories) {
            getLog().info(inputDirectory.getAbsolutePath());
        }
    }

    private void createHandler(int cmd, String msg, String comment) throws IOException {
        File file = new File(handlerDir + msg+"Handler.java");
        if (!file.exists()) {
            ST st = new ST(template, '$', '$');
            st.add("msg", msg);
            String content = st.render();
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            writer.write(content);
            writer.flush();
            writer.close();
            System.out.println(file.getPath());
        }
    }

    private String getTemplate()  {
        InputStream templateInputStream = this.getClass().getResourceAsStream("/template.java");
        StringWriter stringWriter = new StringWriter();
        try {
            IOUtil.copy(templateInputStream, stringWriter, "utf-8");
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return stringWriter.toString();
    }
}
