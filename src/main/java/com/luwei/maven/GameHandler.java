package com.luwei.maven;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.IOUtil;
import org.stringtemplate.v4.ST;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lhcpig on 2017/7/20.
 */
@Mojo(name = "run")
public class GameHandler extends AbstractMojo {
    private static final String DEFAULT_INPUT_DIR = "/src/main/protobuf/".replace('/', File.separatorChar);
    @Component
    private MavenProject project;
    private String template = getTemplate("/Handler.java");
    private String handlerDir;
    private String generateTargetDir;
    private String clientProto;

    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("-----------------------start create handler---------------------------------");
        init();
        List<Entity> entities = getAllEntities();
        buildInCommands(entities);
        buildHandlers(entities);
    }

    private void init() {
        String protobufDir = project.getBasedir().getPath() + DEFAULT_INPUT_DIR;
        clientProto = protobufDir + "Client.proto";
        String pack = "/com/luwei/game/handler2/".replace('/', File.separatorChar);
        handlerDir = project.getBuild().getSourceDirectory() + pack;
        generateTargetDir = project.getBuild().getDirectory() + File.separator + "generated-sources" + pack;
        FileUtils.mkdir(handlerDir);
        FileUtils.mkdir(generateTargetDir);
    }

    private List<Entity> getAllEntities() {
        List<Entity> entities = new ArrayList<>();
        try {
            Pattern p = Pattern.compile("//(\\d+)+:(\\w+) (.*)$");

            List<String> lines = Files.readAllLines(new File(clientProto).toPath());
            for (String line : lines) {
                Matcher matcher = p.matcher(line);
                if (matcher.matches()) {
                    getLog().info(line);
                    int cmd = Integer.parseInt(matcher.group(1));
                    String msg = matcher.group(2);
                    String comment = matcher.group(3);
                    Entity entity = new Entity(cmd, msg, comment);
                    entities.add(entity);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return entities;
    }

    private void buildInCommands(List<Entity> entities) {
        StringBuilder sb = new StringBuilder();
        for (Entity entity : entities) {
            ST line = new ST("    int <msg> = <cmd>;//<comment>\r\n");
            line.add("msg", buildInCommandKey(entity));
            line.add("cmd", entity.getCmd());
            line.add("comment", entity.getComment());
            sb.append(line.render());
        }
        ST content = new ST(getTemplate("/InCommands.java"));
        content.add("lines", sb.toString());
        FileWriter writer = null;
        try {
            writer = new FileWriter(generateTargetDir + "InCommands.java");
            writer.write(content.render());
            writer.flush();
            writer.close();
        } catch (IOException e) {
        }
    }

    private void buildHandlers(List<Entity> entities) {
        for (Entity entity : entities) {
            try {
                createHandler(entity);
            } catch (IOException ignored) {
            }
        }
    }

    private void createHandler(Entity entity) throws IOException {
        int cmd = entity.getCmd();
        String cmdKey = buildInCommandKey(entity);
        String comment = entity.getComment();
        File file = new File(handlerDir + cmdKey + "Handler.java");
        if (!file.exists()) {
            ST st = new ST(template, '$', '$');
            st.add("cmd", cmd);
            st.add("msg", entity.getMsg());
            st.add("cmdKey", cmdKey);
            st.add("comment", comment);
            String content = st.render();
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            writer.write(content);
            writer.flush();
            writer.close();
            System.out.println(file.getPath());
        }
    }

    private String getTemplate(String file) {
        InputStream templateInputStream = this.getClass().getResourceAsStream(file);
        StringWriter stringWriter = new StringWriter();
        try {
            IOUtil.copy(templateInputStream, stringWriter, "utf-8");
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return stringWriter.toString();
    }

    private String buildInCommandKey(Entity entity) {
        return entity.getMsg() + "_" + entity.getCmd();
    }
}
