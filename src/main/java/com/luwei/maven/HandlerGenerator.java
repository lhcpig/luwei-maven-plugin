package com.luwei.maven;

import org.codehaus.plexus.util.FileUtils;
import org.stringtemplate.v4.ST;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created by lhcpig on 2017/7/26.
 */
public class HandlerGenerator {

    private String handlerDir;
    private String template;
    private List<Entity> entities;

    public HandlerGenerator(String handlerDir, String template, String proto) {
        FileUtils.mkdir(handlerDir);
        this.handlerDir = handlerDir;
        this.template = template;
        this.entities = Utils.getAllEntities(proto);

    }

    public void build() {
        for (Entity entity : entities) {
            try {
                createHandler(entity);
            } catch (IOException ignored) {
            }
        }
    }

    private void createHandler(Entity entity) throws IOException {
        int cmd = entity.getCmd();
        String cmdKey = Utils.buildInCommandKey(entity);
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
}
