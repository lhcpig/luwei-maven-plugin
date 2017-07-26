package com.luwei.maven;

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
 * Created by lhcpig on 2017/7/26.
 */
public class Utils {
    public static final String pack = "/com/luwei/game/handler2/".replace('/', File.separatorChar);
    private static final String DEFAULT_INPUT_DIR = "/src/main/protobuf/".replace('/', File.separatorChar);

    public static String getClientProto(MavenProject project) {
        String protobufDir = project.getBasedir().getPath() + DEFAULT_INPUT_DIR;
        return protobufDir + "Client.proto";
    }

    public static String getServerProto(MavenProject project) {
        String protobufDir = project.getBasedir().getPath() + DEFAULT_INPUT_DIR;
        return protobufDir + "Server.proto";
    }


    public static List<Entity> getAllEntities(String clientProto) {
        List<Entity> entities = new ArrayList<>();
        try {
            Pattern p = Pattern.compile("//(\\d+)+:(\\w+) (.*)$");

            List<String> lines = Files.readAllLines(new File(clientProto).toPath());
            for (String line : lines) {
                Matcher matcher = p.matcher(line);
                if (matcher.matches()) {
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

    public static String getTemplate(String file) {
        InputStream templateInputStream = Utils.class.getResourceAsStream(file);
        StringWriter stringWriter = new StringWriter();
        try {
            IOUtil.copy(templateInputStream, stringWriter, "utf-8");
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return stringWriter.toString();
    }

    public static void buildCommands(String clazzName, List<Entity> entities, MavenProject project) {
        String generateTargetDir = project.getBuild().getDirectory() + File.separator + "generated-sources" + Utils.pack;
        FileUtils.mkdir(generateTargetDir);

        StringBuilder sb = new StringBuilder();
        for (Entity entity : entities) {
            ST line = new ST("    int <msg> = <cmd>;//<comment>\r\n");
            line.add("msg", Utils.buildInCommandKey(entity));
            line.add("cmd", entity.getCmd());
            line.add("comment", entity.getComment());
            sb.append(line.render());
        }
        ST content = new ST(Utils.getTemplate("/Commands.java"));
        content.add("lines", sb.toString());
        content.add("clazzName", clazzName);
        FileWriter writer = null;
        try {
            writer = new FileWriter(generateTargetDir + clazzName + ".java");
            writer.write(content.render());
            writer.flush();
            writer.close();
        } catch (IOException e) {
        }
    }

    public static String buildInCommandKey(Entity entity) {
        return entity.getMsg() + "_" + entity.getCmd();
    }
}
