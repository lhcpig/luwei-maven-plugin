package com.luwei.maven;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.util.List;

/**
 * Created by lhcpig on 2017/7/20.
 */
@Mojo(name = "test-gen-handler")
public class GenerateTestHandler extends AbstractMojo {
    @Component
    private MavenProject project;

    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("-----------------------start create server handler---------------------------------");
        String handlerDir = project.getBasedir().getAbsolutePath() + "/src/main/test/com/luwei/game/test/handler/".replace('/', File.separatorChar);;
        String template = Utils.getTemplate("/TestHandler.java");
        String protoFile = Utils.getServerProto(project);
        new HandlerGenerator(handlerDir, template, protoFile).build();
        new GenerateClientCommands(project).execute();
    }


}
